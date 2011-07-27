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
package com.utest.webservice.api.v2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.CategoryValueInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.TestRunTestCaseInfo;
import com.utest.webservice.model.v2.TestRunTestCaseSearchResultInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.TestRunInfo;
import com.utest.webservice.model.v2.TestRunResultInfo;
import com.utest.webservice.model.v2.TestRunResultSearchResultInfo;
import com.utest.webservice.model.v2.TestRunSearchResultInfo;
import com.utest.webservice.model.v2.TestRunTestCaseAssignmentInfo;
import com.utest.webservice.model.v2.TestRunTestCaseAssignmentSearchResultInfo;
import com.utest.webservice.model.v2.TestSuiteInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestRunWebService
{

	Boolean updateTestRunEnvironmentGroups(UriInfo ui, Integer testRunId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> getTestRunEnvironmentGroups(UriInfo ui, Integer testRunId) throws Exception;

	TestRunInfo getTestRun(UriInfo ui, Integer testRunId) throws Exception;

	List<TestRunTestCaseInfo> getTestRunTestCases(UriInfo ui, Integer testRunId) throws Exception;

	TestRunInfo activateTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	TestRunInfo deactivateTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	Boolean deleteTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	List<TestRunTestCaseInfo> createTestCasesFromTestPlan(UriInfo ui, Integer testRunId, Integer testPlanId) throws Exception;

	List<TestRunTestCaseInfo> createTestCasesFromTestSuite(UriInfo ui, Integer testRunId, Integer testSuiteId) throws Exception;

	List<ProductComponentInfo> getTestRunComponents(UriInfo ui, Integer testRunId) throws Exception;

	TestRunTestCaseInfo getTestRunTestCase(UriInfo ui, Integer includedTestCaseId) throws Exception;

	Boolean deleteTestRunTestCase(UriInfo ui, Integer includedTestCaseId, Integer originalVersionId) throws Exception;

	Boolean updateTestRunTestCaseEnvironmentGroups(UriInfo ui, Integer includedTestCaseId, ArrayList<Integer> environmentGroupIds, Integer originalVersionId) throws Exception;

	List<EnvironmentGroupInfo> getTestRunTestCaseEnvironmentGroups(UriInfo ui, Integer includedTestCaseId) throws Exception;

	List<TestRunTestCaseAssignmentInfo> getTestRunTestCaseAssignments(UriInfo ui, Integer includedTestCaseId) throws Exception;

	TestRunTestCaseAssignmentInfo getTestRunTestCaseAssignment(UriInfo ui, Integer assignmentId) throws Exception;

	Boolean deleteTestRunTestCaseAssignment(UriInfo ui, Integer assignmentId, Integer originalVersionId) throws Exception;

	Boolean updateTestRunTestCaseAssignmentEnvironmentGroups(UriInfo ui, Integer assignmentId, ArrayList<Integer> environmentGroupIds, Integer originalVersionId) throws Exception;

	List<EnvironmentGroupInfo> getTestRunTestCaseAssignmentEnvironmentGroups(UriInfo ui, Integer assignmentId) throws Exception;

	List<TestRunResultInfo> getTestRunTestCaseAssignmentResults(UriInfo ui, Integer assignmentId) throws Exception;

	TestRunResultInfo retestTestRunResult(UriInfo ui, Integer resultId, Integer testerId) throws Exception;

	TestRunResultInfo getTestRunResult(UriInfo ui, Integer resultId) throws Exception;

	TestRunResultInfo startTestRunResultExecution(UriInfo ui, Integer resultId, Integer originalVersionId) throws Exception;

	TestRunResultInfo approveTestRunResult(UriInfo ui, Integer resultId, Integer originalVersionId) throws Exception;

	TestRunResultInfo rejectTestRunResult(UriInfo ui, Integer resultId, String comment, Integer originalVersionId) throws Exception;

	List<EnvironmentInfo> getTestRunResultEnvironments(UriInfo ui, Integer resultId) throws Exception;

	TestRunTestCaseInfo createTestRunTestCase(UriInfo ui, Integer testRunId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, String blocking) throws Exception;

	TestRunTestCaseInfo updateTestRunTestCase(UriInfo ui, Integer includedTestCaseId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, String blocking,
			Integer originalVersionId) throws Exception;

	TestRunTestCaseAssignmentInfo createTestRunTestCaseAssignment(UriInfo ui, Integer includedTestCaseId, Integer testerId) throws Exception;

	TestRunResultInfo finishSuccessfulTestRunResultExecution(UriInfo ui, Integer resultId, String comment, Integer originalVersionId) throws Exception;

	TestRunResultInfo finishFailedTestRunResultExecution(UriInfo ui, Integer resultId, Integer failedStepNumber, String actualResult, String comment, Integer originalVersionId)
			throws Exception;

	TestRunResultInfo finishInvalidatedTestRunResultExecution(UriInfo ui, Integer resultId, String comment, Integer originalVersionId) throws Exception;

	TestRunInfo createTestRun(UriInfo ui, Integer testCycleId, String name, String description, String selfAssignAllowed, String selfAssignPerEnvironment, Integer selfAssignLimit,
			Date startDate, Date endDate, String useLatestVersions, String autoAssignToTeam) throws Exception;

	List<UserInfo> getTestRunTeamMembers(UriInfo ui, Integer testCycleId) throws Exception;

	Boolean updateTestRunTeamMembers(UriInfo ui, Integer productId, ArrayList<Integer> userIds, Integer originalVersionId) throws Exception;

	List<RoleInfo> getTestRunTeamMemberRoles(UriInfo ui, Integer productId, Integer userId) throws Exception;

	Boolean updateTestRunTeamMemberRoles(UriInfo ui, Integer productId, Integer userId, ArrayList<Integer> roleIds, Integer originalVersionId) throws Exception;

	Boolean approveAllResultsForTestRun(UriInfo ui, Integer testRunId) throws Exception;

	TestRunInfo cloneTestRun(UriInfo ui, Integer testRunId, String cloneAssignments) throws Exception;

	List<TestSuiteInfo> getTestRunTestSuites(UriInfo ui, Integer testRunId) throws Exception;

	TestRunResultInfo finishSkippedTestRunResultExecution(UriInfo ui, Integer resultId, String comment, Integer originalVersionId) throws Exception;

	List<CategoryValueInfo> getCoverageByResultStatus(UriInfo ui, Integer testRunId) throws Exception;

	List<TestRunResultInfo> retestTestRun(UriInfo ui, Integer testRunId, String failedResultsOnly) throws Exception;

	TestRunInfo cloneTestRun(UriInfo ui, Integer testRunId, Integer targetTestCycleId, String cloneAssignments) throws Exception;

	Boolean approveAllResultsForTestRunTestCase(UriInfo ui, Integer testRunId, Integer testCaseId) throws Exception;

	TestRunInfo updateTestRun(UriInfo ui, Integer testRunId, String name, String description, String useLatestVersions, String selfAssignAllowed, String selfAssignPerEnvironment,
			Integer selfAssignLimit, Date startDate, Date endDate, Integer originalVersionId, String autoAssignToTeam) throws Exception;

	List<EnvironmentGroupExplodedInfo> getTestRunEnvironmentGroupsExploded(UriInfo ui, Integer testRunId) throws Exception;

	List<EnvironmentGroupExplodedInfo> getTestRunTestCaseEnvironmentGroupsExploded(UriInfo ui, Integer includedTestCaseId) throws Exception;

	List<EnvironmentGroupExplodedInfo> getTestRunTestCaseAssignmentEnvironmentGroupsExploded(UriInfo ui, Integer assignmentId) throws Exception;

	List<AttachmentInfo> getTestRunAttachments(UriInfo ui, Integer testRunId) throws Exception;

	List<AttachmentInfo> getTestRunResultAttachments(UriInfo ui, Integer testRunResultId) throws Exception;

	AttachmentInfo createAttachment(UriInfo ui, Integer testRunId, String name, String description, String url, Double size, Integer attachmentTypeId) throws Exception;

	AttachmentInfo createAttachmentForTestRunResult(UriInfo ui, Integer testRunResultId, String name, String description, String url, Double size, Integer attachmentTypeId)
			throws Exception;

	Boolean deleteAttachment(UriInfo ui, Integer testRunId, Integer attachmentId, Integer originalVersionId) throws Exception;

	Boolean deleteAttachmentForTestRunResult(UriInfo ui, Integer testRunResultId, Integer attachmentId, Integer originalVersionId) throws Exception;

	TestRunTestCaseAssignmentSearchResultInfo findTestRunAssignments(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

	TestRunResultSearchResultInfo findTestRunResults(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

	TestRunSearchResultInfo findTestRuns(UriInfo ui, Integer includedEnvironmentId, Integer includedTestSuiteId, Integer includedTestCaseId, Integer includedTestCaseVesionId,
			Integer teamMemberId, UtestSearchRequest request) throws Exception;

	TestRunTestCaseSearchResultInfo findTestRunTestCases(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

	TestRunInfo featureTestRun(UriInfo ui, Integer testRunId, Integer originalVersionId) throws Exception;

	TestRunInfo unfeatureTestRun(UriInfo ui, Integer testRunId, Integer originalVersionId) throws Exception;

}
