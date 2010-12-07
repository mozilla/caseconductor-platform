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
package com.utest.domain.service.impl;

import java.util.Date;
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.AccessRole;
import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.Company;
import com.utest.domain.Permission;
import com.utest.domain.RolePermission;
import com.utest.domain.SignInFact;
import com.utest.domain.User;
import com.utest.domain.UserRole;
import com.utest.domain.UserStatus;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.UserService;
import com.utest.domain.service.cache.LoginUserContextHolder;
import com.utest.domain.util.DomainUtil;
import com.utest.exception.DuplicateNameException;
import com.utest.exception.EmailInUseException;
import com.utest.exception.InvalidUserException;
import com.utest.exception.NotFoundException;
import com.utest.util.EncodeUtil;

public class UserServiceImpl extends BaseServiceImpl implements UserService
{
	private final TypelessDAO	dao;

	/**
	 * Default constructor
	 */
	public UserServiceImpl(final TypelessDAO dao)
	{
		super(dao);
		this.dao = dao;
	}

	@Override
	public User addUser(final Integer companyId_, final String firstName_, final String lastName_, final String email_, final String password_) throws Exception
	{
		// internal users will have company id populated
		if (companyId_ != null)
		{
			final Company company = dao.getById(Company.class, companyId_);
			if (company == null)
			{
				throw new NotFoundException("Company not found: " + companyId_);
			}
		}
		// check for duplicate email
		if (getUserByEmail(email_) != null)
		{
			throw new EmailInUseException();
		}
		final User user = new User();
		user.setCompanyId(companyId_);
		user.setFirstName(firstName_);
		user.setLastName(lastName_);
		user.setEmail(email_);
		user.setCode(EncodeUtil.encode(email_));
		user.setPassword(EncodeUtil.encode(password_));
		user.setUserStatusId(UserStatus.USER_STATUS_INACTIVE);
		user.setConfirmedEmail(false);
		final Integer userId = dao.addAndReturnId(user);
		return getUser(userId);
	}

	@Override
	public User getUserByCode(final String code_)
	{
		final Search search = new Search(User.class);
		search.addFilterEqual("code", code_);
		final User user = (User) dao.searchUnique(User.class, search);
		if (user == null)
		{
			throw new NotFoundException("UserCode#" + code_);
		}
		setUserContext(user);
		return user;
	}

	@Override
	public User getUserByEmail(final String email_)
	{
		final Search search = new Search(User.class);
		search.addFilterEqual("email", email_);
		final User user = (User) dao.searchUnique(User.class, search);
		setUserContext(user);
		return user;
	}

	@Override
	public User closeUserAccount(final Integer userId_) throws Exception
	{
		final User user = getUser(userId_);
		if (user == null)
		{
			throw new InvalidUserException();
		}
		user.setUserStatusId(UserStatus.USER_STATUS_DISABLED);
		return dao.merge(user);
	}

	@Override
	public User confirmUserEmail(final String code_) throws Exception
	{
		final User user = getUserByCode(code_);
		if (user == null)
		{
			throw new InvalidUserException();
		}
		DomainUtil.loadUpdatedTimeline(user, user, user.getVersion());
		user.setUserStatusId(UserStatus.USER_STATUS_ACTIVE);
		user.setConfirmedEmail(true);
		return dao.merge(user);
	}

	@Override
	public UtestSearchResult findUsers(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(User.class, search_);
	}

	@Override
	public UtestSearchResult findRoles(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(AccessRole.class, search_);
	}

	@Override
	public UtestSearchResult findPermissions(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(Permission.class, search_);
	}

	@Override
	public User getUser(final Integer userId_) throws Exception
	{
		final User user = dao.getById(User.class, userId_);
		if (user == null)
		{
			throw new NotFoundException("UserId#" + userId_);
		}
		setUserContext(user);
		return user;
	}

	@Override
	public User changeUserEmail(final Integer userId_, final String newEmail_) throws Exception
	{
		final User user = getUser(userId_);
		if (user == null)
		{
			throw new NotFoundException("UserId#" + userId_);
		}
		// check for duplicate email
		if (getUserByEmail(newEmail_) != null)
		{
			throw new EmailInUseException();
		}
		user.setEmail(newEmail_);
		user.setConfirmedEmail(false);
		user.setUserStatusId(UserStatus.USER_STATUS_INACTIVE);
		return dao.merge(user);
	}

