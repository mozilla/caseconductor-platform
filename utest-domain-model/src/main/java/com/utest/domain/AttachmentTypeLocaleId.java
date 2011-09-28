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

public class AttachmentTypeLocaleId implements java.io.Serializable
{

	private Integer	attachmentTypeId;
	private String	localeCode;

	public AttachmentTypeLocaleId()
	{
	}

	public Integer getAttachmentTypeId()
	{
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(Integer attachmentTypeId)
	{
		this.attachmentTypeId = attachmentTypeId;
	}

	public String getLocaleCode()
	{
		return localeCode;
	}

	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachmentTypeId == null) ? 0 : attachmentTypeId.hashCode());
		result = prime * result + ((localeCode == null) ? 0 : localeCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttachmentTypeLocaleId other = (AttachmentTypeLocaleId) obj;
		if (attachmentTypeId == null)
		{
			if (other.attachmentTypeId != null)
				return false;
		}
		else if (!attachmentTypeId.equals(other.attachmentTypeId))
			return false;
		if (localeCode == null)
		{
			if (other.localeCode != null)
				return false;
		}
		else if (!localeCode.equals(other.localeCode))
			return false;
		return true;
	}

}
