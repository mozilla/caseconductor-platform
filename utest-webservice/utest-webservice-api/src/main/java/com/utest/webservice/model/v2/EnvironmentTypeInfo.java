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
@XmlType(name = "environmenttype")
public class EnvironmentTypeInfo extends BaseInfo
{
	@XmlElement(required = false)
	private Integer			parentEnvironmentTypeId	= null;
	@XmlElement(type = ResourceLocator.class, name = "parentEnvironmentTypeLocator")
	private ResourceLocator	parentEnvironmentTypeLocator;
	@XmlElement(required = false)
	private Integer			companyId;
	@XmlElement(type = ResourceLocator.class, name = "companyLocator")
	private ResourceLocator	companyLocator;
	@XmlElement(required = true)
	private boolean			groupType;
	@XmlElement(required = true)
	private String			name;
	@XmlElement(required = false)
	private String			localeCode;
	@XmlElement(required = false)
	private Integer			sortOrder;

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

	public Integer getParentEnvironmentTypeId()
	{
		return parentEnvironmentTypeId;
	}

	public ResourceLocator getParentEnvironmentTypeLocator()
	{
		return parentEnvironmentTypeLocator;
	}

	public void setParentEnvironmentTypeLocator(ResourceLocator parentEnvironmentTypeLocator)
	{
		this.parentEnvironmentTypeLocator = parentEnvironmentTypeLocator;
	}

	public ResourceLocator getCompanyLocator()
	{
		return companyLocator;
	}

	public void setCompanyLocator(ResourceLocator companyLocator)
	{
		this.companyLocator = companyLocator;
	}

	public void setParentEnvironmentTypeId(Integer parentEnvironmentTypeId)
	{
		this.parentEnvironmentTypeId = parentEnvironmentTypeId;
	}
}
