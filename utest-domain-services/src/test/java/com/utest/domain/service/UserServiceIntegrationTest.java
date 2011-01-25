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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import com.utest.domain.AccessRole;
import com.utest.domain.User;

public class UserServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private UserService	userService;

	// @Test(groups = { "integration" })
	public void testAddUser() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final User user1 = userService.addUser(null, "admin", "admin", "admin123@utest.com", "admin", null);
		Assert.assertTrue(user1 != null);
	}

	//
	// @Test(groups = { "integration" })
	public void testAddRole() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = 9;
		final List<Integer> permissions = new ArrayList<Integer>();
		permissions.add(3);
		permissions.add(4);
		permissions.add(5);
		final AccessRole role1 = userService.addRole(companyId, "New user role with permissions: 3,4,5", permissions);
		Assert.assertTrue(role1 != null);
	}

	//
	// @Test(groups = { "integration" })
	public void testSaveRolePermissions() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer roleId = 5;
		AccessRole role = userService.getRole(roleId);
		final List<Integer> permissions = new ArrayList<Integer>();
		permissions.add(21);
		permissions.add(22);
		permissions.add(23);
		userService.saveRolePermissions(roleId, permissions, role.getVersion());
		Assert.assertTrue(permissions != null);
	}

	// @Test(groups = { "integration" })
	public void testSaveUserRoles() throws Exception
	{
		User user = userService.getUser(1);
		loginUser(user);
		final Integer userId = 4;
		user = userService.getUser(userId);
		final List<Integer> roles = new ArrayList<Integer>();
		roles.add(2);
		roles.add(3);
		roles.add(4);
		roles.add(5);
		userService.saveUserRoles(userId, roles, user.getVersion());
		Assert.assertTrue(true);
	}

	// @Test(groups = { "integration" })
	public void testDeleteUserRole() throws Exception
	{
		User user = userService.getUser(1);
		loginUser(user);
		final Integer userId = 4;
		user = userService.getUser(userId);
		final Integer roleId = 5;
		userService.deleteUserRole(roleId, user.getId(), user.getVersion());
		Assert.assertTrue(true);
	}
}
