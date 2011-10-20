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
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Product;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.TestRunTestCase;
import com.utest.domain.TestSuite;
import com.utest.domain.TestSuiteStatus;
import com.utest.domain.TestSuiteTestCase;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.AttachmentService;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TestSuiteService;
import com.utest.domain.view.TestSuiteTestCaseView;
import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.IncludingMultileVersionsOfSameEntityException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

public class TestSuiteServiceImpl extends BaseServiceImpl implements TestSuiteService
{
	private final AttachmentService	attachmentService;

	/**
	 * Default constructor
	 */
	public TestSuiteServiceImpl(final TypelessDAO dao, final EnvironmentService environmentService, final AttachmentService attachmentService)
	{
		super(dao, environmentService);
		this.attachmentService = attachmentService;
	}

	@Override
	public TestSuite addTestSuite(final Integer productId_, final boolean useLatestVersions_, final String name_, final String description_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);

		checkForDuplicateNameWithinParent(TestSuite.class, name_, productId_, "productId", null);

		final TestSuite testSuite = new TestSuite();
		testSuite.setTestSuiteStatusId(TestSuiteStatus.PENDING);
		testSuite.setUseLatestVersions(useLatestVersions_);
		testSuite.setCompanyId(product.getCompanyId());
		testSuite.setProductId(productId_);
		testSuite.setName(name_);
		testSuite.setDescription(description_);
		// set environment profile from the product by default
		testSuite.setEnvironmentProfileId(product.getEnvironmentProfileId());

