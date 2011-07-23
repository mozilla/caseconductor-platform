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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.AccessRole;
import com.utest.domain.Attachment;
import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.Company;
import com.utest.domain.EntityType;
import com.utest.domain.Permission;
import com.utest.domain.RolePermission;
import com.utest.domain.SignInFact;
import com.utest.domain.User;
import com.utest.domain.UserRole;
import com.utest.domain.UserStatus;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.AttachmentService;
import com.utest.domain.service.UserService;
import com.utest.domain.service.cache.LoginUserContextHolder;
import com.utest.exception.DuplicateNameException;
import com.utest.exception.EmailInUseException;
import com.utest.exception.InvalidUserException;
import com.utest.exception.NotFoundException;
import com.utest.exception.ScreenNameInUseException;
import com.utest.util.EncodeUtil;

public class UserServiceImpl extends BaseServiceImpl implements UserService
{
	private final AttachmentService	attachmentService;
	private final TypelessDAO		dao;
	// permission assigned to all users by default
	// configured in spring context
	private List<Integer>			defaultPermissions	= new ArrayList<Integer>();

	/**
	 * Default constructor
	 */
	public UserServiceImpl(final TypelessDAO dao, final AttachmentService attachmentService)
	{
		super(dao);
		this.dao = dao;
		this.attachmentService = attachmentService;
	}

	@Override
	public User addUser(final Integer companyId_, final String firstName_, final String lastName_, final String email_, final String password_, String screenName_)
			throws Exception
	{
		if (password_ == null)
		{
			throw new IllegalArgumentException("Password is null");
		}
		// internal users will have company id populated
		if (companyId_ != null)
		{
			getRequiredEntityById(Company.class, companyId_);
		}
		// check for duplicate email
		if (getUserByEmail(email_) != null)
		{
			throw new EmailInUseException("Email is used: " + email_);
		}
		// check for duplicate screen name
		if (getUserByScreenName(screenName_) != null)
		{
			throw new ScreenNameInUseException("Screen name is used: " + screenName_);
		}
		final User user = new User();
		user.setCompanyId(companyId_);
		user.setFirstName(firstName_);
		user.setLastName(lastName_);
		user.setEmail(email_);
		user.setScreenName(screenName_);
		user.setCode(EncodeUtil.encode(email_));
		user.setPassword(EncodeUtil.encode(password_));
		user.setUserStatusId(UserStatus.INACTIVE);
		user.setConfirmedEmail(false);
		final Integer userId = dao.addAndReturnId(user);
		return getUser(userId);
	}

	@Override
	public User getUserByCode(final String code_)
	{
		if (code_ == null)
		{
			throw new IllegalArgumentException("User code is null");
		}
		final Search search = new Search(User.class);
		search.addFilterEqual("code", code_);
		final User user = (User) dao.searchUnique(User.class, search);
		setUserContext(user);
		return user;
	}

	@Override
	public User getUserByEmail(final String email_)
	{
		if (email_ == null)
		{
			throw new IllegalArgumentException("User email is null");
		}
		final Search search = new Search(User.class);
		search.addFilterEqual("email", email_);
		final User user = (User) dao.searchUnique(User.class, search);
		setUserContext(user);
		return user;
	}

	@Override
	public User getUserByScreenName(final String screenName_)
	{
		if (screenName_ == null)
		{
			throw new IllegalArgumentException("User screen name is null");
		}
		final Search search = new Search(User.class);
		search.addFilterEqual("screenName", screenName_);
		final User user = (User) dao.searchUnique(User.class, search);
		setUserContext(user);
		return user;
	}

	@Override
	public User closeUserAccount(final Integer userId_, final Integer originalVersionId_) throws Exception
	{
		final User user = getUser(userId_);
		if (user == null)
		{
			throw new InvalidUserException();
		}
		user.setUserStatusId(UserStatus.DISABLED);
		user.setVersion(originalVersionId_);
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
		return confirmUserEmail(user, null);
	}

	@Override
	public User confirmUserEmail(final Integer userId_, final Integer originalVersionId_) throws Exception
	{
		final User user = getUser(userId_);
		if (user == null)
		{
			throw new InvalidUserException();
		}
		return confirmUserEmail(user, originalVersionId_);
	}

	private User confirmUserEmail(User user_, final Integer originalVersionId_)
	{
		user_.setUserStatusId(UserStatus.ACTIVE);
		user_.setConfirmedEmail(true);
		if (originalVersionId_ != null)
		{
			user_.setVersion(originalVersionId_);
		}
		return dao.merge(user_);
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
		final User user = getRequiredEntityById(User.class, userId_);
		setUserContext(user);
		return user;
	}

