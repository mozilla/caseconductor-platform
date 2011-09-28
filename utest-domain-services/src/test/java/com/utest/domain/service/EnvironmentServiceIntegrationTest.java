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

import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentType;
import com.utest.domain.Locale;
import com.utest.domain.User;
import com.utest.domain.util.DomainUtil;

public class EnvironmentServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private EnvironmentService	environmentService;
	@Autowired
	private UserService			userService;

	// @Test(groups = { "integration" })
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

		environmentService.deleteEnvironmentType(environmentType2.getId(), environmentType2.getVersion());
		environmentService.deleteEnvironmentType(environmentType1.getId(), environmentType1.getVersion());
	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// DuplicateNameException.class })
	public void testAddEnvironmentTypeDuplicateNameException() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = 9;
		environmentService.addEnvironmentType(companyId, null, "VMK type - no parent", false, Locale.DEFAULT_LOCALE);
		environmentService.addEnvironmentType(companyId, null, "VMK type - no parent", false, Locale.DEFAULT_LOCALE);
	}

	// @Test(groups = { "integration" })
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
		environmentService.deleteEnvironment(environment1.getId(), environment1.getVersion());
		environmentService.deleteEnvironment(environment2.getId(), environment2.getVersion());
	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// DuplicateNameException.class })
	public void testAddEnvironmentDuplicateNameException() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		environmentService.addEnvironment(1, "VMK  - env 1");
		environmentService.addEnvironment(1, "VMK  - env 1");
	}

	// @Test(groups = { "integration" })
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

	// @Test(groups = { "integration" })
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
		environmentService.deleteEnvironmentGroup(group.getId(), group.getVersion());
	}
}
