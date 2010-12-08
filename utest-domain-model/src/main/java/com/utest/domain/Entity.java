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

import java.io.Serializable;

public class Entity implements Serializable
{

	private Integer	id;

	public Integer getId()
	{
		return this.id;
	}

	public void setId(final Integer id)
	{
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Entity other = (Entity) obj;
		if (id == null)
		{
			if (other.id != null)
			{
				return false;
			}
			else
			{
				// ids are null, check obj reference because there is no other
				// unique identifier. Can not use UID because this will create a
				// new UID since the ids are null.
				return this == obj;
			}
		}
		else if (!id.equals(other.id))
		{
			return false;
		}
		return true;
	}

	public boolean isNew()
	{
		return ((id == null) || (id == 0));
	}
}
