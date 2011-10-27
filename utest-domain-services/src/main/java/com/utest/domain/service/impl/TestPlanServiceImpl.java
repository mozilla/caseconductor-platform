/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 * @author Vadim Kisen
 *
 * copyright 2010 by uTest 
 */
package com.utest.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.Attachment;
import com.utest.domain.EntityType;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.Product;
import com.utest.domain.TestPlan;
import com.utest.domain.TestPlanStatus;
import com.utest.domain.TestPlanTestSuite;
import com.utest.domain.TestSuite;
import com.utest.domain.TestSuiteStatus;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.AttachmentService;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TestPlanService;
import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.IncludingMultileVersionsOfSameEntityException;
import com.utest.exception.IncludingNotActivatedEntityException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

public class TestPlanServiceImpl extends BaseServiceImpl implements TestPlanService
{
	private final AttachmentService	attachmentService;

	/**
	 * Default constructor
	 */
	public TestPlanServiceImpl(final TypelessDAO dao, final EnvironmentService environmentService, final AttachmentService attachmentService)
	{
		super(dao, environmentService);
		this.attachmentService = attachmentService;
	}

	@Override
	public TestPlan addTestPlan(final Integer productId_, final String name_, final String description_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		checkForDuplicateNameWithinParent(TestPlan.class, name_, productId_, "productId", null);

		final TestPlan testPlan = new TestPlan();
		testPlan.setTestPlanStatusId(TestPlanStatus.PENDING);
		testPlan.setCompanyId(product.getCompanyId());
		testPlan.setProductId(product.getId());
		testPlan.setName(name_);
		testPlan.setDescription(description_);

		final Integer testPlanId = dao.addAndReturnId(testPlan);
		return getTestPlan(testPlanId);
	}

	@Override
	public TestPlanTestSuite addTestPlanTestSuite(final Integer testPlanId_, final Integer testSuiteId_) throws Exception
	{
		final Integer runOrder = 0;
		return addTestPlanTestSuite(testPlanId_, testSuiteId_, runOrder);
	}

	@Override
	public TestPlanTestSuite addTestPlanTestSuite(final Integer testPlanId_, final Integer testSuiteId_, final Integer runOrder_) throws Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		// check if products match
		checkProductMatch(testSuite, testPlan);
		// prevent if test suite not activated
		if (!TestSuiteStatus.ACTIVE.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		// prevent if test plan already activated
		if (!TestPlanStatus.PENDING.equals(testPlan.getTestPlanStatusId()))
		{
			throw new ChangingActivatedEntityException(TestPlan.class.getSimpleName() + " : " + testPlan.getId());
		}
		// prevent if test suite already included
		final Search search = new Search(TestPlanTestSuite.class);
		search.addFilterEqual("testSuiteId", testSuiteId_);
		search.addFilterEqual("testPlanId", testPlanId_);
		final List<TestPlanTestSuite> foundItems = dao.search(TestPlanTestSuite.class, search);
		if ((foundItems != null) && !foundItems.isEmpty())
		{
			throw new IncludingMultileVersionsOfSameEntityException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}

		final TestPlanTestSuite includedTestSuite = new TestPlanTestSuite();
		includedTestSuite.setTestSuiteId(testSuiteId_);
		includedTestSuite.setTestPlanId(testPlanId_);
		includedTestSuite.setRunOrder(runOrder_);
		return dao.merge(includedTestSuite);
	}

