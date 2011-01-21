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
import com.utest.webservice.model.v2.IncludedTestCaseInfo;
import com.utest.webservice.model.v2.TestRunInfo;
import com.utest.webservice.model.v2.TestRunSearchResultInfo;
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

	IncludedTestCaseInfo updateTestRunTestCase(UriInfo ui, Integer testRunId, Integer includedTestCaseId, IncludedTestCaseInfo includedTestCaseInfo) throws Exception;

	TestRunInfo activateTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	TestRunInfo deactivateTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	Boolean deleteTestRunTestCase(UriInfo ui, Integer testRunId, Integer includedTestCaseId, Integer originalVesionId) throws Exception;

	Boolean deleteTestRun(UriInfo ui, Integer testRunId, Integer originalVesionId) throws Exception;

	List<IncludedTestCaseInfo> createTestCasesFromTestPlan(UriInfo ui, Integer testRunId, Integer testPlanId) throws Exception;

	List<IncludedTestCaseInfo> createTestCasesFromTestSuite(UriInfo ui, Integer testRunId, Integer testSuiteId) throws Exception;

}
