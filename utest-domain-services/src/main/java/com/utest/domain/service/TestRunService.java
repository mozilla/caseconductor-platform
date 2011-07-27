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

import com.utest.domain.AccessRole;
import com.utest.domain.Attachment;
import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.ProductComponent;
import com.utest.domain.TestRun;
import com.utest.domain.TestRunResult;
import com.utest.domain.TestRunTestCase;
import com.utest.domain.TestRunTestCaseAssignment;
import com.utest.domain.TestSuite;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.view.CategoryValue;
import com.utest.domain.view.TestRunTestCaseView;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to the Test Case Management.
 */
public interface TestRunService
{

	List<EnvironmentGroup> getEnvironmentGroupsForTestRun(Integer testRunId) throws Exception;

	TestRunTestCase addTestRunTestCase(Integer testRunId, Integer testCaseVersionId) throws Exception;

	void deleteTestRun(Integer testRunId, final Integer originalVersionId_) throws Exception;

	void deleteTestRunTestCase(Integer testRunTestCaseId, final Integer originalVersionId_) throws Exception;

	UtestSearchResult findTestRuns(UtestSearch search, Integer includedTestSuiteId_, Integer includedTestCaseId_, Integer includedTestCaseVersionId_, Integer teamMemberId_, Integer includedEnvironmentId_) throws Exception;

	TestRun getTestRun(Integer testRunId) throws Exception;

	TestRunTestCase getTestRunTestCase(Integer testRunTestCaseId) throws Exception;

	List<TestRunTestCase> getTestRunTestCases(Integer testRunId) throws Exception;

	TestRunTestCaseAssignment addAssignment(Integer testRunTestCaseId, Integer testerId) throws Exception;

	TestRunTestCaseAssignment addAssignment(Integer testRunTestCaseId, Integer testerId, List<Integer> environmentGroupIds) throws Exception;

	List<TestRunResult> getTestRunResults(Integer testRunId) throws Exception;

	List<TestRunTestCaseAssignment> getTestRunAssignments(Integer testRunId) throws Exception;

	List<TestRunResult> getTestRunResults(Integer testRunId, Integer testerId, Integer environmentGroupId) throws Exception;

	UtestSearchResult findTestRunAssignments(UtestSearch search, Integer includedEnvironmentId_) throws Exception;

	UtestSearchResult findTestRunResults(UtestSearch search, Integer includedEnvironmentId_) throws Exception;

	TestRunResult retestTestRunResult(Integer testRunResultId, Integer testerId) throws Exception;

