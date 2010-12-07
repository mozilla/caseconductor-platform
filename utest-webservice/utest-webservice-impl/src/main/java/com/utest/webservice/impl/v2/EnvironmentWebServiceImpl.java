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

import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentType;
import com.utest.domain.Permission;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.webservice.api.v2.EnvironmentWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentGroupResultInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.EnvironmentResultInfo;
import com.utest.webservice.model.v2.EnvironmentTypeInfo;
import com.utest.webservice.model.v2.EnvironmentTypeResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/env/")
public class EnvironmentWebServiceImpl extends BaseWebServiceImpl implements EnvironmentWebService
{
	private final EnvironmentService	environmentService;

	public EnvironmentWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final EnvironmentService environmentService)
	{
		super(objectBuildFactory);
		this.environmentService = environmentService;
	}

	@PUT
	@Path("/environmenttypes/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public Boolean updateEnvironmentType(@Context final UriInfo ui_, @PathParam("id") final Integer environmentTypeId_,
			@FormParam("") final EnvironmentTypeInfo environmentTypeInfo_) throws Exception
	{
		environmentService.saveEnvironmentTypeLocale(environmentTypeId_, environmentTypeInfo_.getName(), environmentTypeInfo_.getLocaleCode(), environmentTypeInfo_.getSortOrder());

		return Boolean.TRUE;
	}

	@POST
	@Path("/environmenttypes/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public EnvironmentTypeInfo createEnvironmentType(@Context final UriInfo ui_, @FormParam("") final EnvironmentTypeInfo environmentTypeInfo_) throws Exception
	{
		final EnvironmentType environmentType = environmentService.addEnvironmentType(environmentTypeInfo_.getCompanyId(), environmentTypeInfo_.getParentEnvironmentTypeId(),
				environmentTypeInfo_.getName(), environmentTypeInfo_.isGroupType(), environmentTypeInfo_.getLocaleCode());

		return objectBuilderFactory.toInfo(EnvironmentTypeInfo.class, environmentType, ui_.getAbsolutePathBuilder().path("/environmenttypes/{id}/"));
	}

	@DELETE
	@Path("/environmenttypes/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_EDIT)
	public Boolean deleteEnvironmentType(@Context final UriInfo ui_, @PathParam("id") final Integer environmentTypeId_) throws Exception
	{
		environmentService.deleteEnvironmentType(environmentTypeId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/environmenttypes/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentTypeInfo getEnvironmentType(@Context final UriInfo ui_, @PathParam("id") final Integer environmentTypeId_) throws Exception
	{
		final EnvironmentType environmentType = environmentService.getEnvironmentType(environmentTypeId_);

		return objectBuilderFactory.toInfo(EnvironmentTypeInfo.class, environmentType, ui_.getAbsolutePathBuilder().path(""));
	}

	@GET
	@Path("/environmenttypes/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentTypeResultInfo findEnvironmentTypes(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(EnvironmentTypeInfo.class, request_, ui_);
		final UtestSearchResult result = environmentService.findEnvironmentTypes(search);

		return (EnvironmentTypeResultInfo) objectBuilderFactory.createResult(EnvironmentTypeInfo.class, EnvironmentType.class, request_, result, ui_.getBaseUriBuilder().path(
				"/environmenttypes/{id}"));
	}

	@GET
	@Path("/environmenttypes/{id}/environments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public List<EnvironmentInfo> getEnvironmentTypeEnvironments(@Context UriInfo ui_, @PathParam("id") final Integer environmentTypeId_, @QueryParam("") UtestSearchRequest request)
			throws Exception
	{
		final List<Environment> environments = environmentService.findEnvironmentsForType(environmentTypeId_);
		final List<EnvironmentInfo> environmentsInfo = objectBuilderFactory.toInfo(EnvironmentInfo.class, environments, ui_.getBaseUriBuilder().path("/environments/{id}"));
		return environmentsInfo;
	}

	// /////////////

	@PUT
	@Path("/environments/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public Boolean updateEnvironment(@Context final UriInfo ui_, @PathParam("id") final Integer environmentId_, @FormParam("") final EnvironmentInfo environmentInfo_)
			throws Exception
	{
		environmentService.saveEnvironmentLocale(environmentId_, environmentInfo_.getName(), environmentInfo_.getLocaleCode(), environmentInfo_.getSortOrder());

		return Boolean.TRUE;
	}

	@POST
	@Path("/environments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public EnvironmentInfo createEnvironment(@Context final UriInfo ui_, @FormParam("") final EnvironmentInfo environmentInfo_) throws Exception
	{
		final Environment environment = environmentService.addEnvironment(environmentInfo_.getCompanyId(), environmentInfo_.getEnvironmentTypeId(), environmentInfo_.getName(),
				environmentInfo_.getLocaleCode());

		return objectBuilderFactory.toInfo(EnvironmentInfo.class, environment, ui_.getAbsolutePathBuilder().path("/{id}/"));
	}

	@DELETE
	@Path("/environments/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_EDIT)
	public Boolean deleteEnvironment(@Context final UriInfo ui_, @PathParam("id") final Integer environmentId_) throws Exception
	{
		environmentService.deleteEnvironment(environmentId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/environments/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentInfo getEnvironment(@Context final UriInfo ui_, @PathParam("id") final Integer environmentId_) throws Exception
	{
		final Environment environment = environmentService.getEnvironment(environmentId_);

		return objectBuilderFactory.toInfo(EnvironmentInfo.class, environment, ui_.getAbsolutePathBuilder().path(""));
	}

	@GET
	@Path("/environments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentResultInfo findEnvironments(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(EnvironmentInfo.class, request_, ui_);
		final UtestSearchResult result = environmentService.findEnvironments(search);

		return (EnvironmentResultInfo) objectBuilderFactory.createResult(EnvironmentInfo.class, Environment.class, request_, result, ui_.getBaseUriBuilder().path(
				"/environments/{id}"));
	}

	// /////////////

	@PUT
	@Path("/companies/{id}/environmentgroups/autogenerate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer companyId_,
			@FormParam("environmentIds") final ArrayList<Integer> environmentIds_) throws Exception
	{
		List<EnvironmentGroup> environmentGroups = environmentService.addGeneratedEnvironmentGroups(companyId_, environmentIds_);
		final List<EnvironmentGroupInfo> environmentGroupsInfo = objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroups, ui_.getBaseUriBuilder().path(
				"/environmentgroups/{id}"));
		return environmentGroupsInfo;
	}

	@PUT
	@Path("/companies/{id}/environmentgroups/environmenttypes/{typeId}/autogenerate/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer companyId_,
			@PathParam("typeId") final Integer environmentTypeId_, @FormParam("environmentIds") final ArrayList<Integer> environmentIds_) throws Exception
	{
		List<EnvironmentGroup> environmentGroups = environmentService.addGeneratedEnvironmentGroups(companyId_, environmentTypeId_, environmentIds_);
		final List<EnvironmentGroupInfo> environmentGroupsInfo = objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroups, ui_.getBaseUriBuilder().path(
				"/environmentgroups/{id}"));
		return environmentGroupsInfo;
	}

	@PUT
	@Path("/environmentgroups/{id}/environments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public Boolean updateEnvironmentGroupEnvironments(@Context final UriInfo ui_, @PathParam("id") final Integer environmentGroupId_,
			@FormParam("environmentIds") final ArrayList<Integer> environmentIds_) throws Exception
	{
		environmentService.saveEnvironmentsForGroup(environmentGroupId_, environmentIds_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/environmentgroups/{id}/environments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public List<EnvironmentInfo> getEnvironmentGroupEnvironments(@Context UriInfo ui_, @PathParam("id") final Integer environmentGroupId_,
			@QueryParam("") UtestSearchRequest request) throws Exception
	{
		final List<Environment> environments = environmentService.findEnvironmentsForGroup(environmentGroupId_);
		final List<EnvironmentInfo> environmentsInfo = objectBuilderFactory.toInfo(EnvironmentInfo.class, environments, ui_.getBaseUriBuilder().path("/environments/{id}"));
		return environmentsInfo;
	}

	@GET
	@Path("/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentGroupResultInfo findEnvironmentGroups(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(EnvironmentGroupInfo.class, request_, ui_);
		final UtestSearchResult result = environmentService.findEnvironmentGroups(search);

		return (EnvironmentGroupResultInfo) objectBuilderFactory.createResult(EnvironmentGroupInfo.class, EnvironmentGroup.class, request_, result, ui_.getBaseUriBuilder().path(
				"/environmentgroups/{id}"));
	}

	// /////////////

	@PUT
	@Path("/environmentgroups/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public Boolean updateEnvironmentGroup(@Context final UriInfo ui_, @PathParam("id") final Integer environmentGroupId_,
			@FormParam("") final EnvironmentGroupInfo environmentGroupInfo_) throws Exception
	{
		environmentService.saveEnvironmentGroup(environmentGroupId_, environmentGroupInfo_.getName(), environmentGroupInfo_.getDescription(), environmentGroupInfo_
				.getExternalIdentifier(), environmentGroupInfo_.getResourceIdentity().getVersion());

		return Boolean.TRUE;
	}

	@POST
	@Path("/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public EnvironmentGroupInfo createEnvironmentGroup(@Context final UriInfo ui_, @FormParam("") final EnvironmentGroupInfo environmentGroupInfo_) throws Exception
	{
		final EnvironmentGroup environmentGroup = environmentService.addEnvironmentGroup(environmentGroupInfo_.getCompanyId(), environmentGroupInfo_.getEnvironmentTypeId(),
				environmentGroupInfo_.getName(), environmentGroupInfo_.getDescription(), new ArrayList<Integer>());

		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroup, ui_.getAbsolutePathBuilder().path("/environmentgroups/{id}/"));
	}

	@DELETE
	@Path("/environmentgroups/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_EDIT)
	public Boolean deleteEnvironmentGroup(@Context final UriInfo ui_, @PathParam("id") final Integer environmentGroupId_) throws Exception
	{
		environmentService.deleteEnvironmentGroup(environmentGroupId_);

		return Boolean.TRUE;
	}

	@GET
	@Path("/environmentgroups/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentGroupInfo getEnvironmentGroup(@Context final UriInfo ui_, @PathParam("id") final Integer environmentGroupId_) throws Exception
	{
		final EnvironmentGroup environmentGroup = environmentService.getEnvironmentGroup(environmentGroupId_);

		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroup, ui_.getAbsolutePathBuilder().path("/environmentgroups/{id}/"));
	}

}
