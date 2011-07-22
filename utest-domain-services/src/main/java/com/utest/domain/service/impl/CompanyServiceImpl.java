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
import com.utest.domain.Attachment;
import com.utest.domain.Company;
import com.utest.domain.Country;
import com.utest.domain.EntityType;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.Product;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.AttachmentService;
import com.utest.domain.service.CompanyService;
import com.utest.domain.service.EnvironmentService;
import com.utest.exception.DeletingUsedEntityException;

public class CompanyServiceImpl extends BaseServiceImpl implements CompanyService
{
	private final AttachmentService	attachmentService;

	/**
	 * Default constructor
	 */
	public CompanyServiceImpl(final TypelessDAO dao, final EnvironmentService environmentService, final AttachmentService attachmentService)
	{
		super(dao, environmentService);
		this.attachmentService = attachmentService;
	}

	@Override
	public Company addCompany(final Integer countryId_, final String name_, final String address_, final String city_, final String zip_, final String url_, final String phone_)
			throws Exception
	{
		final Country country = getRequiredEntityById(Country.class, countryId_);
		checkForDuplicateName(Company.class, name_, null);

		final Company company = new Company();
		company.setCountryId(country.getId());
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
	public void deleteCompany(final Integer companyId_, final Integer originalVersionId_) throws Exception
	{
		Company company = getRequiredEntityById(Company.class, companyId_);
		// check for companys
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
		company.setVersion(originalVersionId_);
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
		final Company company = getRequiredEntityById(Company.class, companyId_);
		return company;

	}

	@Override
	public Company saveCompany(final Integer companyId_, final Integer countryId_, final String name_, final String address_, final String city_, final String zip_,
			final String url_, final String phone_, final Integer originalVersionId_) throws Exception
	{
		final Company company = getRequiredEntityById(Company.class, companyId_);
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
	}

	@Override
	public List<EnvironmentGroup> addGeneratedEnvironmentGroupsForCompany(final Integer companyId_, final List<Integer> environmentIds_, final Integer originalVersionId_)
			throws Exception
	{
		return addGeneratedEnvironmentGroupsForCompany(companyId_, null, environmentIds_, originalVersionId_);
	}

	@Override
	public List<EnvironmentGroup> addGeneratedEnvironmentGroupsForCompany(final Integer companyId_, final Integer environmentTypeId_, final List<Integer> environmentIds_,
			final Integer originalVersionId_) throws Exception
	{
		getRequiredEntityById(Company.class, companyId_);
		final List<EnvironmentGroup> groups = environmentService.addGeneratedEnvironmentGroups(companyId_, environmentTypeId_, environmentIds_);
		return groups;
	}

	@Override
	public Attachment addAttachmentForCompany(final String name, final String description, final String url, final Double size, final Integer companyId,
			final Integer attachmentTypeId) throws Exception
	{
		return attachmentService.addAttachment(name, description, url, size, EntityType.COMPANY, companyId, attachmentTypeId);
	}

	@Override
	public List<Attachment> getAttachmentsForCompany(final Integer companyId_) throws Exception
	{
		return attachmentService.getAttachmentsForEntity(companyId_, EntityType.COMPANY);
	}

}
