/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Product;
import com.utest.domain.TcmEntityStatus;
import com.utest.domain.TestPlan;
import com.utest.domain.TestPlanTestSuite;
import com.utest.domain.TestSuite;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TestPlanService;
import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.IncludingMultileVersionsOfSameEntityException;
import com.utest.exception.IncludingNotActivatedEntityException;
import com.utest.exception.NotFoundException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

public class TestPlanServiceImpl extends BaseServiceImpl implements TestPlanService
{
	private final TypelessDAO			dao;
	private final EnvironmentService	environmentService;

	/**
	 * Default constructor
	 */
	public TestPlanServiceImpl(final TypelessDAO dao, final EnvironmentService environmentService)
	{
		super(dao);
		this.dao = dao;
		this.environmentService = environmentService;
	}

	@Override
	public TestPlan addTestPlan(final Integer productId_, final String name_, final String description_) throws Exception
	{
		final Product product = dao.getById(Product.class, productId_);
		if (product == null)
		{
			throw new NotFoundException("Product not found: " + productId_);
		}
		checkForDuplicateNameWithinParent(TestPlan.class, name_, productId_, "productId", null);

		final TestPlan testPlan = new TestPlan();
		testPlan.setTestPlanStatusId(TcmEntityStatus.DRAFT);
		testPlan.setProductId(productId_);
		testPlan.setName(name_);
		testPlan.setDescription(description_);

		final Integer testPlanId = dao.addAndReturnId(testPlan);
		return getTestPlan(testPlanId);
	}

	@Override
	public TestPlanTestSuite addTestPlanTestSuite(final Integer testPlanId_, final Integer testSuiteId_, final Integer originalVersionId_) throws Exception
	{
		final Integer runOrder = 0;
		return addTestPlanTestSuite(testPlanId_, testSuiteId_, runOrder, originalVersionId_);
	}