	@Override
	public User getCurrentUser() throws Exception
	{
		return getUser(getCurrentUserId());
	}

	@Override
	public User changeUserEmail(final Integer userId_, final String newEmail_, final Integer originalVersionId_) throws Exception
	{
		final User user = getUser(userId_);
		// check for duplicate email
		if (getUserByEmail(newEmail_) != null)
		{
			throw new EmailInUseException();
		}
		user.setEmail(newEmail_);
		user.setConfirmedEmail(false);
		user.setUserStatusId(UserStatus.INACTIVE);
		user.setVersion(originalVersionId_);
		return dao.merge(user);
	}

	@Override
	public User changeUserPassword(final Integer userId_, final String newPassword_, final Integer originalVersionId_) throws Exception
	{
		if (newPassword_ == null)
		{
			throw new IllegalArgumentException("Password is null");
		}
		final User user = getUser(userId_);
		user.setPassword(EncodeUtil.encode(newPassword_));
		user.setVersion(originalVersionId_);
		return dao.merge(user);
	}

	@Override
	public User saveUser(final Integer userId_, final Integer companyId_, final String firstName_, final String lastName_, final Integer originalVersionId_) throws Exception
	{
		// internal users will have company id populated
		if (companyId_ != null)
		{
			@SuppressWarnings("unused")
			final Company company = getRequiredEntityById(Company.class, companyId_);
		}
		final User user = getUser(userId_);
		user.setCompanyId(companyId_);
		user.setFirstName(firstName_);
		user.setLastName(lastName_);
		user.setVersion(originalVersionId_);
		return dao.merge(user);
	}

	@Override
	public User activateUserAccount(final Integer userId_, final Integer originalVersionId_) throws Exception
	{
		final User user = getUser(userId_);
		user.setUserStatusId(UserStatus.ACTIVE);
		user.setVersion(originalVersionId_);
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
		AccessRole role = new AccessRole();
		role.setCompanyId(companyId_);
		role.setName(name_);
		role.setSortOrder(0);
		final Integer roleId = dao.addAndReturnId(role);
		role = getRequiredEntityById(AccessRole.class, roleId);
		// add passed permissions
		for (final Integer permissionId : permissionIds_)
		{
			addRolePermission(roleId, permissionId, role.getVersion());
		}
		return role;
	}

	@Override
	public void saveRolePermissions(final Integer roleId_, final List<Integer> permissionIds_, final Integer originalVersionId_)
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
			addRolePermission(roleId_, permissionId, originalVersionId_);
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
	public void addRolePermission(final Integer roleId_, final Integer permissionId_, final Integer originalVersionId_)
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

