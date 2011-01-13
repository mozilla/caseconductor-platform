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
import com.utest.webservice.model.v2.TestCycleInfo;
import com.utest.webservice.model.v2.TestCycleResultInfo;
import com.utest.webservice.model.v2.TestRunInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestCycleWebService
{

	TestCycleInfo updateTestCycle(UriInfo ui, Integer testCycleId, TestCycleInfo testCycleInfo) throws Exception;

	TestCycleInfo activateTestCycle(UriInfo ui, Integer testCycleId, TestCycleInfo testCycleInfo) throws Exception;

	TestCycleInfo deactivateTestCycle(UriInfo ui, Integer testCycleId, TestCycleInfo testCycleInfo) throws Exception;

	Boolean updateTestCycleEnvironmentGroups(UriInfo ui, Integer testCycleId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> getTestCycleEnvironmentGroups(UriInfo ui, Integer testCycleId) throws Exception;

	TestCycleInfo createTestCycle(UriInfo ui, TestCycleInfo testCycleInfo) throws Exception;

	Boolean deleteTestCycle(UriInfo ui, Integer testCycleId) throws Exception;

	TestCycleInfo getTestCycle(UriInfo ui, Integer testCycleId) throws Exception;

	TestCycleResultInfo findTestCycles(UriInfo ui, UtestSearchRequest request) throws Exception;

	List<TestRunInfo> getTestCycleTestRuns(UriInfo ui, Integer testCycleId) throws Exception;
}