		return dao.merge(testSuite);
	}

	@Override
	public TestSuite cloneTestSuite(final Integer fromTestSuiteId_) throws Exception
	{
		final TestSuite fromTestSuite = getRequiredEntityById(TestSuite.class, fromTestSuiteId_);
		// clone test Suite
		final TestSuite toTestSuite = new TestSuite();
		toTestSuite.setUseLatestVersions(fromTestSuite.isUseLatestVersions());
		toTestSuite.setProductId(fromTestSuite.getProductId());
		toTestSuite.setCompanyId(fromTestSuite.getCompanyId());
		toTestSuite.setName(fromTestSuite.getName());
		toTestSuite.setDescription(fromTestSuite.getDescription());
		toTestSuite.setTestSuiteStatusId(TestSuiteStatus.PENDING);
		toTestSuite.setEnvironmentProfileId(fromTestSuite.getEnvironmentProfileId());
		final Integer toTestSuiteId = dao.addAndReturnId(toTestSuite);

		// clone test cases
		List<TestSuiteTestCase> oldTestCases = getTestSuiteTestCases(fromTestSuiteId_);
		if (oldTestCases != null)
		{
			for (TestSuiteTestCase oldCase : oldTestCases)
			{
				addTestSuiteTestCase(toTestSuiteId, oldCase.getTestCaseVersionId(), oldCase.getPriorityId(), oldCase.getRunOrder(), oldCase.isBlocking());
			}
		}
		// return newly created test suite
		return getTestSuite(toTestSuiteId);
	}

	@Override
	public List<Attachment> getAttachmentsForTestSuite(final Integer testSuiteId_) throws Exception
	{
		return attachmentService.getAttachmentsForEntity(testSuiteId_, EntityType.TEST_SUITE);
	}

	@Override
	public Attachment addAttachmentForTestSuite(final String name, final String description, final String url, final Double size, final Integer testSuiteId_,
			final Integer attachmentTypeId) throws Exception
	{
		return attachmentService.addAttachment(name, description, url, size, EntityType.TEST_SUITE, testSuiteId_, attachmentTypeId);
	}

	@Override
	public boolean deleteAttachment(final Integer attachmentId_, final Integer entityId_) throws Exception
	{
		return attachmentService.deleteAttachment(attachmentId_, entityId_, EntityType.TEST_SUITE);
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForTestSuite(final Integer testSuiteId_, Integer includedEnvironmentId_) throws Exception
	{
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		if (testSuite.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfile(testSuite.getEnvironmentProfileId(), includedEnvironmentId_);
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestSuite(final Integer testSuiteId_, Integer includedEnvironmentId_) throws Exception
	{
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		if (testSuite.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfileExploded(testSuite.getEnvironmentProfileId(), includedEnvironmentId_);
		}
		else
		{
			return new ArrayList<EnvironmentGroupExploded>();
		}
	}

	@Override
	public void saveEnvironmentGroupsForTestSuite(final Integer testSuiteId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		// cannot change after activation
		if (!TestSuiteStatus.PENDING.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new ChangingActivatedEntityException(TestSuite.class.getSimpleName());
		}
		// prevent from changing if there are any test cases included already?
		final Search search = new Search(TestSuiteTestCase.class);
		search.addFilterEqual("testSuiteId", testSuiteId_);
		final List<TestSuiteTestCase> foundItems = dao.search(TestSuiteTestCase.class, search);
		if ((foundItems != null) && !foundItems.isEmpty())
		{
			throw new UnsupportedEnvironmentSelectionException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		// check that groups are included in the parent profile.
		final Product product = getRequiredEntityById(Product.class, testSuite.getProductId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(product.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		// update environment profile
		adjustParentChildProfiles(product, testSuite, product.getCompanyId(), environmentGroupIds_);
		testSuite.setVersion(originalVersionId_);
		dao.merge(testSuite);
	}

	@Override
	public TestSuiteTestCase addTestSuiteTestCase(final Integer testSuiteId_, final Integer testCaseVersionId_) throws Exception
	{
		final Integer priorityId = 0;
		final Integer runOrder = 0;
		final boolean blocking = false;
		return addTestSuiteTestCase(testSuiteId_, testCaseVersionId_, priorityId, runOrder, blocking);
	}

	@Override
	public TestSuiteTestCase addTestSuiteTestCase(final Integer testSuiteId_, final Integer testCaseVersionId_, final Integer priorityId_, final Integer runOrder_,
			final boolean blocking_) throws Exception
	{
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		// check if products match
		checkProductMatch(testSuite, testCaseVersion);
		// prevent adding to activated test suite
		// if
		// (!TestSuiteStatus.PENDING.equals(testSuite.getTestSuiteStatusId()))
		// {
		// throw new
		// ChangingActivatedEntityException(TestSuite.class.getSimpleName() +
		// " : " + testSuiteId_);
		// }
		// prevent if test case not activated
		// if
		// (!TestCaseStatus.ACTIVE.equals(testCaseVersion.getTestCaseStatusId()))
		// {
		// throw new
		// IncludingNotActivatedEntityException(TestCaseVersion.class.getSimpleName()
		// + " : " + testCaseVersionId_);
		// }
		// prevent if another version of the same test case already included
		final Search search = new Search(TestSuiteTestCase.class);
		search.addFilterEqual("testSuiteId", testSuiteId_);
		search.addFilterEqual("testCaseId", testCaseVersion.getTestCaseId());
		final List<TestSuiteTestCase> foundItems = dao.search(TestSuiteTestCase.class, search);
		if ((foundItems != null) && !foundItems.isEmpty())
		{
			throw new IncludingMultileVersionsOfSameEntityException(TestCaseVersion.class.getSimpleName() + " : " + testCaseVersionId_);
		}
		final TestSuiteTestCase includedTestCase = new TestSuiteTestCase();
		includedTestCase.setTestSuiteId(testSuiteId_);
		includedTestCase.setTestCaseId(testCaseVersion.getTestCaseId());
		includedTestCase.setTestCaseVersionId(testCaseVersion.getId());
		includedTestCase.setPriorityId(priorityId_);
		includedTestCase.setRunOrder(runOrder_);
		includedTestCase.setBlocking(blocking_);
		if (testSuite.getEnvironmentProfileId() != null && testCaseVersion.getEnvironmentProfileId() == null)
		{
			includedTestCase.setEnvironmentProfileId(testSuite.getEnvironmentProfileId());
		}
		else if (testSuite.getEnvironmentProfileId() == null && testCaseVersion.getEnvironmentProfileId() != null)
		{
			includedTestCase.setEnvironmentProfileId(testCaseVersion.getEnvironmentProfileId());
		}
		else if (testSuite.getEnvironmentProfileId() != null && testCaseVersion.getEnvironmentProfileId() != null)
		{
			if (testSuite.getEnvironmentProfileId().equals(testCaseVersion.getEnvironmentProfileId()))
			{
				includedTestCase.setEnvironmentProfileId(testSuite.getEnvironmentProfileId());
			}
			else
			{
				// if no matching groups were found returns null.
				EnvironmentProfile intersectedProfile = environmentService.intersectEnvironmentProfiles(testSuite.getEnvironmentProfileId(), testCaseVersion
						.getEnvironmentProfileId());
				if (intersectedProfile != null)
				{
					includedTestCase.setEnvironmentProfileId(intersectedProfile.getId());
				}
			}
		}
		final Integer id = dao.addAndReturnId(includedTestCase);
		return getRequiredEntityById(TestSuiteTestCase.class, id);
	}

	@Override
	public void deleteTestSuite(final Integer testSuiteId_, final Integer originalVersionId_) throws Exception
	{
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		if (!TestSuiteStatus.PENDING.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new DeletingActivatedEntityException(TestSuite.class.getSimpleName());
		}
		// delete all included test cases
		final List<TestSuiteTestCase> includedTestCases = getTestSuiteTestCases(testSuiteId_);
		dao.delete(includedTestCases);
		// delete test suite
		testSuite.setVersion(originalVersionId_);
		dao.delete(testSuite);
	}

	@Override
	public void deleteTestSuiteTestCase(final Integer testSuiteTestCaseId_, final Integer originalVersionId_) throws Exception
	{
		final TestSuiteTestCase includedTestCase = getRequiredEntityById(TestSuiteTestCase.class, testSuiteTestCaseId_);
		// final TestSuite testSuite = getRequiredEntityById(TestSuite.class,
		// includedTestCase.getTestSuiteId());
		// prevent if already activated
		// if
		// (!TestSuiteStatus.PENDING.equals(testSuite.getTestSuiteStatusId()))
		// {
		// throw new
		// ChangingActivatedEntityException(TestSuiteTestCase.class.getSimpleName());
		// }
		includedTestCase.setVersion(originalVersionId_);
		dao.delete(includedTestCase);
	}

	@Override
	public UtestSearchResult findTestSuites(final UtestSearch search_, Integer hasTestCasesInTestRunId_, Integer includedTestCaseId, Integer includedTestCaseVersionId,
			Integer includedEnvironmentId_) throws Exception
	{
		if (includedEnvironmentId_ != null)
		{
			List<Integer> profileIds = environmentService.getProfilesContainingEnvironment(includedEnvironmentId_);
			if (profileIds != null && !profileIds.isEmpty())
			{
				search_.addFilterIn("environmentProfileId", profileIds);
			}
			else
			{
				return new UtestSearchResult();
			}
		}
		// search in target test runs
		if (hasTestCasesInTestRunId_ != null)
		{
			Search search = new Search(TestRunTestCase.class);
			search.addField("testSuiteId");
			if (hasTestCasesInTestRunId_ != null)
			{
				search.addFilterEqual("testRunId", hasTestCasesInTestRunId_);
			}
			final List<?> testSuiteIdList = dao.search(TestRunTestCase.class, search);
			if (testSuiteIdList != null && !testSuiteIdList.isEmpty())
			{
				search_.addFilterIn("id", testSuiteIdList);
			}
			else
			{
				return new UtestSearchResult();
			}
		}
		// search in contained test cases
		if (includedTestCaseId != null || includedTestCaseVersionId != null)
		{
			Search search = new Search(TestSuiteTestCase.class);
			search.addField("testSuiteId");
			if (includedTestCaseId != null)
			{
				search.addFilterEqual("testCaseId", includedTestCaseId);
			}
			if (includedTestCaseVersionId != null)
			{
				search.addFilterEqual("testCaseVersionId", includedTestCaseVersionId);
			}
			final List<?> testSuiteIdList = dao.search(TestSuiteTestCase.class, search);
			if (testSuiteIdList != null && !testSuiteIdList.isEmpty())
			{
				search_.addFilterIn("id", testSuiteIdList);
			}
			else
			{
				return new UtestSearchResult();
			}
		}
		return dao.getBySearch(TestSuite.class, search_);
	}

	@Override
	public UtestSearchResult findTestSuiteTestCases(final UtestSearch search_, Integer includedEnvironmentId_) throws Exception
	{
		if (includedEnvironmentId_ != null)
		{
			List<Integer> profileIds = environmentService.getProfilesContainingEnvironment(includedEnvironmentId_);
			if (profileIds != null && !profileIds.isEmpty())
			{
				search_.addFilterIn("environmentProfileId", profileIds);
			}
			else
			{
				return new UtestSearchResult();
			}
		}
		return dao.getBySearch(TestSuiteTestCaseView.class, search_);
	}

	@Override
	public TestSuite getTestSuite(final Integer testSuiteId_) throws Exception
	{
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		return testSuite;

	}

	@Override
	public TestSuiteTestCase getTestSuiteTestCase(final Integer testSuiteTestCaseId_) throws Exception
	{
		final TestSuiteTestCase testSuiteTestCase = getRequiredEntityById(TestSuiteTestCase.class, testSuiteTestCaseId_);
		return testSuiteTestCase;

	}

	@Override
	public TestSuiteTestCaseView getTestSuiteTestCaseView(final Integer testSuiteTestCaseId_) throws Exception
	{
		final TestSuiteTestCaseView testSuiteTestCase = getRequiredEntityById(TestSuiteTestCaseView.class, testSuiteTestCaseId_);
		return testSuiteTestCase;

	}

	@Override
	public List<TestSuiteTestCase> getTestSuiteTestCases(final Integer testSuiteId_) throws Exception
	{
		final Search search = new Search(TestSuiteTestCase.class);
		search.addFilterEqual("testSuiteId", testSuiteId_);
		search.addSortAsc("runOrder");
		return dao.search(TestSuiteTestCase.class, search);
	}

	@Override
	public List<TestSuiteTestCaseView> getTestSuiteTestCasesViews(final Integer testSuiteId_) throws Exception
	{
		final Search search = new Search(TestSuiteTestCaseView.class);
		search.addFilterEqual("testSuiteId", testSuiteId_);
		search.addSortAsc("runOrder");
		return dao.search(TestSuiteTestCaseView.class, search);
	}

	@Override
	public TestSuite saveTestSuite(final Integer testSuiteId_, final String name_, final String description_, final Integer originalVersionId_) throws Exception
	{
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		//
		checkForDuplicateNameWithinParent(TestSuite.class, name_, testSuite.getProductId(), "productId", testSuiteId_);

		testSuite.setName(name_);
		testSuite.setDescription(description_);
		testSuite.setVersion(originalVersionId_);
		return dao.merge(testSuite);
	}

	@Override
	public TestSuiteTestCase saveTestSuiteTestCase(final Integer includedTestCaseId_, final Integer priorityId_, final Integer runOrder_, final boolean blocking_,
			final Integer originalVersionId_)
	{
		final TestSuiteTestCase includedTestCase = getRequiredEntityById(TestSuiteTestCase.class, includedTestCaseId_);
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, includedTestCase.getTestSuiteId());
		// prevent if already activated
		if (!TestSuiteStatus.PENDING.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new ChangingActivatedEntityException(TestSuiteTestCase.class.getSimpleName());
		}
		includedTestCase.setPriorityId(priorityId_);
		includedTestCase.setRunOrder(runOrder_);
		includedTestCase.setBlocking(blocking_);
		includedTestCase.setVersion(originalVersionId_);
		return dao.merge(includedTestCase);
	}

	@Override
	public TestSuite activateTestSuite(final Integer testSuiteId_, final Integer originalVersionId_) throws Exception
	{
		return updateActivationStatus(testSuiteId_, TestSuiteStatus.ACTIVE, originalVersionId_);
	}

	@Override
	public TestSuite lockTestSuite(final Integer testSuiteId_, final Integer originalVersionId_) throws Exception
	{
		// TODO - need to define rules for deactivating
		return updateActivationStatus(testSuiteId_, TestSuiteStatus.LOCKED, originalVersionId_);
	}

	private TestSuite updateActivationStatus(final Integer testSuiteId_, final Integer activationStatusId_, final Integer originalVersionId_) throws Exception
	{
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		if (activationStatusId_ != testSuite.getTestSuiteStatusId())
		{
			final List<TestSuiteTestCase> includedTestCases = getTestSuiteTestCases(testSuiteId_);
			if ((includedTestCases == null) || includedTestCases.isEmpty())
			{
				throw new ActivatingIncompleteEntityException(TestCaseVersion.class.getSimpleName() + " : " + testSuiteId_);
			}
			testSuite.setTestSuiteStatusId(activationStatusId_);
			testSuite.setVersion(originalVersionId_);
			dao.addOrUpdate(testSuite);
			dao.flush();
			return getTestSuite(testSuiteId_);
		}
		else
		{
			return testSuite;
		}
	}

}