	@Override
	public User changeUserPassword(final Integer userId_, final String newPassword_) throws Exception
	{
		final User user = getUser(userId_);
		user.setPassword(EncodeUtil.encode(newPassword_));
		return dao.merge(user);
	}

	@Override
	public User saveUser(final Integer userId_, final Integer companyId_, final String firstName_, final String lastName_, final Integer originalVersionId_) throws Exception
	{
		// internal users will have company id populated
		if (companyId_ != null)
		{
			final Company company = dao.getById(Company.class, companyId_);
			if (company == null)
			{
				throw new NotFoundException("Company not found: " + companyId_);
			}
		}
		final User user = getUser(userId_);
		if (user == null)
		{
			throw new NotFoundException("UserId#" + userId_);
		}
		user.setVersion(originalVersionId_);
		user.setCompanyId(companyId_);
		user.setFirstName(firstName_);
		user.setLastName(lastName_);
		return dao.merge(user);
	}

	@Override
	public User activateUserAccount(final Integer userId_) throws Exception
	{
		final User user = getUser(userId_);
		if (user == null)
		{
			throw new NotFoundException("UserId#" + userId_);
		}
		user.setUserStatusId(UserStatus.USER_STATUS_ACTIVE);
		return dao.merge(user);
	}

	@Override
	public AccessRole addRole(final Integer companyId_, final String name_, final List<Integer> permissionIds_) throws Exception
	{

		if (companyId_ == null)
		{
			throw new IllegalArgumentException("Company id is null.");
		}
		// cannot add to system roles
		if (Company.SYSTEM_WIDE_COMPANY_ID.equals(companyId_))
		{
			throw new UnsupportedOperationException("Cannot create system role.");
		}
		final Search search = new Search(AccessRole.class);
		search.addFilterEqual("companyId", companyId_);
		search.addFilterEqual("name", name_);
		final List<AccessRole> roles = dao.search(AccessRole.class, search);
		if ((roles != null) && !roles.isEmpty())
		{
			throw new DuplicateNameException();
		}
		final AccessRole role = new AccessRole();
		role.setCompanyId(companyId_);
		role.setName(name_);
		role.setSortOrder(0);
		final Integer roleId = dao.addAndReturnId(role);
		for (final Integer permissionId : permissionIds_)
		{
			addRolePermission(roleId, permissionId);
		}
		return dao.getById(AccessRole.class, roleId);
	}

	@Override
	public void saveRolePermissions(final Integer roleId_, final List<Integer> permissionIds_)
	{
		if (isSystemRole(roleId_))
		{
			throw new UnsupportedOperationException("Cannot modify system role.");
		}
		// delete old permissions
		final Search search = new Search(RolePermission.class);
		search.addFilterEqual("accessRoleId", roleId_);
		final List<RolePermission> rolePermissions = dao.search(RolePermission.class, search);
		dao.delete(rolePermissions);
		for (final Integer permissionId : permissionIds_)
		{
			addRolePermission(roleId_, permissionId);
		}
	}

	private boolean isSystemRole(final Integer roleId_)
	{
		if (roleId_ == null)
		{
			throw new IllegalArgumentException("Role is null.");
		}
		final Search search = new Search(AccessRole.class);
		search.addFilterEqual("id", roleId_);
		search.addFilterEqual("companyId", Company.SYSTEM_WIDE_COMPANY_ID);
		final AccessRole role = (AccessRole) dao.searchUnique(AccessRole.class, search);
		return (role != null);
	}

