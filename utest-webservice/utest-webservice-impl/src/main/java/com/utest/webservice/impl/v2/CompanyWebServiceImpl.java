/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
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

import com.utest.domain.Attachment;
import com.utest.domain.Company;
import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.Permission;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.CompanyService;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.service.UserService;
import com.utest.webservice.api.v2.CompanyWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.CompanyInfo;
import com.utest.webservice.model.v2.CompanySearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/companies/")
public class CompanyWebServiceImpl extends BaseWebServiceImpl implements CompanyWebService
{
	@SuppressWarnings("unused")
	private final UserService			userService;
	private final CompanyService		companyService;
	private final EnvironmentService	environmentService;

	public CompanyWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final UserService userService, final CompanyService companyService,
			final EnvironmentService environmentService)
	{
		super(objectBuildFactory);
		this.userService = userService;
		this.companyService = companyService;
		this.environmentService = environmentService;
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT })
	public CompanyInfo createCompany(@Context final UriInfo ui_, @FormParam("countryId") final Integer countryId_, @FormParam("name") final String name_,
			@FormParam("address") final String address_, @FormParam("city") final String city_, @FormParam("zip") final String zip_, @FormParam("url") final String url_,
			@FormParam("phone") final String phone_) throws Exception
	{
		final Company company = companyService.addCompany(countryId_, name_, address_, city_, zip_, url_, phone_);
		return objectBuilderFactory.toInfo(CompanyInfo.class, company, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT })
	public Boolean deleteCompany(@Context final UriInfo ui_, @PathParam("id") final Integer companyId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		companyService.deleteCompany(companyId_, originalVersionId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}/undo_delete/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT, Permission.DELETED_ENTITY_UNDO })
	public Boolean undeleteCompany(@Context final UriInfo ui_, @PathParam("id") final Integer companyId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		companyService.undoDeletedEntity(Company.class, companyId_);

		return Boolean.TRUE;
	}

	@PUT
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT })
	public CompanyInfo updateCompany(@Context final UriInfo ui_, @PathParam("id") final Integer companyId, @FormParam("countryId") final Integer countryId_,
			@FormParam("name") final String name_, @FormParam("address") final String address_, @FormParam("city") final String city_, @FormParam("zip") final String zip_,
			@FormParam("url") final String url_, @FormParam("phone") final String phone_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		final Company company = companyService.saveCompany(companyId, countryId_, name_, address_, city_, zip_, url_, phone_, originalVersionId_);

		return objectBuilderFactory.toInfo(CompanyInfo.class, company, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.COMPANY_INFO_VIEW)
	public CompanySearchResultInfo findCompanies(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(CompanyInfo.class, request, ui_);
		final UtestSearchResult result = companyService.findCompanies(search);

		return (CompanySearchResultInfo) objectBuilderFactory.createResult(CompanyInfo.class, Company.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.COMPANY_INFO_VIEW)
	@Path("/deleted/")
	public CompanySearchResultInfo findDeletedCompanies(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(CompanyInfo.class, request, ui_);
		final UtestSearchResult result = companyService.findDeletedEntities(Company.class, search);

		return (CompanySearchResultInfo) objectBuilderFactory.createResult(CompanyInfo.class, Company.class, request, result, ui_.getBaseUriBuilder());
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

		return objectBuilderFactory.toInfo(CompanyInfo.class, company, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.COMPANY_INFO_VIEW)
	public List<AttachmentInfo> getCompanyAttachments(@Context UriInfo ui_, @PathParam("id") final Integer companyId) throws Exception
	{
		final List<Attachment> attachments = companyService.getAttachmentsForCompany(companyId);
		final List<AttachmentInfo> attachmentsInfo = objectBuilderFactory.toInfo(AttachmentInfo.class, attachments, ui_.getBaseUriBuilder());
		return attachmentsInfo;
	}

	@POST
	@Path("/{id}/attachments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT })
	public AttachmentInfo createAttachment(@Context UriInfo ui, @PathParam("id") final Integer companyId, @FormParam("name") String name,
			@FormParam("description") String description, @FormParam("url") String url, @FormParam("size") Double size, @FormParam("attachmentTypeId") Integer attachmentTypeId)
			throws Exception
	{
		Attachment attachment = companyService.addAttachmentForCompany(name, description, url, size, companyId, attachmentTypeId);
		return objectBuilderFactory.toInfo(AttachmentInfo.class, attachment, ui.getBaseUriBuilder());
	}

	@DELETE
	@Path("/{id}/attachments/{attachmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.COMPANY_INFO_EDIT })
	public Boolean deleteAttachment(@Context UriInfo ui, @PathParam("id") final Integer companyId, @PathParam("attachmentId") final Integer attachmentId,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		return companyService.deleteAttachment(attachmentId, companyId);
	}

	@PUT
	@Path("/{id}/environmentgroups/autogenerate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer companyId_,
			@FormParam("environmentIds") final ArrayList<Integer> environmentIds_, @FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		List<EnvironmentGroup> environmentGroups = companyService.addGeneratedEnvironmentGroupsForCompany(companyId_, environmentIds_, originalVesionId_);
		final List<EnvironmentGroupInfo> environmentGroupsInfo = objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroups, ui_.getBaseUriBuilder());
		return environmentGroupsInfo;
	}

	@PUT
	@Path("/{id}/environmentgroups/environmenttypes/{environmentTypeId}/autogenerate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer companyId_,
			@PathParam("environmentTypeId") final Integer environmentTypeId_, @FormParam("environmentIds") final ArrayList<Integer> environmentIds_,
			@FormParam("originalVersionId") final Integer originalVesionId_) throws Exception
	{
		List<EnvironmentGroup> environmentGroups = companyService.addGeneratedEnvironmentGroupsForCompany(companyId_, environmentTypeId_, environmentIds_, originalVesionId_);
		final List<EnvironmentGroupInfo> environmentGroupsInfo = objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroups, ui_.getBaseUriBuilder());
		return environmentGroupsInfo;
	}

	@GET
	@Path("/{id}/parentchildenvironments/{parentEnvironmentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public List<EnvironmentInfo> getParentDependableEnvironments(@Context UriInfo ui_, @PathParam("id") final Integer companyId_,
			@PathParam("parentEnvironmentId") final Integer parentEnvironmentId_) throws Exception
	{
		final List<Environment> environments = environmentService.getParentDependableEnvironments(companyId_, parentEnvironmentId_);
		final List<EnvironmentInfo> environmentsInfo = objectBuilderFactory.toInfo(EnvironmentInfo.class, environments, ui_.getBaseUriBuilder());
		return environmentsInfo;
	}

	@PUT
	@Path("/{id}/parentchildenvironments/{parentId}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public Boolean updateParentDependableEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer companyId_,
			@PathParam("parentId") final Integer parentEnvironmentId_, @FormParam("environmentIds") final ArrayList<Integer> environmentIds_) throws Exception
	{
		environmentService.saveParentDependableEnvironments(companyId_, parentEnvironmentId_, environmentIds_);

		return Boolean.TRUE;
	}
}
