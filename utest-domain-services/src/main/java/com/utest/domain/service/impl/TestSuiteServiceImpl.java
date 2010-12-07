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
import com.utest.domain.TestCaseVersion;
import com.utest.domain.TestSuite;
import com.utest.domain.TestSuiteTestCase;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TestSuiteService;
import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.IncludingMultileVersionsOfSameEntityException;
import com.utest.exception.IncludingNotActivatedEntityException;
import com.utest.exception.NotFoundException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

public class TestSuiteServiceImpl extends BaseServiceImpl implements TestSuiteService
{
	private final TypelessDAO			dao;
	private final EnvironmentService	environmentService;

	/**
	 * Default constructor
	 */
	public TestSuiteServiceImpl(final TypelessDAO dao, final EnvironmentService environmentService)
	{
		super(dao);
		this.dao = dao;
		this.environmentService = environmentService;
	}

	@Override
	public TestSuite addTestSuite(final Integer productId_, final boolean useLatestVersions_, final String name_, final String description_) throws Exception
	{
		final Product product = dao.getById(Product.class, productId_);
		if (product == null)
		{
			throw new IllegalArgumentException("Product not found: " + productId_);
		}

		checkForDuplicateNameWithinParent(TestSuite.class, name_, productId_, "productId", null);

		final TestSuite testSuite = new TestSuite();
		testSuite.setTestSuiteStatusId(TcmEntityStatus.DRAFT);
		testSuite.setUseLatestVersions(useLatestVersions_);
		testSuite.setProductId(productId_);
		testSuite.setName(name_);
		testSuite.setDescription(description_);
		// set environment profile from the product by default
		testSuite.setEnvironmentProfileId(product.getEnvironmentProfileId());

		// final Integer testSuiteId = dao.addAndReturnId(testSuite);
		// dao.flush();
		// return getTestSuite(testSuiteId);
		return dao.merge(testSuite);
	}

