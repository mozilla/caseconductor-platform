/*
Case Conductor is a Test Case Management system.
Copyright (C) 2011 uTest Inc.

This file is part of Case Conductor.

Case Conductor is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Case Conductor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Case Conductor.  If not, see <http://www.gnu.org/licenses/>.

*/
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

import com.utest.dao.TypelessDAO;
import com.utest.domain.TestSuite;
import com.utest.domain.User;

public class TestSuiteServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private TestSuiteService		testSuiteService;
	@Autowired
	private UserService			userService;
	@Autowired
	private EnvironmentService	environmentService;

	@Autowired
	private TypelessDAO				dao;

	//@Test(groups = { "integration" })
	public void testAddTestSuite() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer productId = 1;
		final TestSuite testSuite11 = testSuiteService.addTestSuite(productId, true, "Test Case for product 1", "windows test calculator");
		Assert.assertTrue(testSuite11 != null);
	}

	//@Test(groups = { "integration" })
	public void testIncludeTestCasesInTestSuite() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer testSuiteId = 18;
		Assert.assertTrue(testSuiteService.addTestSuiteTestCase(testSuiteId, 45) != null);
		Assert.assertTrue(testSuiteService.addTestSuiteTestCase(testSuiteId, 54) != null);
		Assert.assertTrue(testSuiteService.addTestSuiteTestCase(testSuiteId, 61) != null);
	}

	//@Test(groups = { "integration" }, expectedExceptions = { UnsupportedEnvironmentSelectionException.class })
	public void testSaveTestSuiteInvalidEnvironmentGroups() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> environmentGroupsIds = new ArrayList<Integer>();
		environmentGroupsIds.add(35);
		environmentGroupsIds.add(36);
		environmentGroupsIds.add(37);
		environmentGroupsIds.add(2);

		testSuiteService.saveEnvironmentGroupsForTestSuite(3, environmentGroupsIds, 1);
		Assert.assertTrue(true);

	}

	//@Test(groups = { "integration" })
	public void testSaveTestSuiteValidEnvironmentGroups() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> environmentGroupsIds = new ArrayList<Integer>();
		environmentGroupsIds.add(35);
		environmentGroupsIds.add(36);
		environmentGroupsIds.add(37);

		testSuiteService.saveEnvironmentGroupsForTestSuite(3, environmentGroupsIds, 1);
		Assert.assertTrue(true);

	}

	//@Test(groups = { "integration" })
	public void testActivateTestSuite() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		Assert.assertTrue(testSuiteService.activateTestSuite(3, 1) != null);

	}

}
