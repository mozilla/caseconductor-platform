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
import com.utest.domain.ApprovalStatus;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Product;
import com.utest.domain.TcmEntityStatus;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.TestCycle;
import com.utest.domain.TestPlan;
import com.utest.domain.TestPlanTestSuite;
import com.utest.domain.TestRun;
import com.utest.domain.TestRunResult;
import com.utest.domain.TestRunTestCase;
import com.utest.domain.TestRunTestCaseAssignment;
import com.utest.domain.TestSuite;
import com.utest.domain.TestSuiteTestCase;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TestPlanService;
import com.utest.domain.service.TestRunService;
import com.utest.domain.service.TestSuiteService;
import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ApprovingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.IncludingNotActivatedEntityException;
import com.utest.exception.InvalidUserException;
import com.utest.exception.NotFoundException;
import com.utest.exception.TestCaseExecutionBlockedException;
import com.utest.exception.TestCaseExecutionWithoutRestartException;
import com.utest.exception.TestCycleClosedException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;
import com.utest.util.DateUtil;

public class TestRunServiceImpl extends BaseServiceImpl implements TestRunService
{
	private final TypelessDAO			dao;
	private final EnvironmentService	environmentService;
	private final TestPlanService	testPlanService;
	private final TestSuiteService	testSuiteService;

	/**
	 * Default constructor
	 */
	public TestRunServiceImpl(final TypelessDAO dao, final TestPlanService testPlanService, final TestSuiteService testSuiteService,
			final EnvironmentService environmentService)
	{
		super(dao);
		this.dao = dao;
		this.environmentService = environmentService;
		this.testPlanService = testPlanService;
		this.testSuiteService = testSuiteService;
	}

	@Override
	public TestRun addTestRun(final Integer testCycleId_, final boolean useLatestVersions_, final String name_, final String description_, final Date startDate_,
			final Date endDate_, final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_) throws Exception
	{
		final TestCycle testCycle = dao.getById(TestCycle.class, testCycleId_);
		if (testCycle == null)
		{
			throw new IllegalArgumentException("TestCycle not found: " + testCycleId_);
		}
		checkForDuplicateNameWithinParent(TestRun.class, name_, testCycleId_, "testCycleId", null);

		// TODO - validate test cycle status
		final TestRun testRun = new TestRun();
		testRun.setTestRunStatusId(TcmEntityStatus.DRAFT);
		testRun.setTestCycleId(testCycleId_);
		testRun.setProductId(testCycle.getProductId());
		testRun.setName(name_);
		testRun.setDescription(description_);
		testRun.setStartDate(startDate_);
		testRun.setEndDate(endDate_);
		testRun.setSelfAssignAllowed(selfAssignAllowed_);
		testRun.setSelfAssignLimit(selfAssignLimit_);
		testRun.setSelfAssignPerEnvironment(selfAssignPerEnvironment_);
		// set environment profile from test cycle by default
		testRun.setEnvironmentProfileId(testCycle.getEnvironmentProfileId());

		final Integer testRunId = dao.addAndReturnId(testRun);
		return getTestRun(testRunId);
	}

	@Override
	public TestRun addTestRunFromTestPlan(final Integer testCycleId_, final Integer testPlanId_, final boolean useLatestVersiuons_, final String name_, final String description_,
			final Date startDate_, final Date endDate_, final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_) throws Exception
	{
		final TestPlan testPlan = dao.getById(TestPlan.class, testPlanId_);
		if (testPlan == null)
		{
			throw new IllegalArgumentException("TestPlan not found: " + testPlanId_);
		}
		// check if test plan is activated
		if (!TcmEntityStatus.ACTIVATED.equals(testPlan.getTestPlanStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestPlan.class.getSimpleName() + " : " + testPlanId_);
		}
		final TestRun testRun = addTestRun(testCycleId_, useLatestVersiuons_, name_, description_, startDate_, endDate_, selfAssignAllowed_, selfAssignPerEnvironment_,
				selfAssignLimit_);
		final List<TestPlanTestSuite> includedTestSuites = testPlanService.getTestPlanTestSuites(testPlanId_);
		Integer startingRunOrder = 0;
		for (final TestPlanTestSuite testPlanTestSuite : includedTestSuites)
		{
			// increment by 1 before adding new test suite cases
			startingRunOrder++;
			startingRunOrder = addTestCasesFromTestSuite(testRun, testPlanTestSuite.getTestSuiteId(), startingRunOrder);
		}
		return testRun;
	}

