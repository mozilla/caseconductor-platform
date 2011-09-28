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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import com.utest.dao.TypelessDAO;
import com.utest.domain.TestCycle;
import com.utest.domain.TestRun;
import com.utest.domain.TestRunResult;
import com.utest.domain.TestRunTestCase;
import com.utest.domain.TestSuite;
import com.utest.domain.User;
import com.utest.util.DateUtil;

public class TestRunServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private TestCycleService	testCycleService;
	@Autowired
	private TestRunService		testRunService;
	@Autowired
	private TestSuiteService	testSuiteService;
	@Autowired
	private UserService			userService;
	@Autowired
	private EnvironmentService	environmentService;

	@Autowired
	private TypelessDAO			dao;

	// @Test(groups = { "integration" })
	public void testAddTestRun() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer productId = 1;
		final TestCycle testCycle11 = testCycleService.addTestCycle(productId, "Test Cycle for product 1" + new Date().getTime(), "Test cycle for windows test calculator",
				new Date(), null, true, true);
		Assert.assertTrue(testCycle11 != null);

		final TestRun testRun = testRunService.addTestRun(testCycle11.getId(), true, "Test Run for cycle: " + testCycle11.getName(), "VMK testing description.", new Date(),
				DateUtil.addMonths(new Date(), 12), true, false, 3, true);
		Assert.assertTrue(testRun != null);
	}

	// @Test(groups = { "integration" })
	public void testAddTestRunFromTestSuite() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer productId = 1;
		final TestSuite testSuite11 = testSuiteService.addTestSuite(productId, true, "Test Case for product 1: " + new Date().getTime(), "windows test calculator");
		Assert.assertTrue(testSuite11 != null);

		final Integer testSuiteId = testSuite11.getId();
		Assert.assertTrue(testSuiteService.addTestSuiteTestCase(testSuiteId, 45) != null);
		Assert.assertTrue(testSuiteService.addTestSuiteTestCase(testSuiteId, 54) != null);
		Assert.assertTrue(testSuiteService.addTestSuiteTestCase(testSuiteId, 61) != null);

		final TestSuite testSuite2 = testSuiteService.getTestSuite(testSuiteId);
		Assert.assertTrue(testSuiteService.activateTestSuite(testSuiteId, testSuite2.getVersion()) != null);

		final TestCycle testCycle11 = testCycleService.addTestCycle(productId, "Test Cycle for product 1" + new Date().getTime(), "Test cycle for windows test calculator",
				new Date(), null, true, true);

		final Integer testCycleId = 1;
		Assert.assertTrue(testCycle11 != null);

		final TestRun testRun = testRunService.addTestRun(testCycleId, true, "Test run from test suite: " + testSuiteId, "VMK test run from test suite.", new Date(), DateUtil
				.addMonths(new Date(), 12), true, false, 3, false);
		Assert.assertTrue(testRun != null);
		final List<TestRunTestCase> includedCases = testRunService.addTestCasesFromTestSuite(testRun.getId(), testSuiteId);
		final Integer testerId = 1;
		for (final TestRunTestCase includedCase : includedCases)
		{
			Assert.assertTrue(testRunService.addAssignment(includedCase.getId(), testerId) != null);
		}
	}

	// @Test(groups = { "integration" })
	public void testStartExecutingTestCase() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);

		final List<TestRunResult> testRunResults = testRunService.getTestRunResults(16, 1, 51);
		TestRunResult testRunResult = testRunResults.get(0);
		testRunResult = testRunService.startExecutingAssignedTestCase(testRunResult.getId(), testRunResult.getVersion());

		Assert.assertTrue(testRunResult != null);
	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// TestCaseExecutionBlockedException.class })
	public void testFinishExecutingBlockingTestCaseWithFailure() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);

		final List<TestRunResult> testRunResults = testRunService.getTestRunResults(16, 1, 51);
		TestRunResult testRunResult = testRunResults.get(0);
		testRunResult = testRunService.finishExecutingAssignedTestCaseWithFailure(testRunResult.getId(), 1, "Got null pointer exception.", "VMK Testing comment", testRunResult
				.getVersion());

		// try to execute blocked test case
		testRunResult = testRunResults.get(1);
		testRunResult = testRunService.startExecutingAssignedTestCase(testRunResult.getId(), testRunResult.getVersion());

		Assert.assertTrue(testRunResult != null);
	}

	// @Test(groups = { "integration" })
	public void testFinishExecutingBlockingTestCaseWithSuccess() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);

		final List<TestRunResult> testRunResults = testRunService.getTestRunResults(16, 1, 51);
		TestRunResult testRunResult = testRunResults.get(0);
		testRunResult = testRunService.finishExecutingAssignedTestCaseWithSuccess(testRunResult.getId(), null, testRunResult.getVersion());

		// try to execute un-blocked test case
		testRunResult = testRunResults.get(1);
		testRunResult = testRunService.startExecutingAssignedTestCase(testRunResult.getId(), testRunResult.getVersion());

		Assert.assertTrue(testRunResult != null);
	}

	// @Test(groups = { "integration" })
	public void testApproveResult() throws Exception
	{

		final User user = userService.getUser(16316);
		loginUser(user);
		final List<TestRunResult> testRunResults = testRunService.getTestRunResults(16, 1, 51);
		TestRunResult testRunResult = testRunResults.get(0);
		testRunResult = testRunService.approveTestRunResult(testRunResult.getId(), testRunResult.getVersion());// testRunResult.getVersion()
		Assert.assertTrue(testRunResult != null);

	}

	// @Test(groups = { "integration" })
	public void testRetestResult() throws Exception
	{

		final User user = userService.getUser(16316);
		loginUser(user);
		final List<TestRunResult> testRunResults = testRunService.getTestRunResults(16, 1, 51);
		TestRunResult testRunResult = testRunResults.get(0);
		testRunResult = testRunService.retestTestRunResult(testRunResult.getId(), 16316);// testRunResult.getVersion()
		Assert.assertTrue(testRunResult != null);

	}

	// @Test(groups = { "integration" })
	public void testRetestTestRun() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<TestRunResult> testRunResults = testRunService.retestTestRun(16, false);
		Assert.assertTrue(testRunResults != null);

	}

}
