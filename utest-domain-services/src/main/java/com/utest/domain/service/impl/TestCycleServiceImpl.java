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
import java.util.Date;
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Product;
import com.utest.domain.TcmEntityStatus;
import com.utest.domain.TestCycle;
import com.utest.domain.TestRun;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TestCycleService;
import com.utest.domain.service.TestRunService;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.NotFoundException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

public class TestCycleServiceImpl extends BaseServiceImpl implements TestCycleService
{
	private final TypelessDAO			dao;
	private final EnvironmentService	environmentService;
	private final TestRunService		testRunService;

	/**
	 * Default constructor
	 */
	public TestCycleServiceImpl(final TypelessDAO dao, final TestRunService testRunService, final EnvironmentService environmentService)
	{
		super(dao);
		this.dao = dao;
		this.environmentService = environmentService;
		this.testRunService = testRunService;
	}

	@Override
	public TestCycle addTestCycle(final Integer productId_, final String name_, final String description_, final Date startDate_, final Date endDate_,
			final boolean communityAuthoringAllowed_, final boolean communityAccessAllowed_) throws Exception
	{
		final Product product = dao.getById(Product.class, productId_);
		if (product == null)
		{
			throw new NotFoundException("Product not found.");
		}
		checkForDuplicateNameWithinParent(TestCycle.class, name_, productId_, "productId", null);

		final TestCycle testCycle = new TestCycle();
		testCycle.setTestCycleStatusId(TcmEntityStatus.DRAFT);
		testCycle.setProductId(productId_);
		testCycle.setName(name_);
		testCycle.setDescription(description_);
		testCycle.setStartDate(startDate_);
		testCycle.setEndDate(endDate_);
		testCycle.setCommunityAccessAllowed(communityAuthoringAllowed_);
		testCycle.setCommunityAuthoringAllowed(communityAuthoringAllowed_);
		// set environment profile from product by default
		testCycle.setEnvironmentProfileId(product.getEnvironmentProfileId());

		final Integer testCycleId = dao.addAndReturnId(testCycle);
		dao.flush();
		return getTestCycle(testCycleId);
	}

	@Override
	public List<EnvironmentGroup> findEnvironmentGroupsForTestCycle(final Integer testCycleId_) throws Exception
	{
		final TestCycle testCycle = dao.getById(TestCycle.class, testCycleId_);
		if (testCycle == null)
		{
			throw new NotFoundException("TestCycle not found: " + testCycleId_);
		}
		if (testCycle.getEnvironmentProfileId() != null)
		{
			return environmentService.findEnvironmentGroupsForProfile(testCycle.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public void saveEnvironmentGroupsForTestCycle(final Integer testCycleId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		// cannot change after activation
		final TestCycle testCycle = dao.getById(TestCycle.class, testCycleId_);
		if (testCycle == null)
		{
			throw new NotFoundException("TestCycle not found: " + testCycleId_);
		}
		if (!TcmEntityStatus.DRAFT.equals(testCycle.getTestCycleStatusId()))
		{
			throw new DeletingActivatedEntityException(TestCycle.class.getSimpleName());
		}
		// check that groups are included in the parent profile
		final Product product = dao.getById(Product.class, testCycle.getProductId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(product.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		// update environment profile
		final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test cycle : " + testCycleId_,
				"Included groups: " + environmentGroupIds_.toString(), environmentGroupIds_);
		testCycle.setEnvironmentProfileId(environmentProfile.getId());
		testCycle.setVersion(originalVersionId_);
		dao.merge(testCycle);
	}

	@Override
	public void deleteTestCycle(final Integer testCycleId_) throws Exception
	{
		final TestCycle testCycle = dao.getById(TestCycle.class, testCycleId_);
		if (testCycle == null)
		{
			throw new NotFoundException("TestCycle not found. Id: " + testCycleId_);
		}
		if (!TcmEntityStatus.DRAFT.equals(testCycle.getTestCycleStatusId()))
		{
			throw new DeletingActivatedEntityException(TestCycle.class.getSimpleName());
		}
		// delete all included test runs
		final List<TestRun> includedTestRuns = findTestRunsForTestCycle(testCycleId_);
		for (final TestRun testRun : includedTestRuns)
		{
			testRunService.deleteTestRun(testRun.getId());
		}
		// delete test cycle
		dao.delete(testCycle);
	}

	@Override
	public UtestSearchResult findTestCycles(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(TestCycle.class, search_);
	}

	@Override
	public TestCycle getTestCycle(final Integer testCycleId_) throws Exception
	{
		final TestCycle testCycle = dao.getById(TestCycle.class, testCycleId_);
		if (testCycle == null)
		{
			throw new NotFoundException("TestCycle not found: " + testCycleId_);
		}
		return testCycle;

	}

	@Override
	public List<TestRun> findTestRunsForTestCycle(final Integer testCycleId_) throws Exception
	{
		final Search search = new Search(TestRun.class);
		search.addFilterEqual("testCycleId", testCycleId_);
		return dao.search(TestRun.class, search);
	}

	@Override
	public TestCycle saveTestCycle(final Integer testCycleId_, final String name_, final String description_, final Date startDate_, final Date endDate_,
			final boolean communityAuthoringAllowed_, final boolean communityAccessAllowed_, final Integer originalVersionId_) throws Exception
	{
		final TestCycle testCycle = dao.getById(TestCycle.class, testCycleId_);
		if (testCycle == null)
		{
			throw new NotFoundException("TestCycle not found: " + testCycleId_);
		}
		if (!TcmEntityStatus.DRAFT.equals(testCycle.getTestCycleStatusId()))
		{
			throw new ChangingActivatedEntityException(TestCycle.class.getSimpleName() + " : " + testCycleId_);
		}
		checkForDuplicateNameWithinParent(TestCycle.class, name_, testCycle.getProductId(), "productId", testCycleId_);

		testCycle.setName(name_);
		testCycle.setDescription(description_);
		testCycle.setStartDate(startDate_);
		testCycle.setEndDate(endDate_);
		testCycle.setCommunityAccessAllowed(communityAuthoringAllowed_);
		testCycle.setCommunityAuthoringAllowed(communityAuthoringAllowed_);
		testCycle.setVersion(originalVersionId_);
		return dao.merge(testCycle);
	}

	@Override
	public TestCycle activateTestCycle(final Integer testCycleId_, final Integer originalVersionId_) throws Exception
	{
		// change status for the test run
		final TestCycle testCycle = dao.getById(TestCycle.class, testCycleId_);
		if (testCycle == null)
		{
			throw new NotFoundException("TestCycle not found: " + testCycleId_);
		}
		testCycle.setTestCycleStatusId(TcmEntityStatus.ACTIVATED);
		testCycle.setVersion(originalVersionId_);
		return dao.merge(testCycle);
	}

	@Override
	public TestCycle lockTestCycle(final Integer testCycleId_, final Integer originalVersionId_) throws Exception
	{
		// change status for the test run
		final TestCycle testCycle = dao.getById(TestCycle.class, testCycleId_);
		if (testCycle == null)
		{
			throw new NotFoundException("TestCycle not found: " + testCycleId_);
		}

		// lock all included test runs
		final List<TestRun> includedTestRuns = findTestRunsForTestCycle(testCycleId_);
		for (final TestRun testRun : includedTestRuns)
		{
			testRunService.lockTestRun(testRun.getId(), testRun.getVersion());
		}
		// lock test cycle
		testCycle.setTestCycleStatusId(TcmEntityStatus.LOCKED);
		testCycle.setVersion(originalVersionId_);
		return dao.merge(testCycle);
	}
}
