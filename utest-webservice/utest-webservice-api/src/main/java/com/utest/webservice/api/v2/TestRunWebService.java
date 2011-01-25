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
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.IncludedTestCaseInfo;
import com.utest.webservice.model.v2.IncludedTestCaseSearchResultInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.TestRunInfo;
import com.utest.webservice.model.v2.TestRunResultInfo;
import com.utest.webservice.model.v2.TestRunResultSearchResultInfo;
import com.utest.webservice.model.v2.TestRunSearchResultInfo;
import com.utest.webservice.model.v2.TestRunTestCaseAssignmentInfo;
import com.utest.webservice.model.v2.TestRunTestCaseAssignmentSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestRunWebService
{

	TestRunInfo updateTestRun(UriInfo ui, Integer testRunId, TestRunInfo testRunInfo) throws Exception;

	Boolean updateTestRunEnvironmentGroups(UriInfo ui, Integer testRunId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> getTestRunEnvironmentGroups(UriInfo ui, Integer testRunId) throws Exception;

	TestRunInfo createTestRun(UriInfo ui, TestRunInfo testRunInfo) throws Exception;

	TestRunInfo getTestRun(UriInfo ui, Integer testRunId) throws Exception;

	TestRunSearchResultInfo findTestRuns(UriInfo ui, UtestSearchRequest request) throws Exception;

	List<IncludedTestCaseInfo> getTestRunTestCases(UriInfo ui, Integer testRunId) throws Exception;

	IncludedTestCaseInfo createTestRunTestCase(UriInfo ui, Integer testRunId, IncludedTestCaseInfo testCaseInfo) throws Exception;

	TestRunInfo activateTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	TestRunInfo deactivateTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	Boolean deleteTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	List<IncludedTestCaseInfo> createTestCasesFromTestPlan(UriInfo ui, Integer testRunId, Integer testPlanId) throws Exception;

	List<IncludedTestCaseInfo> createTestCasesFromTestSuite(UriInfo ui, Integer testRunId, Integer testSuiteId) throws Exception;

	TestRunTestCaseAssignmentSearchResultInfo findTestRunAssignments(UriInfo ui, UtestSearchRequest request) throws Exception;

	TestRunResultSearchResultInfo findTestRunResults(UriInfo ui, UtestSearchRequest request) throws Exception;

	List<ProductComponentInfo> getTestRunComponents(UriInfo ui, Integer testRunId) throws Exception;

	List<TestRunResultInfo> retestTestRun(UriInfo ui, Integer testRunId, boolean failedResultsOnly) throws Exception;

	IncludedTestCaseInfo getTestRunTestCase(UriInfo ui, Integer includedTestCaseId) throws Exception;

	Boolean deleteTestRunTestCase(UriInfo ui, Integer includedTestCaseId, Integer originalVersionId) throws Exception;

	IncludedTestCaseInfo updateTestRunTestCase(UriInfo ui, Integer includedTestCaseId, IncludedTestCaseInfo includedTestCaseInfo) throws Exception;

	Boolean updateTestRunTestCaseEnvironmentGroups(UriInfo ui, Integer includedTestCaseId, ArrayList<Integer> environmentGroupIds, Integer originalVersionId) throws Exception;

	List<EnvironmentGroupInfo> getTestRunTestCaseEnvironmentGroups(UriInfo ui, Integer includedTestCaseId) throws Exception;

	List<TestRunTestCaseAssignmentInfo> getTestRunTestCaseAssignments(UriInfo ui, Integer includedTestCaseId) throws Exception;

	TestRunTestCaseAssignmentInfo createTestRunTestCaseAssignment(UriInfo ui, Integer includedTestCaseId, TestRunTestCaseAssignmentInfo assignmentInfo) throws Exception;

	TestRunTestCaseAssignmentInfo getTestRunTestCaseAssignment(UriInfo ui, Integer assignmentId) throws Exception;

	Boolean deleteTestRunTestCaseAssignment(UriInfo ui, Integer assignmentId, Integer originalVersionId) throws Exception;

	Boolean updateTestRunTestCaseAssignmentEnvironmentGroups(UriInfo ui, Integer assignmentId, ArrayList<Integer> environmentGroupIds, Integer originalVersionId) throws Exception;

	List<EnvironmentGroupInfo> getTestRunTestCaseAssignmentEnvironmentGroups(UriInfo ui, Integer assignmentId) throws Exception;

	List<TestRunResultInfo> getTestRunTestCaseAssignmentResults(UriInfo ui, Integer assignmentId) throws Exception;

	TestRunResultInfo retestTestRunResult(UriInfo ui, Integer resultId, Integer testerId) throws Exception;

	TestRunResultInfo getTestRunResult(UriInfo ui, Integer resultId) throws Exception;

	Boolean startTestRunResultExecution(UriInfo ui, Integer resultId, Integer originalVersionId) throws Exception;

	Boolean finishFailedTestRunResultExecution(UriInfo ui, Integer resultId, TestRunResultInfo resultInfo, Integer originalVersionId) throws Exception;

	Boolean finishSuccessfulTestRunResultExecution(UriInfo ui, Integer resultId, TestRunResultInfo resultInfo, Integer originalVersionId) throws Exception;

	Boolean approveTestRunResult(UriInfo ui, Integer resultId, Integer originalVersionId) throws Exception;

	Boolean rejectTestRunResult(UriInfo ui, Integer resultId, String comment, Integer originalVersionId) throws Exception;

	List<EnvironmentInfo> getTestRunResultEnvironments(UriInfo ui, Integer resultId) throws Exception;

	IncludedTestCaseSearchResultInfo findTestRunTestCases(UriInfo ui, UtestSearchRequest request) throws Exception;

}
