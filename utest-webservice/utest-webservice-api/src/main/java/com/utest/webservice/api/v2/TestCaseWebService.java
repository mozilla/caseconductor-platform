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

import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.EntityExternalBugInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.TagInfo;
import com.utest.webservice.model.v2.TestCaseInfo;
import com.utest.webservice.model.v2.TestCaseSearchResultInfo;
import com.utest.webservice.model.v2.TestCaseStepInfo;
import com.utest.webservice.model.v2.TestCaseVersionInfo;
import com.utest.webservice.model.v2.TestCaseVersionSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestCaseWebService
{

	TestCaseVersionInfo cloneTestCase(UriInfo ui, Integer testCaseId) throws Exception;

	TestCaseVersionInfo getLatestTestCaseVersion(UriInfo ui, Integer testCaseId) throws Exception;

	List<TestCaseVersionInfo> getTestCaseVersions(UriInfo ui, Integer testCaseId) throws Exception;

	Boolean updateTestCaseComponents(UriInfo ui, Integer testCaseId, ArrayList<Integer> productComponentIds) throws Exception;

	List<ProductComponentInfo> getTestCaseComponents(UriInfo ui, Integer testCaseId) throws Exception;

	List<TagInfo> getTestCaseVersionTags(UriInfo ui, Integer testCaseId) throws Exception;

	TestCaseInfo getTestCase(UriInfo ui, Integer testCaseId) throws Exception;

	TestCaseSearchResultInfo findTestCases(UriInfo ui, UtestSearchRequest request) throws Exception;

	Boolean updateTestCaseVersionTags(UriInfo ui, Integer testCaseId, ArrayList<Integer> tagIds) throws Exception;

	TestCaseVersionInfo getTestCaseVersion(UriInfo ui, Integer testCaseVersionId) throws Exception;

	List<TestCaseStepInfo> getTestCaseVersionSteps(UriInfo ui, Integer testCaseVersionId) throws Exception;

	TestCaseStepInfo getTestCaseVersionStep(UriInfo ui, Integer testCaseVersionId, Integer testCaseStepId) throws Exception;

	Boolean updateTestCaseEnvironmentGroups(UriInfo ui, Integer testCaseVersionId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	Boolean deleteTestCaseVersion(UriInfo ui, Integer testCaseVersionId, Integer originalVesionId) throws Exception;

	Boolean deleteTestCase(UriInfo ui, Integer testCaseId, Integer originalVesionId) throws Exception;

	Boolean deleteTestCaseStep(UriInfo ui, Integer testCaseStepId, Integer originalVesionId) throws Exception;

	TestCaseVersionInfo activateTestCaseVersion(UriInfo ui, Integer testCaseVersionId, Integer originalVersionId) throws Exception;

	TestCaseVersionInfo deactivateTestCaseVersion(UriInfo ui, Integer testCaseVersionId, Integer originalVersionId) throws Exception;

	TestCaseVersionInfo rejectTestCaseVersion(UriInfo ui, Integer testCaseVersionId, Integer originalVersionId) throws Exception;

	TestCaseVersionInfo approveTestCaseVersion(UriInfo ui, Integer testCaseVersionId, Integer originalVersionId) throws Exception;

	TestCaseInfo updateTestCase(UriInfo ui, Integer testCaseId, Integer maxAttachmentSizeInMbytes, Integer maxNumberOfAttachments, String name, Integer originalVersionId)
			throws Exception;

	TestCaseStepInfo createTestCaseStep(UriInfo ui, Integer testCaseVersionId, Integer stepNumber, String name, String instruction, String expectedResult,
			Integer estimatedTimeInMin) throws Exception;

	TestCaseStepInfo updateTestCaseStep(UriInfo ui, Integer testCaseStepId, Integer stepNumber, String name, String instruction, String expectedResult, Integer estimatedTimeInMin,
			Integer originalVersionId) throws Exception;

	List<EnvironmentGroupInfo> getTestCaseEnvironmentGroups(UriInfo ui, Integer testCaseVersionId) throws Exception;

	TestCaseVersionInfo updateTestCaseVersion(UriInfo ui, Integer testCaseVersionId, String versionIncrement, String description, Integer originalVersionId, String automated,
			String automationUri) throws Exception;

	TestCaseVersionInfo updateTestCaseVersion(UriInfo ui, Integer testCaseVersionId, String description, Integer originalVersionId, String automated, String automationUri)
			throws Exception;

	TestCaseVersionInfo createTestCase(UriInfo ui, Integer productId, Integer testCycleId, Integer maxAttachmentSizeInMbytes, Integer maxNumberOfAttachments, String name,
			String description, String automated, String automationUri) throws Exception;

	List<EnvironmentGroupExplodedInfo> getTestCaseEnvironmentGroupsExploded(UriInfo ui, Integer productId) throws Exception;

	List<AttachmentInfo> getTestCaseAttachments(UriInfo ui, Integer testCaseId) throws Exception;

	AttachmentInfo createAttachment(UriInfo ui, Integer testCaseId, String name, String description, String url, Double size, Integer attachmentTypeId) throws Exception;

	Boolean deleteAttachment(UriInfo ui, Integer testCaseId, Integer attachmentId, Integer originalVersionId) throws Exception;

	TestCaseVersionSearchResultInfo findLatestTestCaseVersions(UriInfo ui, Integer includedEnvironmentId, Integer includedInTestSuiteId, String tag, UtestSearchRequest request)
			throws Exception;

	List<EntityExternalBugInfo> getTestCaseBugs(UriInfo ui, Integer testCaseId) throws Exception;

	TestCaseVersionSearchResultInfo findTestCaseVersions(UriInfo ui, Integer includedEnvironmentId, Integer includedInTestSuiteId, String tag, UtestSearchRequest request)
			throws Exception;

	Boolean undeleteTestCaseVersion(UriInfo ui, Integer testCaseVersionId, Integer originalVesionId) throws Exception;

	TestCaseVersionSearchResultInfo findDeletedTestCaseVersions(UriInfo ui, Integer includedEnvironmentId, Integer includedInTestSuiteId, String tag, UtestSearchRequest request)
			throws Exception;

	Boolean undeleteTestCase(UriInfo ui, Integer testCaseId, Integer originalVesionId) throws Exception;
}
