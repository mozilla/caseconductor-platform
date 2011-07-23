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

import java.util.List;

import com.utest.domain.AccessRole;
import com.utest.domain.Attachment;
import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.Permission;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

/**
 * Service to handle all domain operations related to User Management.
 */
public interface UserService
{
	boolean isUserExists(String email_);

	User getUser(Integer userId_) throws Exception;

	User getUserByEmail(String email_);

	List<Permission> getUserPermissions(Integer userId_);

	List<AccessRole> getUserRoles(Integer userId_);

	List<Permission> getRolePermissions(Integer roleId_);

	void addRolePermission(Integer roleId_, Integer permissionId_, Integer originalVersionId);

	void deleteRolePermission(Integer roleId_, Integer permissionId_, Integer originalVersionId);

	void deleteRole(Integer roleId_, Integer originalVersionId) throws Exception;

	UtestSearchResult findUsers(UtestSearch search_) throws Exception;

	UtestSearchResult findRoles(UtestSearch search_) throws Exception;

	UtestSearchResult findPermissions(UtestSearch search_) throws Exception;

	User addUser(Integer companyId, String firstName, String lastName, String email, String password, String screenName) throws Exception;

	User getUserByCode(String code);

	User confirmUserEmail(String code) throws Exception;

	void saveRolePermissions(Integer roleId, List<Integer> permissionIds, Integer originalVersionId);

	void login(AuthenticatedUserInfo authInfo, String sessionId) throws Exception;

	void logout(AuthenticatedUserInfo authInfo, String sessionId);

	AccessRole addRole(Integer companyId, String name, List<Integer> permissionIds) throws Exception;

	void addUserRole(Integer roleId, Integer userId, Integer originalVersionId);

	void saveUserRoles(Integer userId, List<Integer> roleIds, Integer originalVersionId);

	void deleteUserRole(Integer roleId_, Integer userId_, Integer originalVersionId);

	User activateUserAccount(Integer userId, Integer originalVersionId) throws Exception;

	User changeUserEmail(Integer userId, String newEmail, Integer originalVersionId) throws Exception;

	User changeUserPassword(Integer userId, String newPassword, Integer originalVersionId) throws Exception;

	User saveUser(Integer userId, Integer companyId, String firstName, String lastName, Integer originalVersionId) throws Exception;

	Permission getPermission(Integer permissionId);

	AccessRole getRole(Integer roleId);

	User getCurrentUser() throws Exception;

	User getUserByScreenName(String screenName);

	User closeUserAccount(Integer userId, Integer originalVersionId) throws Exception;

	User confirmUserEmail(Integer userId, Integer originalVersionId) throws Exception;

	boolean isUserInPermission(Integer userId, String permissionCode);

	List<Attachment> getAttachmentsForUser(Integer userId) throws Exception;

	Attachment addAttachmentForUser(String name, String description, String url, Double size, Integer userId, Integer attachmentTypeId) throws Exception;

	boolean deleteAttachment(Integer attachmentId, Integer entityId) throws Exception;
}
