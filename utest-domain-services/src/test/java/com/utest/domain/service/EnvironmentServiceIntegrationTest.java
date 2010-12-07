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
import org.testng.annotations.Test;

import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentType;
import com.utest.domain.Locale;
import com.utest.domain.User;
import com.utest.domain.util.DomainUtil;
import com.utest.exception.DuplicateNameException;

public class EnvironmentServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private EnvironmentService	environmentService;
	@Autowired
	private UserService			userService;

	//@Test(groups = { "integration" })
	public void testAddEnvironmentType() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = 9;
		final EnvironmentType environmentType1 = environmentService.addEnvironmentType(companyId, null, "VMK type +++ no parent", false, Locale.DEFAULT_LOCALE);
		Assert.assertTrue(environmentType1 != null);
		final EnvironmentType environmentType2 = environmentService.addEnvironmentType(companyId, environmentType1.getId(), "VMK type +++ parent 1", false, Locale.DEFAULT_LOCALE);
		Assert.assertTrue(environmentType2 != null);

		// add locales
		environmentService.saveEnvironmentTypeLocale(environmentType1.getId(), "???????? ??????? ????", "ru_RU", 0);
		environmentService.saveEnvironmentTypeLocale(environmentType2.getId(), "???????? ??????? ???? - 2", "ru_RU", 0);

		environmentService.deleteEnvironmentType(environmentType2.getId());
		environmentService.deleteEnvironmentType(environmentType1.getId());
	}

	//@Test(groups = { "integration" }, expectedExceptions = { DuplicateNameException.class })
	public void testAddEnvironmentTypeDuplicateNameException() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = 9;
		environmentService.addEnvironmentType(companyId, null, "VMK type - no parent", false, Locale.DEFAULT_LOCALE);
		environmentService.addEnvironmentType(companyId, null, "VMK type - no parent", false, Locale.DEFAULT_LOCALE);
	}

	//@Test(groups = { "integration" })
	public void testAddEnvironment() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Environment environment1 = environmentService.addEnvironment(1, "VMK  - Environment 1");
		Assert.assertTrue(environment1 != null);
		final Environment environment2 = environmentService.addEnvironment(1, "VMK  - Environment 2");
		Assert.assertTrue(environment2 != null);

		// add locales
		environmentService.saveEnvironmentLocale(environment1.getId(), "???????? ??????? ????", "ru_RU", 0);
		environmentService.saveEnvironmentLocale(environment2.getId(), "???????? ??????? ???? - 2", "ru_RU", 0);

		// delete
		environmentService.deleteEnvironment(environment1.getId());
		environmentService.deleteEnvironment(environment2.getId());
	}

	//@Test(groups = { "integration" }, expectedExceptions = { DuplicateNameException.class })
	public void testAddEnvironmentDuplicateNameException() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		environmentService.addEnvironment(1, "VMK  - env 1");
		environmentService.addEnvironment(1, "VMK  - env 1");
	}

	//@Test(groups = { "integration" })
	public void testAddGeneratedGroups() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = 9;
		final List<Environment> allTypes = new ArrayList<Environment>();

		allTypes.add(environmentService.addEnvironment(1, "VMK  - Environment 1:1"));
		allTypes.add(environmentService.addEnvironment(1, "VMK  - Environment 1:2"));
		allTypes.add(environmentService.addEnvironment(72, "VMK  - Environment 2:3"));
		allTypes.add(environmentService.addEnvironment(72, "VMK  - Environment 2:4"));
		allTypes.add(environmentService.addEnvironment(73, "VMK  - Environment 3:5"));
		allTypes.add(environmentService.addEnvironment(73, "VMK  - Environment 3:6"));
		allTypes.add(environmentService.addEnvironment(74, "VMK  - Environment 4:7"));
		allTypes.add(environmentService.addEnvironment(74, "VMK  - Environment 4:8"));

		final List<EnvironmentGroup> outGroups = environmentService.addGeneratedEnvironmentGroups(companyId, DomainUtil.extractEntityIds(allTypes));
		Assert.assertTrue(outGroups != null);
		Assert.assertTrue(outGroups.size() == 2 * 2 * 2 * 2);

	}

	//@Test(groups = { "integration" })
	public void testSaveEnvironmentGroup() throws Exception
	{
		final Integer companyId = 9;
		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> environmentIds = new ArrayList<Integer>();
		environmentIds.add(1);
		environmentIds.add(2);
		environmentIds.add(3);
		EnvironmentGroup group = environmentService.addEnvironmentGroup(companyId, "VMK test group", "VMK description", environmentIds);
		environmentIds.clear();
		environmentIds.add(1);
		environmentIds.add(4);
		group = environmentService.saveEnvironmentsForGroup(group.getId(), environmentIds);
		environmentService.deleteEnvironmentGroup(group.getId());
	}
}