	@Override
	public void addRolePermission(final Integer roleId_, final Integer permissionId_)
	{
		if (isSystemRole(roleId_))
		{
			throw new UnsupportedOperationException("Cannot modify system role.");
		}
		if (!isPermissionAssignable(permissionId_))
		{
			throw new UnsupportedOperationException("Cannot assign system permissions.");
		}
		final Search search = new Search(RolePermission.class);
		search.addFilterEqual("accessRoleId", roleId_);
		search.addFilterEqual("permissionId", permissionId_);
		RolePermission rolePermission = (RolePermission) dao.searchUnique(RolePermission.class, search);
		if (rolePermission == null)
		{
			rolePermission = new RolePermission();
			rolePermission.setAccessRoleId(roleId_);
			rolePermission.setPermissionId(permissionId_);
			dao.addAndReturnId(rolePermission);

			// final AccessRole role = dao.getById(AccessRole.class, roleId_);
			// DomainUtil.loadUpdatedTimeline(role);
			// dao.addOrUpdate(role);
		}
	}

	private boolean isPermissionAssignable(final Integer permissionId_)
	{
		if (permissionId_ == null)
		{
			throw new IllegalArgumentException("Permission is null.");
		}
		final Search search = new Search(Permission.class);
		search.addFilterEqual("id", permissionId_);
		final Permission permission = (Permission) dao.searchUnique(Permission.class, search);
		if (permission == null)
		{
			throw new IllegalArgumentException("Permission not found: " + permissionId_);
		}
		return permission.isAssignable();
	}

	@Override
	public void saveUserRoles(final Integer userId_, final List<Integer> roleIds_)
	{
		// delete old roles
		final Search search = new Search(UserRole.class);
		search.addFilterEqual("userId", userId_);
		final List<UserRole> userRoles = dao.search(UserRole.class, search);
		dao.delete(userRoles);
		for (final Integer roleId : roleIds_)
		{
			addUserRole(roleId, userId_);
		}
	}

	@Override
	public void addUserRole(final Integer roleId_, final Integer userId_)
	{
		final User user = dao.getById(User.class, userId_);
		if (user == null)
		{
			throw new NotFoundException("UserId#" + userId_);
		}
		final AccessRole role = dao.getById(AccessRole.class, roleId_);
		if (role == null)
		{
			throw new NotFoundException("AccessRole not found: " + roleId_);
		}
		if (AccessRole.getProtectedSystemRoleIds().contains(roleId_))
		{
			throw new UnsupportedOperationException("Cannot assign this system role: " + roleId_);
		}
		final Search search = new Search(UserRole.class);
		search.addFilterEqual("accessRoleId", roleId_);
		search.addFilterEqual("userId", userId_);
		UserRole userRole = (UserRole) dao.searchUnique(UserRole.class, search);
		if (userRole == null)
		{
			userRole = new UserRole();
			userRole.setAccessRoleId(roleId_);
			userRole.setUserId(userId_);
			dao.addAndReturnId(userRole);
		}
	}

	@Override
	public void deleteRole(final Integer roleId_) throws Exception
	{
		final AccessRole role = dao.getById(AccessRole.class, roleId_);
		if (role == null)
		{
			throw new NotFoundException("Role not found. Id: " + roleId_);
		}
		// TODO - check same company before deleting
		if (isSystemRole(roleId_))
		{
			throw new UnsupportedOperationException("Cannot delete system role.");
		}
		final Search search = new Search(RolePermission.class);
		search.addFilterEqual("accessRoleId", roleId_);
		final List<RolePermission> rolePermissions = dao.search(RolePermission.class, search);
		dao.delete(rolePermissions);
		dao.delete(role);
	}

	@Override
	public void deleteRolePermission(final Integer roleId_, final Integer permissionId_)
	{
		final Search search = new Search(RolePermission.class);
		search.addFilterEqual("accessRoleId", roleId_);
		search.addFilterEqual("permissionId", permissionId_);
		final RolePermission rolePermission = (RolePermission) dao.searchUnique(RolePermission.class, search);
		if (rolePermission == null)
		{
			throw new NotFoundException("Permission not found. Id: " + permissionId_ + " For Role: " + roleId_);
		}
		if (isSystemRole(roleId_))
		{
			throw new UnsupportedOperationException("Cannot modify system role.");
		}
		dao.delete(rolePermission);
	}

	@Override
	public void deleteUserRole(final Integer roleId_, final Integer userId_)
	{
		final Search search = new Search(UserRole.class);
		search.addFilterEqual("accessRoleId", roleId_);
		search.addFilterEqual("userId", userId_);
		final UserRole userRole = (UserRole) dao.searchUnique(UserRole.class, search);
		if (userRole == null)
		{
			throw new NotFoundException("Role not found. Id: " + roleId_ + " For User: " + userId_);
		}
		dao.delete(userRole);
	}

