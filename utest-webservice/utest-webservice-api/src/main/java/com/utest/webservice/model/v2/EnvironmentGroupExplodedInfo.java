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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environmentgroup")
public class EnvironmentGroupExplodedInfo extends BaseInfo
{
	@XmlElement(required = false)
	private Integer					environmentTypeId;
	@XmlElement(type = ResourceLocator.class, name = "environmentTypeLocator")
	private ResourceLocator			environmentTypeLocator;
	@XmlElement(required = false)
	private Integer					companyId;
	@XmlElement(type = ResourceLocator.class, name = "companyLocator")
	private ResourceLocator			companyLocator;
	@XmlElement(required = true)
	private String					name;
	@XmlElement(required = false)
	private String					description;
	@XmlElement(required = false)
	private List<EnvironmentInfo>	environments;

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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setEnvironmentTypeLocator(ResourceLocator environmentTypeLocator)
	{
		this.environmentTypeLocator = environmentTypeLocator;
	}

	public ResourceLocator getEnvironmentTypeLocator()
	{
		return environmentTypeLocator;
	}

	public void setCompanyLocator(ResourceLocator companyLocator)
	{
		this.companyLocator = companyLocator;
	}

	public ResourceLocator getCompanyLocator()
	{
		return companyLocator;
	}

	public void setEnvironments(List<EnvironmentInfo> environments)
	{
		this.environments = environments;
	}

	public List<EnvironmentInfo> getEnvironments()
	{
		return environments;
	}

}
