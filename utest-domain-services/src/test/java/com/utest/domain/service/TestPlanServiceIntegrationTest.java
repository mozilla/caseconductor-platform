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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import com.utest.domain.TestPlan;
import com.utest.domain.TestSuite;
import com.utest.domain.User;

public class TestPlanServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private TestPlanService	testPlanService;
	@Autowired
	private TestSuiteService	testSuiteService;
	@Autowired
	private TestCaseService	testCaseService;
	@Autowired
	private UserService		userService;

	//@Test(groups = { "integration" })
	public void testAddTestPlan() throws Exception
	{
		final User user = userService.getUser(16315);
		loginUser(user);
		final Integer productId = 1;
		final TestPlan testPlan11 = testPlanService.addTestPlan(productId, "Test Plan for product 1", "Plan with many test suites");
		Assert.assertTrue(testPlan11 != null);
	}

	//@Test(groups = { "integration" })
	public void testIncludeTestSuitesInTestPlan() throws Exception
	{
		final User user = userService.getUser(16315);
		loginUser(user);
		final Integer productId = 1;
		final TestPlan testPlan11 = testPlanService.addTestPlan(productId, "Test Plan for product 1 +--+" + new Date().getTime(), "Plan with many test suites");

		final TestSuite testSuite1 = testSuiteService.addTestSuite(productId, true, "Test Suite 1 for product 1 +++" + new Date().getTime(), "Plan with many test suites");
		final TestSuite testSuite2 = testSuiteService.addTestSuite(productId, true, "Test Suite 2 for product 1 +++" + new Date().getTime(), "Plan with many test suites");
		final TestSuite testSuite3 = testSuiteService.addTestSuite(productId, true, "Test Suite 3 for product 1 +++" + new Date().getTime(), "Plan with many test suites");

		testSuiteService.activateTestSuite(testSuite1.getId(), testSuite1.getVersion());
		testSuiteService.activateTestSuite(testSuite2.getId(), testSuite2.getVersion());
		testSuiteService.activateTestSuite(testSuite3.getId(), testSuite3.getVersion());

		Assert.assertTrue(testPlanService.addTestPlanTestSuite(testPlan11.getId(), testSuite1.getId()) != null);
		Assert.assertTrue(testPlanService.addTestPlanTestSuite(testPlan11.getId(), testSuite2.getId()) != null);
		Assert.assertTrue(testPlanService.addTestPlanTestSuite(testPlan11.getId(), testSuite3.getId()) != null);
	}

	//@Test(groups = { "integration" })
	public void testActivateTestPlan() throws Exception
	{

		final User user = userService.getUser(16315);
		loginUser(user);
		Assert.assertTrue(testPlanService.activateTestPlan(1, 1) != null);

	}

}
