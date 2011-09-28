/*
Case Conductor is a Test Case Management system.
Copyright (C) 2011 uTest Inc.

This file is part of Case Conductor.

Case Conductor is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Case Conductor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Case Conductor.  If not, see <http://www.gnu.org/licenses/>.

*/
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

import com.utest.domain.Attachment;
import com.utest.domain.Permission;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.AttachmentService;
import com.utest.webservice.api.v2.AttachmentWebService;
import com.utest.webservice.builders.ObjectBuilderFactory;
import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.AttachmentSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

@Path("/attachments/")
public class AttachmentWebServiceImpl extends BaseWebServiceImpl implements AttachmentWebService
{
	private final AttachmentService	attachmentService;

	public AttachmentWebServiceImpl(final ObjectBuilderFactory objectBuildFactory, final AttachmentService attachmentService)
	{
		super(objectBuildFactory);
		this.attachmentService = attachmentService;
	}

	@DELETE
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ATTACHMENT_EDIT })
	public Boolean deleteAttachment(@Context final UriInfo ui_, @PathParam("id") final Integer attachmentId_, @FormParam("entityId") final Integer entityId_,
			@FormParam("entityTypeId") final Integer entityTypeId_) throws Exception
	{
		return attachmentService.deleteAttachment(attachmentId_, entityId_, entityTypeId_);
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ATTACHMENT_VIEW)
	public AttachmentSearchResultInfo findAttachments(@Context final UriInfo ui_, @QueryParam("") final UtestSearchRequest request) throws Exception
	{
		final UtestSearch search = objectBuilderFactory.createSearch(AttachmentInfo.class, request, ui_);
		final UtestSearchResult result = attachmentService.findAttachments(search);

		return (AttachmentSearchResultInfo) objectBuilderFactory.createResult(AttachmentInfo.class, Attachment.class, request, result, ui_.getBaseUriBuilder());
	}

	@GET
	@Path("/{id}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured(Permission.ATTACHMENT_VIEW)
	public AttachmentInfo getAttachment(@Context final UriInfo ui_, @PathParam("id") final Integer attachmentId) throws Exception
	{
		final Attachment attachment = attachmentService.getAttachment(attachmentId);

		return objectBuilderFactory.toInfo(AttachmentInfo.class, attachment, ui_.getBaseUriBuilder());
	}

	@POST
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes( { MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	@Secured( { Permission.ATTACHMENT_EDIT })
	public AttachmentInfo createAttachment(@Context UriInfo ui, @FormParam("name") String name, @FormParam("description") String description, @FormParam("url") String url,
			@FormParam("size") Double size, @FormParam("entityTypeId") Integer entityTypeId, @FormParam("entityId") Integer entityId,
			@FormParam("attachmentTypeId") Integer attachmentTypeId) throws Exception
	{
		Attachment attachment = attachmentService.addAttachment(name, description, url, size, entityTypeId, entityId, attachmentTypeId);
		return objectBuilderFactory.toInfo(AttachmentInfo.class, attachment, ui.getBaseUriBuilder());
	}

}
