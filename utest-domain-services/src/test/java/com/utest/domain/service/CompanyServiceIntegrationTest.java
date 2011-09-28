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

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import com.utest.domain.Company;
import com.utest.domain.User;

public class CompanyServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private CompanyService	companyService;
	@Autowired
	private UserService		userService;

	// @Test(groups = { "integration" })
	public void testAddCompany() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer countryId = 211;
		final Company company0 = companyService.getCompany(9);
		Assert.assertTrue(company0 != null);
		final Company company1 = companyService.addCompany(countryId, "VMK new company", "100 main street", "weston", "02493", "vmk.com", "617.281.0001");
		Assert.assertTrue(company1 != null);
	}

	// @Test(groups = { "integration" })
	public void testDeleteCompany() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer originalVersionId = 0;
		final Integer companyId = 14;
		companyService.deleteCompany(companyId, originalVersionId);
	}

	// @Test(groups = { "integration" })
	public void testSaveCompany() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer countryId = 213;
		final Integer companyId = 9;
		final Company company1 = companyService.saveCompany(companyId, countryId, "VMK new company - updated", "100 main street", "weston", "02493", "vmk.com", "617.281.0001", 1);
		Assert.assertTrue(company1 != null);
	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// DuplicateNameException.class })
	public void testAddCompanyDuplicateNameException() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer countryId = 211;
		final Company company0 = companyService.getCompany(9);
		Assert.assertTrue(company0 != null);
		final Company company1 = companyService.addCompany(countryId, "VMK new company 1", "100 main street", "weston", "02493", "vmk.com", "617.281.0001");
		Assert.assertTrue(company1 != null);
		companyService.addCompany(countryId, "VMK new company 1", "100 main street", "weston", "02493", "vmk.com", "617.281.0001");
	}
}
