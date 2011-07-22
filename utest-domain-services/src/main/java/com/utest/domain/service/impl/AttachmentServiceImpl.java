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
import com.utest.domain.Attachment;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.AttachmentService;

public class AttachmentServiceImpl extends BaseServiceImpl implements AttachmentService
{
	private final TypelessDAO	dao;

	/**
	 * Default constructor
	 */
	public AttachmentServiceImpl(final TypelessDAO dao)
	{
		super(dao);
		this.dao = dao;
	}

	@Override
	public Attachment addAttachment(final String name, final String description, final String url, final Double size, final Integer entityTypeId, final Integer entityId,
			final Integer attachmentTypeId) throws Exception
	{
		final Attachment attachment = new Attachment(name, description, url, size, entityTypeId, entityId, attachmentTypeId);
		final Integer attachmentId = dao.addAndReturnId(attachment);
		return getAttachment(attachmentId);
	}

	@Override
	public void deleteAttachment(final Integer attachmentId_, final Integer entityId_, Integer entityTypeId_) throws Exception
	{
		Attachment attachment = getRequiredEntityById(Attachment.class, attachmentId_);
		// make sure entity id and type matches before deleting
		if (attachment.getEntityId().equals(entityId_) && attachment.getEntityTypeId().equals(entityTypeId_))
		{
			dao.delete(attachment);
		}
	}

	@Override
	public UtestSearchResult findAttachments(final UtestSearch search_) throws Exception
	{
		return dao.getBySearch(Attachment.class, search_);
	}

	@Override
	public Attachment getAttachment(final Integer attachmentId_) throws Exception
	{
		final Attachment attachment = getRequiredEntityById(Attachment.class, attachmentId_);
		return attachment;
	}

	@Override
	public List<Attachment> getAttachmentsForEntity(final Integer entityId_, final Integer entityTypeId_) throws Exception
	{
		Search search = new Search(Attachment.class);
		search.addFilterEqual("entityId", entityId_);
		search.addFilterEqual("entityTypeId", entityTypeId_);
		final List<Attachment> attachments = dao.search(Attachment.class, search);
		if ((attachments != null) && !attachments.isEmpty())
		{
			return attachments;
		}
		else
		{
			return new ArrayList<Attachment>();
		}
	}
}
