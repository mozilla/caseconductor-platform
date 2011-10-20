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

// Generated Oct 7, 2009 11:18:35 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.List;

public class User extends TimelineEntity implements CompanyDependable, Named
{
	public static String		EXTERANAL_PASSWORD	= "__EXTERNAL_PASSWORD__";

	private String				password;
	private String				screenName;
	private String				firstName;
	private String				lastName;
	private String				email;
	private boolean				forumUser;
	private boolean				confirmedEmail;
	private Integer				userStatusId;
	private List<AccessRole>	roles;
	private List<Permission>	permissions;
	private Date				lastLoginDate;
	// added for TCM
	private Integer				companyId;

	private String				code;

	public void setLastLoginDate(final Date lastLoginDate)
	{
		this.lastLoginDate = lastLoginDate;
	}

	public Date getLastLoginDate()
	{
		return lastLoginDate;
	}

	public User()
	{
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	public String getFullName()
	{
		return this.firstName + " " + this.lastName;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getCode()
	{
		return this.code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public boolean isEnabled()
	{
		return !UserStatus.DISABLED.equals(userStatusId);
	}

	public boolean isForumUser()
	{
		return this.forumUser;
	}

	public void setForumUser(final boolean forumUser)
	{
		this.forumUser = forumUser;
	}

	public boolean isConfirmedEmail()
	{
		return this.confirmedEmail;
	}

	public void setConfirmedEmail(final boolean confirmedEmail)
	{
		this.confirmedEmail = confirmedEmail;
	}

	public Integer getUserStatusId()
	{
		return this.userStatusId;
	}

	public void setUserStatusId(final Integer userStatusId)
	{
		this.userStatusId = userStatusId;
	}

	/**
	 * @return the roles
	 */
	public List<AccessRole> getRoles()
	{
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(final List<AccessRole> roles)
	{
		this.roles = roles;
	}

	public void setPermissions(final List<Permission> permissions)
	{
		this.permissions = permissions;
	}

	public List<Permission> getPermissions()
	{
		return permissions;
	}

	public boolean hasAccessRole(final Integer accessRoleId)
	{
		if (accessRoleId != null)
		{
			for (final AccessRole role : getRoles())
			{
				if (accessRoleId.equals(role.getId()))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasPermission(final String permissionCode)
	{
		if (permissionCode != null)
		{
			for (final Permission permission : getPermissions())
			{
				if (permissionCode.equals(permission.getPermissionCode()))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void setCompanyId(final Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setScreenName(String screenName)
	{
		this.screenName = screenName;
	}

	public String getScreenName()
	{
		return screenName;
	}

	@Override
	public String getName()
	{
		return screenName;
	}

	@Override
	public void setName(String name)
	{
		screenName = name;

	}

}
