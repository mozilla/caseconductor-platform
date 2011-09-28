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
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.User;
import com.utest.domain.util.DomainUtil;

public class ProductServiceIntegrationTest extends BaseDomainServiceIntegrationTest
{
	@Autowired
	private ProductService		productService;
	@Autowired
	private UserService			userService;
	@Autowired
	private EnvironmentService	environmentService;

	// @Test(groups = { "integration" })
	public void testAddProduct() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = 9;
		final Product product0 = productService.getProduct(1);
		Assert.assertTrue(product0 != null);
		final Product product1 = productService.addProduct(companyId, "VMK Web product - windows", "windows test");
		Assert.assertTrue(product1 != null);
		productService.deleteProduct(product1.getId(), product1.getVersion());
		final Product product2 = productService.addProduct(companyId, "VMK Web product - Linux", "linux test");
		Assert.assertTrue(product2 != null);
		productService.deleteProduct(product2.getId(), product2.getVersion());
	}

	// @Test(groups = { "integration" })
	public void testSaveProductEnvironmentGroups() throws Exception
	{

		final User user = userService.getUser(1);
		loginUser(user);
		final List<Integer> environmentGroupsIds = new ArrayList<Integer>();
		environmentGroupsIds.add(51);
		environmentGroupsIds.add(52);
		environmentGroupsIds.add(53);
		final Product product = productService.getProduct(1);// testCase11.getLatestVersion().getId()
		productService.saveEnvironmentGroupsForProduct(product.getId(), environmentGroupsIds, product.getVersion());
		Assert.assertTrue(true);

	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// IllegalArgumentException.class })
	public void testAddProductCompanyNotFoundNameException() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = -123;
		final Product product1 = productService.addProduct(companyId, "VMK Web product - unonown company", "windows test");
		productService.deleteProduct(product1.getId(), product1.getVersion());
	}

	// @Test(groups = { "integration" })
	public void testAddProductComponent() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = 9;
		final Product product1 = productService.addProduct(companyId, "VMK Web product - windows", "windows test");
		final ProductComponent component1 = productService.addProductComponent(product1.getId(), "Component1", "Component for product.");
		Assert.assertTrue(component1 != null);
		final ProductComponent component2 = productService.addProductComponent(product1.getId(), "Component2", "Component for product.");
		Assert.assertTrue(component2 != null);
		final ProductComponent component3 = productService.addProductComponent(product1.getId(), "Component3", "Component for product.");
		Assert.assertTrue(component3 != null);
		final ProductComponent component4 = productService.addProductComponent(product1.getId(), "Component4", "Component for product.");
		Assert.assertTrue(component4 != null);
		productService.deleteProductComponent(component1.getId(), component1.getVersion());
		productService.deleteProductComponent(component2.getId(), component2.getVersion());
		productService.deleteProductComponent(component3.getId(), component3.getVersion());
		productService.deleteProductComponent(component4.getId(), component4.getVersion());
	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// DuplicateNameException.class })
	public void testAddProductDuplicateNameException() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer companyId = 9;
		final Product product1 = productService.addProduct(companyId, "VMK Web product - windows", "windows test");
		Assert.assertTrue(product1 != null);
		productService.addProduct(companyId, "VMK Web product - windows", "windows test");
	}

	// @Test(groups = { "integration" }, expectedExceptions = {
	// DuplicateNameException.class })
	public void testAddProductComponentsDuplicateNameException() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer productId = 1;
		final ProductComponent component1 = productService.addProductComponent(productId, "VMK Web product - windows", "windows test");
		Assert.assertTrue(component1 != null);
		productService.addProductComponent(productId, "VMK Web product - windows", "windows test");
	}

	// @Test(groups = { "integration" })
	public void testAddGeneratedGroups() throws Exception
	{
		final User user = userService.getUser(1);
		loginUser(user);
		final Integer productId = 1;
		final Integer companyId = 9;

		final EnvironmentType environmentType1 = environmentService.addEnvironmentType(companyId, null, "VMK type - no parent", false, Locale.DEFAULT_LOCALE);
		Assert.assertTrue(environmentType1 != null);
		final EnvironmentType environmentType2 = environmentService.addEnvironmentType(companyId, environmentType1.getId(), "VMK type - parent 1", false, Locale.DEFAULT_LOCALE);
		Assert.assertTrue(environmentType2 != null);

		final List<Environment> allTypes = new ArrayList<Environment>();
		allTypes.add(environmentService.addEnvironment(1, "VMK  - Environment 1:1"));
		allTypes.add(environmentService.addEnvironment(1, "VMK  - Environment 1:2"));
		allTypes.add(environmentService.addEnvironment(2, "VMK  - Environment 2:3"));
		allTypes.add(environmentService.addEnvironment(2, "VMK  - Environment 2:4"));
		allTypes.add(environmentService.addEnvironment(3, "VMK  - Environment 3:5"));
		allTypes.add(environmentService.addEnvironment(3, "VMK  - Environment 3:6"));
		allTypes.add(environmentService.addEnvironment(4, "VMK  - Environment 4:7"));
		allTypes.add(environmentService.addEnvironment(4, "VMK  - Environment 4:8"));

		final List<EnvironmentGroup> outGroups = productService.addGeneratedEnvironmentGroupsForProduct(productId, DomainUtil.extractEntityIds(allTypes), 1);
		Assert.assertTrue(outGroups != null);
		Assert.assertTrue(outGroups.size() == 2 * 2 * 2 * 2);

	}

}