	@Override
	public void deleteTestPlan(final Integer testPlanId_, final Integer originalVersionId_) throws Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		if (!TestPlanStatus.PENDING.equals(testPlan.getTestPlanStatusId()))
		{
			throw new DeletingActivatedEntityException(TestPlan.class.getSimpleName());
		}
		// delete all included test suites
		final List<TestPlanTestSuite> includedTestSuites = getTestPlanTestSuites(testPlanId_);
		dao.delete(includedTestSuites);
		// delete test suite
		testPlan.setVersion(originalVersionId_);
		dao.delete(testPlan);
	}

	@Override
	public void deleteTestPlanTestSuite(final Integer testPlanTestSuiteId_, final Integer originalVersionId_) throws Exception
	{
		final TestPlanTestSuite includedTestSuite = getRequiredEntityById(TestPlanTestSuite.class, testPlanTestSuiteId_);
		// prevent if already activated
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, includedTestSuite.getTestPlanId());
		if (!TestPlanStatus.PENDING.equals(testPlan.getTestPlanStatusId()))
		{
			throw new ChangingActivatedEntityException(TestPlanTestSuite.class.getSimpleName());
		}
		includedTestSuite.setVersion(originalVersionId_);
		dao.delete(includedTestSuite);
	}

	@Override
	public UtestSearchResult findTestPlans(final UtestSearch search_, List<Integer> includedEnvironmentId_) throws Exception
	{
		if (includedEnvironmentId_ != null && !includedEnvironmentId_.isEmpty())
		{
			List<Integer> profileIds = environmentService.getProfilesContainingEnvironments(includedEnvironmentId_);
			if (profileIds != null && !profileIds.isEmpty())
			{
				search_.addFilterIn("environmentProfileId", profileIds);
			}
			else
			{
				return new UtestSearchResult();
			}
		}
		return dao.getBySearch(TestPlan.class, search_);
	}

	@Override
	public TestPlan getTestPlan(final Integer testPlanId_) throws Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		return testPlan;
	}

	@Override
	public TestPlanTestSuite getTestPlanTestSuite(final Integer testPlanTestSuiteId_) throws Exception
	{
		final TestPlanTestSuite testPlanTestSuite = getRequiredEntityById(TestPlanTestSuite.class, testPlanTestSuiteId_);
		return testPlanTestSuite;
	}

	@Override
	public List<TestPlanTestSuite> getTestPlanTestSuites(final Integer testPlanId_) throws Exception
	{
		if (testPlanId_ == null)
		{
			throw new IllegalArgumentException("TestPlan ID is null");
		}
		final Search search = new Search(TestPlanTestSuite.class);
		search.addFilterEqual("testPlanId", testPlanId_);
		search.addSortAsc("runOrder");
		return dao.search(TestPlanTestSuite.class, search);
	}

	@Override
	public TestPlan saveTestPlan(final Integer testPlanId_, final String name_, final String description_, final Integer originalVersionId_) throws Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		checkForDuplicateNameWithinParent(TestPlan.class, name_, testPlan.getProductId(), "productId", testPlanId_);

		testPlan.setName(name_);
		testPlan.setDescription(description_);
		testPlan.setVersion(originalVersionId_);
		return dao.merge(testPlan);
	}

	@Override
	public TestPlanTestSuite saveTestPlanTestSuite(final Integer includedTestSuiteId_, final Integer runOrder_, final Integer originalVersionId_)
	{
		final TestPlanTestSuite includedTestSuite = getRequiredEntityById(TestPlanTestSuite.class, includedTestSuiteId_);
		// prevent if test plan already activated
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, includedTestSuite.getTestPlanId());
		if (!TestPlanStatus.PENDING.equals(testPlan.getTestPlanStatusId()))
		{
			throw new ChangingActivatedEntityException(TestPlan.class.getSimpleName() + " : " + testPlan.getId());
		}
		includedTestSuite.setVersion(originalVersionId_);
		includedTestSuite.setRunOrder(runOrder_);
		return dao.merge(includedTestSuite);
	}

	@Override
	public TestPlan activateTestPlan(final Integer testPlanId_, final Integer originalVersionId_) throws Exception
	{
		return updateActivationStatus(testPlanId_, TestPlanStatus.ACTIVE, originalVersionId_);
	}

	@Override
	public TestPlan lockTestPlan(final Integer testPlanId_, final Integer originalVersionId_) throws Exception
	{
		// TODO - need to define rules for deactivating
		return updateActivationStatus(testPlanId_, TestPlanStatus.LOCKED, originalVersionId_);
	}

	private TestPlan updateActivationStatus(final Integer testPlanId_, final Integer activationStatusId_, final Integer originalVersionId_) throws Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		if (TestPlanStatus.ACTIVE != testPlan.getTestPlanStatusId())
		{
			final List<TestPlanTestSuite> includedTestSuites = getTestPlanTestSuites(testPlanId_);
			if ((includedTestSuites == null) || includedTestSuites.isEmpty())
			{
				throw new ActivatingIncompleteEntityException(TestPlan.class.getSimpleName() + " : " + testPlanId_);
			}
			testPlan.setTestPlanStatusId(TestPlanStatus.ACTIVE);
			testPlan.setVersion(originalVersionId_);
			return dao.merge(testPlan);
		}
		else
		{
			return testPlan;
		}
	}

	@Override
	public void saveEnvironmentGroupsForTestPlan(final Integer testPlanId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		// cannot change after activation
		if (!TestPlanStatus.PENDING.equals(testPlan.getTestPlanStatusId()))
		{
			throw new DeletingActivatedEntityException(TestPlan.class.getSimpleName());
		}
		// prevent from changing if there are any test suites included already?
		final Search search = new Search(TestPlanTestSuite.class);
		search.addFilterEqual("testPlanId", testPlanId_);
		final List<TestPlanTestSuite> foundItems = dao.search(TestPlanTestSuite.class, search);
		if ((foundItems != null) && !foundItems.isEmpty())
		{
			throw new UnsupportedEnvironmentSelectionException(TestPlanTestSuite.class.getSimpleName() + " : " + testPlanId_);
		}
		// check that groups are included in the parent profile.
		final Product product = getRequiredEntityById(Product.class, testPlan.getProductId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(product.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException(TestPlan.class.getSimpleName() + " : " + testPlanId_);
		}
		// update environment profile
		adjustParentChildProfiles(product, testPlan, product.getCompanyId(), environmentGroupIds_);
		testPlan.setVersion(originalVersionId_);
		dao.merge(testPlan);
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForTestPlan(final Integer testPlanId_, List<Integer> includedEnvironmentId_) throws Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		if (testPlan.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfile(testPlan.getEnvironmentProfileId(), includedEnvironmentId_);
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public List<Attachment> getAttachmentsForTestPlan(final Integer testPlanId_) throws Exception
	{
		return attachmentService.getAttachmentsForEntity(testPlanId_, EntityType.TEST_PLAN);
	}

	@Override
	public Attachment addAttachmentForTestPlan(final String name, final String description, final String url, final Double size, final Integer testPlanId,
			final Integer attachmentTypeId) throws Exception
	{
		return attachmentService.addAttachment(name, description, url, size, EntityType.TEST_PLAN, testPlanId, attachmentTypeId);
	}

	@Override
	public boolean deleteAttachment(final Integer attachmentId_, final Integer entityId_) throws Exception
	{
		return attachmentService.deleteAttachment(attachmentId_, entityId_, EntityType.TEST_PLAN);
	}

	@Override
	public List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestPlan(final Integer testPlanId_, List<Integer> includedEnvironmentId_) throws Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		if (testPlan.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfileExploded(testPlan.getEnvironmentProfileId(), includedEnvironmentId_);
		}
		else
		{
			return new ArrayList<EnvironmentGroupExploded>();
		}
	}
}
