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

import com.utest.domain.Company;
import com.utest.domain.Permission;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.CompanyService;
import com.utest.domain.service.UserService;
import com.utest.webservice.api.v2.CompanyWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.CompanyInfo;
import com.utest.webservice.model.v2.CompanyResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/companies/")
public class CompanyWebServiceImpl extends BaseWebServiceImpl implements CompanyWebService
{
	private final UserService	userService;
	private final CompanyService	companyService;

	public CompanyWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final UserService userService, final CompanyService companyService)
	{
		super(objectBuildFactory);
		this.userService = userService;
		this.companyService = companyService;
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT })
	public CompanyInfo createCompany(@Context final UriInfo ui_, @FormParam("") final CompanyInfo companyInfo) throws Exception
	{
		final Company company = companyService.addCompany(companyInfo.getCountryId(), companyInfo.getName(), companyInfo.getAddress(), companyInfo.getCity(), companyInfo.getZip(),
				companyInfo.getUrl(), companyInfo.getPhone());

		// TODO - create default TM if needed ?
		// ui_.getAbsolutePathBuilder().path(this.getClass(), "getCompany")
		return objectBuilderFactory.toInfo(CompanyInfo.class, company, ui_.getAbsolutePathBuilder().path("/{id}"));
	}

	@DELETE
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT })
	public void deleteCompany(@Context final UriInfo ui_, @PathParam("id") final Integer companyId_) throws Exception
	{
		companyService.deleteCompany(companyId_);
	}

	@PUT
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT })
	public CompanyInfo updateCompany(@Context final UriInfo ui_, @PathParam("id") final Integer companyId, @FormParam("") final CompanyInfo companyInfo) throws Exception
	{
		final Company company = companyService.saveCompany(companyId, companyInfo.getCountryId(), companyInfo.getName(), companyInfo.getAddress(), companyInfo.getCity(),
				companyInfo.getZip(), companyInfo.getUrl(), companyInfo.getPhone(), companyInfo.getResourceIdentity().getVersion());

		return objectBuilderFactory.toInfo(CompanyInfo.class, company, ui_.getAbsolutePathBuilder().path("/{id}"));
	}

	@GET
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.COMPANY_INFO_VIEW)
	public CompanyInfo getCompany(@Context final UriInfo ui_, @PathParam("id") final Integer companyId) throws Exception
	{
		final Company company = companyService.getCompany(companyId);

		return objectBuilderFactory.toInfo(CompanyInfo.class, company, ui_.getAbsolutePathBuilder().path(""));
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.COMPANY_INFO_VIEW)
	public CompanyResultInfo findCompanies(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(CompanyInfo.class, request, ui_);
		final UtestSearchResult result = companyService.findCompanies(search);

		return (CompanyResultInfo) objectBuilderFactory.createResult(CompanyInfo.class, Company.class, request, result, ui_.getAbsolutePathBuilder().path(this.getClass(),
				"getCompany"));
	}

}
