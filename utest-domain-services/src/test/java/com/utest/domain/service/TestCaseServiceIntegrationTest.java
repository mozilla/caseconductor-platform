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
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.utest.domain.TestCase;
import com.utest.domain.TestCaseStep;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.User;
import com.utest.domain.VersionIncrement;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

public class TestCaseServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private TestCaseService	testCaseService;
	@Autowired
	private UserService		userService;

	// @Test(groups = { "integration" })
	public void testAddTestCase() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer productId = 1;

		// final Transaction tran = dao.getSession().beginTransaction();

		final TestCase testCase11 = testCaseService.addTestCase(productId, 10, 5, "Test Case for product 1" + new Date().getTime(), "windows test calculator");
		Assert.assertTrue(testCase11 != null);
		final List<TestCaseStep> steps = new ArrayList<TestCaseStep>();
		steps.add(testCaseService.addTestCaseStep(testCase11.getLatestVersion().getId(), "Step 1", 1, "Press OK button", "See the totals calculated." + new Date().getTime(), 2));
		steps.add(testCaseService.addTestCaseStep(testCase11.getLatestVersion().getId(), "Step 2", 2, "Press Subtract button", "See the totals calculated." + new Date().getTime(),
				2));
		steps.add(testCaseService.addTestCaseStep(testCase11.getLatestVersion().getId(), "Step 3", 3, "Press Add button", "See the totals calculated." + new Date().getTime(), 2));
		steps.add(testCaseService.addTestCaseStep(testCase11.getLatestVersion().getId(), "Step 4", 4, "Press Multiply button", "See the totals calculated." + new Date().getTime(),
				2));
		steps.add(testCaseService
				.addTestCaseStep(testCase11.getLatestVersion().getId(), "Step 5", 5, "Press Divide button", "See the totals calculated." + new Date().getTime(), 2));
		steps.add(testCaseService.addTestCaseStep(testCase11.getLatestVersion().getId(), "Step 6", 6, "Press Percent button", "See the totals calculated." + new Date().getTime(),
				2));

		TestCaseVersion testCaseVersion = testCaseService.getTestCaseVersion(testCase11.getLatestVersion().getId());// testCase11.getLatestVersion().getId()

		// tran.commit();

		testCaseVersion = testCaseService.approveTestCaseVersion(testCaseVersion.getId(), testCaseVersion.getVersion());
		testCaseVersion = testCaseService.activateTestCaseVersion(testCaseVersion.getId(), testCaseVersion.getVersion());

	}

	// @Test(groups = { "integration" })
	public void testAddTestCaseStep() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);

		// final Transaction tran = dao.getSession().beginTransaction();

		final List<TestCaseStep> steps = new ArrayList<TestCaseStep>();
		steps.add(testCaseService.addTestCaseStep(45, "Step 1" + new Date().getTime(), 1, "Press OK button", "See the totals calculated." + new Date().getTime(), 2));
		steps.add(testCaseService.addTestCaseStep(45, "Step 2" + new Date().getTime(), 2, "Press Subtract button", "See the totals calculated." + new Date().getTime(), 2));
		steps.add(testCaseService.addTestCaseStep(45, "Step 3" + new Date().getTime(), 3, "Press Add button", "See the totals calculated." + new Date().getTime(), 2));
		steps.add(testCaseService.addTestCaseStep(45, "Step 4" + new Date().getTime(), 4, "Press Multiply button", "See the totals calculated." + new Date().getTime(), 2));
		steps.add(testCaseService.addTestCaseStep(45, "Step 5" + new Date().getTime(), 5, "Press Divide button", "See the totals calculated." + new Date().getTime(), 2));
		steps.add(testCaseService.addTestCaseStep(45, "Step 6" + new Date().getTime(), 6, "Press Percent button", "See the totals calculated." + new Date().getTime(), 2));

		// logout();
	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// UnsupportedEnvironmentSelectionException.class })
	public void testSaveTestCaseInvalidEnvironmentGroups() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> environmentGroupsIds = new ArrayList<Integer>();
		environmentGroupsIds.add(35);
		environmentGroupsIds.add(36);
		environmentGroupsIds.add(37);
		environmentGroupsIds.add(2);
		final TestCaseVersion testCaseVersion = testCaseService.getTestCaseVersion(3);// testCase11.getLatestVersion().getId()

		testCaseService.saveEnvironmentGroupsForTestCaseVersion(testCaseVersion.getId(), environmentGroupsIds, testCaseVersion.getVersion());
		Assert.assertTrue(true);

	}

	// @Test(groups = { "integration" })
	public void testFindTestCaseVersionsByTestCaseName() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		// UtestSearchResult findTestCaseVersions(final UtestSearch search_)
		UtestSearch search = new UtestSearch();
		// test case field
		search.addFilterEqual("name", "test case 2");
		// test case version field
		search.addFilterEqual("description", "test case 2");
		UtestSearchResult result = testCaseService.findTestCaseVersions(search, null, null);
		Assert.assertTrue(result.getResults() != null);

	}

	// @Test(groups = { "integration" })
	public void testSaveTestCaseValidEnvironmentGroups() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> environmentGroupsIds = new ArrayList<Integer>();
		environmentGroupsIds.add(1);
		environmentGroupsIds.add(2);
		final TestCaseVersion testCaseVersion = testCaseService.getTestCaseVersion(7);// testCase11.getLatestVersion().getId()
		testCaseService.saveEnvironmentGroupsForTestCaseVersion(testCaseVersion.getId(), environmentGroupsIds, testCaseVersion.getVersion());
		Assert.assertTrue(true);

	}

	// saveProductComponentsForTestCase(Integer, List<Integer>)
	// @Test(groups = { "integration" })
	public void testSaveTestCaseValidProductComponents() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(2);
		ids.add(3);
		Integer testCaseId = 45;
		TestCase testCase = testCaseService.getTestCase(testCaseId);
		testCaseService.saveProductComponentsForTestCase(testCase.getId(), ids, testCase.getVersion());
		Assert.assertTrue(true);

	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// UnsupportedEnvironmentSelectionException.class })
	public void testSaveTestCaseInvalidProductComponents() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> ids = new ArrayList<Integer>();
		ids.add(4);
		ids.add(5);
		ids.add(6);

		Integer testCaseId = 3;
		TestCase testCase = testCaseService.getTestCase(testCaseId);
		testCaseService.saveProductComponentsForTestCase(testCase.getId(), ids, testCase.getVersion());
		Assert.assertTrue(true);
	}

	// @Test(groups = { "integration" })
	public void testCloneTestCase() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final Integer testCaseId = 45;
		final TestCase clonedTestCase = testCaseService.cloneTestCase(testCaseId);
		Assert.assertTrue(clonedTestCase != null);
	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// StaleObjectStateException.class })
	public void testSaveTestCaseVersion() throws Exception
	{
		// saveTestCaseVersion(final User auth_, final TestCaseVersion
		// testCaseVersion_, final VersionIncrement versionIncrement_)
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer testCaseVersionId = 1;
		final TestCaseVersion testCaseVersion = testCaseService.getTestCaseVersion(testCaseVersionId);
		final TestCaseVersion clonedTestCase = testCaseService.saveTestCaseVersion(testCaseVersion.getId(), testCaseVersion.getDescription(), false, null, testCaseVersion
				.getVersion(), VersionIncrement.BOTH);
		Assert.assertTrue(clonedTestCase != null);
	}

	// @Test(groups = { "integration" })
	public void testActivateAndApproveTestCaseVersion() throws Exception
	{
		// saveTestCaseVersion(final User auth_, final TestCaseVersion
		// testCaseVersion_, final VersionIncrement versionIncrement_)
		final User user = userService.getUser(1);
		loginUser(user);

		final Integer id = 1;
		final TestCaseVersion testCaseVersion = testCaseService.getTestCaseVersion(id);
		testCaseService.approveTestCaseVersion(testCaseVersion.getId(), testCaseVersion.getVersion());
		testCaseService.activateTestCaseVersion(testCaseVersion.getId(), testCaseVersion.getVersion());
	}

}
