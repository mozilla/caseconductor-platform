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
package com.utest.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.Company;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.TestCase;
import com.utest.domain.TestCaseProductComponent;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.ProductService;
import com.utest.domain.util.DomainUtil;
import com.utest.exception.DeletingUsedEntityException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

public class ProductServiceImpl extends BaseServiceImpl implements ProductService
{
	private final TypelessDAO			dao;
	private final EnvironmentService	environmentService;

	/**
	 * Default constructor
	 */
	public ProductServiceImpl(final TypelessDAO dao, final EnvironmentService environmentService)
	{
		super(dao);
		this.dao = dao;
		this.environmentService = environmentService;
	}

	@Override
	public Product addProduct(final Integer companyId_, final String name_, final String description_) throws Exception
	{
		final Company company = getRequiredEntityById(Company.class, companyId_);
		checkForDuplicateNameWithinParent(Product.class, name_, companyId_, "companyId", null);

		final Product product = new Product();
		product.setCompanyId(company.getId());
		product.setName(name_);
		product.setDescription(description_);

		final Integer productId = dao.addAndReturnId(product);
		return getProduct(productId);
	}

	@Override
	public ProductComponent addProductComponent(final Integer productId_, final String name_, final String description_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		checkForDuplicateNameWithinParent(ProductComponent.class, name_, productId_, "productId", null);

		final ProductComponent productComponent = new ProductComponent();
		productComponent.setProductId(product.getId());
		productComponent.setName(name_);
		productComponent.setDescription(description_);

		final Integer productComponentId = dao.addAndReturnId(productComponent);
		return getProductComponent(productComponentId);
	}

	@Override
	public List<EnvironmentGroup> addGeneratedEnvironmentGroupsForProduct(final Integer productId_, final List<Integer> environmentIds_, final Integer originalVersionId_)
			throws Exception
	{
		return addGeneratedEnvironmentGroupsForProduct(productId_, null, environmentIds_, originalVersionId_);
	}

	@Override
	public List<EnvironmentGroup> addGeneratedEnvironmentGroupsForProduct(final Integer productId_, final Integer environmentTypeId_, final List<Integer> environmentIds_,
			final Integer originalVersionId_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		final List<EnvironmentGroup> groups = environmentService.addGeneratedEnvironmentGroups(product.getCompanyId(), environmentTypeId_, environmentIds_);
		saveEnvironmentGroupsForProduct(productId_, DomainUtil.extractEntityIds(groups), originalVersionId_);
		return groups;
	}

	@Override
	public List<EnvironmentGroup> getEnvironmentGroupsForProduct(final Integer productId_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		if (product.getEnvironmentProfileId() != null)
		{
			return environmentService.getEnvironmentGroupsForProfile(product.getEnvironmentProfileId());
		}
		else
		{
			return new ArrayList<EnvironmentGroup>();
		}
	}

	@Override
	public void saveEnvironmentGroupsForProduct(final Integer productId_, final List<Integer> environmentGroupIds_, final Integer originalVersionId_)
			throws UnsupportedEnvironmentSelectionException, Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		// check that groups are selected from system wide groups or from the
		// groups defined by this company
		if (!isValidSelectionForCompany(product.getCompanyId(), environmentGroupIds_, EnvironmentGroup.class))
		{
			throw new UnsupportedEnvironmentSelectionException();
		}
		// update environment profile
		if (product.getEnvironmentProfileId() != null)
		{
			environmentService.saveEnvironmentGroupsForProfile(product.getEnvironmentProfileId(), environmentGroupIds_);
		}
		else
		{
			final EnvironmentProfile environmentProfile = environmentService.addEnvironmentProfile(product.getCompanyId(), "Created for product : " + productId_,
					"Included groups: " + environmentGroupIds_.toString(), environmentGroupIds_);
			product.setEnvironmentProfileId(environmentProfile.getId());
		}
		// update product
		product.setVersion(originalVersionId_);
		dao.merge(product);
	}

	@Override
	public void deleteProduct(final Integer productId_, final Integer originalVersionId_) throws Exception
	{
		Product product = getRequiredEntityById(Product.class, productId_);
		final Search search = new Search(TestCase.class);
		search.addFilterEqual("productId", productId_);
		final List<TestCase> foundTestCases = dao.search(TestCase.class, search);
		if ((foundTestCases != null) && !foundTestCases.isEmpty())
		{
			throw new DeletingUsedEntityException(Product.class.getSimpleName() + " : " + productId_);
		}
		// delete all product components
		final List<ProductComponent> components = getComponentsForProduct(productId_);
		dao.delete(components);
		// delete product
		product.setVersion(originalVersionId_);
		dao.delete(product);
	}

	@Override
	public void deleteProductComponent(final Integer productComponentId_, final Integer originalVersionId_) throws Exception
	{
		ProductComponent productComponent = getRequiredEntityById(ProductComponent.class, productComponentId_);
		final Search search = new Search(TestCaseProductComponent.class);
		search.addFilterEqual("productComponentId", productComponentId_);
		final List<TestCaseProductComponent> foundEntities = dao.search(TestCaseProductComponent.class, search);
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			throw new DeletingUsedEntityException(ProductComponent.class.getSimpleName() + " : " + productComponentId_);
		}
		// delete product component
		productComponent.setVersion(originalVersionId_);
		dao.delete(productComponent);
	}

	@Override
	public UtestSearchResult findProducts(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(Product.class, search_);
	}

	@Override
	public UtestSearchResult findProductComponents(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(ProductComponent.class, search_);
	}

	@Override
	public Product getProduct(final Integer productId_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		return product;

	}

	@Override
	public ProductComponent getProductComponent(final Integer productComponentId_) throws Exception
	{
		final ProductComponent productComponent = getRequiredEntityById(ProductComponent.class, productComponentId_);
		return productComponent;
	}

	@Override
	public List<ProductComponent> getComponentsForProduct(final Integer productId_) throws Exception
	{
		final Search search = new Search(ProductComponent.class);
		search.addFilterEqual("productId", productId_);
		return dao.search(ProductComponent.class, search);
	}

	@Override
	public Product saveProduct(final Integer productId_, final String name_, final String description_, final Integer originalVersionId_) throws Exception
	{
		final Product product = getRequiredEntityById(Product.class, productId_);
		checkForDuplicateNameWithinParent(Product.class, name_, product.getCompanyId(), "companyId", productId_);

		product.setName(name_);
		product.setDescription(description_);
		product.setVersion(originalVersionId_);
		return dao.merge(product);
	}

	@Override
	public ProductComponent saveProductComponent(final Integer productComponentId_, final String name_, final String description_, final Integer originalVersionId_)
			throws Exception
	{
		final ProductComponent productComponent = getRequiredEntityById(ProductComponent.class, productComponentId_);
		checkForDuplicateNameWithinParent(ProductComponent.class, name_, productComponent.getProductId(), "productId", productComponentId_);

		productComponent.setName(name_);
		productComponent.setDescription(description_);
		productComponent.setVersion(originalVersionId_);
		return dao.merge(productComponent);
	}

}
