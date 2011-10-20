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
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.PermissionInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.TeamInfo;
import com.utest.webservice.model.v2.TeamSearchResultInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TeamWebService
{

	TeamInfo updateTeam(UriInfo ui, Integer teamId, Integer companyId, String name, String description, Integer originalVersionId) throws Exception;

	TeamInfo createTeam(UriInfo ui, Integer companyId, String name, String description) throws Exception;

	TeamInfo getTeam(UriInfo ui, Integer teamId) throws Exception;

	TeamSearchResultInfo findTeams(UriInfo ui, UtestSearchRequest request) throws Exception;

	List<UserInfo> getTeamMembers(UriInfo ui, Integer teamId) throws Exception;

	List<RoleInfo> getTeamMemberRoles(UriInfo ui, Integer teamId, Integer userId) throws Exception;

	Boolean deleteTeamMember(UriInfo ui, Integer teamId, Integer userId, Integer originalVersionId) throws Exception;

	Boolean updateTeamMemberRoles(UriInfo ui, Integer teamId, Integer userId, ArrayList<Integer> roleIds, Integer originalVersionId) throws Exception;

	Boolean updateTeamMembers(UriInfo ui, Integer teamId, ArrayList<Integer> userIds, Integer originalVersionId) throws Exception;

	Boolean deleteTeam(UriInfo ui, Integer teamId, Integer originalVersionId) throws Exception;

	Boolean addTeamMember(UriInfo ui, Integer teamId, Integer userId, Integer originalVersionId) throws Exception;

	Boolean addTeamMemberRole(UriInfo ui, Integer teamId, Integer userId, Integer roleId, Integer originalVersionId) throws Exception;

	List<PermissionInfo> getTeamMemberPermissions(UriInfo ui, Integer teamId, Integer userId) throws Exception;

}
