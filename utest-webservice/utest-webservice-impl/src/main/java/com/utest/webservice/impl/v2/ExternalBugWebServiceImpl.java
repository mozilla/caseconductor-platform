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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.security.access.annotation.Secured;

import com.utest.domain.EntityExternalBug;
import com.utest.domain.Permission;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.ExternalBugService;
import com.utest.webservice.api.v2.ExternalBugWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.EntityExternalBugInfo;
import com.utest.webservice.model.v2.EntityExternalBugSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/externalbugs/")
public class ExternalBugWebServiceImpl extends BaseWebServiceImpl implements ExternalBugWebService
{
	private final ExternalBugService	externalBugService;

	public ExternalBugWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final ExternalBugService externalBugService)
	{
		super(objectBuildFactory);
		this.externalBugService = externalBugService;
	}

	@DELETE
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ATTACHMENT_EDIT })
	public Boolean deleteExternalBug(@Context final UriInfo ui_, @PathParam("id") final Integer externalBugId_, @FormParam("entityId") final Integer entityId_,
			@FormParam("entityTypeId") final Integer entityTypeId_) throws Exception
	{
		return externalBugService.deleteEntityExternalBug(externalBugId_, entityId_, entityTypeId_);
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ATTACHMENT_VIEW)
	public EntityExternalBugSearchResultInfo findExternalBugs(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(EntityExternalBugInfo.class, request, ui_);
		final UtestSearchResult result = externalBugService.findEntityExternalBugs(search);

		return (EntityExternalBugSearchResultInfo) objectBuilderFactory
				.createResult(EntityExternalBugInfo.class, EntityExternalBug.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ATTACHMENT_VIEW)
	public EntityExternalBugInfo getExternalBug(@Context final UriInfo ui_, @PathParam("id") final Integer externalBugId) throws Exception
	{
		final EntityExternalBug attachment = externalBugService.getEntityExternalBug(externalBugId);

		return objectBuilderFactory.toInfo(EntityExternalBugInfo.class, attachment, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ATTACHMENT_EDIT })
	public EntityExternalBugInfo createExternalBug(@Context UriInfo ui, @FormParam("externalIdentifier") String externalIdentifier, @FormParam("url") String url,
			@FormParam("entityTypeId") Integer entityTypeId, @FormParam("entityId") Integer entityId) throws Exception
	{
		EntityExternalBug attachment = externalBugService.addEntityExternalBug(externalIdentifier, url, entityTypeId, entityId);
		return objectBuilderFactory.toInfo(EntityExternalBugInfo.class, attachment, ui.getBaseUriBuilder());
	}

}
