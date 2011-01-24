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
import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.TestCaseStatus;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.TestCycle;
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
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.TestCaseService;
import com.utest.domain.service.TestPlanService;
import com.utest.domain.service.TestRunService;
import com.utest.domain.service.TestSuiteService;
import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ApprovingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.IncludingMultileVersionsOfSameEntityException;
import com.utest.exception.IncludingNotActivatedEntityException;
import com.utest.exception.InvalidUserException;
import com.utest.exception.TestCaseExecutionBlockedException;
import com.utest.exception.TestCaseExecutionWithoutRestartException;
import com.utest.exception.TestCycleClosedException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;
import com.utest.util.DateUtil;

public class TestRunServiceImpl extends BaseServiceImpl implements TestRunService
{
	private final TypelessDAO			dao;
	private final EnvironmentService	environmentService;
	private final TestPlanService		testPlanService;
	private final TestSuiteService		testSuiteService;
	private final TestCaseService		testCaseService;

	/**
	 * Default constructor
	 */
	public TestRunServiceImpl(final TypelessDAO dao, final TestPlanService testPlanService, final TestSuiteService testSuiteService, final TestCaseService testCaseService,
			final EnvironmentService environmentService)
	{
		super(dao);
		this.dao = dao;
		this.environmentService = environmentService;
		this.testPlanService = testPlanService;
		this.testSuiteService = testSuiteService;
		this.testCaseService = testCaseService;
	}

