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
package com.utest.domain.view;

// Generated Oct 7, 2009 11:18:35 AM by Hibernate Tools 3.2.4.GA

import java.util.Map;

import com.utest.domain.Entity;

@SuppressWarnings("unchecked")
public class CategoryValue extends Entity implements Comparable
{

	private String				categoryName;
	private Double				categoryValue;

	// unmapped properties below
	private Map<String, Object>	parms;

	public CategoryValue()
	{
	}

	public String getCategoryName()
	{
		return categoryName;
	}

	public void setCategoryName(final String categoryName)
	{
		this.categoryName = categoryName;
	}

	public Double getCategoryValue()
	{
		return categoryValue;
	}

	public void setCategoryValue(final Double categoryValue)
	{
		this.categoryValue = categoryValue;
	}

	public void setParms(final Map<String, Object> parms)
	{
		this.parms = parms;
	}

	public Map<String, Object> getParms()
	{
		return parms;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!super.equals(obj))
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final CategoryValue other = (CategoryValue) obj;
		if (categoryName == null)
		{
			if (other.categoryName != null)
			{
				return false;
			}
		}
		else if (!categoryName.equals(other.categoryName))
		{
			return false;
		}
		return true;
	}

	public int compareTo(final Object o1)
	{
		if (!(o1 instanceof CategoryValue))
		{
			return 0;
		}
		else
		{
			final CategoryValue cv1 = (CategoryValue) o1;
			if ((cv1.getId() == null) || (this.getId() == null))
			{
				return 0;
			}
			return (cv1.getId().compareTo(this.getId()));
		}
	}

}
