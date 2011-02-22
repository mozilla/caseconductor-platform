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
import com.utest.domain.AccessRole;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Product;
import com.utest.domain.Team;
import com.utest.domain.TestCycle;
import com.utest.domain.TestCycleStatus;
import com.utest.domain.TestRun;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TeamService;
import com.utest.domain.service.TestCycleService;
import com.utest.domain.service.TestRunService;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.NoTeamDefinitionException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;
import com.utest.exception.UnsupportedTeamSelectionException;

public class TestCycleServiceImpl extends BaseServiceImpl implements TestCycleService
{
	private final TypelessDAO			dao;
	private final EnvironmentService	environmentService;
	private final TestRunService		testRunService;
	private final TeamService			teamService;

	/**
	 * Default constructor
	 */
	public TestCycleServiceImpl(final TypelessDAO dao, final TestRunService testRunService, final EnvironmentService environmentService, final TeamService teamService)
	{
		super(dao);
		this.dao = dao;
		this.environmentService = environmentService;
		this.testRunService = testRunService;
		this.teamService = teamService;
	}

	@Override
	public TestCycle addTestCycle(final Integer productId_, final String name_, final String description_, final Date startDate_, final Date endDate_,
			final boolean communityAuthoringAllowed_, final boolean communityAccessAllowed_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		checkForDuplicateNameWithinParent(TestCycle.class, name_, productId_, "productId", null);

		final TestCycle testCycle = new TestCycle();
		testCycle.setTestCycleStatusId(TestCycleStatus.PENDING);
		testCycle.setProductId(productId_);
		testCycle.setName(name_);
		testCycle.setDescription(description_);
		testCycle.setStartDate(startDate_);
		testCycle.setEndDate(endDate_);
		testCycle.setCommunityAccessAllowed(communityAuthoringAllowed_);
		testCycle.setCommunityAuthoringAllowed(communityAuthoringAllowed_);
		// set environment profile from product by default
		testCycle.setEnvironmentProfileId(product.getEnvironmentProfileId());
		// set team from product by default
		testCycle.setTeamId(product.getTeamId());

		final Integer testCycleId = dao.addAndReturnId(testCycle);
		dao.flush();
		return getTestCycle(testCycleId);
	}

	@Override
	public List<User> getTestingTeamForTestCycle(final Integer testCycleId_) throws Exception
	{
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		if (testCycle.getTeamId() != null)
		{
			return teamService.getTeamUsers(testCycle.getTeamId());
		}
		else
		{
			return new ArrayList<User>();
		}
	}

	@Override
	public void saveTestingTeamForTestCycle(final Integer testCycleId_, final List<Integer> userIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		final Product product = getRequiredEntityById(Product.class, testCycle.getProductId());
		// check that users are selected from community users or users from the
		// matching company
		if (!isValidSelectionForCompany(product.getCompanyId(), userIds_, User.class))
		{
			throw new UnsupportedTeamSelectionException("Selecting testers from other company.");
		}
		// update team profile
		Team team = null;
		if ((product.getTeamId() != null && product.getTeamId() == testCycle.getTeamId()) || testCycle.getTeamId() == null)
		{
			team = teamService.addTeam(product.getCompanyId(), "Created for test cycle : " + testCycleId_, "Included users: " + userIds_.toString());
			teamService.saveTeamUsers(team.getId(), userIds_, team.getVersion());
			testCycle.setTeamId(team.getId());
		}
		else
		{
			team = getRequiredEntityById(Team.class, testCycle.getTeamId());
			teamService.saveTeamUsers(team.getId(), userIds_, team.getVersion());
		}
		// update version
		testCycle.setVersion(originalVersionId_);
		dao.merge(testCycle);
	}

	@Override
	public List<AccessRole> getTestingTeamMemberRolesForTestCycle(final Integer testCycleId_, final Integer userId_) throws Exception
	{
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		// update team profile
		if (testCycle.getTeamId() != null)
		{
			return teamService.getTeamUserRoles(testCycle.getTeamId(), userId_);
		}
		else
		{
			return new ArrayList<AccessRole>();
		}
	}

