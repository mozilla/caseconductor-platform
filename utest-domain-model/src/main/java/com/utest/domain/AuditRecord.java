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

import java.util.Date;

public class AuditRecord
{
	public static final Integer	INSERT	= new Integer(1);
	public static final Integer	UPDATE	= new Integer(2);
	public static final Integer	DELETE	= new Integer(3);
	private Integer				id;
	private Integer				entityId;
	private Integer				userId;
	private String				userName;
	private String				propertyValue;
	private Date				date;
	private String				entity;
	private String				propertyName;
	private Integer				eventId;
	private Object				entityObject;

	public AuditRecord()
	{
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setId(final Integer id)
	{
		this.id = id;
	}

	public Integer getEventId()
	{
		return this.eventId;
	}

	public void setEventId(final Integer eventId)
	{
		this.eventId = eventId;
	}

	public Integer getEntityId()
	{
		return this.entityId;
	}

	public void setEntityId(final Integer entityId)
	{
		this.entityId = entityId;
	}

	public Integer getUserId()
	{
		return this.userId;
	}

	public void setUserId(final Integer userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return this.userName;
	}

	public void setUserName(final String userName)
	{
		this.userName = userName;
	}

	public String getPropertyValue()
	{
		return this.propertyValue;
	}

	public void setPropertyValue(final String propertyValue)
	{
		this.propertyValue = propertyValue;
	}

	public Date getDate()
	{
		return this.date;
	}

	public void setDate(final Date date)
	{
		this.date = date;
	}

	public String getEntity()
	{
		return this.entity;
	}

	public void setEntity(final String entity)
	{
		this.entity = entity;
	}

	public Object getEntityObject()
	{
		return entityObject;
	}

	public void setEntityObject(final Object entityObject)
	{
		this.entityObject = entityObject;
	}

	public String getPropertyName()
	{
		return this.propertyName;
	}

	public void setPropertyName(final String propertyName)
	{
		this.propertyName = propertyName;
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (!(other instanceof AuditRecord))
		{
			return false;
		}
		final AuditRecord otherAR = (AuditRecord) other;
		return ((entityId == null) ? otherAR.getEntityId() == null : entityId.equals(otherAR.getEntityId()))
				&& ((userId == null) ? otherAR.getUserId() == null : userId.equals(otherAR.getUserId()))
				&& ((propertyValue == null) ? otherAR.getPropertyValue() == null : propertyValue.equals(otherAR.getPropertyValue()))
				&& ((entity == null) ? otherAR.getEntity() == null : entity.equals(otherAR.getEntity()))
				&& ((propertyName == null) ? otherAR.getPropertyName() == null : propertyName.equals(otherAR.getPropertyName()))
				&& ((eventId == null) ? otherAR.getEventId() == null : eventId.equals(otherAR.getEventId()));
	}

	@Override
	public int hashCode()
	{
		int hash = 1;

		hash = hash * 31 + (entityId == null ? 0 : entityId.hashCode());
		hash = hash * 31 + (userId == null ? 0 : userId.hashCode());
		hash = hash * 31 + (propertyValue == null ? 0 : propertyValue.hashCode());
		hash = hash * 31 + (entity == null ? 0 : entity.hashCode());
		hash = hash * 31 + (propertyName == null ? 0 : propertyName.hashCode());
		hash = hash * 31 + (eventId == null ? 0 : eventId.hashCode());

		return hash;
	}

}
