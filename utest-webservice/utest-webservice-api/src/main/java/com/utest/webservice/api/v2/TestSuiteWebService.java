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
import com.utest.webservice.model.v2.TestSuiteSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestSuiteWebService
{

	Boolean updateTestSuiteEnvironmentGroups(UriInfo ui, Integer testSuiteId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> getTestSuiteEnvironmentGroups(UriInfo ui, Integer testSuiteId) throws Exception;

	TestSuiteInfo getTestSuite(UriInfo ui, Integer testSuiteId) throws Exception;

	TestSuiteSearchResultInfo findTestSuites(UriInfo ui, UtestSearchRequest request) throws Exception;

	List<IncludedTestCaseInfo> getTestSuiteTestCases(UriInfo ui, Integer testSuiteId) throws Exception;

	TestSuiteInfo activateTestSuite(UriInfo ui, Integer testSuiteId, Integer originalVesionId) throws Exception;

	TestSuiteInfo deactivateTestSuite(UriInfo ui, Integer testSuiteId, Integer originalVesionId) throws Exception;

	Boolean deleteTestSuite(UriInfo ui, Integer testSuiteId, Integer originalVesionId) throws Exception;

	Boolean deleteTestSuiteTestCase(UriInfo ui, Integer includedTestCaseId, Integer originalVesionId) throws Exception;

	IncludedTestCaseInfo getTestSuiteTestCase(UriInfo ui, Integer includedTestCaseId) throws Exception;

	TestSuiteInfo createTestSuite(UriInfo ui, Integer productId, String useLatestVersions, String name, String description) throws Exception;

	TestSuiteInfo updateTestSuite(UriInfo ui, Integer testSuiteId, String name, String description, Integer originalVersionId) throws Exception;

	IncludedTestCaseInfo createTestSuiteTestCase(UriInfo ui, Integer testSuiteId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, String blocking)
			throws Exception;

	IncludedTestCaseInfo updateTestSuiteTestCase(UriInfo ui, Integer includedTestCaseId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, String blocking,
			Integer originalVersionId) throws Exception;

}