	@Override
	public TestRun addTestRunFromTestSuite(final Integer testCycleId_, final Integer testSuiteId_, final boolean useLatestVersiuons_, final String name_,
			final String description_, final Date startDate_, final Date endDate_, final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_,
			final Integer selfAssignLimit_) throws Exception
	{
		final TestSuite testSuite = dao.getById(TestSuite.class, testSuiteId_);
		if (testSuite == null)
		{
			throw new IllegalArgumentException("TestSuite not found: " + testSuiteId_);
		}
		// check if test plan is activated
		if (!TcmEntityStatus.ACTIVATED.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		final TestRun testRun = addTestRun(testCycleId_, useLatestVersiuons_, name_, description_, startDate_, endDate_, selfAssignAllowed_, selfAssignPerEnvironment_,
				selfAssignLimit_);
		final Integer startingRunOrder = 0;
		addTestCasesFromTestSuite(testRun, testSuiteId_, startingRunOrder);
		return testRun;
	}

	private Integer addTestCasesFromTestSuite(final TestRun testRun_, final Integer testSuiteId_, final Integer startingRunOrder_) throws Exception
	{
		final List<TestSuiteTestCase> includedTestCases = testSuiteService.getTestSuiteTestCases(testSuiteId_);
		Integer lastRunOrder = startingRunOrder_;
		for (final TestSuiteTestCase testSuiteTestCase : includedTestCases)
		{
			lastRunOrder += testSuiteTestCase.getRunOrder();
			final TestRunTestCase testRunTestCase = addTestRunTestCase(testRun_.getId(), testSuiteTestCase.getTestCaseVersionId(), testSuiteTestCase.getPriorityId(),
					testSuiteTestCase.getRunOrder(), testSuiteTestCase.isBlocking(), testSuiteId_);
			// TODO - determine if need the intersection
			if (testRun_.getEnvironmentProfileId() != null)
			{
				testRunTestCase.setEnvironmentProfileId(testRun_.getEnvironmentProfileId());
			}
			else
			{
				testRunTestCase.setEnvironmentProfileId(testSuiteTestCase.getEnvironmentProfileId());
			}
			saveTestRunTestCase(testRunTestCase);
		}
		return lastRunOrder;
	}

	@Override
	public List<EnvironmentGroup> findEnvironmentGroupsForTestRun(final Integer testRunId_) throws Exception
	{
		final TestRun testRun = dao.getById(TestRun.class, testRunId_);
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
	public void saveEnvironmentGroupsForTestRun(final Integer testRunId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRun testRun = dao.getById(TestRun.class, testRunId_);
		if (testRun == null)
		{
			throw new IllegalArgumentException("TestRun not found: " + testRunId_);
		}
		// cannot change after activation
		if (!TcmEntityStatus.DRAFT.equals(testRun.getTestRunStatusId()))
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
		final TestCycle testCycle = dao.getById(TestCycle.class, testRun.getTestCycleId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(testCycle.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		// update environment profile
		final Product product = dao.getById(Product.class, testRun.getProductId());
		final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test run : " + testRunId_, "Included groups: "
				+ environmentGroupIds_.toString(), environmentGroupIds_);
		testRun.setEnvironmentProfileId(environmentProfile.getId());
		testRun.setVersion(originalVersionId_);
		dao.merge(testRun);
	}

	private TestRunTestCase addTestRunTestCase(final TestRunTestCase includedTestCase_) throws Exception
	{
		// prevent if already activated
		final TestRun testRun = dao.getById(TestRun.class, includedTestCase_.getTestRunId());
		if (!TcmEntityStatus.DRAFT.equals(testRun.getTestRunStatusId()))
		{
			throw new ChangingActivatedEntityException(TestRunTestCase.class.getSimpleName());
		}
		// will crush on adding test suite /test plan
		// DomainUtil.loadUpdatedTimeline(testRun, null);
		dao.merge(testRun);
		final Integer id = dao.addAndReturnId(includedTestCase_);
		return dao.getById(TestRunTestCase.class, id);
	}

	@Override
	public TestRunTestCase addTestRunTestCase(final Integer testRunId_, final Integer testCaseVersionId_) throws Exception
	{
		return addTestRunTestCase(testRunId_, testCaseVersionId_, 0, 0, false, null);
	}

	@Override
	public TestRunTestCase addTestRunTestCase(final Integer testRunId_, final Integer testCaseVersionId_, final Integer priorityId_, final Integer runOrder_,
			final boolean isBlocking_, final Integer testSuiteId_) throws Exception
	{
		final TestCaseVersion testCaseVersion = dao.getById(TestCaseVersion.class, testCaseVersionId_);
		if (testCaseVersion == null)
		{
			throw new IllegalArgumentException("TestCaseVersion not found: " + testCaseVersionId_);
		}
		final TestRun testRun = dao.getById(TestRun.class, testRunId_);
		if (testRun == null)
		{
			throw new IllegalArgumentException("TestRun not found: " + testRunId_);
		}
		final TestRunTestCase includedTestCase = new TestRunTestCase();
		includedTestCase.setTestRunId(testRunId_);
		includedTestCase.setTestCaseId(testCaseVersion.getTestCaseId());
		includedTestCase.setTestCaseVersionId(testCaseVersion.getId());
		includedTestCase.setSelfAssignAllowed(testRun.isSelfAssignAllowed());
		includedTestCase.setSelfAssignPerEnvironment(testRun.isSelfAssignPerEnvironment());
		includedTestCase.setSelfAssignLimit(testRun.getSelfAssignLimit());
		includedTestCase.setPriorityId(priorityId_);
		includedTestCase.setRunOrder(runOrder_);
		includedTestCase.setBlocking(isBlocking_);
		includedTestCase.setTestSuiteId(testSuiteId_);
		// TODO - determine what takes precedence TestCase, TestSuite or
		// intersection or both
		if (testRun.getEnvironmentProfileId() != null)
		{
			includedTestCase.setEnvironmentProfileId(testRun.getEnvironmentProfileId());
		}
		else
		{
			includedTestCase.setEnvironmentProfileId(testCaseVersion.getEnvironmentProfileId());

		}
		return addTestRunTestCase(includedTestCase);
	}

	@Override
	public TestRunTestCaseAssignment addAssignment(final Integer testRunTestCaseId_, final Integer testerId_, final List<Integer> environmentGroupIds_) throws Exception
	{
		final TestRunTestCase includedTestCase = dao.getById(TestRunTestCase.class, testRunTestCaseId_);
		if (includedTestCase == null)
		{
			throw new IllegalArgumentException("TestRunTestCase not found: " + testRunTestCaseId_);
		}
		final User tester = dao.getById(User.class, testerId_);
		if (tester == null)
		{
			throw new IllegalArgumentException("Tester not found: " + testerId_);
		}
		// check if groups assignment is correct
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(includedTestCase.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}

		TestRunTestCaseAssignment assignment = addAssignmentNoResults(testRunTestCaseId_, testerId_);
		// create new profile for new group selection
		final TestRun testRun = dao.getById(TestRun.class, includedTestCase.getTestRunId());
		final Product product = dao.getById(Product.class, testRun.getProductId());
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
		// TODO - check tester company, community and profile matching
		final TestRunTestCase includedTestCase = dao.getById(TestRunTestCase.class, testRunTestCaseId_);
		if (includedTestCase == null)
		{
			throw new IllegalArgumentException("TestRunTestCase not found: " + testRunTestCaseId_);
		}
		final User tester = dao.getById(User.class, testerId_);
		if (tester == null)
		{
			throw new IllegalArgumentException("Tester not found: " + testerId_);
		}
		final TestRunTestCaseAssignment assignment = addAssignmentNoResults(testRunTestCaseId_, testerId_);
		// generate results for new assignment
		addResultsForAssignment(assignment);
		return assignment;
	}

	private TestRunTestCaseAssignment addAssignmentNoResults(final Integer testRunTestCaseId_, final Integer testerId_)
	{
		// TODO - check if this test case is already assigned and test run
		// settings allow to assign again to another tester or another
		// environment
		final TestRunTestCase includedTestCase = dao.getById(TestRunTestCase.class, testRunTestCaseId_);
		// prevent if already activated
		final TestRun testRun = dao.getById(TestRun.class, includedTestCase.getTestRunId());
		final TestRunTestCaseAssignment assignment = new TestRunTestCaseAssignment();
		assignment.setTestRunId(testRun.getId());
		assignment.setProductId(testRun.getProductId());
		assignment.setTestCaseId(includedTestCase.getTestCaseId());
		assignment.setTestCaseVersionId(includedTestCase.getTestCaseVersionId());
		assignment.setTestRunTestCaseId(testRunTestCaseId_);
		// TODO - validate if tester from the same company as tested product or
		// is a community tester?
		assignment.setTesterId(testerId_);
		assignment.setEnvironmentProfileId(includedTestCase.getEnvironmentProfileId());
		final Integer id = dao.addAndReturnId(assignment);
		return dao.getById(TestRunTestCaseAssignment.class, id);

	}

	private void addResultsForAssignment(final TestRunTestCaseAssignment assignment_) throws Exception
	{

		final List<EnvironmentGroup> groups = environmentService.getEnvironmentGroupsForProfile(assignment_.getEnvironmentProfileId());
		for (final EnvironmentGroup group : groups)
		{
			addResultForEnvironmentGroup(assignment_, group.getId());
		}
	}

	private TestRunResult addResultForEnvironmentGroup(final Integer assignmentId_, final Integer environmentGroupId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = dao.getById(TestRunTestCaseAssignment.class, assignmentId_);
		if (assignment == null)
		{
			throw new IllegalArgumentException("TestRunTestCaseAssignment not found: " + assignmentId_);
		}
		return addResultForEnvironmentGroup(assignment, environmentGroupId_);
	}

	private TestRunResult addResultForEnvironmentGroup(final TestRunTestCaseAssignment assignment_, final Integer environmentGroupId_) throws Exception
	{

		final TestRunResult result = new TestRunResult();
		result.setApprovalStatusId(ApprovalStatus.PENDING);
		result.setTestRunResultStatusId(TcmEntityStatus.DRAFT);
		result.setEnvironmentGroupId(environmentGroupId_);
		result.setProductId(assignment_.getProductId());
		result.setTestCaseId(assignment_.getTestCaseId());
		result.setTestCaseVersionId(assignment_.getTestCaseVersionId());
		result.setTesterId(assignment_.getTesterId());
		result.setTestRunAssignmentId(assignment_.getId());
		result.setTestRunId(assignment_.getTestRunId());
		return dao.merge(result);
	}

	@Override
	public void deleteTestRun(final Integer testRunId_) throws Exception
	{
		final TestRun testRun = dao.getById(TestRun.class, testRunId_);
		if (testRun == null)
		{
			throw new NotFoundException("TestRun not found. Id: " + testRunId_);
		}
		if (!TcmEntityStatus.DRAFT.equals(testRun.getTestRunStatusId()))
		{
			throw new DeletingActivatedEntityException(TestRun.class.getSimpleName());
		}
		// delete all included test cases
		final List<TestRunTestCase> includedTestCases = findTestRunTestCases(testRunId_);
		dao.delete(includedTestCases);
		// delete assignments
		final List<TestRunTestCaseAssignment> assignments = findTestRunAssignments(testRunId_);
		dao.delete(assignments);
		// delete results
		final List<TestRunResult> results = findTestRunResults(testRunId_);
		dao.delete(results);

		// delete test run
		dao.delete(testRun);
	}

	@Override
	public void deleteTestRunTestCase(final Integer testRunTestCaseId_) throws Exception
	{
		final TestRunTestCase includedTestCase = dao.getById(TestRunTestCase.class, testRunTestCaseId_);
		if (includedTestCase == null)
		{
			throw new NotFoundException("TestRunTestCase not found. Id: " + testRunTestCaseId_);
		}
		final TestRun testRun = dao.getById(TestRun.class, includedTestCase.getTestRunId());
		// prevent if already activated
		if (!TcmEntityStatus.DRAFT.equals(testRun.getTestRunStatusId()))
		{
			throw new ChangingActivatedEntityException(TestRunTestCase.class.getSimpleName());
		}
		// DomainUtil.loadUpdatedTimeline(testRun);
		dao.merge(testRun);
		dao.delete(includedTestCase);
	}

	@Override
	public void deleteAssignment(final Integer assignmentId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = dao.getById(TestRunTestCaseAssignment.class, assignmentId_);
		if (assignment == null)
		{
			throw new NotFoundException("Assignment not found. Id: " + assignmentId_);
		}
		// prevent if already any of the results were executed
		final Search search = new Search(TestRunResult.class);
		search.addFilterEqual("testRunAssignmentId", assignmentId_);
		final List<TestRunResult> results = dao.search(TestRunResult.class, search);
		for (final TestRunResult result : results)
		{
			if (!TcmEntityStatus.DRAFT.equals(result.getTestRunResultStatusId()))
			{
				throw new DeletingActivatedEntityException(TestRunResult.class.getSimpleName() + " : " + result.getId());
			}
		}
		// delete generated results
		dao.delete(results);
		// delete assignment
		dao.delete(assignment);
	}

	@Override
	public UtestSearchResult findTestRunAssignments(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(TestRunTestCaseAssignment.class, search_);
	}

	@Override
	public UtestSearchResult findTestRunResults(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(TestRunResult.class, search_);
	}

	@Override
	public UtestSearchResult findTestRuns(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(TestRun.class, search_);
	}

	@Override
	public TestRun getTestRun(final Integer testRunId_) throws Exception
	{
		final TestRun testRun = dao.getById(TestRun.class, testRunId_);
		if (testRun == null)
		{
			throw new NotFoundException("TestRun#" + testRunId_);
		}
		return testRun;

	}

	@Override
	public TestRunTestCase getTestRunTestCase(final Integer testRunTestCaseId_) throws Exception
	{
		final TestRunTestCase testRunTestCase = dao.getById(TestRunTestCase.class, testRunTestCaseId_);
		if (testRunTestCase == null)
		{
			throw new NotFoundException("TestRunTestCase#" + testRunTestCaseId_);
		}
		return testRunTestCase;

	}

	@Override
	public List<TestRunTestCase> findTestRunTestCases(final Integer testRunId_) throws Exception
	{
		final Search search = new Search(TestRunTestCase.class);
		search.addFilterEqual("testRunId", testRunId_);
		return dao.search(TestRunTestCase.class, search);
	}

	@Override
	public List<TestRunTestCaseAssignment> findTestRunAssignments(final Integer testRunId_) throws Exception
	{
		final Search search = new Search(TestRunTestCaseAssignment.class);
		search.addFilterEqual("testRunId", testRunId_);
		return dao.search(TestRunTestCaseAssignment.class, search);
	}

	@Override
	public List<TestRunResult> findTestRunResults(final Integer testRunId_, final Integer testerId_, final Integer environmentGroupId_) throws Exception
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
	public List<TestRunResult> findTestRunResults(final Integer testRunId_) throws Exception
	{
		return findTestRunResults(testRunId_, null);
	}

	@Override
	public List<TestRunResult> findTestRunResults(final Integer testRunId_, final List<Integer> resultStatusIds_) throws Exception
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
		final TestRun testRun = dao.getById(TestRun.class, testRunId_);
		if (testRun == null)
		{
			throw new IllegalArgumentException("TestRun not found: " + testRunId_);
		}
		final List<Integer> statusesToRetest = new ArrayList<Integer>();
		statusesToRetest.add(TcmEntityStatus.FAILED);
		if (!retestFailedOnly_)
		{
			statusesToRetest.add(TcmEntityStatus.PASSED);
		}
		final List<TestRunResult> newResults = new ArrayList<TestRunResult>();
		final List<TestRunResult> resultsToRetest = findTestRunResults(testRunId_, statusesToRetest);
		for (final TestRunResult resultToRetest : resultsToRetest)
		{
			newResults.add(addResultForEnvironmentGroup(resultToRetest.getTestRunAssignmentId(), resultToRetest.getEnvironmentGroupId()));
		}
		return newResults;
	}

	@Override
	public TestRunResult retestTestRunResult(final Integer testRunResultId_, final Integer testerId_) throws Exception
	{
		final TestRunResult result = dao.getById(TestRunResult.class, testRunResultId_);
		if (result == null)
		{
			throw new IllegalArgumentException("TestRunResult not found: " + testRunResultId_);
		}
		final TestRunTestCaseAssignment assignment = dao.getById(TestRunTestCaseAssignment.class, result.getTestRunAssignmentId());
		// create new assignment for new tester
		final TestRunTestCaseAssignment newAssignment = addAssignmentNoResults(assignment.getTestRunTestCaseId(), testerId_);
		// create pending result for new assignment
		return addResultForEnvironmentGroup(newAssignment, result.getEnvironmentGroupId());
	}

	@Override
	public TestRunResult approveTestRunResult(final Integer testRunResultId_, final Integer originalVersionId_) throws Exception
	{
		return updateApprovalStatus(testRunResultId_, ApprovalStatus.APPROVED, originalVersionId_);
	}

	@Override
	public TestRunResult rejectTestRunResult(final Integer testRunResultId_, final Integer originalVersionId_) throws Exception
	{
		return updateApprovalStatus(testRunResultId_, ApprovalStatus.REJECTED, originalVersionId_);
	}

	@Override
	public TestRunResult startExecutingAssignedTestCase(final Integer testRunResultId_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = dao.getById(TestRunResult.class, testRunResultId_);
		if (result == null)
		{
			throw new IllegalArgumentException("TestRunResult not found: " + testRunResultId_);
		}
		// only do if not activated already
		if (!TcmEntityStatus.ACTIVATED.equals(result.getTestRunResultStatusId()))
		{
			if (TcmEntityStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
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
			final TestRun testRun = dao.getById(TestRun.class, result.getTestRunId());
			if (TcmEntityStatus.LOCKED.equals(testRun.getTestRunStatusId()))
			{
				throw new TestCycleClosedException();
			}

			result.setActualTimeInMin(null);
			result.setActualResult(null);
			result.setComment(null);
			result.setFailedStepNumber(null);
			result.setVersion(originalVersionId_);
			result.setTestRunResultStatusId(TcmEntityStatus.ACTIVATED);
			result.setRunDate(new Date());
			return dao.merge(result);
		}
		else
		{
			return result;
		}
	}

	@Override
	public TestRunResult finishExecutingAssignedTestCaseWithSuccess(final Integer testRunResultId_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = dao.getById(TestRunResult.class, testRunResultId_);
		if (result == null)
		{
			throw new IllegalArgumentException("TestRunResult not found: " + testRunResultId_);
		}
		if (!TcmEntityStatus.PASSED.equals(result.getTestRunResultStatusId()))
		{
			if (!TcmEntityStatus.ACTIVATED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionWithoutRestartException();
			}
			if (TcmEntityStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionBlockedException();
			}
			// make sure user executing the result is the same as assigned
			if (!getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			// prevent if test run locked
			final TestRun testRun = dao.getById(TestRun.class, result.getTestRunId());
			if (TcmEntityStatus.LOCKED.equals(testRun.getTestRunStatusId()))
			{
				throw new TestCycleClosedException();
			}

			// unblock all dependent test cases
			blockUnblockSubsequentExecution(result, false);

			result.setVersion(originalVersionId_);
			result.setTestRunResultStatusId(TcmEntityStatus.PASSED);
			result.setActualTimeInMin((int) DateUtil.minutesDifference(result.getRunDate(), new Date()));
			result.setActualResult(null);
			result.setComment(null);
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
		final TestRunResult result = dao.getById(TestRunResult.class, testRunResultId_);
		if (result == null)
		{
			throw new IllegalArgumentException("TestRunResult not found: " + testRunResultId_);
		}
		if (!TcmEntityStatus.FAILED.equals(result.getTestRunResultStatusId()))
		{
			if (!TcmEntityStatus.ACTIVATED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionWithoutRestartException();
			}
			if (TcmEntityStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
			{
				throw new TestCaseExecutionBlockedException();
			}
			// make sure user executing the result is the same as assigned
			if (!getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			// prevent if test run locked
			final TestRun testRun = dao.getById(TestRun.class, result.getTestRunId());
			if (TcmEntityStatus.LOCKED.equals(testRun.getTestRunStatusId()))
			{
				throw new TestCycleClosedException();
			}

			// block all dependent test cases
			blockUnblockSubsequentExecution(result, true);

			result.setVersion(originalVersionId_);
			result.setTestRunResultStatusId(TcmEntityStatus.FAILED);
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
				if (!TcmEntityStatus.FAILED.equals(result.getTestRunResultStatusId()) && !TcmEntityStatus.PASSED.equals(result.getTestRunResultStatusId()))
				{
					if (needToBlock_ && !TcmEntityStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
					{
						result.setTestRunResultStatusId(TcmEntityStatus.BLOCKED);
						dao.addOrUpdate(result);
					}
					// unblock blocked test cases
					else if (TcmEntityStatus.BLOCKED.equals(result.getTestRunResultStatusId()))
					{
						result.setTestRunResultStatusId(TcmEntityStatus.DRAFT);
						dao.addOrUpdate(result);
					}
				}
			}
		}

	}

	@Override
	public TestRun saveTestRun(final Integer testRunId_, final String name_, final String description_, final Date startDate_, final Date endDate_,
			final boolean selfAssignAllowed_, final Integer originalVersionId_) throws Exception
	{
		final TestRun testRun = dao.getById(TestRun.class, testRunId_);
		if (testRun == null)
		{
			throw new NotFoundException(TestRun.class.getSimpleName() + " not found: " + testRunId_);
		}

		checkForDuplicateNameWithinParent(TestRun.class, name_, testRun.getTestCycleId(), "testCycleId", testRunId_);

		testRun.setName(name_);
		testRun.setDescription(description_);
		testRun.setStartDate(startDate_);
		testRun.setEndDate(endDate_);
		testRun.setSelfAssignAllowed(selfAssignAllowed_);
		testRun.setVersion(originalVersionId_);
		return dao.merge(testRun);
	}

	@Override
	public TestRunTestCase saveTestRunTestCase(final Integer includedTestCaseId_, final Integer priorityId_, final Integer runOrder_, final boolean blocking_,
			final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_, final Integer originalVersionId_)
	{
		final TestRunTestCase includedTestCase = dao.getById(TestRunTestCase.class, includedTestCaseId_);
		if (includedTestCase == null)
		{
			throw new NotFoundException("TestRunTestCase not found: " + includedTestCaseId_);
		}
		// prevent if already activated
		final TestRun testRun = dao.getById(TestRun.class, includedTestCase.getTestRunId());
		if (testRun == null)
		{
			throw new NotFoundException("TestRun not found: " + includedTestCase.getTestSuiteId());
		}
		if (!TcmEntityStatus.DRAFT.equals(testRun.getTestRunStatusId()))
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

	private TestRunTestCase saveTestRunTestCase(final TestRunTestCase includedTestCase_)
	{
		// prevent if already activated
		final TestRun testRun = dao.getById(TestRun.class, includedTestCase_.getTestRunId());
		if (!TcmEntityStatus.DRAFT.equals(testRun.getTestRunStatusId()))
		{
			throw new ChangingActivatedEntityException(TestRunTestCase.class.getSimpleName());
		}
		return dao.merge(includedTestCase_);
	}

	@Override
	public TestRun activateTestRun(final Integer testRunId_, final Integer originalVersionId_) throws Exception
	{
		return updateActivationStatus(testRunId_, TcmEntityStatus.ACTIVATED, originalVersionId_);
	}

	private TestRun updateActivationStatus(final Integer testRunId_, final Integer activationStatusId_, final Integer originalVersionId_) throws Exception
	{
		// change status for the test run
		final TestRun testRun = dao.getById(TestRun.class, testRunId_);
		if (testRun == null)
		{
			throw new IllegalArgumentException(TestRun.class.getSimpleName() + " not found: " + testRunId_);
		}
		if (!testRun.getTestRunStatusId().equals(activationStatusId_))
		{
			if (TcmEntityStatus.ACTIVATED.equals(activationStatusId_))
			{
				// prevent activating if parent test cycle is not activated
				final TestCycle testCycle = dao.getById(TestCycle.class, testRun.getTestCycleId());
				if (!TcmEntityStatus.ACTIVATED.equals(testCycle.getTestCycleStatusId()))
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

	private TestRunResult updateApprovalStatus(final Integer testRunResultId_, final Integer approvalStatus_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = dao.getById(TestRunResult.class, testRunResultId_);
		if (result == null)
		{
			throw new IllegalArgumentException("TestRunResult not found: " + testRunResultId_);
		}
		if (approvalStatus_ != result.getApprovalStatusId())
		{
			// make sure user approving the result is not the same as assigned
			if (getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			if ((!TcmEntityStatus.FAILED.equals(result.getTestRunResultStatusId()) && !TcmEntityStatus.PASSED.equals(result.getTestRunResultStatusId()))
					&& ApprovalStatus.APPROVED.equals(approvalStatus_))
			{
				throw new ApprovingIncompleteEntityException(TestRunResult.class.getSimpleName() + " : " + testRunResultId_);
			}
			result.setVersion(originalVersionId_);
			result.setApprovalStatusId(approvalStatus_);
			result.setApproveDate(new Date());
			result.setApprovedBy(getCurrentUserId());
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
		return updateActivationStatus(testRunId_, TcmEntityStatus.LOCKED, originalVersionId_);
	}
}
