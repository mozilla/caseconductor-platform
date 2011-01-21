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
 * @author Greg Zheng
 *
 * copyright 2010 by uTest
 */
package com.utest.webservice.api.v2;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.ProductComponentSearchResultInfo;
import com.utest.webservice.model.v2.ProductInfo;
import com.utest.webservice.model.v2.ProductSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface ProductWebService
{
	Boolean deleteProduct(UriInfo ui, Integer productId) throws Exception;

	ProductInfo updateProduct(UriInfo ui, Integer productId, ProductInfo productInfo) throws Exception;

	ProductInfo getProduct(UriInfo ui, Integer productId) throws Exception;

	ProductSearchResultInfo findProducts(UriInfo ui, UtestSearchRequest request) throws Exception;

	ProductInfo createProduct(UriInfo ui, ProductInfo productInfo) throws Exception;

	ProductComponentInfo createProductComponent(UriInfo ui, Integer productId, ProductComponentInfo productComponentInfo) throws Exception;

	Boolean deleteProductComponent(UriInfo ui, Integer productComponentId) throws Exception;

	ProductComponentInfo updateProductComponent(UriInfo ui, Integer productComponentId, ProductComponentInfo productComponentInfo) throws Exception;

	ProductComponentInfo getProductComponent(UriInfo ui, Integer productComponentId) throws Exception;

	ProductComponentSearchResultInfo findProductComponents(UriInfo ui, UtestSearchRequest request) throws Exception;

	List<ProductComponentInfo> getProductComponents(UriInfo ui, Integer productId) throws Exception;

	List<EnvironmentGroupInfo> getProducEnvironmentGroups(UriInfo ui, Integer productId) throws Exception;

	Boolean updateProductEnvironmentGroups(UriInfo ui, Integer productId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(UriInfo ui, Integer productId, ArrayList<Integer> environmentIds, Integer originalVesionId)
			throws Exception;

	List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(UriInfo ui, Integer productId, Integer environmentTypeId, ArrayList<Integer> environmentIds,
			Integer originalVesionId) throws Exception;

}
