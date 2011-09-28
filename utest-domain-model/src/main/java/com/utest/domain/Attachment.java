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
package com.utest.domain;

public class Attachment extends TimelineEntity
{
	private String	name;
	private String	description;
	private String	url;
	private Double	size;
	private Integer	entityTypeId;
	private Integer	entityId;
	private Integer	attachmentTypeId;

	public Attachment()
	{
		super();
	}

	public Attachment(final String name, final String description, final String url, final Double size, final Integer entityTypeId, final Integer entityId,
			final Integer attachmentTypeId)
	{
		super();
		this.name = name;
		this.description = description;
		this.url = url;
		this.size = size;
		this.entityId = entityId;
		this.entityTypeId = entityTypeId;
		this.attachmentTypeId = attachmentTypeId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Double getSize()
	{
		return size;
	}

	public void setSize(Double size)
	{
		this.size = size;
	}

	public Integer getEntityTypeId()
	{
		return entityTypeId;
	}

	public void setEntityTypeId(Integer entityTypeId)
	{
		this.entityTypeId = entityTypeId;
	}

	public Integer getEntityId()
	{
		return entityId;
	}

	public void setEntityId(Integer entityId)
	{
		this.entityId = entityId;
	}

	public Integer getAttachmentTypeId()
	{
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(Integer attachmentTypeId)
	{
		this.attachmentTypeId = attachmentTypeId;
	}

}
