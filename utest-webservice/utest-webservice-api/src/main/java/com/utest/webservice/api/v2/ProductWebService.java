package com.utest.webservice.api.v2;

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.ProductInfo;
import com.utest.webservice.model.v2.ProductResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface ProductWebService
{
	ProductInfo createProduct(ProductInfo productInfo) throws Exception;

	void deleteProduct(UriInfo ui, Integer productId) throws Exception;

	ProductInfo updateProduct(UriInfo ui, Integer productId, ProductInfo productInfo) throws Exception;

	ProductInfo getProduct(UriInfo ui, Integer productId) throws Exception;

	ProductResultInfo findProducts(UriInfo ui, UtestSearchRequest request) throws Exception;

	// API for testcycles
	// GET  - products/{id}/testcycles/
//	ProductInfo getProduct(UriInfo ui, Integer productId) throws Exception;

	// API for testcases
	// GET  - products/{id}/testcases/
	
	// API for environments
	// POST - products/{id}/environments	
	// GET  - products/{id}/environments/	
	// DELETE - products/{id}/environments/{id}	
}
