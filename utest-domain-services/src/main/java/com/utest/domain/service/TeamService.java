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
package com.utest.domain.service;

import java.util.List;

import com.utest.domain.AccessRole;
import com.utest.domain.Permission;
import com.utest.domain.Team;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

/**
 * Service to handle all domain operations related to Team Management.
 */
public interface TeamService
{

	Team addTeam(Integer companyId, String name, String description) throws Exception;

	UtestSearchResult findTeams(UtestSearch search) throws Exception;

	Team getTeam(Integer teamId) throws Exception;

	Team saveTeam(Integer teamId, String name, String description, Integer originalVersionId) throws Exception;

	void addTeamUser(Integer teamId, Integer userId, Integer originalVersionId);

	void addTeamUserRole(Integer teamId, Integer userId, Integer roleId, Integer originalVersionId);

	void saveTeamUserRoles(Integer teamId, Integer userId, List<Integer> roleIds, Integer originalVersionId);

	void deleteTeamUser(Integer teamId, Integer userId, Integer originalVersionId) throws Exception;

	List<Permission> getTeamUserPermissions(Integer teamId, Integer userId);

	List<AccessRole> getTeamUserRoles(Integer teamId, Integer userId);

	List<User> getTeamUsers(Integer teamId);

	void saveTeamUsers(Integer teamId, List<Integer> userIds, Integer originalVersionId);

	void deleteTeam(Integer teamId, Integer originalVersionId) throws Exception;

	void copyTeams(Integer fromTeamId, Integer toTeamId, Integer originalVersionId);
}