	@Override
	public TestPlanTestSuite addTestPlanTestSuite(final Integer testPlanId_, final Integer testSuiteId_, final Integer runOrder_, final Integer originalVersionId_)
			throws Exception
	{
		final TestPlan testPlan = dao.getById(TestPlan.class, testPlanId_);
		if (testPlan == null)
		{
			throw new NotFoundException("TestPlan not found: " + testPlanId_);
		}
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if (testSuite == null)
		{
			throw new NotFoundException("TestSuite not found: " + testSuiteId_);
		}
		// prevent if test suite not activated
		if (!TcmEntityStatus.ACTIVATED.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		// prevent if test plan already activated
		if (!TcmEntityStatus.DRAFT.equals(testPlan.getTestPlanStatusId()))
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
	public void deleteTestPlan(final Integer testPlanId_) throws Exception
	{
		final TestPlan testPlan = dao.getById(TestPlan.class, testPlanId_);
		if (testPlan == null)
		{
			throw new NotFoundException("TestPlan not found. Id: " + testPlanId_);
		}
		if (!TcmEntityStatus.DRAFT.equals(testPlan.getTestPlanStatusId()))
		{
			throw new DeletingActivatedEntityException(TestPlan.class.getSimpleName());
		}
		// delete all included test suites
		final List<TestPlanTestSuite> includedTestSuites = findTestPlanTestSuites(testPlanId_);
		dao.delete(includedTestSuites);
		// delete test suite
		dao.delete(testPlan);
	}

	@Override
	public void deleteTestPlanTestSuite(final Integer testPlanTestSuiteId_, final Integer originalVersionId_) throws Exception
	{
		final TestPlanTestSuite includedTestSuite = dao.getById(TestPlanTestSuite.class, testPlanTestSuiteId_);
		if (includedTestSuite == null)
		{
			throw new NotFoundException("Included TestSuite not found. Id: " + testPlanTestSuiteId_);
		}
		// prevent if already activated
		final TestPlan testPlan = dao.getById(TestPlan.class, includedTestSuite.getTestPlanId());
		if (!TcmEntityStatus.DRAFT.equals(testPlan.getTestPlanStatusId()))
		{
			throw new ChangingActivatedEntityException(TestPlanTestSuite.class.getSimpleName());
		}
		dao.delete(includedTestSuite);
	}

	@Override
	public UtestSearchResult findTestPlans(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(TestPlan.class, search_);
	}

	@Override
	public TestPlan getTestPlan(final Integer testPlanId_) throws Exception
	{
		final TestPlan testPlan = dao.getById(TestPlan.class, testPlanId_);
		if (testPlan == null)
		{
			throw new NotFoundException("TestPlan#" + testPlanId_);
		}
		return testPlan;
	}

	@Override
	public List<TestPlanTestSuite> findTestPlanTestSuites(final Integer testPlanId_) throws Exception
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
		final TestPlan testPlan = dao.getById(TestPlan.class, testPlanId_);
		if (testPlan == null)
		{
			throw new NotFoundException("TestPlan#" + testPlanId_);
		}
		checkForDuplicateNameWithinParent(TestPlan.class, name_, testPlan.getProductId(), "productId", testPlanId_);

		testPlan.setName(name_);
		testPlan.setDescription(description_);
		testPlan.setVersion(originalVersionId_);
		return dao.merge(testPlan);
	}

	@Override
	public TestPlanTestSuite saveTestPlanTestSuite(final Integer includedTestSuiteId_, final Integer runOrder_, final Integer originalVersionId_)
	{
		final TestPlanTestSuite includedTestSuite = dao.getById(TestPlanTestSuite.class, includedTestSuiteId_);
		if (includedTestSuite == null)
		{
			throw new NotFoundException("Included TestSuite not found: " + includedTestSuiteId_);
		}
		// prevent if test plan already activated
		final TestPlan testPlan = dao.getById(TestPlan.class, includedTestSuite.getTestPlanId());
		if (!TcmEntityStatus.DRAFT.equals(testPlan.getTestPlanStatusId()))
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
		return updateActivationStatus(testPlanId_, TcmEntityStatus.ACTIVATED, originalVersionId_);
	}

	@Override
	public TestPlan lockTestPlan(final Integer testPlanId_, final Integer originalVersionId_) throws Exception
	{
		// TODO - need to define rules for deactivating
		return updateActivationStatus(testPlanId_, TcmEntityStatus.LOCKED, originalVersionId_);
	}

	private TestPlan updateActivationStatus(final Integer testPlanId_, final Integer activationStatusId_, final Integer originalVersionId_) throws Exception
	{
		final TestPlan testPlan = dao.getById(TestPlan.class, testPlanId_);
		if (testPlan == null)
		{
			throw new NotFoundException("TestPlan not found: " + testPlanId_);
		}
		if (TcmEntityStatus.ACTIVATED != testPlan.getTestPlanStatusId())
		{
			final List<TestPlanTestSuite> includedTestSuites = findTestPlanTestSuites(testPlanId_);
			if ((includedTestSuites == null) || includedTestSuites.isEmpty())
			{
				throw new ActivatingIncompleteEntityException(TestPlan.class.getSimpleName() + " : " + testPlanId_);
			}
			testPlan.setTestPlanStatusId(TcmEntityStatus.ACTIVATED);
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
		final TestPlan testPlan = dao.getById(TestPlan.class, testPlanId_);
		if (testPlan == null)
		{
			throw new NotFoundException("TestPlan not found: " + testPlanId_);
		}
		// cannot change after activation
		if (!TcmEntityStatus.DRAFT.equals(testPlan.getTestPlanStatusId()))
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
		final Product product = dao.getById(Product.class, testPlan.getProductId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(product.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException(TestPlan.class.getSimpleName() + " : " + testPlanId_);
		}
		// update environment profile
		final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test suite : " + testPlanId_,
				"Included groups: " + environmentGroupIds_.toString(), environmentGroupIds_);
		testPlan.setEnvironmentProfileId(environmentProfile.getId());
		testPlan.setVersion(originalVersionId_);
		dao.merge(testPlan);
	}

	@Override
	public List<EnvironmentGroup> findEnvironmentGroupsForTestPlan(final Integer testPlanId_) throws Exception
	{
		final TestPlan testPlan = dao.getById(TestPlan.class, testPlanId_);
		if (testPlan == null)
		{
			throw new NotFoundException("TestPlan not found: " + testPlanId_);
		}
		if (testPlan.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfile(testPlan.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

}