	@Override
	public List<EnvironmentGroup> findEnvironmentGroupsForTestSuite(final Integer testSuiteId_) throws Exception
	{
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if ((testSuite != null) && (testSuite.getEnvironmentProfileId() != null))
		{
			return environmentService.findEnvironmentGroupsForProfile(testSuite.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public void saveEnvironmentGroupsForTestSuite(final Integer testSuiteId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if (testSuite == null)
		{
			throw new IllegalArgumentException("TestSuite not found: " + testSuiteId_);
		}
		// cannot change after activation
		if (!TcmEntityStatus.DRAFT.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new DeletingActivatedEntityException(TestSuite.class.getSimpleName());
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
		final Product product = dao.getById(Product.class, testSuite.getProductId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(product.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		// update environment profile
		final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test suite : " + testSuiteId_,
				"Included groups: " + environmentGroupIds_.toString(), environmentGroupIds_);
		testSuite.setEnvironmentProfileId(environmentProfile.getId());
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
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if (testSuite == null)
		{
			throw new IllegalArgumentException("TestSuite not found: " + testSuiteId_);
		}
		final TestCaseVersion testCaseVersion = dao.getById(TestCaseVersion.class, testCaseVersionId_);
		if (testCaseVersion == null)
		{
			throw new IllegalArgumentException("TestCaseVersion not found: " + testCaseVersionId_);
		}
		// prevent adding to activated test suite
		if (!TcmEntityStatus.DRAFT.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new ChangingActivatedEntityException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		// prevent if test case not activated
		if (!TcmEntityStatus.ACTIVATED.equals(testCaseVersion.getTestCaseStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestCaseVersion.class.getSimpleName() + " : " + testCaseVersionId_);
		}
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
		// TODO - how do profiles intersect? test case or test suite takes
		// precedent? Do we need to take intersection of both groups?
		if (testSuite.getEnvironmentProfileId() != null)
		{
			includedTestCase.setEnvironmentProfileId(testSuite.getEnvironmentProfileId());
		}
		else
		{
			includedTestCase.setEnvironmentProfileId(testCaseVersion.getEnvironmentProfileId());
		}
		final Integer id = dao.addAndReturnId(includedTestCase);
		return dao.getById(TestSuiteTestCase.class, id);
	}

	@Override
	public void deleteTestSuite(final Integer testSuiteId_) throws Exception
	{
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if (testSuite == null)
		{
			throw new NotFoundException("TestSuite not found. Id: " + testSuiteId_);
		}
		if (!TcmEntityStatus.DRAFT.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new DeletingActivatedEntityException(TestSuite.class.getSimpleName());
		}
		// delete all included test cases
		final List<TestSuiteTestCase> includedTestCases = findTestSuiteTestCases(testSuiteId_);
		dao.delete(includedTestCases);
		// delete test suite
		dao.delete(testSuite);
	}

	@Override
	public void deleteTestSuiteTestCase(final Integer testSuiteTestCaseId_) throws Exception
	{
		final TestSuiteTestCase includedTestCase = dao.getById(TestSuiteTestCase.class, testSuiteTestCaseId_);
		if (includedTestCase == null)
		{
			throw new NotFoundException("TestSuiteTestCase not found. Id: " + testSuiteTestCaseId_);
		}
		final TestSuite testSuite = dao.getById(TestSuite.class, includedTestCase.getTestSuiteId());
		// prevent if already activated
		if (!TcmEntityStatus.DRAFT.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new ChangingActivatedEntityException(TestSuiteTestCase.class.getSimpleName());
		}
		// DomainUtil.loadUpdatedTimeline(testSuite);
		// dao.merge(testSuite);
		dao.delete(includedTestCase);
	}

	@Override
	public UtestSearchResult findTestSuites(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(TestSuite.class, search_);
	}

	@Override
	public TestSuite getTestSuite(final Integer testSuiteId_) throws Exception
	{
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if (testSuite == null)
		{
			throw new NotFoundException("TestSuite not found: " + testSuiteId_);
		}
		return testSuite;

	}

	@Override
	public TestSuiteTestCase getTestSuiteTestCase(final Integer testSuiteTestCaseId_) throws Exception
	{
		final TestSuiteTestCase testSuiteTestCase = dao.getById(TestSuiteTestCase.class, testSuiteTestCaseId_);
		if (testSuiteTestCase == null)
		{
			throw new NotFoundException("TestSuiteTestCase not found: " + testSuiteTestCaseId_);
		}
		return testSuiteTestCase;

	}

	@Override
	public List<TestSuiteTestCase> findTestSuiteTestCases(final Integer testSuiteId_) throws Exception
	{
		final Search search = new Search(TestSuiteTestCase.class);
		search.addFilterEqual("testSuiteId", testSuiteId_);
		search.addSortAsc("runOrder");
		return dao.search(TestSuiteTestCase.class, search);
	}

	@Override
	public TestSuite saveTestSuite(final Integer testSuiteId_, final String name_, final String description_, final Integer originalVersionId_) throws Exception
	{
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if (testSuite == null)
		{
			throw new NotFoundException("TestSuite not found: " + testSuiteId_);
		}
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
		final TestSuiteTestCase includedTestCase = dao.getById(TestSuiteTestCase.class, includedTestCaseId_);
		if (includedTestCase == null)
		{
			throw new NotFoundException("TestSuiteTestCase not found: " + includedTestCaseId_);
		}
		final TestSuite testSuite = dao.getById(TestSuite.class, includedTestCase.getTestSuiteId());
		if (testSuite == null)
		{
			throw new NotFoundException("TestSuite not found: " + includedTestCase.getTestSuiteId());
		}
		// prevent if already activated
		if (!TcmEntityStatus.DRAFT.equals(testSuite.getTestSuiteStatusId()))
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
		return updateActivationStatus(testSuiteId_, TcmEntityStatus.ACTIVATED, originalVersionId_);
	}

	@Override
	public TestSuite lockTestSuite(final Integer testSuiteId_, final Integer originalVersionId_) throws Exception
	{
		// TODO - need to define rules for deactivating
		return updateActivationStatus(testSuiteId_, TcmEntityStatus.LOCKED, originalVersionId_);
	}

	private TestSuite updateActivationStatus(final Integer testSuiteId_, final Integer activationStatusId_, final Integer originalVersionId_) throws Exception
	{
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if (testSuite == null)
		{
			throw new NotFoundException("TestSuite not found: " + testSuiteId_);
		}
		if (activationStatusId_ != testSuite.getTestSuiteStatusId())
		{
			final List<TestSuiteTestCase> includedTestCases = findTestSuiteTestCases(testSuiteId_);
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
