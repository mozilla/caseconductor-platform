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
package com.utest.domain.service;

import java.util.List;

import com.utest.domain.Attachment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.TestPlan;
import com.utest.domain.TestPlanTestSuite;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to the Test Plan Management.
 */
public interface TestPlanService
{

	UtestSearchResult findTestPlans(UtestSearch search, Integer includedEnvironmentId_) throws Exception;

	TestPlan getTestPlan(Integer testPlanId) throws Exception;

	List<TestPlanTestSuite> getTestPlanTestSuites(Integer testPlanId) throws Exception;

	TestPlan addTestPlan(Integer productId, String name, String description) throws Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForTestPlan(Integer testPlanId) throws Exception;

	TestPlan activateTestPlan(Integer testPlanId, Integer originalVersionId) throws Exception;

	TestPlan lockTestPlan(Integer testPlanId, Integer originalVersionId) throws Exception;

	TestPlanTestSuite addTestPlanTestSuite(Integer testPlanId, Integer testSuiteId, Integer runOrder) throws Exception;

	TestPlanTestSuite addTestPlanTestSuite(Integer testPlanId, Integer testSuiteId) throws Exception;

	void deleteTestPlanTestSuite(Integer testPlanTestSuiteId, Integer originalVersionId) throws Exception;

	void saveEnvironmentGroupsForTestPlan(Integer testPlanId, List<Integer> environmentGroupIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	TestPlan saveTestPlan(Integer testPlanId, String name, String description, Integer originalVersionId) throws Exception;

	TestPlanTestSuite saveTestPlanTestSuite(Integer includedTestSuiteId, Integer runOrder, Integer originalVersionId);

	void deleteTestPlan(Integer testPlanId, Integer originalVersionId) throws Exception;

	TestPlanTestSuite getTestPlanTestSuite(Integer testPlanTestSuiteId) throws Exception;

	List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestPlan(Integer testPlanId) throws Exception;

	List<Attachment> getAttachmentsForTestPlan(Integer testPlanId) throws Exception;

	Attachment addAttachmentForTestPlan(String name, String description, String url, Double size, Integer testPlanId, Integer attachmentTypeId) throws Exception;

	boolean deleteAttachment(Integer attachmentId, Integer originalVersionId) throws Exception;

}
