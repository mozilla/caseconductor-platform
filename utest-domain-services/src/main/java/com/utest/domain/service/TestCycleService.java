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

import java.util.Date;
import java.util.List;

import com.utest.domain.AccessRole;
import com.utest.domain.Attachment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.TestCycle;
import com.utest.domain.TestRun;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to the Test Cycle Management.
 */
public interface TestCycleService
{

	TestCycle addTestCycle(Integer productId, String name, String description, Date startDate, Date endDate, boolean communityAuthoringAllowed, boolean communityAccessAllowed)
			throws Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForTestCycle(Integer testCycleId_) throws Exception;

	List<TestRun> getTestRunsForTestCycle(Integer testCycleId) throws Exception;

	UtestSearchResult findTestCycles(UtestSearch search, Integer teamMemberId_, Integer includedEnvironmentId) throws Exception;

	TestCycle getTestCycle(Integer testCycleId) throws Exception;

	TestCycle lockTestCycle(Integer testCycleId, Integer originalVersionId) throws Exception;

	TestCycle activateTestCycle(Integer testCycleId, Integer originalVersionId) throws Exception;

	TestCycle saveTestCycle(Integer testCycleId, String name, String description, Date startDate, Date endDate, boolean communityAuthoringAllowed, boolean communityAccessAllowed,
			Integer originalVersionId) throws Exception;

	void saveEnvironmentGroupsForTestCycle(Integer testCycleId, List<Integer> environmentGroupIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	void deleteTestCycle(Integer testCycleId, Integer originalVersionId) throws Exception;

	List<User> getTestingTeamForTestCycle(Integer testCycleId) throws Exception;

	void saveTestingTeamForTestCycle(Integer testCycleId, List<Integer> userIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException, Exception;

	void saveTestingTeamMemberRolesForTestCycle(Integer testCycleId, Integer userId, List<Integer> roleIds, Integer originalVersionId)
			throws UnsupportedEnvironmentSelectionException, Exception;

	List<AccessRole> getTestingTeamMemberRolesForTestCycle(Integer testCycleId, Integer userId) throws Exception;

	void approveAllTestRunResultsForTestCycle(Integer testCycleId) throws Exception;

	TestCycle cloneTestCycle(Integer fromTestCycleId, boolean cloneAssignments) throws Exception;

	List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestCycle(Integer testCycleId) throws Exception;

	List<Attachment> getAttachmentsForTestCycle(Integer testCycleId) throws Exception;

	Attachment addAttachmentForTestCycle(String name, String description, String url, Double size, Integer testCycleId, Integer attachmentTypeId) throws Exception;

	boolean deleteAttachment(Integer attachmentId, Integer originalVersionId) throws Exception;

	TestCycle featureTestCycle(Integer testCycleId, Integer originalVersionId) throws Exception;

	TestCycle unfeatureTestCycle(Integer testCycleId, Integer originalVersionId) throws Exception;
}