	@Override
	public TestRun addTestRun(final Integer testCycleId_, final boolean useLatestVersions_, final String name_, final String description_, final Date startDate_,
			final Date endDate_, final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_) throws Exception
	{
		final TestCycle testCycle = findEntityById(TestCycle.class, testCycleId_);
		checkForDuplicateNameWithinParent(TestRun.class, name_, testCycleId_, "testCycleId", null);

		// TODO - do we need to validate test cycle status before creating a
		// test run?
		final TestRun testRun = new TestRun();
		testRun.setTestRunStatusId(TestRunStatus.PENDING);
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
	public List<TestRunTestCase> addTestCasesFromTestPlan(final Integer testRunId_, final Integer testPlanId_) throws Exception
	{
		final TestPlan testPlan = findEntityById(TestPlan.class, testPlanId_);
		// check if test plan is activated
		if (!TestPlanStatus.ACTIVE.equals(testPlan.getTestPlanStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestPlan.class.getSimpleName() + " : " + testPlanId_);
		}
		final List<TestPlanTestSuite> includedTestSuites = testPlanService.getTestPlanTestSuites(testPlanId_);
		Integer startingRunOrder = 0;
		for (final TestPlanTestSuite testPlanTestSuite : includedTestSuites)
		{
			// increment by 1 before adding new test suite cases
			startingRunOrder++;
			startingRunOrder = addTestCasesFromTestSuite(testRunId_, testPlanTestSuite.getTestSuiteId(), startingRunOrder);
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
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
		final TestSuite testSuite = findEntityById(TestSuite.class, testSuiteId_);
		// check if test plan is activated
		if (!TestSuiteStatus.ACTIVE.equals(testSuite.getTestSuiteStatusId()))
		{
			throw new IncludingNotActivatedEntityException(TestSuite.class.getSimpleName() + " : " + testSuiteId_);
		}
		final List<TestSuiteTestCase> includedTestCases = testSuiteService.getTestSuiteTestCases(testSuiteId_);
		Integer lastRunOrder = startingRunOrder_;
		for (final TestSuiteTestCase testSuiteTestCase : includedTestCases)
		{
			lastRunOrder += testSuiteTestCase.getRunOrder();
			final TestRunTestCase testRunTestCase = addTestRunTestCase(testRunId_, testSuiteTestCase.getTestCaseVersionId(), testSuiteTestCase.getPriorityId(), testSuiteTestCase
					.getRunOrder(), testSuiteTestCase.isBlocking(), testSuiteId_);
			// TODO - determine what is needed for the intersection
			if (testRun.getEnvironmentProfileId() != null)
			{
				testRunTestCase.setEnvironmentProfileId(testRun.getEnvironmentProfileId());
			}
			else
			{
				testRunTestCase.setEnvironmentProfileId(testSuiteTestCase.getEnvironmentProfileId());
			}
			dao.merge(testRunTestCase);
		}
		return lastRunOrder;
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForTestRun(final Integer testRunId_) throws Exception
	{
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
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
	public List<EnvironmentGroup> getEnvironmentGroupsForTestRunTestCase(final Integer testRunTestCaseId_) throws Exception
	{
		final TestRunTestCase testRunTestCase = findEntityById(TestRunTestCase.class, testRunTestCaseId_);
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
	public List<Environment> getEnvironmentsForTestResult(final Integer resultId_) throws Exception
	{
		final TestRunResult result = findEntityById(TestRunResult.class, resultId_);
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
		final TestRunTestCaseAssignment assignment = findEntityById(TestRunTestCaseAssignment.class, assignmentId_);
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
	public void saveEnvironmentGroupsForTestRun(final Integer testRunId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
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
		final TestCycle testCycle = findEntityById(TestCycle.class, testRun.getTestCycleId());
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(testCycle.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		// update environment profile
		final Product product = findEntityById(Product.class, testRun.getProductId());
		final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test run : " + testRunId_, "Included groups: "
				+ environmentGroupIds_.toString(), environmentGroupIds_);
		testRun.setEnvironmentProfileId(environmentProfile.getId());
		testRun.setVersion(originalVersionId_);
		dao.merge(testRun);
	}

	@Override
	public void saveEnvironmentGroupsForTestRunTestCase(final Integer testRunTestCaseId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRunTestCase testRunTestCase = findEntityById(TestRunTestCase.class, testRunTestCaseId_);
		final TestRun testRun = findEntityById(TestRun.class, testRunTestCase.getTestRunId());
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
		final Product product = findEntityById(Product.class, testRun.getProductId());
		final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for test run test case: " + testRunTestCaseId_,
				"Included groups: " + environmentGroupIds_.toString(), environmentGroupIds_);
		testRunTestCase.setEnvironmentProfileId(environmentProfile.getId());
		testRunTestCase.setVersion(originalVersionId_);
		dao.merge(testRunTestCase);
	}

	@Override
	public void saveEnvironmentGroupsForAssignment(final Integer assignmentId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final TestRunTestCaseAssignment assignment = findEntityById(TestRunTestCaseAssignment.class, assignmentId_);
		final TestRunTestCase testRunTestCase = findEntityById(TestRunTestCase.class, assignment.getTestRunTestCaseId());
		final TestRun testRun = findEntityById(TestRun.class, testRunTestCase.getTestRunId());
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
		final Product product = findEntityById(Product.class, testRun.getProductId());
		final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for assignment: " + assignmentId_,
				"Included groups: " + environmentGroupIds_.toString(), environmentGroupIds_);
		assignment.setEnvironmentProfileId(environmentProfile.getId());
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
		final TestCaseVersion testCaseVersion = findEntityById(TestCaseVersion.class, testCaseVersionId_);
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
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
			throw new IncludingMultileVersionsOfSameEntityException(TestCaseVersion.class.getSimpleName() + " : " + testCaseVersionId_);
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
		return dao.merge(includedTestCase);
	}

	@Override
	public TestRunTestCaseAssignment addAssignment(final Integer testRunTestCaseId_, final Integer testerId_, final List<Integer> environmentGroupIds_) throws Exception
	{
		final TestRunTestCase includedTestCase = findEntityById(TestRunTestCase.class, testRunTestCaseId_);
		@SuppressWarnings("unused")
		final User tester = findEntityById(User.class, testerId_);
		// check if groups assignment is correct
		if (!environmentService.isValidEnvironmentGroupSelectionForProfile(includedTestCase.getEnvironmentProfileId(), environmentGroupIds_))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}

		TestRunTestCaseAssignment assignment = addAssignmentNoResults(testRunTestCaseId_, testerId_);
		// create new profile for new group selection
		final TestRun testRun = findEntityById(TestRun.class, includedTestCase.getTestRunId());
		final Product product = findEntityById(Product.class, testRun.getProductId());
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
		final TestRunTestCase includedTestCase = findEntityById(TestRunTestCase.class, testRunTestCaseId_);
		final TestRunTestCaseAssignment assignment = new TestRunTestCaseAssignment();
		// prevent if already activated
		final TestRun testRun = findEntityById(TestRun.class, includedTestCase.getTestRunId());
		assignment.setTestRunId(testRun.getId());
		assignment.setProductId(testRun.getProductId());
		assignment.setTestCaseId(includedTestCase.getTestCaseId());
		assignment.setTestCaseVersionId(includedTestCase.getTestCaseVersionId());
		assignment.setTestRunTestCaseId(testRunTestCaseId_);
		// TODO - validate if tester from the same company as tested product or
		// is a community tester?
		final User tester = findEntityById(User.class, testerId_);
		assignment.setTesterId(tester.getId());
		assignment.setEnvironmentProfileId(includedTestCase.getEnvironmentProfileId());
		final Integer id = dao.addAndReturnId(assignment);
		return findEntityById(TestRunTestCaseAssignment.class, id);

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
		final List<EnvironmentGroup> groups = environmentService.getEnvironmentGroupsForProfile(assignment_.getEnvironmentProfileId());
		for (final EnvironmentGroup group : groups)
		{
			addResultForEnvironmentGroup(assignment_, group.getId());
		}
	}

	private TestRunResult addResultForEnvironmentGroup(final Integer assignmentId_, final Integer environmentGroupId_) throws Exception
	{
		final TestRunTestCaseAssignment assignment = findEntityById(TestRunTestCaseAssignment.class, assignmentId_);
		return addResultForEnvironmentGroup(assignment, environmentGroupId_);
	}

	private TestRunResult addResultForEnvironmentGroup(final TestRunTestCaseAssignment assignment_, final Integer environmentGroupId_) throws Exception
	{

		final TestRunResult result = new TestRunResult();
		result.setApprovalStatusId(ApprovalStatus.PENDING);
		result.setTestRunResultStatusId(TestRunStatus.PENDING);
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
	public void deleteTestRun(final Integer testRunId_, final Integer originalVersionId_) throws Exception
	{
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
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
		final TestRunTestCase includedTestCase = findEntityById(TestRunTestCase.class, testRunTestCaseId_);
		final TestRun testRun = findEntityById(TestRun.class, includedTestCase.getTestRunId());
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
		final TestRunTestCaseAssignment assignment = findEntityById(TestRunTestCaseAssignment.class, assignmentId_);
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
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
		return testRun;

	}

	@Override
	public TestRunTestCase getTestRunTestCase(final Integer testRunTestCaseId_) throws Exception
	{
		final TestRunTestCase testRunTestCase = findEntityById(TestRunTestCase.class, testRunTestCaseId_);
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
		TestRunTestCaseAssignment assignment = findEntityById(TestRunTestCaseAssignment.class, assignmentId_);
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
		return findEntityById(TestRunResult.class, testRunResultId_);
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
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
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
		final TestRunResult result = findEntityById(TestRunResult.class, testRunResultId_);
		final TestRunTestCaseAssignment assignment = findEntityById(TestRunTestCaseAssignment.class, result.getTestRunAssignmentId());
		// create new assignment for new tester
		final TestRunTestCaseAssignment newAssignment = addAssignmentNoResults(assignment.getTestRunTestCaseId(), testerId_);
		// create pending result for new assignment
		return addResultForEnvironmentGroup(newAssignment, result.getEnvironmentGroupId());
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
		final TestRunResult result = findEntityById(TestRunResult.class, testRunResultId_);
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
			final TestRun testRun = findEntityById(TestRun.class, result.getTestRunId());
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
	public TestRunResult finishExecutingAssignedTestCaseWithSuccess(final Integer testRunResultId_, final String comment_, final Integer originalVersionId_) throws Exception
	{
		final TestRunResult result = findEntityById(TestRunResult.class, testRunResultId_);
		if (!TestRunResultStatus.PASSED.equals(result.getTestRunResultStatusId()))
		{
			if (!TestRunStatus.ACTIVE.equals(result.getTestRunResultStatusId()))
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
			final TestRun testRun = findEntityById(TestRun.class, result.getTestRunId());
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
		final TestRunResult result = findEntityById(TestRunResult.class, testRunResultId_);
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
			final TestRun testRun = findEntityById(TestRun.class, result.getTestRunId());
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
	public TestRun saveTestRun(final Integer testRunId_, final String name_, final String description_, final Date startDate_, final Date endDate_,
			final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_, final Integer originalVersionId_) throws Exception
	{
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
		checkForDuplicateNameWithinParent(TestRun.class, name_, testRun.getTestCycleId(), "testCycleId", testRunId_);

		testRun.setName(name_);
		testRun.setDescription(description_);
		testRun.setStartDate(startDate_);
		testRun.setEndDate(endDate_);
		testRun.setSelfAssignAllowed(selfAssignAllowed_);
		testRun.setSelfAssignLimit(selfAssignLimit_);
		testRun.setSelfAssignPerEnvironment(selfAssignPerEnvironment_);
		testRun.setVersion(originalVersionId_);
		return dao.merge(testRun);
	}

	@Override
	public TestRunTestCase saveTestRunTestCase(final Integer includedTestCaseId_, final Integer priorityId_, final Integer runOrder_, final boolean blocking_,
			final boolean selfAssignAllowed_, final boolean selfAssignPerEnvironment_, final Integer selfAssignLimit_, final Integer originalVersionId_)
	{
		final TestRunTestCase includedTestCase = findEntityById(TestRunTestCase.class, includedTestCaseId_);
		// prevent if already activated
		final TestRun testRun = findEntityById(TestRun.class, includedTestCase.getTestRunId());
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
		final TestRunTestCase includedTestCase = findEntityById(TestRunTestCase.class, includedTestCaseId_);
		// prevent if already activated
		final TestRun testRun = findEntityById(TestRun.class, includedTestCase.getTestRunId());

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
		final TestRun testRun = findEntityById(TestRun.class, testRunId_);
		if (!testRun.getTestRunStatusId().equals(activationStatusId_))
		{
			if (TestRunStatus.ACTIVE.equals(activationStatusId_))
			{
				// prevent activating if parent test cycle is not activated
				final TestCycle testCycle = findEntityById(TestCycle.class, testRun.getTestCycleId());
				if (!TestRunStatus.ACTIVE.equals(testCycle.getTestCycleStatusId()))
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

	private TestRunResult updateApprovalStatus(final Integer testRunResultId_, final String comment_, final Integer approvalStatus_, final Integer originalVersionId_)
			throws Exception
	{
		final TestRunResult result = findEntityById(TestRunResult.class, testRunResultId_);
		if (approvalStatus_ != result.getApprovalStatusId())
		{
			// make sure user approving the result is not the same as assigned
			if (getCurrentUserId().equals(result.getTesterId()))
			{
				throw new InvalidUserException();
			}
			if ((!TestRunResultStatus.FAILED.equals(result.getTestRunResultStatusId()) && !TestRunResultStatus.PASSED.equals(result.getTestRunResultStatusId()))
					&& ApprovalStatus.APPROVED.equals(approvalStatus_))
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
}
