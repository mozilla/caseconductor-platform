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
package com.utest.webservice.model.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attachment")
public class AttachmentInfo extends BaseInfo
{
	@XmlElement(required = true)
	private String	name;
	@XmlElement(required = false)
	private String	description;
	@XmlElement(required = false)
	private String	url;
	@XmlElement(required = false)
	private Double	size;
	@XmlElement(required = false)
	private Integer	entityTypeId;
	@XmlElement(required = false)
	private Integer	entityId;
	@XmlElement(required = false)
	private Integer	attachmentTypeId;

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
