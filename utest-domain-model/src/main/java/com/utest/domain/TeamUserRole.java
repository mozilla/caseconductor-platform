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
package com.utest.domain;

public class TeamUserRole extends TimelineEntity
{

	private Integer	teamId;
	private Integer	userId;
	private Integer	accessRoleId;

	public TeamUserRole()
	{
	}

	public TeamUserRole(final Integer teamId, final Integer userId, final Integer accessRoleId)
	{
		this.teamId = teamId;
		this.userId = userId;
		this.accessRoleId = accessRoleId;
	}

	public Integer getUserId()
	{
		return this.userId;
	}

	public void setUserId(final Integer userId)
	{
		this.userId = userId;
	}

	public Integer getAccessRoleId()
	{
		return this.accessRoleId;
	}

	public void setAccessRoleId(final Integer accessRoleId)
	{
		this.accessRoleId = accessRoleId;
	}

	public void setTeamId(Integer teamId)
	{
		this.teamId = teamId;
	}

	public Integer getTeamId()
	{
		return teamId;
	}

}
