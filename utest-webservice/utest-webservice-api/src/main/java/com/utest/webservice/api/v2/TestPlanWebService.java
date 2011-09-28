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
import com.utest.webservice.model.v2.IncludedTestSuiteInfo;
import com.utest.webservice.model.v2.TestPlanInfo;
import com.utest.webservice.model.v2.TestPlanSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestPlanWebService
{

	Boolean updateTestPlanEnvironmentGroups(UriInfo ui, Integer testPlanId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> getTestPlanEnvironmentGroups(UriInfo ui, Integer testPlanId) throws Exception;

	List<IncludedTestSuiteInfo> getTestPlanTestSuites(UriInfo ui, Integer testPlanId) throws Exception;

	TestPlanInfo getTestPlan(UriInfo ui, Integer testPlanId) throws Exception;

	TestPlanInfo activateTestPlan(UriInfo ui, Integer testPlanId, Integer originalVesionId) throws Exception;

	TestPlanInfo deactivateTestPlan(UriInfo ui, Integer testPlanId, Integer originalVesionId) throws Exception;

	Boolean deleteTestPlan(UriInfo ui, Integer testPlanId, Integer originalVesionId) throws Exception;

	Boolean deleteTestPlanTestSuite(UriInfo ui, Integer includedTestSuiteId, Integer originalVesionId) throws Exception;

	IncludedTestSuiteInfo getTestPlanTestSuite(UriInfo ui, Integer includedTestSuiteId) throws Exception;

	TestPlanInfo createTestPlan(UriInfo ui, Integer productId, String name, String description) throws Exception;

	IncludedTestSuiteInfo updateTestPlanTestSuite(UriInfo ui, Integer includedTestSuiteId, Integer runOrder, Integer originalVesionId) throws Exception;

	IncludedTestSuiteInfo createTestPlanTestSuite(UriInfo ui, Integer testPlanId, Integer testSuiteId, Integer runOrder) throws Exception;

	TestPlanInfo updateTestPlan(UriInfo ui, Integer testPlanId, String name, String description, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupExplodedInfo> getTestPlanEnvironmentGroupsExploded(UriInfo ui, Integer productId) throws Exception;

	List<AttachmentInfo> getTestPlanAttachments(UriInfo ui, Integer testPlanId) throws Exception;

	AttachmentInfo createAttachment(UriInfo ui, Integer testPlanId, String name, String description, String url, Double size, Integer attachmentTypeId) throws Exception;

	Boolean deleteAttachment(UriInfo ui, Integer testPlanId, Integer attachmentId, Integer originalVersionId) throws Exception;

	TestPlanSearchResultInfo findTestPlans(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

}
