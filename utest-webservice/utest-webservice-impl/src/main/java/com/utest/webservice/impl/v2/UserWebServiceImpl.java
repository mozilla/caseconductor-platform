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
package com.utest.webservice.impl.v2;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.jboss.util.Base64;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import com.utest.domain.AccessRole;
import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.Permission;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.UserService;
import com.utest.domain.service.util.UserUtil;
import com.utest.webservice.api.v2.UserWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.PermissionInfo;
import com.utest.webservice.model.v2.PermissionResultInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.RoleResultInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UserResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;
import com.utest.webservice.util.SessionUtil;

@Path("/users/")
public class UserWebServiceImpl extends BaseWebServiceImpl implements UserWebService
{
	private final UserService	userService;

	public UserWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final UserService userService)
	{
		super(objectBuildFactory);
		this.userService = userService;
	}

	@PUT
	@Path("/login")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public Boolean login(@Context MessageContext context) throws Exception
	{
		Integer auth = UserUtil.getCurrentUserId();
		if (auth == null)
		{
			throw new org.apache.cxf.interceptor.security.AccessDeniedException("No logged in user!");
		}
		final AuthenticatedUserInfo authInfo = (AuthenticatedUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sessionId = SessionUtil.extractSession(context, true);
		userService.login(authInfo, sessionId);

		String token = Base64.encodeObject(SecurityContextHolder.getContext().getAuthentication(), Base64.GZIP | Base64.DONT_BREAK_LINES);
		context.getHttpServletResponse().addCookie(new Cookie(SessionUtil.AUTH_TOKEN, token));
		return Boolean.TRUE;
	}

	@PUT
	@Path("/logout")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	public Boolean logout(@Context MessageContext context) throws Exception
	{
		Integer auth = UserUtil.getCurrentUserId();
		if (auth == null)
		{
			throw new org.apache.cxf.interceptor.security.AccessDeniedException("No logged in user!");
		}

		final AuthenticatedUserInfo authInfo = (AuthenticatedUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.logout(authInfo, SessionUtil.extractSession(context, false));
		context.getHttpServletResponse().addCookie(new Cookie(SessionUtil.AUTH_TOKEN, null));
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/activate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public UserInfo activateUser(@Context final UriInfo ui_, @PathParam("id") final Integer userId, @FormParam("resourceVersionId") final Integer resourceVersionId_)
			throws Exception
	{
		final User user = userService.activateUserAccount(userId, resourceVersionId_);
		return objectBuilderFactory.toInfo(UserInfo.class, user, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/deactivate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public UserInfo deactivateUser(@Context final UriInfo ui_, @PathParam("id") final Integer userId, @FormParam("resourceVersionId") final Integer resourceVersionId_)
			throws Exception
	{
		final User user = userService.closeUserAccount(userId, resourceVersionId_);
		return objectBuilderFactory.toInfo(UserInfo.class, user, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/passwordchange/{newpassword}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public UserInfo changeUserPassword(@Context final UriInfo ui_, @PathParam("id") final Integer userId, @PathParam("newpassword") final String newPassword,
			@FormParam("resourceVersionId") final Integer resourceVersionId_) throws Exception
	{
		final User user = userService.changeUserPassword(userId, newPassword, resourceVersionId_);
		return objectBuilderFactory.toInfo(UserInfo.class, user, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/emailchange/{newemail}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public UserInfo changeUserEmail(@Context final UriInfo ui_, @PathParam("id") final Integer userId, @PathParam("newemail") final String newEmail,
			@FormParam("resourceVersionId") final Integer resourceVersionId_) throws Exception
	{
		final User user = userService.changeUserEmail(userId, newEmail, resourceVersionId_);
		return objectBuilderFactory.toInfo(UserInfo.class, user, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public UserInfo updateUser(@Context final UriInfo ui_, @PathParam("id") final Integer userId, @FormParam("") final UserInfo userInfo) throws Exception
	{
		final User user = userService.saveUser(userId, userInfo.getCompanyId(), userInfo.getFirstName(), userInfo.getLastName(), userInfo.getResourceIdentity().getVersion());

		return objectBuilderFactory.toInfo(UserInfo.class, user, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public UserInfo createUser(@Context final UriInfo ui_, @FormParam("") final UserInfo userInfo) throws Exception
	{
		final User user = userService.addUser(userInfo.getCompanyId(), userInfo.getFirstName(), userInfo.getLastName(), userInfo.getEmail(), userInfo.getPassword(), userInfo
				.getScreenName());

		return objectBuilderFactory.toInfo(UserInfo.class, user, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public UserInfo getUser(@Context final UriInfo ui_, @PathParam("id") final Integer userId) throws Exception
	{
		final User user = userService.getUser(userId);

		return objectBuilderFactory.toInfo(UserInfo.class, user, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/current/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public UserInfo getCurrentUser(@Context final UriInfo ui_) throws Exception
	{
		final User user = userService.getCurrentUser();

		return objectBuilderFactory.toInfo(UserInfo.class, user, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public UserResultInfo findUsers(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(UserInfo.class, request, ui_);
		final UtestSearchResult result = userService.findUsers(search);

		return (UserResultInfo) objectBuilderFactory.createResult(UserInfo.class, User.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/permissions/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public PermissionResultInfo findPermissions(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(PermissionInfo.class, request, ui_);
		final UtestSearchResult result = userService.findPermissions(search);

		return (PermissionResultInfo) objectBuilderFactory.createResult(PermissionInfo.class, Permission.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/permissions/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public PermissionInfo getPermission(@Context final UriInfo ui_, @PathParam("id") final Integer permissionId_) throws Exception
	{
		final Permission permission = userService.getPermission(permissionId_);

		return objectBuilderFactory.toInfo(PermissionInfo.class, permission, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/permissions/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public List<PermissionInfo> getUserPermissions(@Context UriInfo ui_, @PathParam("id") final Integer userId_, @QueryParam("") UtestSearchRequest request) throws Exception
	{
		final List<Permission> permissions = userService.getUserPermissions(userId_);
		final List<PermissionInfo> permissionsInfo = objectBuilderFactory.toInfo(PermissionInfo.class, permissions, ui_.getBaseUriBuilder());
		return permissionsInfo;

	}

	@GET
	@Path("/{id}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public List<RoleInfo> getUserRoles(@Context UriInfo ui_, @PathParam("id") final Integer userId_, @QueryParam("") UtestSearchRequest request) throws Exception
	{
		final List<AccessRole> roles = userService.getUserRoles(userId_);
		final List<RoleInfo> rolesInfo = objectBuilderFactory.toInfo(RoleInfo.class, roles, ui_.getBaseUriBuilder());
		return rolesInfo;
	}

	@POST
	@Path("/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ROLE_EDIT })
	public RoleInfo createRole(@Context final UriInfo ui_, @FormParam("") final RoleInfo roleInfo) throws Exception
	{
		final AccessRole role = userService.addRole(roleInfo.getCompanyId(), roleInfo.getName(), new ArrayList<Integer>());

		return objectBuilderFactory.toInfo(RoleInfo.class, role, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/roles/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public Boolean deleteRole(@Context final UriInfo ui_, @PathParam("id") final Integer roleId_, @FormParam("resourceVersionId") final Integer resourceVersionId_)
			throws Exception
	{
		userService.deleteRole(roleId_, resourceVersionId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/roles/{id}/permissions/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public Boolean updateRolePermissions(@Context final UriInfo ui_, @PathParam("id") final Integer roleId, @FormParam("permissionIds") final ArrayList<Integer> permissionIds)
			throws Exception
	{
		userService.saveRolePermissions(roleId, permissionIds, 0);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public Boolean updateUserRoles(@Context final UriInfo ui_, @PathParam("id") final Integer userId_, @FormParam("roleIds") final ArrayList<Integer> roleIds_) throws Exception
	{
		userService.saveUserRoles(userId_, roleIds_, 0);
		return Boolean.TRUE;
	}

	@POST
	@Path("/roles/{id}/permissions/{permissionId}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public Boolean addRolePermission(@Context final UriInfo ui_, @PathParam("id") final Integer roleId_, @PathParam("permissionId") final Integer permissionId_) throws Exception
	{
		// TODO - do we need a version here?
		userService.addRolePermission(roleId_, permissionId_, 0);
		return Boolean.TRUE;
	}

	@POST
	@Path("/{id}/roles/{roleId}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public Boolean addUserRole(@Context final UriInfo ui_, @PathParam("id") final Integer userId_, @PathParam("roleId") final Integer roleId_) throws Exception
	{
		// TODO - do we need a version here?
		userService.addUserRole(roleId_, userId_, 0);
		return Boolean.TRUE;
	}

	@DELETE
	@Path("/roles/{id}/permissions/{permissionId}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public Boolean deleteRolePermission(@Context final UriInfo ui_, @PathParam("id") final Integer roleId_, @PathParam("permissionId") final Integer permissionId_)
			throws Exception
	{
		// TODO - do we need a version here?
		userService.deleteRolePermission(roleId_, permissionId_, 0);
		return Boolean.TRUE;
	}

	@DELETE
	@Path("/{id}/roles/{roleId}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.USER_ACCOUNT_EDIT })
	public Boolean deleteUserRole(@Context final UriInfo ui_, @PathParam("id") final Integer userId_, @PathParam("roleId") final Integer roleId_) throws Exception
	{
		// TODO - fix version
		userService.deleteUserRole(roleId_, userId_, 0);
		return Boolean.TRUE;
	}

	@GET
	@Path("/roles/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public RoleResultInfo findRoles(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(PermissionInfo.class, request, ui_);
		final UtestSearchResult result = userService.findRoles(search);

		return (RoleResultInfo) objectBuilderFactory.createResult(RoleInfo.class, AccessRole.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/roles/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.USER_ACCOUNT_VIEW)
	public RoleInfo getRole(@Context final UriInfo ui_, @PathParam("id") final Integer roleId_) throws Exception
	{
		final AccessRole role = userService.getRole(roleId_);

		return objectBuilderFactory.toInfo(RoleInfo.class, role, ui_.getBaseUriBuilder());
	}

}