	@Override
	public List<Permission> getRolePermissions(final Integer roleId_)
	{
		Search search = new Search(RolePermission.class);
		search.addField("permissionId");
		search.addFilterEqual("accessRoleId", roleId_);
		final List<?> permissionIdList = dao.search(RolePermission.class, search);
		search = new Search(Permission.class);
		search.addFilterIn("id", permissionIdList);
		final List<Permission> list = dao.search(Permission.class, search);
		return list;
	}

	@Override
	public List<Permission> getUserPermissions(final Integer userId_)
	{
		Search search = new Search(UserRole.class);
		search.addFilterEqual("userId", userId_);
		search.addField("accessRoleId");
		final List<?> roleIdList = dao.search(UserRole.class, search);
		search = new Search(RolePermission.class);
		search.addField("permissionId");
		search.addFilterIn("accessRoleId", roleIdList);
		final List<?> permissionIdList = dao.search(RolePermission.class, search);
		search = new Search(Permission.class);
		search.addFilterIn("id", permissionIdList);
		final List<Permission> list = dao.search(Permission.class, search);
		return list;
	}

	@Override
	public List<AccessRole> getUserRoles(final Integer userId_)
	{
		Search search = new Search(UserRole.class);
		search.addFilterEqual("userId", userId_);
		search.addField("accessRoleId");
		final List<?> roleIdList = dao.search(UserRole.class, search);
		search = new Search(AccessRole.class);
		search.addFilterIn("id", roleIdList);
		final List<AccessRole> list = dao.search(AccessRole.class, search);
		return list;
	}

	@Override
	public boolean isUserExists(final String email_)
	{
		final Search search = new Search(User.class);
		search.addFilterEqual("email", email_);
		final List<?> list = dao.search(User.class, search);
		return list.size() > 0;
	}

	@Override
	public void login(final AuthenticatedUserInfo authInfo_, final String sessionId_) throws Exception
	{
		if ((authInfo_ == null) || (sessionId_ == null))
		{
			throw new IllegalArgumentException();
		}
		final SignInFact imp = new SignInFact();
		imp.setUserId(authInfo_.getUserId());
		imp.setImpersonatedUserId(authInfo_.getImpsersonatedUserId());
		imp.setStartDate(authInfo_.getAuthDate());
		imp.setSessionId(sessionId_);
		dao.addOrUpdate(imp);
		dao.flush();
		LoginUserContextHolder.getInstance().addUser(getUser(authInfo_.getLoggedInUserId()));
	}

	@Override
	public void logout(final AuthenticatedUserInfo authInfo_, final String sessionId_)
	{
		if ((authInfo_ == null) || (sessionId_ == null))
		{
			throw new IllegalArgumentException();
		}
		final Search s = new Search(SignInFact.class);
		s.addFilterEqual("sessionId", sessionId_);
		s.addFilterEqual("startDate", authInfo_.getAuthDate());
		// it should be unique and found
		final SignInFact imp = (SignInFact) dao.searchUnique(SignInFact.class, s);
		if (imp != null)
		{
			imp.setEndDate(new Date());
			dao.addOrUpdate(imp);
			dao.flush();
		}
		LoginUserContextHolder.getInstance().removeUser(authInfo_.getLoggedInUserId());
	}

	private void setUserContext(final User user_)
	{
		if (user_ != null)
		{
			final Integer userId_ = user_.getId();
			user_.setPermissions(getUserPermissions(userId_));
			user_.setRoles(getUserRoles(userId_));
		}
	}

	@Override
	public Permission getPermission(Integer permissionId_)
	{
		final Permission permission = dao.getById(Permission.class, permissionId_);
		if (permission == null)
		{
			throw new NotFoundException("PermissionId#" + permissionId_);
		}
		return permission;
	}

	@Override
	public AccessRole getRole(Integer roleId_)
	{
		final AccessRole role = dao.getById(AccessRole.class, roleId_);
		if (role == null)
		{
			throw new NotFoundException("RoleId#" + roleId_);
		}
		return role;
	}

}
