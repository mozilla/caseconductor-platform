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

public class AttachmentTypeLocale implements LocaleDescriptable
{

	private AttachmentTypeLocaleId	id;
	private String					name;
	private Integer					sortOrder;
	private boolean					deleted;

	public AttachmentTypeLocale()
	{
	}

	public AttachmentTypeLocaleId getId()
	{
		return id;
	}

	public void setId(AttachmentTypeLocaleId id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Override
	public Integer getEntityId()
	{
		return id.getAttachmentTypeId();
	}

	@Override
	public String getLocaleCode()
	{
		return id.getLocaleCode();
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

}
