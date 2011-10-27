/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
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
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.CategoryValueInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.TestCycleInfo;
import com.utest.webservice.model.v2.TestCycleSearchResultInfo;
import com.utest.webservice.model.v2.TestRunInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestCycleWebService
{

	Boolean updateTestCycleEnvironmentGroups(UriInfo ui, Integer testCycleId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	TestCycleInfo getTestCycle(UriInfo ui, Integer testCycleId) throws Exception;

	List<TestRunInfo> getTestCycleTestRuns(UriInfo ui, Integer testCycleId) throws Exception;

	Boolean deleteTestCycle(UriInfo ui, Integer testCycleId, Integer originalVesionId) throws Exception;

	TestCycleInfo activateTestCycle(UriInfo ui, Integer testCycleId, Integer originalVersionId) throws Exception;

	TestCycleInfo deactivateTestCycle(UriInfo ui, Integer testCycleId, Integer originalVersionId) throws Exception;

	TestCycleInfo createTestCycle(UriInfo ui, String name, String description, Integer productId, String communityAuthoringAllowed, String communityAccessAllowed, Date startDate,
			Date endDate) throws Exception;

	TestCycleInfo updateTestCycle(UriInfo ui, Integer testCycleId, String name, String description, Integer productId, String communityAuthoringAllowed,
			String communityAccessAllowed, Date startDate, Date endDate, Integer originalVersionId) throws Exception;

	List<UserInfo> getTestCycleTeamMembers(UriInfo ui, Integer testCycleId) throws Exception;

	Boolean updateTestCycleTeamMembers(UriInfo ui, Integer productId, ArrayList<Integer> userIds, Integer originalVersionId) throws Exception;

	List<RoleInfo> getTestCycleTeamMemberRoles(UriInfo ui, Integer productId, Integer userId) throws Exception;

	Boolean updateTestCycleTeamMemberRoles(UriInfo ui, Integer productId, Integer userId, ArrayList<Integer> roleIds, Integer originalVersionId) throws Exception;

	Boolean approveAllResultsForTestCycle(UriInfo ui, Integer testCycleId) throws Exception;

	TestCycleInfo cloneTestCycle(UriInfo ui, Integer testCycleId, String cloneAssignments) throws Exception;

	List<AttachmentInfo> getTestCycleAttachments(UriInfo ui, Integer testCycleId) throws Exception;

	AttachmentInfo createAttachment(UriInfo ui, Integer testCycleId, String name, String description, String url, Double size, Integer attachmentTypeId) throws Exception;

	Boolean deleteAttachment(UriInfo ui, Integer testCycleId, Integer attachmentId, Integer originalVersionId) throws Exception;

	TestCycleSearchResultInfo findTestCycles(UriInfo ui, List<Integer> includedEnvironmentId, Integer teamMemberId, UtestSearchRequest request) throws Exception;

	TestCycleInfo featureTestCycle(UriInfo ui, Integer testCycleId, Integer originalVersionId) throws Exception;

	TestCycleInfo unfeatureTestCycle(UriInfo ui, Integer testCycleId, Integer originalVersionId) throws Exception;

	List<CategoryValueInfo> getCoverageByResultStatus(UriInfo ui, Integer testCycleId, Integer testSuiteId, Integer testRunId, Integer testCaseId) throws Exception;

	Boolean undeleteTestCycle(UriInfo ui, Integer testCycleId, Integer originalVesionId) throws Exception;

	TestCycleSearchResultInfo findDeletedTestCycles(UriInfo ui, Integer includedEnvironmentId, Integer teamMemberId, UtestSearchRequest request) throws Exception;

	CategoryValueInfo getCoveragePercentComplete(UriInfo ui, Integer testCycleId, Integer testRunId) throws Exception;

	List<EnvironmentGroupInfo> getTestCycleEnvironmentGroups(UriInfo ui, Integer testCycleId, List<Integer> includedEnvironmentId) throws Exception;

	List<EnvironmentGroupExplodedInfo> getTestCycleEnvironmentGroupsExploded(UriInfo ui, Integer productId, List<Integer> includedEnvironmentId) throws Exception;
}
