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

import org.apache.cxf.jaxrs.ext.MessageContext;

import com.utest.webservice.model.v2.PermissionInfo;
import com.utest.webservice.model.v2.PermissionSearchResultInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.RoleSearchResultInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UserSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface UserWebService
{
	UserInfo updateUser(UriInfo ui, Integer userId, UserInfo userInfo) throws Exception;

	UserInfo getUser(UriInfo ui, Integer userId) throws Exception;

	UserSearchResultInfo findUsers(UriInfo ui, UtestSearchRequest request) throws Exception;

	PermissionSearchResultInfo findPermissions(UriInfo ui, UtestSearchRequest request) throws Exception;

	PermissionInfo getPermission(UriInfo ui, Integer permissionId) throws Exception;

	RoleSearchResultInfo findRoles(UriInfo ui, UtestSearchRequest request) throws Exception;

	RoleInfo getRole(UriInfo ui, Integer roleId) throws Exception;

	UserInfo createUser(UriInfo ui, UserInfo userInfo) throws Exception;

	RoleInfo createRole(UriInfo ui, RoleInfo roleInfo) throws Exception;

	Boolean login(MessageContext context) throws Exception;

	Boolean logout(MessageContext context) throws Exception;

	UserInfo getCurrentUser(UriInfo ui) throws Exception;

	UserInfo activateUser(UriInfo ui, Integer userId, Integer originalVersionId) throws Exception;

	UserInfo deactivateUser(UriInfo ui, Integer userId, Integer originalVersionId) throws Exception;

	UserInfo changeUserEmail(UriInfo ui, Integer userId, String newEmail, Integer originalVersionId) throws Exception;

	UserInfo changeUserPassword(UriInfo ui, Integer userId, String newPassword, Integer originalVersionId) throws Exception;

	Boolean deleteRole(UriInfo ui, Integer roleId, Integer originalVersionId) throws Exception;

	UserInfo confirmUserEmail(UriInfo ui, Integer userId, Integer originalVersionId) throws Exception;

	Boolean deleteUserRole(UriInfo ui, Integer userId, Integer roleId, Integer originalVersionId) throws Exception;

	Boolean deleteRolePermission(UriInfo ui, Integer roleId, Integer permissionId, Integer originalVersionId) throws Exception;

	Boolean addRolePermission(UriInfo ui, Integer roleId, Integer permissionId, Integer originalVersionId) throws Exception;

	Boolean addUserRole(UriInfo ui, Integer userId, Integer roleId, Integer originalVersionId) throws Exception;

	Boolean updateRolePermissions(UriInfo ui, Integer roleId, ArrayList<Integer> permissionIds, Integer originalVersionId) throws Exception;

	Boolean updateUserRoles(UriInfo ui, Integer userId, ArrayList<Integer> roleIds, Integer originalVersionId) throws Exception;

	List<RoleInfo> getUserRoles(UriInfo ui, Integer userId) throws Exception;

	List<PermissionInfo> getRolePermissions(UriInfo ui, Integer roleId) throws Exception;

	List<PermissionInfo> getUserPermissions(UriInfo ui, Integer userId) throws Exception;

}
