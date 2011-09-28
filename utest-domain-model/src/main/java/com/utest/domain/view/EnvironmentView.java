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

import com.utest.domain.CompanyDependable;
import com.utest.domain.Named;
import com.utest.domain.ParentDependable;
import com.utest.domain.TimelineEntity;

public class EnvironmentView extends TimelineEntity implements Named, ParentDependable, CompanyDependable
{
	private Integer	environmentTypeId;
	private Integer	companyId;
	private String	name;
	private String	localeCode;
	private Integer	sortOrder;

	public EnvironmentView()
	{
	}

	public Integer getEnvironmentTypeId()
	{
		return environmentTypeId;
	}

	public void setEnvironmentTypeId(Integer environmentTypeId)
	{
		this.environmentTypeId = environmentTypeId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocaleCode()
	{
		return localeCode;
	}

	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
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
	public Integer getChildId()
	{
		return getId();
	}

	@Override
	public Integer getParentId()
	{
		return getEnvironmentTypeId();
	}

	@Override
	public void setParentId(Integer parentId)
	{
		setEnvironmentTypeId(parentId);
	}
}
