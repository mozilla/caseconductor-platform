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

import com.utest.dao.TypelessDAO;
import com.utest.domain.TestCycle;
import com.utest.domain.User;

public class TestCycleServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private TestCycleService		testCycleService;
	@Autowired
	private UserService			userService;
	@Autowired
	private EnvironmentService	environmentService;

	@Autowired
	private TypelessDAO				dao;

	//@Test(groups = { "integration" })
	public void testAddTestCycle() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer productId = 1;
		final TestCycle testCycle11 = testCycleService.addTestCycle(productId, "Test Cycle for product 1", "Test cycle for windows test calculator", new Date(), null, true, true);
		Assert.assertTrue(testCycle11 != null);
	}

	//@Test(groups = { "integration" })
	public void testSaveTestCycleValidEnvironmentGroups() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> environmentGroupsIds = new ArrayList<Integer>();
		environmentGroupsIds.add(35);
		environmentGroupsIds.add(36);
		environmentGroupsIds.add(37);

		testCycleService.saveEnvironmentGroupsForTestCycle(3, environmentGroupsIds, 1);
		Assert.assertTrue(true);

	}

	//@Test(groups = { "integration" })
	public void testActivateTestCycle() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		Assert.assertTrue(testCycleService.activateTestCycle(3, 1) != null);

	}

	// //@Test(groups = { "integration" })
	// public void testNamedQuery() throws Exception
	// {
	//
	// final String namedQuery = "UnreadMessagesForTM";
	// final int numParams = 1;
	// final String[] paramNames = new String[numParams];
	// final Object[] values = new Object[numParams];
	// paramNames[0] = "tmId";
	// values[0] = 4148;
	//
	// final List<BugMessage> messages = (List<BugMessage>)
	// dao.findByNamedQueryAndNamedParam(namedQuery, paramNames, values, false,
	// false);
	// messages.size();
	// }

}
