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

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "testsuite")
public class TestRunInfo extends BaseInfo
{

	@XmlElement(required = true)
	private String			name;
	@XmlElement(required = true)
	private String			description;
	@XmlElement(required = true)
	private Integer			productId;
	@XmlElement(type = ResourceLocator.class, name = "productLocator")
	private ResourceLocator	productLocator;
	@XmlElement(required = false)
	private Integer			testRunStatusId;
	@XmlElement(required = false)
	private Integer			testCycleId;
	@XmlElement(type = ResourceLocator.class, name = "testCycleLocator")
	private ResourceLocator	testCycleLocator;
	@XmlElement(required = false)
	private Date			startDate;
	@XmlElement(required = false)
	private Date			endDate;
	@XmlElement(required = false)
	private boolean			selfAssignAllowed;
	@XmlElement(required = false)
	private Integer			selfAssignLimit;
	@XmlElement(required = false)
	private boolean			selfAssignPerEnvironment;
	@XmlElement(required = false)
	private boolean			useLatestVersions;

	public TestRunInfo()
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

	public void setUseLatestVersions(boolean useLatestVersions)
	{
		this.useLatestVersions = useLatestVersions;
	}

	public boolean isUseLatestVersions()
	{
		return useLatestVersions;
	}

	public void setProductLocator(ResourceLocator productLocator)
	{
		this.productLocator = productLocator;
	}

	public ResourceLocator getProductLocator()
	{
		return productLocator;
	}

	public Integer getTestRunStatusId()
	{
		return testRunStatusId;
	}

	public void setTestRunStatusId(Integer testRunStatusId)
	{
		this.testRunStatusId = testRunStatusId;
	}

	public Integer getTestCycleId()
	{
		return testCycleId;
	}

	public void setTestCycleId(Integer testCycleId)
	{
		this.testCycleId = testCycleId;
	}

	public ResourceLocator getTestCycleLocator()
	{
		return testCycleLocator;
	}

	public void setTestCycleLocator(ResourceLocator testCycleLocator)
	{
		this.testCycleLocator = testCycleLocator;
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

	public boolean isSelfAssignAllowed()
	{
		return selfAssignAllowed;
	}

	public void setSelfAssignAllowed(boolean selfAssignAllowed)
	{
		this.selfAssignAllowed = selfAssignAllowed;
	}

	public Integer getSelfAssignLimit()
	{
		return selfAssignLimit;
	}

	public void setSelfAssignLimit(Integer selfAssignLimit)
	{
		this.selfAssignLimit = selfAssignLimit;
	}

	public boolean isSelfAssignPerEnvironment()
	{
		return selfAssignPerEnvironment;
	}

	public void setSelfAssignPerEnvironment(boolean selfAssignPerEnvironment)
	{
		this.selfAssignPerEnvironment = selfAssignPerEnvironment;
	}

}
