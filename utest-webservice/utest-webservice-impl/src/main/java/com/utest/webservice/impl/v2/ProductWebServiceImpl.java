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
package com.utest.webservice.impl.v2;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.security.access.annotation.Secured;

import com.utest.domain.EnvironmentGroup;
import com.utest.domain.Permission;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.ProductService;
import com.utest.webservice.api.v2.ProductWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.ProductComponentSearchResultInfo;
import com.utest.webservice.model.v2.ProductInfo;
import com.utest.webservice.model.v2.ProductSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/products/")
public class ProductWebServiceImpl extends BaseWebServiceImpl implements ProductWebService
{
	private final ProductService	productService;

	public ProductWebServiceImpl(ObjectBuilderFactory objectBuildFactory, ProductService productService)
	{
		super(objectBuildFactory);
		this.productService = productService;
	}

	@POST
	@Path("/{id}/components/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductComponentInfo createProductComponent(@Context final UriInfo ui_, @PathParam("id") final Integer productId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_) throws Exception
	{
		final ProductComponent productComponent = productService.addProductComponent(productId_, name_, description_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, productComponent, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductInfo createProduct(@Context final UriInfo ui_, @FormParam("companyId") final Integer companyId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_) throws Exception
	{
		final Product product = productService.addProduct(companyId_, name_, description_);
		return objectBuilderFactory.toInfo(ProductInfo.class, product, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean deleteProduct(@Context final UriInfo ui_, @PathParam("id") final Integer productId, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		productService.deleteProduct(productId, originalVersionId_);

		return Boolean.TRUE;
	}

	@DELETE
	@Path("/components/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean deleteProductComponent(@Context final UriInfo ui_, @PathParam("id") final Integer productComponentId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		productService.deleteProductComponent(productComponentId_, originalVersionId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductInfo updateProduct(@Context final UriInfo ui_, @PathParam("id") final Integer productId, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		Product product = productService.saveProduct(productId, name_, description_, originalVersionId_);
		return objectBuilderFactory.toInfo(ProductInfo.class, product, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/components/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductComponentInfo updateProductComponent(@Context final UriInfo ui_, @PathParam("id") final Integer productComponentId_, @FormParam("name") final String name_,
			@FormParam("description") final String description_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		ProductComponent productComponent = productService.saveProductComponent(productComponentId_, name_, description_, originalVersionId_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, productComponent, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductInfo getProduct(@Context final UriInfo ui_, @PathParam("id") final Integer productId) throws Exception
	{
		Product product = productService.getProduct(productId);
		return objectBuilderFactory.toInfo(ProductInfo.class, product, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/components/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductComponentInfo getProductComponent(@Context final UriInfo ui_, @PathParam("id") final Integer productComponentId_) throws Exception
	{
		ProductComponent productComponent = productService.getProductComponent(productComponentId_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, productComponent, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductSearchResultInfo findProducts(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		UtestSearch search = objectBuilderFactory.createSearch(ProductInfo.class, request, ui_);
		UtestSearchResult result = productService.findProducts(search);
		return (ProductSearchResultInfo) objectBuilderFactory.createResult(ProductInfo.class, Product.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/components/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductComponentSearchResultInfo findProductComponents(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		UtestSearch search = objectBuilderFactory.createSearch(ProductComponentInfo.class, request, ui_);
		UtestSearchResult result = productService.findProductComponents(search);
		return (ProductComponentSearchResultInfo) objectBuilderFactory.createResult(ProductComponentInfo.class, ProductComponent.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/components/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	/**
	 * Returns all product components of a test case
	 */
	public List<ProductComponentInfo> getProductComponents(@Context final UriInfo ui_, @PathParam("id") final Integer productId_) throws Exception
	{
		final List<ProductComponent> components = productService.getComponentsForProduct(productId_);
		return objectBuilderFactory.toInfo(ProductComponentInfo.class, components, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	/**
	 * Returns all versions of a test case
	 */
	public List<EnvironmentGroupInfo> getProducEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer productId_) throws Exception
	{
		final List<EnvironmentGroup> groups = productService.getEnvironmentGroupsForProduct(productId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, groups, ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/{id}/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public Boolean updateProductEnvironmentGroups(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@FormParam("environmentGroupIds") final ArrayList<Integer> environmentGroupIds_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		productService.saveEnvironmentGroupsForProduct(productId_, environmentGroupIds_, originalVesionId_);
		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/environmentgroups/autogenerate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@FormParam("environmentIds") final ArrayList<Integer> environmentIds_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		List<EnvironmentGroup> environmentGroups = productService.addGeneratedEnvironmentGroupsForProduct(productId_, environmentIds_, originalVesionId_);
		final List<EnvironmentGroupInfo> environmentGroupsInfo = objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroups, ui_.getBaseUriBuilder());
		return environmentGroupsInfo;
	}

	@PUT
	@Path("/{id}/environmentgroups/environmenttypes/{typeId}/autogenerate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer productId_,
			@PathParam("typeId") final Integer environmentTypeId_, @FormParam("environmentIds") final ArrayList<Integer> environmentIds_,
			@FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		List<EnvironmentGroup> environmentGroups = productService.addGeneratedEnvironmentGroupsForProduct(productId_, environmentTypeId_, environmentIds_, originalVesionId_);
		final List<EnvironmentGroupInfo> environmentGroupsInfo = objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroups, ui_.getBaseUriBuilder());
		return environmentGroupsInfo;
	}

}
