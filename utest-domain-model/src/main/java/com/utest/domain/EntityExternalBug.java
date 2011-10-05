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

public class EntityExternalBug extends TimelineEntity
{
	private String	externalIdentifier;
	private String	url;
	private Integer	entityTypeId;
	private Integer	entityId;

	public EntityExternalBug()
	{
		super();
	}

	public String getExternalIdentifier()
	{
		return externalIdentifier;
	}

	public void setExternalIdentifier(String externalIdentifier)
	{
		this.externalIdentifier = externalIdentifier;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((externalIdentifier == null) ? 0 : externalIdentifier.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityExternalBug other = (EntityExternalBug) obj;
		if (externalIdentifier == null)
		{
			if (other.externalIdentifier != null)
				return false;
		}
		else if (!externalIdentifier.equals(other.externalIdentifier))
			return false;
		if (url == null)
		{
			if (other.url != null)
				return false;
		}
		else if (!url.equals(other.url))
			return false;
		return true;
	}

}
