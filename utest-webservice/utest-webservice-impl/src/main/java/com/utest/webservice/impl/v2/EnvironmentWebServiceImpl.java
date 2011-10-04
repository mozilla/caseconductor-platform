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
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.EnvironmentProfileExploded;
import com.utest.domain.EnvironmentType;
import com.utest.domain.Permission;
import com.utest.domain.Tag;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.EnvironmentService;
import com.utest.domain.view.EnvironmentTypeView;
import com.utest.domain.view.EnvironmentView;
import com.utest.webservice.api.v2.EnvironmentWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentGroupSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.EnvironmentProfileExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentTypeInfo;
import com.utest.webservice.model.v2.EnvironmentTypeViewInfo;
import com.utest.webservice.model.v2.EnvironmentTypeViewSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentViewInfo;
import com.utest.webservice.model.v2.EnvironmentViewSearchResultInfo;
import com.utest.webservice.model.v2.TagInfo;
import com.utest.webservice.model.v2.TagSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/")
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
	public EnvironmentTypeInfo updateEnvironmentType(@Context final UriInfo ui_, @PathParam("id") final Integer environmentTypeId_, @FormParam("name") final String name_,
			@FormParam("localCode") final String localeCode_, @FormParam("sortOrder") final Integer sortOrder_) throws Exception
	{
		environmentService.saveEnvironmentTypeLocale(environmentTypeId_, name_, localeCode_, sortOrder_);

		return getEnvironmentType(ui_, environmentTypeId_);
	}

	@POST
	@Path("/environmenttypes/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public EnvironmentTypeInfo createEnvironmentType(@Context final UriInfo ui_, @FormParam("companyId") final Integer companyId_,
			@FormParam("parentEnvironmentTypeId") final Integer parentEnvironmentTypeId_, @FormParam("name") final String name_, @FormParam("localCode") final String localeCode_,
			@FormParam("sortOrder") final Integer sortOrder_, @FormParam("groupType") final String groupType_) throws Exception
	{
		EnvironmentType environmentType = environmentService.addEnvironmentType(companyId_, parentEnvironmentTypeId_, name_, "true".equalsIgnoreCase(groupType_), localeCode_);

		return objectBuilderFactory.toInfo(EnvironmentTypeInfo.class, environmentType, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/environmenttypes/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_EDIT)
	public Boolean deleteEnvironmentType(@Context final UriInfo ui_, @PathParam("id") final Integer environmentTypeId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		environmentService.deleteEnvironmentType(environmentTypeId_, originalVersionId_);

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

		return objectBuilderFactory.toInfo(EnvironmentTypeInfo.class, environmentType, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/environmenttypes/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentTypeViewSearchResultInfo findEnvironmentTypes(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(EnvironmentTypeInfo.class, request_, ui_);
		final UtestSearchResult result = environmentService.findEnvironmentTypesViews(search);

		return (EnvironmentTypeViewSearchResultInfo) objectBuilderFactory.createResult(EnvironmentTypeViewInfo.class, EnvironmentTypeView.class, request_, result, ui_
				.getBaseUriBuilder());
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
		final List<Environment> environments = environmentService.getEnvironmentsForType(environmentTypeId_);
		final List<EnvironmentInfo> environmentsInfo = objectBuilderFactory.toInfo(EnvironmentInfo.class, environments, ui_.getBaseUriBuilder());
		return environmentsInfo;
	}

	@PUT
	@Path("/environments/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public EnvironmentInfo updateEnvironment(@Context final UriInfo ui_, @PathParam("id") final Integer environmentId_, @FormParam("name") final String name_,
			@FormParam("localCode") final String localeCode_, @FormParam("sortOrder") final Integer sortOrder_) throws Exception
	{
		environmentService.saveEnvironmentLocale(environmentId_, name_, localeCode_, sortOrder_);
		return getEnvironment(ui_, environmentId_);
	}

	@PUT
	@Path("/tags/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public TagInfo updateTag(@Context final UriInfo ui_, @PathParam("id") final Integer tagId_, @FormParam("name") final String tag_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		Tag tag = environmentService.saveTag(tagId_, tag_, originalVersionId_);
		return objectBuilderFactory.toInfo(TagInfo.class, tag, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/environments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public EnvironmentInfo createEnvironment(@Context final UriInfo ui_, @FormParam("companyId") final Integer companyId_,
			@FormParam("environmentTypeId") final Integer environmentTypeId_, @FormParam("name") final String name_, @FormParam("localCode") final String localeCode_,
			@FormParam("sortOrder") final Integer sortOrder_) throws Exception
	{
		final Environment environment = environmentService.addEnvironment(companyId_, environmentTypeId_, name_, localeCode_);
		return objectBuilderFactory.toInfo(EnvironmentInfo.class, environment, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/environments/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_EDIT)
	public Boolean deleteEnvironment(@Context final UriInfo ui_, @PathParam("id") final Integer environmentId_, @FormParam("originalVersionId") final Integer originalVersionId_)
			throws Exception
	{
		environmentService.deleteEnvironment(environmentId_, originalVersionId_);
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

		return objectBuilderFactory.toInfo(EnvironmentInfo.class, environment, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/environments/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentViewSearchResultInfo findEnvironments(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(EnvironmentInfo.class, request_, ui_);
		final UtestSearchResult result = environmentService.findEnvironmentsViews(search);

		return (EnvironmentViewSearchResultInfo) objectBuilderFactory.createResult(EnvironmentViewInfo.class, EnvironmentView.class, request_, result, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/tags/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public TagInfo createTag(@Context final UriInfo ui_, @FormParam("companyId") final Integer companyId_, @FormParam("name") final String tag_) throws Exception
	{
		final Tag tag = environmentService.addTag(companyId_, tag_);

		return objectBuilderFactory.toInfo(TagInfo.class, tag, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/tags/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_EDIT)
	public Boolean deleteTag(@Context final UriInfo ui_, @PathParam("id") final Integer tagId_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		environmentService.deleteTag(tagId_, originalVersionId_);
		return Boolean.TRUE;
	}

	@GET
	@Path("/tags/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public TagInfo getTag(@Context final UriInfo ui_, @PathParam("id") final Integer tagId_) throws Exception
	{
		final Tag tag = environmentService.getTag(tagId_);

		return objectBuilderFactory.toInfo(TagInfo.class, tag, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/tags/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public TagSearchResultInfo findTags(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(TagInfo.class, request_, ui_);
		final UtestSearchResult result = environmentService.findTags(search);

		return (TagSearchResultInfo) objectBuilderFactory.createResult(TagInfo.class, Tag.class, request_, result, ui_.getBaseUriBuilder());
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
		final List<Environment> environments = environmentService.getEnvironmentsForGroup(environmentGroupId_);
		final List<EnvironmentInfo> environmentsInfo = objectBuilderFactory.toInfo(EnvironmentInfo.class, environments, ui_.getBaseUriBuilder());
		return environmentsInfo;
	}

	@GET
	@Path("/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentGroupSearchResultInfo findEnvironmentGroups(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(EnvironmentGroupInfo.class, request_, ui_);
		final UtestSearchResult result = environmentService.findEnvironmentGroups(search);

		return (EnvironmentGroupSearchResultInfo) objectBuilderFactory.createResult(EnvironmentGroupInfo.class, EnvironmentGroup.class, request_, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/environmentgroups/exploded/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentGroupExplodedSearchResultInfo findEnvironmentGroupsExploded(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request_) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(EnvironmentGroupInfo.class, request_, ui_);
		final UtestSearchResult result = environmentService.findEnvironmentGroupsExploded(search);

		return (EnvironmentGroupExplodedSearchResultInfo) objectBuilderFactory.createResult(EnvironmentGroupExplodedInfo.class, EnvironmentGroupExploded.class, request_, result,
				ui_.getBaseUriBuilder());
	}

	@PUT
	@Path("/environmentgroups/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public EnvironmentGroupInfo updateEnvironmentGroup(@Context final UriInfo ui_, @PathParam("id") final Integer environmentGroupId_, @FormParam("name") final String name_,
			@FormParam("description_") final String description_, @FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		EnvironmentGroup environmentGroup = environmentService.saveEnvironmentGroup(environmentGroupId_, name_, description_, originalVersionId_);
		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroup, ui_.getBaseUriBuilder());
	}

	@POST
	@Path("/environmentgroups/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ENVIRONMENT_EDIT })
	public EnvironmentGroupInfo createEnvironmentGroup(@Context final UriInfo ui_, @FormParam("companyId") final Integer companyId_,
			@FormParam("environmentTypeId") final Integer environmentTypeId_, @FormParam("name") final String name_, @FormParam("description_") final String description_)
			throws Exception
	{
		final EnvironmentGroup environmentGroup = environmentService.addEnvironmentGroup(companyId_, environmentTypeId_, name_, description_, new ArrayList<Integer>());

		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroup, ui_.getBaseUriBuilder());
	}

	@DELETE
	@Path("/environmentgroups/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_EDIT)
	public Boolean deleteEnvironmentGroup(@Context final UriInfo ui_, @PathParam("id") final Integer environmentGroupId_,
			@FormParam("originalVersionId") final Integer originalVersionId_) throws Exception
	{
		environmentService.deleteEnvironmentGroup(environmentGroupId_, originalVersionId_);

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

		return objectBuilderFactory.toInfo(EnvironmentGroupInfo.class, environmentGroup, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/environmentgroups/exploded/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentGroupExplodedInfo getEnvironmentGroupExploded(@Context final UriInfo ui_, @PathParam("id") final Integer environmentGroupId_) throws Exception
	{
		final EnvironmentGroupExploded environmentGroup = environmentService.getEnvironmentGroupExploded(environmentGroupId_);

		return objectBuilderFactory.toInfo(EnvironmentGroupExplodedInfo.class, environmentGroup, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/environmentprofiles/exploded/{id}/")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ENVIRONMENT_VIEW)
	public EnvironmentProfileExplodedInfo getEnvironmentProfileExploded(@Context final UriInfo ui_, @PathParam("id") final Integer environmentProfileId_) throws Exception
	{
		final EnvironmentProfileExploded environmentProfile = environmentService.getEnvironmentProfileExploded(environmentProfileId_);

		return objectBuilderFactory.toInfo(EnvironmentProfileExplodedInfo.class, environmentProfile, ui_.getBaseUriBuilder());
	}

}
