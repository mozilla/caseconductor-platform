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

import java.util.List;

import com.utest.domain.EnvironmentGroup;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to Product.
 */
public interface ProductService
{
	Product addProduct(Integer companyId, String name, String description) throws Exception;

	Product getProduct(Integer productId) throws Exception;

	List<ProductComponent> findComponentsForProduct(Integer productId) throws Exception;

	void deleteProduct(Integer productId) throws Exception;

	UtestSearchResult findProducts(UtestSearch search) throws Exception;

	UtestSearchResult findProductComponents(UtestSearch search) throws Exception;

	ProductComponent getProductComponent(Integer productComponentId) throws Exception;

	ProductComponent addProductComponent(Integer productId, String name, String description) throws Exception;

	void deleteProductComponent(Integer productComponentId) throws Exception;

	List<EnvironmentGroup> findEnvironmentGroupsForProduct(Integer productId) throws Exception;

	void saveEnvironmentGroupsForProduct(Integer productId, List<Integer> environmentGroupIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	Product saveProduct(Integer productId, String name, String description, Integer originalVersionId) throws Exception;

	ProductComponent saveProductComponent(Integer productComponentId, String name, String description, Integer originalVersionId) throws Exception;

	List<EnvironmentGroup> addGeneratedEnvironmentGroupsForProduct(Integer productId, List<Integer> environmentIds, Integer originalVersionId) throws Exception;
}
