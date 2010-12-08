package com.utest.webservice.impl.v2;

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

import com.utest.domain.Product;
import com.utest.domain.Permission;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.ProductService;
import com.utest.webservice.api.v2.ProductWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.ProductInfo;
import com.utest.webservice.model.v2.ProductResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/products/")
public class ProductWebServiceImpl
extends BaseWebServiceImpl
implements ProductWebService
{
	private ProductService	productService;

	public ProductWebServiceImpl(ObjectBuilderFactory objectBuildFactory, ProductService productService)
	{
		super(objectBuildFactory);
		this.productService = productService;
	}


	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductInfo createProduct(@FormParam("") final ProductInfo productInfo) throws Exception 
	{
		final Product product = productService.addProduct(
				productInfo.getCompanyId(), productInfo.getName(), 
				productInfo.getDescription());
		return objectBuilderFactory.toInfo(ProductInfo.class, product, null);
	}

	@DELETE
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public void deleteProduct(@Context final UriInfo ui_, 
			@PathParam("id") final Integer productId) throws Exception 
	{
		productService.deleteProduct(productId);
	}

	@PUT
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.PRODUCT_EDIT })
	public ProductInfo updateProduct(@Context final UriInfo ui_, 
			@PathParam("id") final Integer productId, 
			@FormParam("") final ProductInfo productInfo) throws Exception
	{
		Product product = productService.saveProduct(
				productId, productInfo.getName(), productInfo.getDescription(), 
				productInfo.getResourceIdentity().getVersion());
		return objectBuilderFactory.toInfo(ProductInfo.class, product, ui_.getAbsolutePathBuilder().path("/{id}"));
	}
	
	@GET
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductInfo getProduct(@Context final UriInfo ui_, 
			@PathParam("id") final Integer productId) throws Exception 
	{
		Product product = productService.getProduct(productId);
		return objectBuilderFactory.toInfo(ProductInfo.class, product, null);
	}
	
	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.PRODUCT_VIEW)
	public ProductResultInfo findProducts(@Context final UriInfo ui_, 
			@QueryParam("") final UtestSearchRequest request) throws Exception
	{
		UtestSearch search = objectBuilderFactory.createSearch(ProductInfo.class, request, ui_);
		UtestSearchResult result = productService.findProducts(search);
		return (ProductResultInfo) objectBuilderFactory.createResult(
				ProductInfo.class, Product.class, request, result, 
				ui_.getAbsolutePathBuilder().path(this.getClass(),
				"getProduct"));
	}

}