	@Override
	public void saveTestingTeamMemberRolesForTestCycle(final Integer testCycleId_, final Integer userId_, final List<Integer> roleIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		// update team profile
		if (testCycle.getTeamId() != null)
		{
			Team team = getRequiredEntityById(Team.class, testCycle.getTeamId());
			teamService.saveTeamUserRoles(testCycle.getTeamId(), userId_, roleIds_, team.getVersion());
		}
		else
		{
			throw new NoTeamDefinitionException("No team defined for TestCycle: " + testCycleId_);
		}
		// update version
		testCycle.setVersion(originalVersionId_);
		dao.merge(testCycle);
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForTestCycle(final Integer testCycleId_) throws Exception
	{
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		if (testCycle.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfile(testCycle.getEnvironmentProfileId());
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
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		if (!TestCycleStatus.PENDING.equals(testCycle.getTestCycleStatusId()))
		{
			throw new DeletingActivatedEntityException(TestCycle.class.getSimpleName());
		}
		// check that groups are included in the parent profile
		final Product product = getRequiredEntityById(Product.class, testCycle.getProductId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(product.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		// update environment profile
		EnvironmentProfile environmentProfile = null;
		// create new one if still uses parent's profile
		if ((product.getEnvironmentProfileId() != null && product.getEnvironmentProfileId() == testCycle.getEnvironmentProfileId()) || testCycle.getEnvironmentProfileId() == null)
		{
			environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test cycle : " + testCycleId_, "Included groups: "
					+ environmentGroupIds_.toString(), environmentGroupIds_);
		}
		// or update existing profile
		else
		{
			environmentService.saveEnvironmentGroupsForProfile(testCycle.getEnvironmentProfileId(), environmentGroupIds_);
		}
		testCycle.setEnvironmentProfileId(environmentProfile.getId());
		testCycle.setVersion(originalVersionId_);
		dao.merge(testCycle);
	}

	@Override
	public void deleteTestCycle(final Integer testCycleId_, final Integer originalVersionId_) throws Exception
	{
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		if (!TestCycleStatus.PENDING.equals(testCycle.getTestCycleStatusId()))
		{
			throw new DeletingActivatedEntityException(TestCycle.class.getSimpleName());
		}
		// delete all included test runs
		final List<TestRun> includedTestRuns = getTestRunsForTestCycle(testCycleId_);
		for (final TestRun testRun : includedTestRuns)
		{
			testRunService.deleteTestRun(testRun.getId(), testRun.getVersion());
		}
		// delete test cycle
		testCycle.setVersion(originalVersionId_);
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
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		return testCycle;

	}

	@Override
	public List<TestRun> getTestRunsForTestCycle(final Integer testCycleId_) throws Exception
	{
		final Search search = new Search(TestRun.class);
		search.addFilterEqual("testCycleId", testCycleId_);
		return dao.search(TestRun.class, search);
	}

	@Override
	public TestCycle saveTestCycle(final Integer testCycleId_, final String name_, final String description_, final Date startDate_, final Date endDate_,
			final boolean communityAuthoringAllowed_, final boolean communityAccessAllowed_, final Integer originalVersionId_) throws Exception
	{
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		if (!TestCycleStatus.PENDING.equals(testCycle.getTestCycleStatusId()))
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
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		testCycle.setTestCycleStatusId(TestCycleStatus.ACTIVE);
		testCycle.setVersion(originalVersionId_);
		return dao.merge(testCycle);
	}

	@Override
	public TestCycle lockTestCycle(final Integer testCycleId_, final Integer originalVersionId_) throws Exception
	{
		// change status for the test run
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);

		// lock all included test runs
		final List<TestRun> includedTestRuns = getTestRunsForTestCycle(testCycleId_);
		for (final TestRun testRun : includedTestRuns)
		{
			testRunService.lockTestRun(testRun.getId(), testRun.getVersion());
		}
		// lock test cycle
		testCycle.setTestCycleStatusId(TestCycleStatus.LOCKED);
		testCycle.setVersion(originalVersionId_);
		return dao.merge(testCycle);
	}
}