	void saveEnvironmentGroupsForTestRun(Integer testRunId, List<Integer> environmentGroupIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	TestRunResult approveTestRunResult(Integer testRunResultId, Integer originalVersionId) throws Exception;

	TestRunResult startExecutingAssignedTestCase(Integer testRunResultId, Integer originalVersionId) throws Exception;

	TestRunResult finishExecutingAssignedTestCaseWithFailure(Integer testRunResultId, Integer failedStepNumber, String actualResult, String comment, Integer originalVersionId)
			throws Exception;

	TestRun activateTestRun(Integer testRunId, Integer originalVersionId) throws Exception;

	TestRun lockTestRun(Integer testRunId, Integer originalVersionId) throws Exception;

	TestRunTestCase saveTestRunTestCase(Integer includedTestCaseId, Integer priorityId, Integer runOrder, boolean blocking, boolean selfAssignAllowed,
			boolean selfAssignPerEnvironment, Integer selfAssignLimit, Integer originalVersionId);

	TestRunTestCase addTestRunTestCase(Integer testRunId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, boolean isBlocking, Integer testSuiteId)
			throws Exception;

	List<TestRunResult> getTestRunResults(Integer testRunId, List<Integer> resultStatusIds) throws Exception;

	List<TestRunResult> retestTestRun(Integer testRunId, boolean retestFailedOnly) throws Exception;

	void deleteAssignment(Integer assignmentId, Integer originalVersionId) throws Exception;

	TestRunTestCase addTestRunTestCase(Integer testRunId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, boolean isBlocking) throws Exception;

	List<TestRunTestCase> addTestCasesFromTestPlan(Integer testRunId, Integer testPlanId) throws Exception;

	List<TestRunTestCase> addTestCasesFromTestSuite(Integer testRunId, Integer testSuiteId) throws Exception;

	TestRunTestCase saveTestRunTestCase(Integer includedTestCaseId, Integer priorityId, Integer runOrder, boolean blocking, Integer originalVersionId);

	void saveEnvironmentGroupsForTestRunTestCase(Integer testRunTestCaseId, List<Integer> environmentGroupIds, Integer originalVersionId)
			throws UnsupportedEnvironmentSelectionException, Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForTestRunTestCase(Integer testRunTestCaseId) throws Exception;

	List<TestRunTestCaseAssignment> getTestRunTestCaseAssignments(Integer testRunTestCaseId) throws Exception;

	TestRunTestCaseAssignment getTestRunTestCaseAssignment(Integer assignmentId) throws Exception;

	void saveEnvironmentGroupsForAssignment(Integer assignmentId, List<Integer> environmentGroupIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForAssignment(Integer assignmentId) throws Exception;

	List<TestRunResult> getTestRunResultsForAssignment(Integer testRunAssignmentId) throws Exception;

	TestRunResult getTestRunResult(Integer testRunResultId) throws Exception;

	TestRunResult finishExecutingAssignedTestCaseWithSuccess(Integer testRunResultId, String comment, Integer originalVersionId) throws Exception;

	TestRunResult rejectTestRunResult(Integer testRunResultId, String comment, Integer originalVersionId) throws Exception;

	List<Environment> getEnvironmentsForTestResult(Integer resultId) throws Exception;

	List<ProductComponent> getTestRunComponents(Integer testRunId) throws Exception;

	UtestSearchResult findTestRunTestCases(UtestSearch search, Integer includedEnvironmentId_) throws Exception;

	TestRunResult finishExecutingAssignedTestCaseWithInvalidation(Integer testRunResultId, String comment, Integer originalVersionId) throws Exception;

	List<User> getTestingTeamForTestRun(Integer testRunId) throws Exception;

	void saveTestingTeamForTestRun(Integer testRunId, List<Integer> userIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException, Exception;

	void saveTestingTeamMemberRolesForTestRun(Integer testRunId, Integer userId, List<Integer> roleIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	TestRun addTestRun(Integer testCycleId, boolean useLatestVersions, String name, String description, Date startDate, Date endDate, boolean selfAssignAllowed,
			boolean selfAssignPerEnvironment, Integer selfAssignLimit, boolean autoAssignToTeam) throws Exception;

	List<AccessRole> getTestingTeamMemberRolesForTestRun(Integer testRunId, Integer userId) throws Exception;

	void approveAllTestRunResultsForTestRun(Integer testRunId) throws Exception;

	TestRun cloneTestRun(Integer fromTestRunId, boolean cloneAssignments) throws Exception;

	TestRun cloneTestRun(Integer fromTestRunId, Integer newTestCycleId, boolean cloneAssignments) throws Exception;

	List<TestSuite> getTestRunTestSuites(Integer testRunId) throws Exception;

	TestRunResult finishExecutingAssignedTestCaseWithSkip(Integer testRunResultId, String comment, Integer originalVersionId) throws Exception;

	List<CategoryValue> getCoverageByStatus(Integer testRunId);

	void approveTestRunResults(List<Integer> testRunResultId) throws Exception;

	void approveAllTestRunResultsForTestRunTestCase(Integer testRunId, Integer testCaseId) throws Exception;

	TestRunTestCaseView getTestRunTestCaseView(Integer testRunTestCaseId) throws Exception;

	List<TestRunTestCaseView> getTestRunTestCasesViews(Integer testRunId) throws Exception;

	TestRun saveTestRun(Integer testRunId, boolean useLatestVersions, String name, String description, Date startDate, Date endDate, boolean selfAssignAllowed,
			boolean selfAssignPerEnvironment, Integer selfAssignLimit, Integer originalVersionId, boolean autoAssignToTeam) throws Exception;

	List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForAssignment(Integer assignmentId) throws Exception;

	List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestRun(Integer testRunId) throws Exception;

	List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestRunTestCase(Integer testRunTestCaseId) throws Exception;

	List<Attachment> getAttachmentsForTestRun(Integer testRunId) throws Exception;

	List<Attachment> getAttachmentsForTestRunResult(Integer testRunResultId) throws Exception;

	Attachment addAttachmentForTestRunResult(String name, String description, String url, Double size, Integer testRunResultId, Integer attachmentTypeId) throws Exception;

	Attachment addAttachmentForTestRun(String name, String description, String url, Double size, Integer testRunId, Integer attachmentTypeId) throws Exception;

	boolean deleteAttachmentForTestRun(Integer attachmentId, Integer entityId) throws Exception;

	boolean deleteAttachmentForTestRunResult(Integer attachmentId, Integer entityId) throws Exception;
}
