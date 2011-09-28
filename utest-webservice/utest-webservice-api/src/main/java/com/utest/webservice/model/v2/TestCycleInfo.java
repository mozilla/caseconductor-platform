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
 * distributed under the License get distributed on an "AS IS" BASIS,
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

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "testcycle")
public class TestCycleInfo extends BaseInfo
{
	@XmlElement(required = true)
	private String			name;
	@XmlElement(required = true)
	private String			description;
	@XmlElement(required = true)
	private Integer			companyId;
	@XmlElement(type = ResourceLocator.class, name = "companyLocator")
	private ResourceLocator	companyLocator;
	@XmlElement(required = true)
	private Integer			productId;
	@XmlElement(type = ResourceLocator.class, name = "productLocator")
	private ResourceLocator	productLocator;
	@XmlElement(required = false)
	private Integer			testCycleStatusId;
	@XmlElement(required = false)
	private Date			startDate;
	@XmlElement(required = false)
	private Date			endDate;
	@XmlElement(required = false)
	private String			communityAuthoringAllowed;
	@XmlElement(required = false)
	private String			communityAccessAllowed;
	@XmlElement(required = false)
	private String			featured;

	public TestCycleInfo()
	{
		super();
	}

	@org.apache.cxf.aegis.type.java5.XmlElement(minOccurs = "1", nillable = false)
	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public ResourceLocator getCompanyLocator()
	{
		return companyLocator;
	}

	public void setCompanyLocator(ResourceLocator companyLocator)
	{
		this.companyLocator = companyLocator;
	}

	@org.apache.cxf.aegis.type.java5.XmlElement(minOccurs = "1", nillable = false)
	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(final Integer productId)
	{
		this.productId = productId;
	}

	@org.apache.cxf.aegis.type.java5.XmlElement(minOccurs = "1", nillable = false)
	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setProductLocator(ResourceLocator productLocator)
	{
		this.productLocator = productLocator;
	}

	public ResourceLocator getProductLocator()
	{
		return productLocator;
	}

	public Integer getTestCycleStatusId()
	{
		return testCycleStatusId;
	}

	public void setTestCycleStatusId(Integer testCycleStatusId)
	{
		this.testCycleStatusId = testCycleStatusId;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getCommunityAuthoringAllowed()
	{
		return communityAuthoringAllowed;
	}

	public void setCommunityAuthoringAllowed(String communityAuthoringAllowed)
	{
		this.communityAuthoringAllowed = communityAuthoringAllowed;
	}

	public String getCommunityAccessAllowed()
	{
		return communityAccessAllowed;
	}

	public void setCommunityAccessAllowed(String communityAccessAllowed)
	{
		this.communityAccessAllowed = communityAccessAllowed;
	}

	public void setFeatured(String featured)
	{
		this.featured = featured;
	}

	public String getFeatured()
	{
		return featured;
	}

}
