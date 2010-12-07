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

import java.util.List;

import com.trg.search.Search;
import com.utest.dao.TypelessDAO;
import com.utest.domain.Company;
import com.utest.domain.Country;
import com.utest.domain.Product;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.CompanyService;
import com.utest.exception.DeletingUsedEntityException;
import com.utest.exception.NotFoundException;

public class CompanyServiceImpl extends BaseServiceImpl implements CompanyService
{
	private final TypelessDAO	dao;

	/**
	 * Default constructor
	 */
	public CompanyServiceImpl(final TypelessDAO dao)
	{
		super(dao);
		this.dao = dao;
	}

	@Override
	public Company addCompany(final Integer countryId_, final String name_, final String address_, final String city_, final String zip_, final String url_, final String phone_)
			throws Exception
	{
		final Country country = dao.getById(Country.class, countryId_);
		if (country == null)
		{
			throw new NotFoundException("Country not found: " + countryId_);
		}
		// check for duplicate name
		checkForDuplicateName(Company.class, name_, null);

		final Company company = new Company();
		company.setCountryId(countryId_);
		company.setName(name_);
		company.setAddress(address_);
		company.setCity(city_);
		company.setZip(zip_);
		company.setUrl(url_);
		company.setPhone(phone_);
		final Integer companyId = dao.addAndReturnId(company);
		return getCompany(companyId);
	}

	@Override
	public void deleteCompany(final Integer companyId_) throws Exception
	{
		Company company = dao.getById(Company.class, companyId_);
		if (company == null)
		{
			throw new NotFoundException("Company not found. Id: " + companyId_);
		}
		// check for products
		Search search = new Search(Product.class);
		search.addFilterEqual("companyId", companyId_);
		List<?> foundEntities = dao.search(Product.class, search);
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			throw new DeletingUsedEntityException("Products reference this company : " + companyId_);
		}
		// check for internal users
		search = new Search(User.class);
		search.addFilterEqual("companyId", companyId_);
		foundEntities = dao.search(User.class, search);
		if ((foundEntities != null) && !foundEntities.isEmpty())
		{
			throw new DeletingUsedEntityException("Users reference this company : " + companyId_);
		}
		// delete company
		dao.delete(company);
	}

	@Override
	public UtestSearchResult findCompanies(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(Company.class, search_);
	}

	@Override
	public Company getCompany(final Integer companyId_) throws Exception
	{
		final Company company = dao.getById(Company.class, companyId_);
		if (company == null)
		{
			throw new NotFoundException("Company not found: " + companyId_);
		}
		return company;

	}

	@Override
	public Company saveCompany(final Integer companyId_, final Integer countryId_, final String name_, final String address_, final String city_, final String zip_,
			final String url_, final String phone_, final Integer originalVersionId_) throws Exception
	{
		final Company company = dao.getById(Company.class, companyId_);
		if (company == null)
		{
			throw new NotFoundException("Company not found:" + companyId_);
		}
		// check for duplicate name
		checkForDuplicateName(Company.class, name_, companyId_);

		company.setVersion(originalVersionId_);
		company.setCountryId(countryId_);
		company.setName(name_);
		company.setAddress(address_);
		company.setCity(city_);
		company.setZip(zip_);
		company.setUrl(url_);
		company.setPhone(phone_);
		return dao.merge(company);
		// dao.update(company);
		// return dao.getById(Company.class, companyId_);
	}

}
