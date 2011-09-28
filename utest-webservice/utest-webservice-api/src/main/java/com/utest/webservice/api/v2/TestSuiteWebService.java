/*
Case Conductor is a Test Case Management system.
Copyright (C) 2011 uTest Inc.

This file is part of Case Conductor.

Case Conductor is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Case Conductor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Case Conductor.  If not, see <http://www.gnu.org/licenses/>.

*/
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
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.TestSuiteTestCaseInfo;
import com.utest.webservice.model.v2.TestSuiteInfo;
import com.utest.webservice.model.v2.TestSuiteSearchResultInfo;
import com.utest.webservice.model.v2.TestSuiteTestCaseSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestSuiteWebService
{

	Boolean updateTestSuiteEnvironmentGroups(UriInfo ui, Integer testSuiteId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> getTestSuiteEnvironmentGroups(UriInfo ui, Integer testSuiteId) throws Exception;

	TestSuiteInfo getTestSuite(UriInfo ui, Integer testSuiteId) throws Exception;

	List<TestSuiteTestCaseInfo> getTestSuiteTestCases(UriInfo ui, Integer testSuiteId) throws Exception;

	TestSuiteInfo activateTestSuite(UriInfo ui, Integer testSuiteId, Integer originalVesionId) throws Exception;

	TestSuiteInfo deactivateTestSuite(UriInfo ui, Integer testSuiteId, Integer originalVesionId) throws Exception;

	Boolean deleteTestSuite(UriInfo ui, Integer testSuiteId, Integer originalVesionId) throws Exception;

	Boolean deleteTestSuiteTestCase(UriInfo ui, Integer includedTestCaseId, Integer originalVesionId) throws Exception;

	TestSuiteTestCaseInfo getTestSuiteTestCase(UriInfo ui, Integer includedTestCaseId) throws Exception;

	TestSuiteInfo createTestSuite(UriInfo ui, Integer productId, String useLatestVersions, String name, String description) throws Exception;

	TestSuiteInfo updateTestSuite(UriInfo ui, Integer testSuiteId, String name, String description, Integer originalVersionId) throws Exception;

	TestSuiteTestCaseInfo createTestSuiteTestCase(UriInfo ui, Integer testSuiteId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, String blocking)
			throws Exception;

	TestSuiteTestCaseInfo updateTestSuiteTestCase(UriInfo ui, Integer includedTestCaseId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, String blocking,
			Integer originalVersionId) throws Exception;

	TestSuiteInfo cloneTestSuite(UriInfo ui, Integer testSuiteId) throws Exception;

	List<EnvironmentGroupExplodedInfo> getTestSuiteEnvironmentGroupsExploded(UriInfo ui, Integer productId) throws Exception;

	List<AttachmentInfo> getTestSuiteAttachments(UriInfo ui, Integer testSuiteId) throws Exception;

	AttachmentInfo createAttachment(UriInfo ui, Integer testSuiteId, String name, String description, String url, Double size, Integer attachmentTypeId) throws Exception;

	Boolean deleteAttachment(UriInfo ui, Integer testSuiteId, Integer attachmentId, Integer originalVersionId) throws Exception;

	TestSuiteSearchResultInfo findTestSuites(UriInfo ui, Integer hasTestCasesInTestRunId, Integer includedTestCaseId, Integer includedTestCaseVesionId,
			Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

	TestSuiteTestCaseSearchResultInfo findTestSuiteTestCases(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

}
