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

import com.utest.domain.EnvironmentGroup;
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

	void deleteTestPlan(Integer testPlanId) throws Exception;

	UtestSearchResult findTestPlans(UtestSearch search) throws Exception;

	TestPlan getTestPlan(Integer testPlanId) throws Exception;

	List<TestPlanTestSuite> getTestPlanTestSuites(Integer testPlanId) throws Exception;

	TestPlan addTestPlan(Integer productId, String name, String description) throws Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForTestPlan(Integer testPlanId) throws Exception;

	TestPlan activateTestPlan(Integer testPlanId, Integer originalVersionId) throws Exception;

	TestPlan lockTestPlan(Integer testPlanId, Integer originalVersionId) throws Exception;

	TestPlanTestSuite addTestPlanTestSuite(Integer testPlanId, Integer testSuiteId, Integer runOrder) throws Exception;

	TestPlanTestSuite addTestPlanTestSuite(Integer testPlanId, Integer testSuiteId) throws Exception;

	void deleteTestPlanTestSuite(Integer testPlanTestSuiteId) throws Exception;

	void saveEnvironmentGroupsForTestPlan(Integer testPlanId, List<Integer> environmentGroupIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	TestPlan saveTestPlan(Integer testPlanId, String name, String description, Integer originalVersionId) throws Exception;

	TestPlanTestSuite saveTestPlanTestSuite(Integer includedTestSuiteId, Integer runOrder, Integer originalVersionId);

}
