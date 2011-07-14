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
import com.utest.domain.TimelineEntity;

public class EnvironmentTypeView extends TimelineEntity implements CompanyDependable
{

	/**
	 * optional id of parent type
	 */
	private Integer	parentEnvironmentTypeId	= null;
	private Integer	companyId;
	private boolean	groupType;
	private String	name;
	private String	localeCode;
	private Integer	sortOrder;

	public EnvironmentTypeView()
	{
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public boolean isGroupType()
	{
		return groupType;
	}

	public void setGroupType(boolean groupType)
	{
		this.groupType = groupType;
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

	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	public String getLocaleCode()
	{
		return localeCode;
	}

	public Integer getParentEnvironmentTypeId()
	{
		return parentEnvironmentTypeId;
	}

	public void setParentEnvironmentTypeId(Integer parentEnvironmentTypeId)
	{
		this.parentEnvironmentTypeId = parentEnvironmentTypeId;
	}

}
