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

import org.springframework.security.access.AccessDeniedException;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.AccessRole;
import com.utest.domain.ApprovalStatus;
import com.utest.domain.Attachment;
import com.utest.domain.EntityExternalBug;
import com.utest.domain.EntityType;
import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Permission;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.Team;
import com.utest.domain.TeamUser;
import com.utest.domain.TestCaseStatus;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.TestCycle;
import com.utest.domain.TestCycleStatus;
import com.utest.domain.TestPlan;
import com.utest.domain.TestPlanStatus;
import com.utest.domain.TestPlanTestSuite;
import com.utest.domain.TestRun;
import com.utest.domain.TestRunResult;
import com.utest.domain.TestRunResultStatus;
import com.utest.domain.TestRunStatus;
import com.utest.domain.TestRunTestCase;
import com.utest.domain.TestRunTestCaseAssignment;
import com.utest.domain.TestSuite;
import com.utest.domain.TestSuiteStatus;
import com.utest.domain.TestSuiteTestCase;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.AttachmentService;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.ExternalBugService;
import com.utest.domain.service.TeamService;
import com.utest.domain.service.TestCaseService;
import com.utest.domain.service.TestPlanService;
import com.utest.domain.service.TestRunService;
import com.utest.domain.service.TestSuiteService;
import com.utest.domain.service.UserService;
import com.utest.domain.view.CategoryValue;
import com.utest.domain.view.TestRunTestCaseView;
import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ApprovingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.IncludingMultileVersionsOfSameEntityException;
import com.utest.exception.IncludingNotActivatedEntityException;
import com.utest.exception.InvalidUserException;
import com.utest.exception.NoTeamDefinitionException;
import com.utest.exception.SelfAssignmentException;
import com.utest.exception.TestCaseExecutionBlockedException;
import com.utest.exception.TestCaseExecutionWithoutRestartException;
import com.utest.exception.TestCycleClosedException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;
import com.utest.exception.UnsupportedTeamSelectionException;
import com.utest.util.DateUtil;

public class TestRunServiceImpl extends BaseServiceImpl implements TestRunService
{
	private final TestPlanService		testPlanService;
	private final TestSuiteService		testSuiteService;
	private final TestCaseService		testCaseService;
	private final TeamService			teamService;
	private final UserService			userService;
	private final AttachmentService		attachmentService;
	private final ExternalBugService	externalBugService;

	/**
	 * Default constructor
	 */
	public TestRunServiceImpl(final TypelessDAO dao, final TestPlanService testPlanService, final TestSuiteService testSuiteService, final TestCaseService testCaseService,
			final EnvironmentService environmentService, final TeamService teamService, final UserService userService, final AttachmentService attachmentService,
			final ExternalBugService externalBugService)
	{
		super(dao, environmentService);
		this.environmentService = environmentService;
		this.testPlanService = testPlanService;
		this.testSuiteService = testSuiteService;
		this.testCaseService = testCaseService;
		this.teamService = teamService;
		this.userService = userService;
		this.attachmentService = attachmentService;
		this.externalBugService = externalBugService;
	}

	@Override
	public TestRun addTestRun(final Integer testCycleId_, final boolean useLatestVersions_, final String name_, final String description_, final Date startDate_,
			final Date endDate_, final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_, final boolean autoAssignToTeam_)
			throws Exception
	{
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testCycleId_);
		checkForDuplicateNameWithinParent(TestRun.class, name_, testCycleId_, "testCycleId", null);

		// TODO - do we need to validate test cycle status before creating a
		// test run?
		final TestRun testRun = new TestRun();
		testRun.setTestRunStatusId(TestRunStatus.PENDING);
		testRun.setTestCycleId(testCycleId_);
		testRun.setProductId(testCycle.getProductId());
		testRun.setCompanyId(testCycle.getCompanyId());
		testRun.setName(name_);
		testRun.setUseLatestVersions(useLatestVersions_);
		testRun.setDescription(description_);
		testRun.setStartDate(startDate_);
		testRun.setEndDate(endDate_);
		testRun.setSelfAssignAllowed(selfAssignAllowed_);
		testRun.setSelfAssignLimit(selfAssignLimit_);
		testRun.setSelfAssignPerEnvironment(selfAssignPerEnvironment_);
		testRun.setAutoAssignToTeam(autoAssignToTeam_);
		// set environment profile from test cycle by default
		testRun.setEnvironmentProfileId(testCycle.getEnvironmentProfileId());
		// set team profile from test cycle by default
		testRun.setTeamId(testCycle.getTeamId());