			final AccessRole role = getRequiredEntityById(AccessRole.class, roleId_);
			role.setVersion(originalVersionId_);
			dao.merge(role);
		}
	}

	private boolean isPermissionAssignable(final Integer permissionId_)
	{
		final Permission permission = getRequiredEntityById(Permission.class, permissionId_);
		return permission.isAssignable();
	}

	@Override
	public void saveUserRoles(final Integer userId_, final List<Integer> roleIds_, final Integer originalVersionId_)
	{
		// delete old roles
		final Search search = new Search(UserRole.class);
		search.addFilterEqual("userId", userId_);
		final List<UserRole> userRoles = dao.search(UserRole.class, search);
		dao.delete(userRoles);
		for (final Integer roleId : roleIds_)
		{
			addUserRole(roleId, userId_, originalVersionId_);
		}
	}

	@Override
	public void addUserRole(final Integer roleId_, final Integer userId_, final Integer originalVersionId_)
	{
		final User user = getRequiredEntityById(User.class, userId_);
		final AccessRole role = getRequiredEntityById(AccessRole.class, roleId_);
		if (AccessRole.getProtectedSystemRoleIds().contains(roleId_))
		{
			throw new UnsupportedOperationException("Cannot assign this system role: " + roleId_);
		}
		final Search search = new Search(UserRole.class);
		search.addFilterEqual("accessRoleId", roleId_);
		search.addFilterEqual("userId", user.getId());
		UserRole userRole = (UserRole) dao.searchUnique(UserRole.class, search);
		if (userRole == null)
		{
			userRole = new UserRole();
			userRole.setAccessRoleId(role.getId());
			userRole.setUserId(userId_);
			dao.addAndReturnId(userRole);

			user.setVersion(originalVersionId_);
			dao.merge(user);
		}
	}

	@Override
	public void deleteRole(final Integer roleId_, final Integer originalVersionId_) throws Exception
	{
		final AccessRole role = getRequiredEntityById(AccessRole.class, roleId_);
		// TODO - check same company before deleting
		if (isSystemRole(roleId_))
		{
			throw new UnsupportedOperationException("Cannot delete system role.");
		}
		final Search search = new Search(RolePermission.class);
		search.addFilterEqual("accessRoleId", roleId_);
		final List<RolePermission> rolePermissions = dao.search(RolePermission.class, search);
		dao.delete(rolePermissions);

		role.setVersion(originalVersionId_);
		dao.delete(role);
	}

	@Override
	public void deleteRolePermission(final Integer roleId_, final Integer permissionId_, final Integer originalVersionId_)
	{
		final AccessRole role = getRequiredEntityById(AccessRole.class, roleId_);
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

		role.setVersion(originalVersionId_);
		dao.merge(role);
	}

	@Override
	public void deleteUserRole(final Integer roleId_, final Integer userId_, final Integer originalVersionId_)
	{
		final User user = getRequiredEntityById(User.class, userId_);
		final Search search = new Search(UserRole.class);
		search.addFilterEqual("accessRoleId", roleId_);
		search.addFilterEqual("userId", userId_);
		final UserRole userRole = (UserRole) dao.searchUnique(UserRole.class, search);
		if (userRole == null)
		{
			throw new NotFoundException("Role not found. Id: " + roleId_ + " For User: " + userId_);
		}
		dao.delete(userRole);

		user.setVersion(originalVersionId_);
		dao.merge(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getRolePermissions(final Integer roleId_)
	{
		Search search = new Search(RolePermission.class);
		search.addField("permissionId");
		search.addFilterEqual("accessRoleId", roleId_);
		final List permissionIdList = dao.search(RolePermission.class, search);
		// add default permissions
		permissionIdList.addAll(getDefaultPermissions());
		search = new Search(Permission.class);
		search.addFilterIn("id", permissionIdList);
		final List<Permission> list = dao.search(Permission.class, search);
		return list;
	}

	@SuppressWarnings("unchecked")
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
		final List permissionIdList = dao.search(RolePermission.class, search);
		// add default permissions
		permissionIdList.addAll(getDefaultPermissions());
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
	public List<Attachment> getAttachmentsForUser(final Integer userId_) throws Exception
	{
		return attachmentService.getAttachmentsForEntity(userId_, EntityType.USER);
	}

	@Override
	public Attachment addAttachmentForUser(final String name, final String description, final String url, final Double size, final Integer userId_, final Integer attachmentTypeId)
			throws Exception
	{
		return attachmentService.addAttachment(name, description, url, size, EntityType.USER, userId_, attachmentTypeId);
	}

	@Override
	public boolean deleteAttachment(final Integer attachmentId_, final Integer entityId_) throws Exception
	{
		return attachmentService.deleteAttachment(attachmentId_, entityId_, EntityType.USER);
	}

	@Override
	public boolean isUserExists(final String email_)
	{
		final Search search = new Search(User.class);
		search.addFilterEqual("email", email_);
		final List<?> list = dao.search(User.class, search);
		return !list.isEmpty();
	}

	@Override
	public boolean isUserInPermission(final Integer userId_, final String permissionCode_)
	{
		List<Permission> permissions = getUserPermissions(userId_);
		for (Permission permission : permissions)
		{
			if (permission.getPermissionCode().equals(permissionCode_))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void login(final AuthenticatedUserInfo authInfo_, final String sessionId_) throws Exception
	{
		if ((authInfo_ == null) || (sessionId_ == null))
		{
			throw new IllegalArgumentException("Session ID is null.");
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
			throw new IllegalArgumentException("Session ID is null.");
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
		final Permission permission = getRequiredEntityById(Permission.class, permissionId_);
		return permission;
	}

	@Override
	public AccessRole getRole(Integer roleId_)
	{
		final AccessRole role = getRequiredEntityById(AccessRole.class, roleId_);
		return role;
	}

	public void setDefaultPermissions(List<Integer> defaultPermissions)
	{
		this.defaultPermissions = defaultPermissions;
	}

	public List<Integer> getDefaultPermissions()
	{
		return defaultPermissions;
	}

}
