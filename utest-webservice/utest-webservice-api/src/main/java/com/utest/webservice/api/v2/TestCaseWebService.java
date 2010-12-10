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
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.ResourceIdentity;
import com.utest.webservice.model.v2.TagInfo;
import com.utest.webservice.model.v2.TestCaseInfo;
import com.utest.webservice.model.v2.TestCaseResultInfo;
import com.utest.webservice.model.v2.TestCaseStepInfo;
import com.utest.webservice.model.v2.TestCaseVersionInfo;
import com.utest.webservice.model.v2.TestCaseVersionResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestCaseWebService
{

	TestCaseVersionInfo activateTestCaseVersion(UriInfo ui, Integer testCaseId, Integer testCaseVersionId, TestCaseVersionInfo testCaseVersionInfo) throws Exception;

	TestCaseVersionInfo approveTestCaseVersion(UriInfo ui, Integer testCaseId, Integer testCaseVersionId, TestCaseVersionInfo testCaseVersionInfo) throws Exception;

	TestCaseVersionInfo cloneTestCase(UriInfo ui, Integer testCaseId) throws Exception;

	TestCaseVersionInfo createTestCase(UriInfo ui, TestCaseVersionInfo testCaseVersionInfo) throws Exception;

	Boolean deleteTestCase(UriInfo ui, Integer testCaseId) throws Exception;

	TestCaseVersionResultInfo findLatestTestCaseVersions(UriInfo ui, UtestSearchRequest request) throws Exception;

	TestCaseVersionInfo getLatestTestCaseVersion(UriInfo ui, Integer testCaseId) throws Exception;

	List<TestCaseVersionInfo> getTestCaseVersions(UriInfo ui, Integer testCaseId) throws Exception;

	Boolean updateTestCaseComponents(UriInfo ui, Integer testCaseId, ArrayList<Integer> productComponentIds) throws Exception;

	List<ProductComponentInfo> getTestCaseComponents(UriInfo ui, Integer testCaseId) throws Exception;

	List<EnvironmentGroupInfo> getTestCaseEnvironmentGroups(UriInfo ui, Integer testCaseId, Integer testCaseVersionId) throws Exception;

	List<TagInfo> getTestCaseTags(UriInfo ui, Integer testCaseId) throws Exception;

	TestCaseInfo getTestCase(UriInfo ui, Integer testCaseId) throws Exception;

	TestCaseResultInfo findTestCases(UriInfo ui, UtestSearchRequest request) throws Exception;

	TestCaseInfo updateTestCase(UriInfo ui, Integer testCaseId, TestCaseInfo testCaseInfo) throws Exception;

	Boolean updateTestCaseTags(UriInfo ui, Integer testCaseId, ArrayList<Integer> tagIds) throws Exception;

	TestCaseVersionResultInfo findTestCaseVersions(UriInfo ui, UtestSearchRequest request) throws Exception;

	TestCaseStepInfo createTestCaseStep(UriInfo ui, Integer testCaseVersionId, TestCaseStepInfo testCaseStepInfo) throws Exception;

	TestCaseVersionInfo deactivateTestCaseVersion(UriInfo ui, Integer testCaseVersionId, TestCaseVersionInfo testCaseVersionInfo) throws Exception;

	Boolean deleteTestCaseStep(UriInfo ui, Integer testCaseVersionId, Integer testCaseStepId) throws Exception;

	Boolean deleteTestCaseVersion(UriInfo ui, Integer testCaseVersionId) throws Exception;

	TestCaseVersionInfo getTestCaseVersion(UriInfo ui, Integer testCaseVersionId) throws Exception;

	List<TestCaseStepInfo> getTestCaseVersionSteps(UriInfo ui, Integer testCaseVersionId) throws Exception;

	TestCaseVersionInfo rejectTestCaseVersion(UriInfo ui, Integer testCaseVersionId, TestCaseVersionInfo testCaseVersionInfo) throws Exception;

	Boolean updateTestCaseEnvironmentGroups(UriInfo ui, Integer testCaseVersionId, ArrayList<Integer> environmentGroupIds, ResourceIdentity testCaseVersionIdentity)
			throws Exception;

	TestCaseStepInfo updateTestCaseStep(UriInfo ui, Integer testCaseVersionId, Integer testCaseStepId, TestCaseStepInfo testCaseStepInfo) throws Exception;

	TestCaseVersionInfo updateTestCaseVersion(UriInfo ui, Integer testCaseVersionId, String versionIncrement, TestCaseVersionInfo testCaseVersionInfo) throws Exception;

	TestCaseVersionInfo updateTestCaseVersion(UriInfo ui, Integer testCaseVersionId, TestCaseVersionInfo testCaseVersionInfo) throws Exception;

	TestCaseStepInfo getTestCaseVersionStep(UriInfo ui, Integer testCaseVersionId, Integer testCaseStepId) throws Exception;

}