		final Integer testRunId = dao.addAndReturnId(testRun);
		return getTestRun(testRunId);
	}

	@Override
	public TestRun cloneTestRun(final Integer fromTestRunId_, final boolean cloneAssignments_) throws Exception
	{
		return cloneTestRun(fromTestRunId_, null, cloneAssignments_);
	}

	@Override
	public TestRun cloneTestRun(final Integer fromTestRunId_, final Integer newTestCycleId_, final boolean cloneAssignments_) throws Exception
	{
		final TestRun fromTestRun = getRequiredEntityById(TestRun.class, fromTestRunId_);
		// clone test run
		final TestRun toTestRun = new TestRun();
		toTestRun.setTestRunStatusId(TestRunStatus.PENDING);
		if (newTestCycleId_ == null)
		{
			toTestRun.setTestCycleId(fromTestRun.getTestCycleId());
		}
		else
		{
			toTestRun.setTestCycleId(newTestCycleId_);
		}
		toTestRun.setProductId(fromTestRun.getProductId());
		toTestRun.setCompanyId(fromTestRun.getCompanyId());
		toTestRun.setName("Cloned on " + new Date() + " " + fromTestRun.getName());
		toTestRun.setDescription(fromTestRun.getDescription());
		toTestRun.setStartDate(fromTestRun.getStartDate());
		toTestRun.setEndDate(null);
		toTestRun.setSelfAssignAllowed(fromTestRun.isSelfAssignAllowed());
		toTestRun.setSelfAssignLimit(fromTestRun.getSelfAssignLimit());
		toTestRun.setSelfAssignPerEnvironment(fromTestRun.isSelfAssignPerEnvironment());
		toTestRun.setAutoAssignToTeam(fromTestRun.isAutoAssignToTeam());
		toTestRun.setEnvironmentProfileId(fromTestRun.getEnvironmentProfileId());
		toTestRun.setTeamId(fromTestRun.getTeamId());
		final Integer toTestRunId = dao.addAndReturnId(toTestRun);

		// clone test cases
		List<TestRunTestCase> oldTestCases = getTestRunTestCases(fromTestRunId_);
		if (oldTestCases != null)
		{
			for (TestRunTestCase oldCase : oldTestCases)
			{
				// only include activated test cases
				TestCaseVersion includedVersion = getRequiredEntityById(TestCaseVersion.class, oldCase.getTestCaseVersionId());
				if (TestCaseStatus.ACTIVE.equals(includedVersion.getTestCaseStatusId()))
				{
					addTestRunTestCase(toTestRunId, oldCase.getTestCaseVersionId(), oldCase.getPriorityId(), oldCase.getRunOrder(), oldCase.isBlocking(), oldCase.getTestSuiteId());
				}
			}
		}
		// clone assignments if requested
		if (cloneAssignments_)
		{
			List<TestRunTestCaseAssignment> assignments = getTestRunAssignments(fromTestRunId_);
			if (assignments != null)
			{
				List<TestRunTestCase> newTestCases = getTestRunTestCases(toTestRunId);
				for (TestRunTestCaseAssignment oldAssignment : assignments)
				{
					TestRunTestCase newTestCase = getIncludedTestCase(newTestCases, oldAssignment.getTestCaseVersionId());
					if (newTestCase != null)
					{
						addAssignment(newTestCase.getId(), oldAssignment.getTesterId());
					}
				}
			}

		}
		// return newly created test run
		return getTestRun(toTestRunId);
	}

	private TestRunTestCase getIncludedTestCase(List<TestRunTestCase> includedTestCases_, Integer testCaseVersionId_)
	{
		for (TestRunTestCase includedTestCase : includedTestCases_)
		{
			if (includedTestCase.getTestCaseVersionId().equals(testCaseVersionId_))
			{
				return includedTestCase;
			}
		}
		return null;
	}

	@Override
	public List<TestRunTestCase> addTestCasesFromTestPlan(final Integer testRunId_, final Integer testPlanId_) throws Exception
	{
		final TestPlan testPlan = getRequiredEntityById(TestPlan.class, testPlanId_);
		// check if test plan is activated
		if (!TestPlanStatus.ACTIVE.equals(testPlan.getTestPlanStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestPlan.class.getSimpleName() + " : " + testPlanId_);
		}
		final List<TestPlanTestSuite> includedTestSuites = testPlanService.getTestPlanTestSuites(testPlanId_);
		Integer startingRunOrder = 0;
		boolean added = false;
		for (final TestPlanTestSuite testPlanTestSuite : includedTestSuites)
		{
			// increment by 1 before adding new test suite cases
			startingRunOrder++;
			try
			{
				startingRunOrder = addTestCasesFromTestSuite(testRunId_, testPlanTestSuite.getTestSuiteId(), startingRunOrder);
				added = true;
			}
			catch (IncludingNotActivatedEntityException e)
			{
				// do nothing now only throw if none of the test cases were
				// added
			}
		}
		if (!added)
		{
			throw new IncludingNotActivatedEntityException("None of the test cases from test plan were active: " + testPlanId_);
		}
		return getTestRunTestCases(testRunId_);
	}

	@Override
	public List<TestRunTestCase> addTestCasesFromTestSuite(final Integer testRunId_, final Integer testSuiteId_) throws Exception
	{
		addTestCasesFromTestSuite(testRunId_, testSuiteId_, 0);
		return getTestRunTestCases(testRunId_);
	}

	private Integer addTestCasesFromTestSuite(final Integer testRunId_, final Integer testSuiteId_, final Integer startingRunOrder_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		final TestSuite testSuite = getRequiredEntityById(TestSuite.class, testSuiteId_);
		// check if products match
		checkProductMatch(testRun, testSuite);
		// check if test suite is activated
		if (!TestSuiteStatus.ACTIVE.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		final List<TestSuiteTestCase> includedTestCases = testSuiteService.getTestSuiteTestCases(testSuiteId_);
		Integer lastRunOrder = startingRunOrder_;
		boolean added = false;
		for (final TestSuiteTestCase testSuiteTestCase : includedTestCases)
		{
			lastRunOrder += testSuiteTestCase.getRunOrder();
			try
			{
				addTestRunTestCase(testRun.getId(), testSuiteTestCase.getTestCaseVersionId(), testSuiteTestCase.getPriorityId(), testSuiteTestCase.getRunOrder(), testSuiteTestCase
						.isBlocking(), testSuiteId_);
				added = true;
			}
			catch (IncludingNotActivatedEntityException e)
			{
				// do nothing now, only throw if none of the test cases were
				// added
			}
		}
		if (!added)
		{
			throw new IncludingNotActivatedEntityException("None of the test cases from test suite were active: " + testSuiteId_);
		}
		return lastRunOrder;
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForTestRun(final Integer testRunId_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		if (testRun.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfile(testRun.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestRun(final Integer testRunId_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		if (testRun.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfileExploded(testRun.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroupExploded>();
		}
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForTestRunTestCase(final Integer testRunTestCaseId_) throws Exception
	{
		final TestRunTestCase testRunTestCase = getRequiredEntityById(TestRunTestCase.class, testRunTestCaseId_);
		if (testRunTestCase.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfile(testRunTestCase.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestRunTestCase(final Integer testRunTestCaseId_) throws Exception
	{
		final TestRunTestCase testRunTestCase = getRequiredEntityById(TestRunTestCase.class, testRunTestCaseId_);
		if (testRunTestCase.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfileExploded(testRunTestCase.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroupExploded>();
		}
	}

	@Override
	public List<Environment> getEnvironmentsForTestResult(final Integer resultId_) throws Exception
	{
		final TestRunResult result = getRequiredEntityById(TestRunResult.class, resultId_);
		if (result.getEnvironmentGroupId() != null)
		{
			return environmentService.getEnvironmentsForGroup(result.getEnvironmentGroupId());
		}
		else
		{
			return new ArrayList<Environment>();
		}
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForAssignment(final Integer assignmentId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = getRequiredEntityById(TestRunTestCaseAssignment.class, assignmentId_);
		if (assignment.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfile(assignment.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForAssignment(final Integer assignmentId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = getRequiredEntityById(TestRunTestCaseAssignment.class, assignmentId_);
		if (assignment.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfileExploded(assignment.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroupExploded>();
		}
	}

	@Override
	public void saveEnvironmentGroupsForTestRun(final Integer testRunId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		// cannot change after activation
		if (!TestRunStatus.PENDING.equals(testRun.getTestRunStatusId()))
		{
			throw new DeletingActivatedEntityException(TestRun.class.getSimpleName());
		}
		// prevent from changing if there are any test cases included already?
		final Search search = new Search(TestRunTestCase.class);
		search.addFilterEqual("testRunId", testRunId_);
		final List<TestRunTestCase> foundItems = dao.search(TestRunTestCase.class, search);
		if ((foundItems != null) && !foundItems.isEmpty())
		{
			throw new UnsupportedEnvironmentSelectionException(TestRun.class.getSimpleName() + " : " + testRunId_);
		}
		// check that groups are included in the parent profile
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testRun.getTestCycleId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(testCycle.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		final Product product = getRequiredEntityById(Product.class, testRun.getProductId());
		// update environment profile
		adjustParentChildProfiles(testCycle, testRun, product.getCompanyId(), environmentGroupIds_);
		testRun.setVersion(originalVersionId_);
		dao.merge(testRun);
	}

	@Override
	public void saveEnvironmentGroupsForTestRunTestCase(final Integer testRunTestCaseId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRunTestCase testRunTestCase = getRequiredEntityById(TestRunTestCase.class, testRunTestCaseId_);
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunTestCase.getTestRunId());
		// cannot change after activation
		if (!TestRunStatus.PENDING.equals(testRun.getTestRunStatusId()))
		{
			throw new ChangingActivatedEntityException(TestRun.class.getSimpleName());
		}
		// check that groups are included in the parent profile
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(testRun.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		// update environment profile
		final Product product = getRequiredEntityById(Product.class, testRun.getProductId());
		// always create new profile instead of calling
		// adjustParentChildProfiles(), because included test case could have
		// different parents, not just a Product
		EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test run test case: " + testRunTestCaseId_,
				"Included groups: " + environmentGroupIds_.toString(), environmentGroupIds_);
		testRunTestCase.setEnvironmentProfileId(environmentProfile.getId());
		testRunTestCase.setVersion(originalVersionId_);
		dao.merge(testRunTestCase);
	}

	@Override
	public void saveEnvironmentGroupsForAssignment(final Integer assignmentId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRunTestCaseAssignment assignment = getRequiredEntityById(TestRunTestCaseAssignment.class, assignmentId_);
		final TestRunTestCase testRunTestCase = getRequiredEntityById(TestRunTestCase.class, assignment.getTestRunTestCaseId());
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunTestCase.getTestRunId());
		// cannot change after activation
		if (!TestRunStatus.PENDING.equals(testRun.getTestRunStatusId()))
		{
			throw new ChangingActivatedEntityException(TestRun.class.getSimpleName());
		}
		// check that groups are included in the parent profile
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(testRunTestCase.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		// update environment profile
		final Product product = getRequiredEntityById(Product.class, testRun.getProductId());
		// update environment profile
		adjustParentChildProfiles(testRunTestCase, assignment, product.getCompanyId(), environmentGroupIds_);
		assignment.setVersion(originalVersionId_);
		dao.merge(assignment);

		// generate results for new assignment
		addResultsForAssignment(assignment);
	}

	@Override
	public TestRunTestCase addTestRunTestCase(final Integer testRunId_, final Integer testCaseVersionId_) throws Exception
	{
		return addTestRunTestCase(testRunId_, testCaseVersionId_, 0, 0, false, null);
	}

	@Override
	public TestRunTestCase addTestRunTestCase(final Integer testRunId_, final Integer testCaseVersionId_, final Integer priorityId_, final Integer runOrder_,
			final boolean isBlocking_) throws Exception
	{
		return addTestRunTestCase(testRunId_, testCaseVersionId_, priorityId_, runOrder_, isBlocking_, null);
	}

	@Override
	public TestRunTestCase addTestRunTestCase(final Integer testRunId_, final Integer testCaseVersionId_, final Integer priorityId_, final Integer runOrder_,
			final boolean isBlocking_, final Integer testSuiteId_) throws Exception
	{
		final TestCaseVersion testCaseVersion = getRequiredEntityById(TestCaseVersion.class, testCaseVersionId_);
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		// check if products match
		checkProductMatch(testRun, testCaseVersion);
		// prevent if already activated
		if (!TestRunStatus.PENDING.equals(testRun.getTestRunStatusId()))
		{
			throw new ChangingActivatedEntityException(TestRunTestCase.class.getSimpleName());
		}
		// prevent if test case not activated
		if (!TestCaseStatus.ACTIVE.equals(testCaseVersion.getTestCaseStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestCaseVersion.class.getSimpleName() + " : " + testCaseVersionId_);
		}
		// prevent if another version of the same test case already included
		final Search search = new Search(TestRunTestCase.class);
		search.addFilterEqual("testRunId", testRunId_);
		search.addFilterEqual("testCaseId", testCaseVersion.getTestCaseId());
		final List<TestRunTestCase> foundItems = dao.search(TestRunTestCase.class, search);
		if ((foundItems != null) && !foundItems.isEmpty())
		{
			TestRunTestCase priorTestRunTestCase = foundItems.get(0);
			if (priorTestRunTestCase.getTestCaseVersionId().equals(testCaseVersionId_))
			{
				// do nothing
				return priorTestRunTestCase;
			}
			else if (testRun.isUseLatestVersions())
			{
				if (priorTestRunTestCase.getTestCaseVersionId() > (testCaseVersionId_))
				{
					// do nothing
					return priorTestRunTestCase;
				}
				else
				{
					// delete prior test case before adding the latest version
					deleteTestRunTestCase(priorTestRunTestCase.getId(), priorTestRunTestCase.getVersion());
				}
			}
			else
			{
				throw new IncludingMultileVersionsOfSameEntityException(TestCaseVersion.class.getSimpleName() + " : " + testCaseVersionId_);
			}
		}
		TestRunTestCase includedTestCase = new TestRunTestCase();
		includedTestCase.setTestRunId(testRunId_);
		includedTestCase.setTestCycleId(testRun.getTestCycleId());
		includedTestCase.setProductId(testRun.getProductId());
		includedTestCase.setCompanyId(testRun.getCompanyId());
		includedTestCase.setTestCaseId(testCaseVersion.getTestCaseId());
		includedTestCase.setTestCaseVersionId(testCaseVersion.getId());
		includedTestCase.setSelfAssignAllowed(testRun.isSelfAssignAllowed());
		includedTestCase.setSelfAssignPerEnvironment(testRun.isSelfAssignPerEnvironment());
		includedTestCase.setSelfAssignLimit(testRun.getSelfAssignLimit());
		includedTestCase.setPriorityId(priorityId_);
		includedTestCase.setRunOrder(runOrder_);
		includedTestCase.setBlocking(isBlocking_);
		includedTestCase.setTestSuiteId(testSuiteId_);
		if (testRun.getEnvironmentProfileId() != null && testCaseVersion.getEnvironmentProfileId() == null)
		{
			includedTestCase.setEnvironmentProfileId(testRun.getEnvironmentProfileId());
		}
		else if (testRun.getEnvironmentProfileId() == null && testCaseVersion.getEnvironmentProfileId() != null)
		{
			includedTestCase.setEnvironmentProfileId(testCaseVersion.getEnvironmentProfileId());
		}
		else if (testRun.getEnvironmentProfileId() != null && testCaseVersion.getEnvironmentProfileId() != null)
		{
			if (testRun.getEnvironmentProfileId().equals(testCaseVersion.getEnvironmentProfileId()))
			{
				includedTestCase.setEnvironmentProfileId(testRun.getEnvironmentProfileId());
			}
			else
			{
				// if no matching groups were found returns null.
				EnvironmentProfile intersectedProfile = environmentService.intersectEnvironmentProfiles(testRun.getEnvironmentProfileId(), testCaseVersion
						.getEnvironmentProfileId());
				if (intersectedProfile != null)
				{
					includedTestCase.setEnvironmentProfileId(intersectedProfile.getId());
				}
			}
		}
		includedTestCase = dao.merge(includedTestCase);
		//
		autoAssignTestCaseToTeam(testRun, includedTestCase);
		return includedTestCase;
	}

	private void autoAssignTestCaseToTeam(TestRun testRun_, TestRunTestCase includedTestCase_) throws Exception
	{
		if (!testRun_.isAutoAssignToTeam())
		{
			return;
		}
		List<User> teamMembers = getTestingTeamForTestRun(testRun_.getId());
		if (teamMembers != null)
		{
			for (User teamMember : teamMembers)
			{
				addAssignment(includedTestCase_.getId(), teamMember.getId());
			}
		}
	}

	private void autoAssignAllTestCasesToTeam(TestRun testRun_) throws Exception
	{
		if (!testRun_.isAutoAssignToTeam())
		{
			return;
		}
		List<User> teamMembers = getTestingTeamForTestRun(testRun_.getId());
		if (teamMembers == null || teamMembers.isEmpty())
		{
			return;
		}
		List<TestRunTestCase> includedTestCases = getTestRunTestCases(testRun_.getId());
		if (includedTestCases == null || includedTestCases.isEmpty())
		{
			return;
		}
		List<TestRunTestCaseAssignment> assignments = getTestRunAssignments(testRun_.getId());

		for (TestRunTestCase includedTestCase : includedTestCases)
		{
			for (User teamMember : teamMembers)
			{
				boolean found = false;
				for (TestRunTestCaseAssignment assignment : assignments)
				{
					if (includedTestCase.getId().equals(assignment.getTestRunTestCaseId()) && teamMember.getId().equals(assignment.getTesterId()))
					{
						found = true;
						break;
					}
				}
				if (!found)
				{
					addAssignment(includedTestCase.getId(), teamMember.getId());
				}
			}
		}
	}

	@Override
	public TestRunTestCaseAssignment addAssignment(final Integer testRunTestCaseId_, final Integer testerId_, final List<Integer> environmentGroupIds_) throws Exception
	{
		final TestRunTestCase includedTestCase = getRequiredEntityById(TestRunTestCase.class, testRunTestCaseId_);
		@SuppressWarnings("unused")
		final User tester = getRequiredEntityById(User.class, testerId_);
		// check if groups assignment is correct
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(includedTestCase.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}

		TestRunTestCaseAssignment assignment = addAssignmentNoResults(testRunTestCaseId_, testerId_);
		// create new profile for new group selection
		final TestRun testRun = getRequiredEntityById(TestRun.class, includedTestCase.getTestRunId());
		final Product product = getRequiredEntityById(Product.class, testRun.getProductId());
		final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test run assignment. Test run: "
				+ testRun.getId() + ", tester: " + testerId_, "Included groups: " + environmentGroupIds_.toString(), environmentGroupIds_);
		assignment.setEnvironmentProfileId(environmentProfile.getId());
		assignment = dao.merge(assignment);
		// generate results for new assignment
		addResultsForAssignment(assignment);
		return assignment;
	}

	@Override
	public TestRunTestCaseAssignment addAssignment(final Integer testRunTestCaseId_, final Integer testerId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = addAssignmentNoResults(testRunTestCaseId_, testerId_);
		// generate results for new assignment
		addResultsForAssignment(assignment);
		return assignment;
	}

	private TestRunTestCaseAssignment addAssignmentNoResults(final Integer testRunTestCaseId_, final Integer testerId_) throws Exception
	{
		// everyone has self assign permission by default, so need to check if
		// user has permission to assign to others
		checkAssignmentPermission(testerId_);

		final TestRunTestCase includedTestCase = getRequiredEntityById(TestRunTestCase.class, testRunTestCaseId_);
		final TestRun testRun = getRequiredEntityById(TestRun.class, includedTestCase.getTestRunId());
		final User tester = getRequiredEntityById(User.class, testerId_);
		final Product product = getRequiredEntityById(Product.class, testRun.getProductId());
		// prevent assigning to self if on self-assigned allowed
		if (getCurrentUserId().equals(testerId_) && !testRun.isSelfAssignAllowed())
		{
			throw new SelfAssignmentException(TestCaseVersion.class.getSimpleName() + " : " + includedTestCase.getTestCaseId() + " / Tester: " + testerId_);
		}
		// check that users are selected from community users or users from the
		// matching company
		List<Integer> userIds = new ArrayList<Integer>();
		userIds.add(testerId_);
		if (!isValidSelectionForCompany(product.getCompanyId(), userIds, User.class))
		{
			throw new UnsupportedTeamSelectionException("Selecting testers from other company.");
		}
		// prevent if another version of the same test case already included
		final Search search = new Search(TestRunTestCaseAssignment.class);
		search.addFilterEqual("testerId", testerId_);
		search.addFilterEqual("testRunTestCaseId", testRunTestCaseId_);
		final List<TestRunTestCaseAssignment> foundItems = dao.search(TestRunTestCaseAssignment.class, search);
		if ((foundItems == null) || foundItems.isEmpty())
		{
			// throw new
			// AssigningMultileVersionsOfSameEntityException(TestCaseVersion.class.getSimpleName()
			// + " : " + includedTestCase.getTestCaseId() + " / Tester: " +
			// testerId_);
			final TestRunTestCaseAssignment assignment = new TestRunTestCaseAssignment();
			assignment.setTestRunId(testRun.getId());
			assignment.setProductId(testRun.getProductId());
			assignment.setTestCycleId(testRun.getTestCycleId());
			assignment.setCompanyId(testRun.getCompanyId());
			assignment.setTestCaseId(includedTestCase.getTestCaseId());
			assignment.setTestCaseVersionId(includedTestCase.getTestCaseVersionId());
			assignment.setTestRunTestCaseId(includedTestCase.getId());
			assignment.setTestSuiteId(includedTestCase.getTestSuiteId());
			assignment.setTesterId(tester.getId());
			assignment.setEnvironmentProfileId(includedTestCase.getEnvironmentProfileId());
			final Integer id = dao.addAndReturnId(assignment);
			return getRequiredEntityById(TestRunTestCaseAssignment.class, id);
		}
		else
		{
			return foundItems.get(0);
		}

	}

	private void checkAssignmentPermission(Integer testerId_) throws AccessDeniedException
	{
		if (!getCurrentUserId().equals(testerId_) && !userService.isUserInPermission(getCurrentUserId(), Permission.TEST_RUN_TEST_CASE_ASSIGN))
		{
			throw new AccessDeniedException("User doesn't have permissions to assign test cases.");
		}
	}

	private void addResultsForAssignment(final TestRunTestCaseAssignment assignment_) throws Exception
	{
		// delete old results for this assignment if still pending
		final Search search = new Search(TestRunResult.class);
		search.addFilterEqual("testRunAssignmentId", assignment_.getId());
		search.addFilterIn("testRunResultStatusId", TestRunResultStatus.PENDING);
		List<TestRunResult> results = dao.search(TestRunResult.class, search);
		dao.delete(results);
		// insert new results
		if (assignment_.getEnvironmentProfileId() != null)
		{
			final List<EnvironmentGroup> groups = environmentService.getEnvironmentGroupsForProfile(assignment_.getEnvironmentProfileId());
			for (final EnvironmentGroup group : groups)
			{
				addResultForEnvironmentGroup(assignment_, group.getId());
			}
		}
		else
		{
			addResultForEnvironmentGroup(assignment_, null);
		}
	}

	private TestRunResult addResultForEnvironmentGroup(final Integer assignmentId_, final Integer environmentGroupId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = getRequiredEntityById(TestRunTestCaseAssignment.class, assignmentId_);
		return addResultForEnvironmentGroup(assignment, environmentGroupId_);
	}

	private TestRunResult addResultForEnvironmentGroup(final TestRunTestCaseAssignment assignment_, final Integer environmentGroupId_) throws Exception
	{

		final TestRunResult result = new TestRunResult();
		result.setApprovalStatusId(ApprovalStatus.PENDING);
		result.setTestRunResultStatusId(TestRunResultStatus.PENDING);
		result.setEnvironmentGroupId(environmentGroupId_);
		result.setProductId(assignment_.getProductId());
		result.setTestRunId(assignment_.getTestRunId());
		result.setTestCycleId(assignment_.getTestCycleId());
		result.setCompanyId(assignment_.getCompanyId());
		result.setTestSuiteId(assignment_.getTestSuiteId());
		result.setTestCaseId(assignment_.getTestCaseId());
		result.setTestCaseVersionId(assignment_.getTestCaseVersionId());
		result.setTesterId(assignment_.getTesterId());
		result.setTestRunAssignmentId(assignment_.getId());
		return dao.merge(result);
	}

	@Override
	public void deleteTestRun(final Integer testRunId_, final Integer originalVersionId_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		if (!TestRunStatus.PENDING.equals(testRun.getTestRunStatusId()))
		{
			throw new DeletingActivatedEntityException(TestRun.class.getSimpleName());
		}
		// delete all included test cases
		final List<TestRunTestCase> includedTestCases = getTestRunTestCases(testRunId_);
		dao.delete(includedTestCases);
		// delete assignments
		final List<TestRunTestCaseAssignment> assignments = getTestRunAssignments(testRunId_);
		dao.delete(assignments);
		// delete results
		final List<TestRunResult> results = getTestRunResults(testRunId_);
		dao.delete(results);

		testRun.setVersion(originalVersionId_);
		// delete test run
		dao.delete(testRun);
	}

	@Override
	public void deleteTestRunTestCase(final Integer testRunTestCaseId_, final Integer originalVersionId_) throws Exception
	{
		final TestRunTestCase includedTestCase = getRequiredEntityById(TestRunTestCase.class, testRunTestCaseId_);
		final TestRun testRun = getRequiredEntityById(TestRun.class, includedTestCase.getTestRunId());
		// prevent if already activated
		if (!TestRunStatus.PENDING.equals(testRun.getTestRunStatusId()))
		{
			throw new DeletingActivatedEntityException(TestRunTestCase.class.getSimpleName());
		}
		includedTestCase.setVersion(originalVersionId_);
		dao.delete(includedTestCase);
	}

	@Override
	public void deleteAssignment(final Integer assignmentId_, final Integer originalVersionId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = getRequiredEntityById(TestRunTestCaseAssignment.class, assignmentId_);
		// prevent if already any of the results were executed
		final Search search = new Search(TestRunResult.class);
		search.addFilterEqual("testRunAssignmentId", assignmentId_);
		final List<TestRunResult> results = dao.search(TestRunResult.class, search);
		for (final TestRunResult result : results)
		{
			if (!TestRunResultStatus.PENDING.equals(result.getTestRunResultStatusId()))
			{
				throw new DeletingActivatedEntityException(TestRunResult.class.getSimpleName() + " : " + result.getId());
			}
		}
		// delete generated results
		dao.delete(results);
		// delete assignment
		assignment.setVersion(originalVersionId_);
		dao.delete(assignment);
	}

	@Override
	public UtestSearchResult findTestRunAssignments(final UtestSearch search_, Integer includedEnvironmentId_) throws Exception
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
		return dao.getBySearch(TestRunTestCaseAssignment.class, search_);
	}

	@Override
	public UtestSearchResult findTestRunTestCases(final UtestSearch search_, Integer includedEnvironmentId_) throws Exception
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
		return dao.getBySearch(TestRunTestCaseView.class, search_);
	}

	@Override
	public UtestSearchResult findTestRunResults(final UtestSearch search_, Integer includedEnvironmentId_) throws Exception
	{
		if (includedEnvironmentId_ != null)
		{
			List<Integer> profileIds = environmentService.getGroupsContainingEnvironment(includedEnvironmentId_);
			if (profileIds != null && !profileIds.isEmpty())
			{
				search_.addFilterIn("environmentGroupId", profileIds);
			}
			else
			{
				return new UtestSearchResult();
			}
		}
		return dao.getBySearch(TestRunResult.class, search_);
	}

	@Override
	public UtestSearchResult findTestRuns(final UtestSearch search_, Integer includedTestSuiteId_, Integer includedTestCaseId_, Integer includedTestCaseVersionId_,
			Integer teamMemberId_, Integer includedEnvironmentId_) throws Exception
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
		if (teamMemberId_ != null)
		{
			Search search = new Search(TeamUser.class);
			search.addField("teamId");
			search.addFilterEqual("userId", teamMemberId_);
			final List<?> teamIdList = dao.search(TeamUser.class, search);
			if (teamIdList != null && !teamIdList.isEmpty())
			{
				search_.addFilterIn("teamId", teamIdList);
			}
			else
			{
				return new UtestSearchResult();
			}
		}
		if (includedTestSuiteId_ != null || includedTestCaseId_ != null || includedTestCaseVersionId_ != null)
		{
			Search search = new Search(TestRunTestCase.class);
			search.addField("testRunId");
			if (includedTestSuiteId_ != null)
			{
				search.addFilterEqual("testSuiteId", includedTestSuiteId_);
			}
			if (includedTestCaseId_ != null)
			{
				search.addFilterEqual("testCaseId", includedTestCaseId_);
			}
			if (includedTestCaseVersionId_ != null)
			{
				search.addFilterEqual("testCaseVersionId", includedTestCaseVersionId_);
			}
			final List<?> testRunIdList = dao.search(TestRunTestCase.class, search);
			if (testRunIdList != null && !testRunIdList.isEmpty())
			{
				search_.addFilterIn("id", testRunIdList);
			}
			else
			{
				return new UtestSearchResult();
			}
		}

		return dao.getBySearch(TestRun.class, search_);
	}

	@Override
	public TestRun getTestRun(final Integer testRunId_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		return testRun;

	}

	@Override
	public TestRunTestCase getTestRunTestCase(final Integer testRunTestCaseId_) throws Exception
	{
		final TestRunTestCase testRunTestCase = getRequiredEntityById(TestRunTestCase.class, testRunTestCaseId_);
		return testRunTestCase;

	}

	@Override
	public TestRunTestCaseView getTestRunTestCaseView(final Integer testRunTestCaseId_) throws Exception
	{
		final TestRunTestCaseView testRunTestCase = getRequiredEntityById(TestRunTestCaseView.class, testRunTestCaseId_);
		return testRunTestCase;

	}

	@Override
	public List<TestRunTestCase> getTestRunTestCases(final Integer testRunId_) throws Exception
	{
		final Search search = new Search(TestRunTestCase.class);
		search.addFilterEqual("testRunId", testRunId_);
		return dao.search(TestRunTestCase.class, search);
	}

	@Override
	public List<TestRunTestCaseView> getTestRunTestCasesViews(final Integer testRunId_) throws Exception
	{
		final Search search = new Search(TestRunTestCaseView.class);
		search.addFilterEqual("testRunId", testRunId_);
		return dao.search(TestRunTestCaseView.class, search);
	}

	@Override
	public List<TestSuite> getTestRunTestSuites(final Integer testRunId_) throws Exception
	{
		List<TestRunTestCase> includedTestCases = getTestRunTestCases(testRunId_);
		List<Integer> testSuiteIds = new ArrayList<Integer>();
		for (TestRunTestCase includedTestCase : includedTestCases)
		{
			if (includedTestCase.getTestSuiteId() != null)
			{
				testSuiteIds.add(includedTestCase.getTestSuiteId());
			}
		}
		final Search search = new Search(TestSuite.class);
		search.addFilterIn("id", testSuiteIds);
		return dao.search(TestSuite.class, search);
	}

	@Override
	public List<TestRunTestCaseAssignment> getTestRunAssignments(final Integer testRunId_) throws Exception
	{
		final Search search = new Search(TestRunTestCaseAssignment.class);
		search.addFilterEqual("testRunId", testRunId_);
		return dao.search(TestRunTestCaseAssignment.class, search);
	}

	@Override
	public List<ProductComponent> getTestRunComponents(final Integer testRunId_) throws Exception
	{
		List<ProductComponent> components = new ArrayList<ProductComponent>();
		List<TestRunTestCase> includedTestCases = getTestRunTestCases(testRunId_);
		if (includedTestCases != null)
		{
			for (TestRunTestCase testCase : includedTestCases)
			{
				components.addAll(testCaseService.getComponentsForTestCase(testCase.getTestCaseId()));
			}
		}
		return components;
	}

	@Override
	public List<TestRunTestCaseAssignment> getTestRunTestCaseAssignments(final Integer testRunTestCaseId_) throws Exception
	{
		final Search search = new Search(TestRunTestCaseAssignment.class);
		search.addFilterEqual("testRunTestCaseId", testRunTestCaseId_);
		return dao.search(TestRunTestCaseAssignment.class, search);
	}

	@Override
	public TestRunTestCaseAssignment getTestRunTestCaseAssignment(final Integer assignmentId_) throws Exception
	{
		TestRunTestCaseAssignment assignment = getRequiredEntityById(TestRunTestCaseAssignment.class, assignmentId_);
		return assignment;
	}

	@Override
	public List<TestRunResult> getTestRunResultsForAssignment(final Integer testRunAssignmentId_) throws Exception
	{
		final Search search = new Search(TestRunResult.class);
		search.addFilterEqual("testRunAssignmentId", testRunAssignmentId_);
		return dao.search(TestRunResult.class, search);
	}

	@Override
	public TestRunResult getTestRunResult(final Integer testRunResultId_) throws Exception
	{
		return getRequiredEntityById(TestRunResult.class, testRunResultId_);
	}

	@Override
	public List<TestRunResult> getTestRunResults(final Integer testRunId_, final Integer testerId_, final Integer environmentGroupId_) throws Exception
	{
		final Search search = new Search(TestRunResult.class);
		search.addFilterEqual("testRunId", testRunId_);
		if (testerId_ != null)
		{
			search.addFilterEqual("testerId", testerId_);
		}
		if (environmentGroupId_ != null)
		{
			search.addFilterEqual("environmentGroupId", environmentGroupId_);
		}
		return dao.search(TestRunResult.class, search);
	}

	@Override
	public List<TestRunResult> getTestRunResults(final Integer testRunId_) throws Exception
	{
		return getTestRunResults(testRunId_, null);
	}

	@Override
	public List<TestRunResult> getTestRunResults(final Integer testRunId_, final List<Integer> resultStatusIds_) throws Exception
	{
		final Search search = new Search(TestRunResult.class);
		search.addFilterEqual("testRunId", testRunId_);
		if ((resultStatusIds_ != null) && !resultStatusIds_.isEmpty())
		{
			search.addFilterIn("testRunResultStatusId", resultStatusIds_);
		}
		return dao.search(TestRunResult.class, search);
	}

	@Override
	public List<TestRunResult> retestTestRun(final Integer testRunId_, final boolean retestFailedOnly_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		final List<Integer> statusesToRetest = new ArrayList<Integer>();
		statusesToRetest.add(TestRunResultStatus.FAILED);
		if (!retestFailedOnly_)
		{
			statusesToRetest.add(TestRunResultStatus.PASSED);
		}
		final List<TestRunResult> newResults = new ArrayList<TestRunResult>();
		final List<TestRunResult> resultsToRetest = getTestRunResults(testRun.getId(), statusesToRetest);
		for (final TestRunResult resultToRetest : resultsToRetest)
		{
			newResults.add(addResultForEnvironmentGroup(resultToRetest.getTestRunAssignmentId(), resultToRetest.getEnvironmentGroupId()));
		}
		return newResults;
	}

	@Override
	public TestRunResult retestTestRunResult(final Integer testRunResultId_, final Integer testerId_) throws Exception
	{
		final TestRunResult result = getRequiredEntityById(TestRunResult.class, testRunResultId_);
		Integer newTesterId = result.getTesterId();
		// if testerId was not passed use the original tester
		if (testerId_ != null)
		{
			newTesterId = testerId_;
		}
		final TestRunTestCaseAssignment assignment = getRequiredEntityById(TestRunTestCaseAssignment.class, result.getTestRunAssignmentId());
		// create new assignment for new tester
		final TestRunTestCaseAssignment newAssignment = addAssignmentNoResults(assignment.getTestRunTestCaseId(), newTesterId);
		// create pending result for new assignment
		return addResultForEnvironmentGroup(newAssignment, result.getEnvironmentGroupId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void approveAllTestRunResultsForTestRun(final Integer testRunId_) throws Exception
	{
		UtestSearch usearch = new UtestSearch();
		usearch.addFilterEqual("testRunId", testRunId_);
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(TestRunResultStatus.FAILED);
		statuses.add(TestRunResultStatus.PASSED);
		statuses.add(TestRunResultStatus.INVALIDATED);
		statuses.add(TestRunResultStatus.SKIPPED);
		usearch.addFilterIn("testRunResultStatusId", statuses);
		UtestSearchResult uresult = findTestRunResults(usearch, null);
		List<TestRunResult> allResults = (List<TestRunResult>) uresult.getResults();
		for (TestRunResult result : allResults)
		{
			updateApprovalStatus(result.getId(), null, ApprovalStatus.APPROVED, result.getVersion());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void approveAllTestRunResultsForTestRunTestCase(final Integer testRunId_, final Integer testCaseId_) throws Exception
	{
		UtestSearch usearch = new UtestSearch();
		usearch.addFilterEqual("testRunId", testRunId_);
		usearch.addFilterEqual("testCaseId", testCaseId_);
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(TestRunResultStatus.FAILED);
		statuses.add(TestRunResultStatus.PASSED);
		statuses.add(TestRunResultStatus.INVALIDATED);
		statuses.add(TestRunResultStatus.SKIPPED);
		usearch.addFilterIn("testRunResultStatusId", statuses);
		UtestSearchResult uresult = findTestRunResults(usearch, null);
		List<TestRunResult> allResults = (List<TestRunResult>) uresult.getResults();
		for (TestRunResult result : allResults)
		{
			updateApprovalStatus(result.getId(), null, ApprovalStatus.APPROVED, result.getVersion());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void approveTestRunResults(final List<Integer> testRunResultId_) throws Exception
	{
		final UtestSearch usearch = new UtestSearch();
		usearch.addFilterIn("id", testRunResultId_);
		final UtestSearchResult uresult = findTestRunResults(usearch, null);
		final List<TestRunResult> allResults = (List<TestRunResult>) uresult.getResults();
		for (final TestRunResult result : allResults)
		{
			updateApprovalStatus(result.getId(), null, ApprovalStatus.APPROVED, result.getVersion());
		}
	}

	@Override
	public TestRunResult approveTestRunResult(final Integer testRunResultId_, final Integer originalVersionId_) throws Exception
	{
		return updateApprovalStatus(testRunResultId_, null, ApprovalStatus.APPROVED, originalVersionId_);
	}

	@Override
	public TestRunResult rejectTestRunResult(final Integer testRunResultId_, final String comment_, final Integer originalVersionId_) throws Exception
	{
		return updateApprovalStatus(testRunResultId_, comment_, ApprovalStatus.REJECTED, originalVersionId_);
	}

	@Override
	public TestRunResult startExecutingAssignedTestCase(final Integer testRunResultId_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = getRequiredEntityById(TestRunResult.class, testRunResultId_);
		// only do if not started already
		if (!TestRunResultStatus.STARTED.equals(result.getTestRunResultStatusId()))
		{
			if (TestRunResultStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionBlockedException();
			}
			// make sure user executing the result is the same as assigned
			// TODO - get logged in user
			if (!getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			// prevent if test run locked
			final TestRun testRun = getRequiredEntityById(TestRun.class, result.getTestRunId());
			if (TestRunStatus.LOCKED.equals(testRun.getTestRunStatusId()))
			{
				throw new TestCycleClosedException();
			}

			result.setActualTimeInMin(null);
			result.setActualResult(null);
			result.setComment(null);
			result.setFailedStepNumber(null);
			result.setVersion(originalVersionId_);
			result.setTestRunResultStatusId(TestRunResultStatus.STARTED);
			result.setRunDate(new Date());
			return dao.merge(result);
		}
		else
		{
			return result;
		}
	}

	@Override
	public TestRunResult finishExecutingAssignedTestCaseWithInvalidation(final Integer testRunResultId_, final String comment_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = getRequiredEntityById(TestRunResult.class, testRunResultId_);
		if (!TestRunResultStatus.INVALIDATED.equals(result.getTestRunResultStatusId()))
		{
			if (!TestRunResultStatus.STARTED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionWithoutRestartException();
			}
			// make sure user executing the result is the same as assigned
			if (!getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			// prevent if test run locked
			final TestRun testRun = getRequiredEntityById(TestRun.class, result.getTestRunId());
			if (TestRunStatus.LOCKED.equals(testRun.getTestRunStatusId()))
			{
				throw new TestCycleClosedException();
			}
			result.setVersion(originalVersionId_);
			result.setTestRunResultStatusId(TestRunResultStatus.INVALIDATED);
			result.setActualTimeInMin(0);
			result.setActualResult(null);
			result.setComment(comment_);
			result.setFailedStepNumber(null);
			return dao.merge(result);
		}
		else
		{
			return result;
		}
	}

	@Override
	public TestRunResult finishExecutingAssignedTestCaseWithSkip(final Integer testRunResultId_, final String comment_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = getRequiredEntityById(TestRunResult.class, testRunResultId_);
		if (!TestRunResultStatus.SKIPPED.equals(result.getTestRunResultStatusId()))
		{
			// make sure user executing the result is the same as assigned
			if (!getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			// prevent if test run locked
			final TestRun testRun = getRequiredEntityById(TestRun.class, result.getTestRunId());
			if (TestRunStatus.LOCKED.equals(testRun.getTestRunStatusId()))
			{
				throw new TestCycleClosedException();
			}
			result.setVersion(originalVersionId_);
			result.setTestRunResultStatusId(TestRunResultStatus.SKIPPED);
			result.setActualTimeInMin(0);
			result.setActualResult(null);
			result.setComment(comment_);
			result.setFailedStepNumber(null);
			return dao.merge(result);
		}
		else
		{
			return result;
		}
	}

	@Override
	public TestRunResult finishExecutingAssignedTestCaseWithSuccess(final Integer testRunResultId_, final String comment_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = getRequiredEntityById(TestRunResult.class, testRunResultId_);
		if (!TestRunResultStatus.PASSED.equals(result.getTestRunResultStatusId()))
		{
			if (!TestRunResultStatus.STARTED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionWithoutRestartException();
			}
			if (TestRunResultStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionBlockedException();
			}
			// make sure user executing the result is the same as assigned
			if (!getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			// prevent if test run locked
			final TestRun testRun = getRequiredEntityById(TestRun.class, result.getTestRunId());
			if (TestRunStatus.LOCKED.equals(testRun.getTestRunStatusId()))
			{
				throw new TestCycleClosedException();
			}

			// unblock all dependent test cases
			blockUnblockSubsequentExecution(result, false);

			result.setVersion(originalVersionId_);
			result.setTestRunResultStatusId(TestRunResultStatus.PASSED);
			result.setActualTimeInMin((int) DateUtil.minutesDifference(result.getRunDate(), new Date()));
			result.setActualResult(null);
			result.setComment(comment_);
			result.setFailedStepNumber(null);
			return dao.merge(result);
		}
		else
		{
			return result;
		}
	}

	@Override
	public TestRunResult finishExecutingAssignedTestCaseWithFailure(final Integer testRunResultId_, final Integer failedStepNumber_, final String actualResult_,
			final String comment_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = getRequiredEntityById(TestRunResult.class, testRunResultId_);
		if (!TestRunResultStatus.FAILED.equals(result.getTestRunResultStatusId()))
		{
			if (!TestRunResultStatus.STARTED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionWithoutRestartException();
			}
			if (TestRunResultStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionBlockedException();
			}
			// make sure user executing the result is the same as assigned
			if (!getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			// prevent if test run locked
			final TestRun testRun = getRequiredEntityById(TestRun.class, result.getTestRunId());
			if (TestRunStatus.LOCKED.equals(testRun.getTestRunStatusId()))
			{
				throw new TestCycleClosedException();
			}

			// block all dependent test cases
			blockUnblockSubsequentExecution(result, true);

			result.setVersion(originalVersionId_);
			result.setTestRunResultStatusId(TestRunResultStatus.FAILED);
			result.setActualTimeInMin((int) DateUtil.minutesDifference(result.getRunDate(), new Date()));
			result.setActualResult(actualResult_);
			result.setComment(comment_);
			result.setFailedStepNumber(failedStepNumber_);

			return dao.merge(result);
		}
		else
		{
			return result;
		}
	}

	private void blockUnblockSubsequentExecution(final TestRunResult result_, final boolean needToBlock_)
	{
		Search search = new Search(TestRunTestCase.class);
		search.addFilterEqual("testRunId", result_.getTestRunId());
		search.addFilterEqual("testCaseVersionId", result_.getTestCaseVersionId());
		final TestRunTestCase includedTestCase = (TestRunTestCase) dao.searchUnique(TestRunTestCase.class, search);
		// skip if not blocking
		if (!includedTestCase.isBlocking() || (includedTestCase.getTestSuiteId() == null))
		{
			return;
		}
		// TODO - only block cases from the same test suite, same tester or all?
		// for now only use the same test suite
		search = new Search(TestRunTestCase.class);
		search.addFilterEqual("testRunId", includedTestCase.getTestRunId());
		search.addFilterEqual("testSuiteId", includedTestCase.getTestSuiteId());
		search.addFilterGreaterOrEqual("runOrder", includedTestCase.getRunOrder());
		search.addFilterNotEqual("testCaseVersionId", includedTestCase.getTestCaseVersionId());
		final List<TestRunTestCase> suiteTestCases = dao.search(TestRunTestCase.class, search);
		// find all assignments for test cases from the same test suite
		for (final TestRunTestCase suiteTestCase : suiteTestCases)
		{
			// find all results generated for this assignment and for the same
			// environment group as original result
			search = new Search(TestRunResult.class);
			search.addFilterEqual("testCaseVersionId", suiteTestCase.getTestCaseVersionId());
			search.addFilterEqual("environmentGroupId", result_.getEnvironmentGroupId());
			final List<TestRunResult> results = dao.search(TestRunResult.class, search);
			for (final TestRunResult result : results)
			{
				// block results that are not tested yet and not blocked already
				if (!TestRunResultStatus.FAILED.equals(result.getTestRunResultStatusId()) && !TestRunResultStatus.PASSED.equals(result.getTestRunResultStatusId()))
				{
					if (needToBlock_ && !TestRunResultStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
					{
						result.setTestRunResultStatusId(TestRunResultStatus.BLOCKED);
						dao.addOrUpdate(result);
					}
					// unblock blocked test cases
					else if (!needToBlock_ && TestRunResultStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
					{
						result.setTestRunResultStatusId(TestRunResultStatus.PENDING);
						dao.addOrUpdate(result);
					}
				}
			}
		}

	}

	@Override
	public TestRun saveTestRun(final Integer testRunId_, final boolean useLatestVersions_, final String name_, final String description_, final Date startDate_,
			final Date endDate_, final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_, final Integer originalVersionId_,
			final boolean autoAssignToTeam_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		checkForDuplicateNameWithinParent(TestRun.class, name_, testRun.getTestCycleId(), "testCycleId", testRunId_);

		testRun.setName(name_);
		testRun.setDescription(description_);
		testRun.setUseLatestVersions(useLatestVersions_);
		testRun.setStartDate(startDate_);
		testRun.setEndDate(endDate_);
		testRun.setSelfAssignAllowed(selfAssignAllowed_);
		testRun.setSelfAssignLimit(selfAssignLimit_);
		testRun.setSelfAssignPerEnvironment(selfAssignPerEnvironment_);
		testRun.setAutoAssignToTeam(autoAssignToTeam_);
		testRun.setVersion(originalVersionId_);
		return dao.merge(testRun);
	}

	@Override
	public TestRunTestCase saveTestRunTestCase(final Integer includedTestCaseId_, final Integer priorityId_, final Integer runOrder_, final boolean blocking_,
			final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_, final Integer originalVersionId_)
	{
		final TestRunTestCase includedTestCase = getRequiredEntityById(TestRunTestCase.class, includedTestCaseId_);
		// prevent if already activated
		final TestRun testRun = getRequiredEntityById(TestRun.class, includedTestCase.getTestRunId());
		if (!TestRunStatus.PENDING.equals(testRun.getTestRunStatusId()))
		{
			throw new ChangingActivatedEntityException(TestRun.class.getSimpleName());
		}
		includedTestCase.setSelfAssignAllowed(selfAssignAllowed_);
		includedTestCase.setSelfAssignLimit(selfAssignLimit_);
		includedTestCase.setSelfAssignPerEnvironment(selfAssignPerEnvironment_);

		includedTestCase.setPriorityId(priorityId_);
		includedTestCase.setRunOrder(runOrder_);
		includedTestCase.setBlocking(blocking_);
		includedTestCase.setVersion(originalVersionId_);
		return dao.merge(includedTestCase);
	}

	@Override
	public TestRunTestCase saveTestRunTestCase(final Integer includedTestCaseId_, final Integer priorityId_, final Integer runOrder_, final boolean blocking_,
			final Integer originalVersionId_)
	{
		final TestRunTestCase includedTestCase = getRequiredEntityById(TestRunTestCase.class, includedTestCaseId_);
		// prevent if already activated
		final TestRun testRun = getRequiredEntityById(TestRun.class, includedTestCase.getTestRunId());

		return saveTestRunTestCase(includedTestCaseId_, priorityId_, runOrder_, blocking_, testRun.isSelfAssignAllowed(), testRun.isSelfAssignPerEnvironment(), testRun
				.getSelfAssignLimit(), originalVersionId_);
	}

	@Override
	public TestRun activateTestRun(final Integer testRunId_, final Integer originalVersionId_) throws Exception
	{
		return updateActivationStatus(testRunId_, TestRunStatus.ACTIVE, originalVersionId_);
	}

	private TestRun updateActivationStatus(final Integer testRunId_, final Integer activationStatusId_, final Integer originalVersionId_) throws Exception
	{
		// change status for the test run
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		if (!testRun.getTestRunStatusId().equals(activationStatusId_))
		{
			if (TestRunStatus.ACTIVE.equals(activationStatusId_))
			{
				// prevent activating if parent test cycle is not activated
				final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testRun.getTestCycleId());
				if (!TestCycleStatus.ACTIVE.equals(testCycle.getTestCycleStatusId()))
				{
					throw new ActivatingIncompleteEntityException(TestRun.class.getSimpleName() + " : " + testRunId_);
				}
			}
			testRun.setTestRunStatusId(activationStatusId_);
			testRun.setVersion(originalVersionId_);
			return dao.merge(testRun);
		}
		else
		{
			return testRun;
		}
	}

	@Override
	public TestRun featureTestRun(final Integer testRunId_, final Integer originalVersionId_) throws Exception
	{
		return updateFeaturedStatus(testRunId_, true, originalVersionId_);
	}

	@Override
	public TestRun unfeatureTestRun(final Integer testRunId_, final Integer originalVersionId_) throws Exception
	{
		return updateFeaturedStatus(testRunId_, false, originalVersionId_);
	}

	private TestRun updateFeaturedStatus(final Integer testRunId_, final boolean featured_, final Integer originalVersionId_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		if (featured_ != testRun.isFeatured())
		{
			testRun.setVersion(originalVersionId_);
			testRun.setFeatured(featured_);
			return dao.merge(testRun);
		}
		else
		{
			return testRun;
		}
	}

	private TestRunResult updateApprovalStatus(final Integer testRunResultId_, final String comment_, final Integer approvalStatus_, final Integer originalVersionId_)
			throws Exception
	{
		final TestRunResult result = getRequiredEntityById(TestRunResult.class, testRunResultId_);
		if (approvalStatus_ != result.getApprovalStatusId())
		{
			// make sure user approving the result is not the same as assigned
			if (getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			if (TestRunResultStatus.PENDING.equals(result.getTestRunResultStatusId()))
			{
				throw new ApprovingIncompleteEntityException(TestRunResult.class.getSimpleName() + " : " + testRunResultId_);
			}
			result.setVersion(originalVersionId_);
			result.setApprovalStatusId(approvalStatus_);
			result.setApproveDate(new Date());
			result.setApprovedBy(getCurrentUserId());
			if (comment_ != null)
			{
				result.setComment(result.getComment() + "\r\n" + getCurrentUserId() + ": " + comment_);
			}
			return dao.merge(result);
		}
		else
		{
			return result;
		}
	}

	@Override
	public TestRun lockTestRun(final Integer testRunId_, final Integer originalVersionId_) throws Exception
	{
		return updateActivationStatus(testRunId_, TestRunStatus.LOCKED, originalVersionId_);
	}

	@Override
	public List<User> getTestingTeamForTestRun(final Integer testRunId_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		if (testRun.getTeamId() != null)
		{
			return teamService.getTeamUsers(testRun.getTeamId());
		}
		else
		{
			return new ArrayList<User>();
		}
	}

	@Override
	public void saveTestingTeamForTestRun(final Integer testRunId_, final List<Integer> userIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		final TestCycle testCycle = getRequiredEntityById(TestCycle.class, testRun.getTestCycleId());
		final Product product = getRequiredEntityById(Product.class, testRun.getProductId());
		// check that users are selected from community users or users from the
		// matching company
		if (!isValidSelectionForCompany(product.getCompanyId(), userIds_, User.class))
		{
			throw new UnsupportedTeamSelectionException("Selecting testers from other company.");
		}
		// update team profile
		Team team = null;
		if ((testCycle.getTeamId() != null && testCycle.getTeamId().equals(testRun.getTeamId())) || testRun.getTeamId() == null)
		{
			team = teamService.addTeam(product.getCompanyId(), "Created for test run : " + testRunId_, "Included users: " + userIds_.toString());
			teamService.saveTeamUsers(team.getId(), userIds_, team.getVersion());
			testRun.setTeamId(team.getId());
		}
		else
		{
			team = getRequiredEntityById(Team.class, testRun.getTeamId());
			teamService.saveTeamUsers(team.getId(), userIds_, team.getVersion());
		}
		// update version
		testRun.setVersion(originalVersionId_);
		dao.merge(testRun);

		// assign all test cases to new team members
		autoAssignAllTestCasesToTeam(testRun);
	}

	@Override
	public List<AccessRole> getTestingTeamMemberRolesForTestRun(final Integer testRunId_, final Integer userId_) throws Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		// update team profile
		if (testRun.getTeamId() != null)
		{
			return teamService.getTeamUserRoles(testRun.getTeamId(), userId_);
		}
		else
		{
			return new ArrayList<AccessRole>();
		}
	}

	@Override
	public void saveTestingTeamMemberRolesForTestRun(final Integer testRunId_, final Integer userId_, final List<Integer> roleIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRun testRun = getRequiredEntityById(TestRun.class, testRunId_);
		// update team profile
		if (testRun.getTeamId() != null)
		{
			Team team = getRequiredEntityById(Team.class, testRun.getTeamId());
			teamService.saveTeamUserRoles(testRun.getTeamId(), userId_, roleIds_, team.getVersion());
		}
		else
		{
			throw new NoTeamDefinitionException("No team defined for TestRun: " + testRunId_);
		}
		// update version
		testRun.setVersion(originalVersionId_);
		dao.merge(testRun);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoryValue> getCoverageByStatus(final Integer testRunId_)
	{
		final int numParams = 1;
		final String[] paramNames = new String[numParams];
		final Object[] values = new Object[numParams];

		final String namedQuery = "Report_TestRunResultTotalsByStatus";
		paramNames[0] = "testRunId";
		values[0] = testRunId_;

		return (List<CategoryValue>) dao.findByNamedQueryAndNamedParam(namedQuery, paramNames, values, false, false);

	}

	@Override
	public boolean deleteAttachmentForTestRun(final Integer attachmentId_, final Integer entityId_) throws Exception
	{
		return attachmentService.deleteAttachment(attachmentId_, entityId_, EntityType.TEST_RUN);
	}

	@Override
	public Attachment addAttachmentForTestRun(final String name, final String description, final String url, final Double size, final Integer testRunId,
			final Integer attachmentTypeId) throws Exception
	{
		return attachmentService.addAttachment(name, description, url, size, EntityType.TEST_RUN, testRunId, attachmentTypeId);
	}

	@Override
	public List<Attachment> getAttachmentsForTestRun(final Integer testRunId_) throws Exception
	{
		return attachmentService.getAttachmentsForEntity(testRunId_, EntityType.TEST_RUN);
	}

	@Override
	public Attachment addAttachmentForTestRunResult(final String name, final String description, final String url, final Double size, final Integer testRunResultId,
			final Integer attachmentTypeId) throws Exception
	{
		return attachmentService.addAttachment(name, description, url, size, EntityType.TEST_RESULT, testRunResultId, attachmentTypeId);
	}

	@Override
	public boolean deleteAttachmentForTestRunResult(final Integer attachmentId_, final Integer entityId_) throws Exception
	{
		return attachmentService.deleteAttachment(attachmentId_, entityId_, EntityType.TEST_RESULT);
	}

	@Override
	public List<Attachment> getAttachmentsForTestRunResult(final Integer testRunResultId_) throws Exception
	{
		return attachmentService.getAttachmentsForEntity(testRunResultId_, EntityType.TEST_RESULT);
	}

	@Override
	public EntityExternalBug addExternalBugForTestRunResult(final String externalIdentifier, final String url, final Integer testRunResultId) throws Exception
	{
		return externalBugService.addEntityExternalBug(externalIdentifier, url, EntityType.TEST_RESULT, testRunResultId);
	}

	@Override
	public boolean deleteExternalBugForTestRunResult(final Integer attachmentId_, final Integer entityId_) throws Exception
	{
		return externalBugService.deleteEntityExternalBug(attachmentId_, entityId_, EntityType.TEST_RESULT);
	}

	@Override
	public List<EntityExternalBug> getExternalBugsForTestRunResult(final Integer testRunResultId_) throws Exception
	{
		return externalBugService.getEntityExternalBugsForEntity(testRunResultId_, EntityType.TEST_RESULT);
	}

}
