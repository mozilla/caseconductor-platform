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
import com.utest.webservice.model.v2.TestSuiteInfo;
import com.utest.webservice.model.v2.TestSuiteResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestSuiteWebService
{

	TestSuiteInfo updateTestSuite(UriInfo ui, Integer testSuiteId, TestSuiteInfo testSuiteInfo) throws Exception;

	TestSuiteInfo activateTestSuite(UriInfo ui, Integer testSuiteId, TestSuiteInfo testSuiteInfo) throws Exception;

	TestSuiteInfo deactivateTestSuite(UriInfo ui, Integer testSuiteId, TestSuiteInfo testSuiteInfo) throws Exception;

	Boolean updateTestSuiteEnvironmentGroups(UriInfo ui, Integer testSuiteId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> getTestSuiteEnvironmentGroups(UriInfo ui, Integer testSuiteId) throws Exception;

	TestSuiteInfo createTestSuite(UriInfo ui, TestSuiteInfo testSuiteInfo) throws Exception;

	Boolean deleteTestSuite(UriInfo ui, Integer testSuiteId) throws Exception;

	TestSuiteInfo getTestSuite(UriInfo ui, Integer testSuiteId) throws Exception;

	TestSuiteResultInfo findTestSuites(UriInfo ui, UtestSearchRequest request) throws Exception;

	List<IncludedTestCaseInfo> getTestSuiteTestCases(UriInfo ui, Integer testSuiteId) throws Exception;

	IncludedTestCaseInfo createTestSuiteTestCase(UriInfo ui, Integer testSuiteId, IncludedTestCaseInfo testCaseInfo) throws Exception;

	Boolean deleteTestSuiteTestCase(UriInfo ui, Integer testSuiteId, Integer includedTestCaseId) throws Exception;

	IncludedTestCaseInfo updateTestSuiteTestCase(UriInfo ui, Integer testSuiteId, Integer includedTestCaseId, IncludedTestCaseInfo includedTestCaseInfo) throws Exception;

}
