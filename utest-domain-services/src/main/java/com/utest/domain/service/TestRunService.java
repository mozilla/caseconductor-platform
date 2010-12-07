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
package com.utest.domain.service;

import java.util.Date;
import java.util.List;

import com.utest.domain.EnvironmentGroup;
import com.utest.domain.TestRun;
import com.utest.domain.TestRunResult;
import com.utest.domain.TestRunTestCase;
import com.utest.domain.TestRunTestCaseAssignment;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to the Test Case Management.
 */
public interface TestRunService
{

	List<EnvironmentGroup> findEnvironmentGroupsForTestRun(Integer testRunId) throws Exception;

	TestRunTestCase addTestRunTestCase(Integer testRunId, Integer testCaseVersionId) throws Exception;

	void deleteTestRun(Integer testRunId) throws Exception;

	void deleteTestRunTestCase(Integer testRunTestCaseId) throws Exception;

	UtestSearchResult findTestRuns(UtestSearch search) throws Exception;

	TestRun getTestRun(Integer testRunId) throws Exception;

	TestRunTestCase getTestRunTestCase(Integer testRunTestCaseId) throws Exception;

	List<TestRunTestCase> findTestRunTestCases(Integer testRunId) throws Exception;

	TestRunTestCaseAssignment addAssignment(Integer testRunTestCaseId, Integer testerId) throws Exception;

	TestRunTestCaseAssignment addAssignment(Integer testRunTestCaseId, Integer testerId, List<Integer> environmentGroupIds) throws Exception;

	List<TestRunResult> findTestRunResults(Integer testRunId) throws Exception;

	List<TestRunTestCaseAssignment> findTestRunAssignments(Integer testRunId) throws Exception;

	List<TestRunResult> findTestRunResults(Integer testRunId, Integer testerId, Integer environmentGrpoupId) throws Exception;

	void deleteAssignment(Integer assignmentId) throws Exception;

	UtestSearchResult findTestRunAssignments(UtestSearch search) throws Exception;

	UtestSearchResult findTestRunResults(UtestSearch search) throws Exception;

	TestRunResult retestTestRunResult(Integer testRunResultId, Integer testerId) throws Exception;

	void saveEnvironmentGroupsForTestRun(Integer testRunId, List<Integer> environmentGroupIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	TestRunResult approveTestRunResult(Integer testRunResultId, Integer originalVersionId) throws Exception;

	TestRunResult rejectTestRunResult(Integer testRunResultId, Integer originalVersionId) throws Exception;

	TestRunResult startExecutingAssignedTestCase(Integer testRunResultId, Integer originalVersionId) throws Exception;

	TestRunResult finishExecutingAssignedTestCaseWithSuccess(Integer testRunResultId, Integer originalVersionId) throws Exception;

	TestRunResult finishExecutingAssignedTestCaseWithFailure(Integer testRunResultId, Integer failedStepNumber, String actualResult, String comment, Integer originalVersionId)
			throws Exception;

	TestRun saveTestRun(Integer testRunId, String name, String description, Date startDate, Date endDate, boolean selfAssignAllowed, Integer originalVersionId) throws Exception;

	TestRun activateTestRun(Integer testRunId, Integer originalVersionId) throws Exception;

	TestRun lockTestRun(Integer testRunId, Integer originalVersionId) throws Exception;

	TestRunTestCase saveTestRunTestCase(Integer includedTestCaseId, Integer priorityId, Integer runOrder, boolean blocking, boolean selfAssignAllowed,
			boolean selfAssignPerEnvironment, Integer selfAssignLimit, Integer originalVersionId);

	TestRun addTestRun(Integer testCycleId, boolean useLatestVersiuons, String name, String description, Date startDate, Date endDate, boolean selfAssignAllowed,
			boolean selfAssignPerEnvironment, Integer selfAssignLimit) throws Exception;

	TestRun addTestRunFromTestSuite(Integer testCycleId, Integer testSuiteId, boolean useLatestVersiuons, String name, String description, Date startDate, Date endDate,
			boolean selfAssignAllowed, boolean selfAssignPerEnvironment, Integer selfAssignLimit) throws Exception;

	TestRun addTestRunFromTestPlan(Integer testCycleId, Integer testPlanId, boolean useLatestVersiuons, String name, String description, Date startDate, Date endDate,
			boolean selfAssignAllowed, boolean selfAssignPerEnvironment, Integer selfAssignLimit) throws Exception;

	TestRunTestCase addTestRunTestCase(Integer testRunId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, boolean isBlocking, Integer testSuiteId)
			throws Exception;

	List<TestRunResult> findTestRunResults(Integer testRunId, List<Integer> resultStatusIds) throws Exception;

	List<TestRunResult> retestTestRun(Integer testRunId, boolean retestFailedOnly) throws Exception;
}
