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
package com.utest.webservice.model.v2;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "testcaseversion")
public class TestCaseVersionInfo extends BaseInfo
{
	@XmlElement(required = false)
	private Integer			companyId;
	@XmlElement(type = ResourceLocator.class, name = "companyLocator")
	private ResourceLocator	companyLocator;
	@XmlElement(required = false)
	private Integer			testCaseId;
	@XmlElement(required = true)
	private Integer			productId;
	@XmlElement(type = ResourceLocator.class, name = "productLocator")
	private ResourceLocator	productLocator;
	@XmlElement(required = false)
	private Integer			maxAttachmentSizeInMbytes;
	@XmlElement(required = false)
	private Integer			maxNumberOfAttachments;
	@XmlElement(required = false)
	private Integer			testCycleId;
	@XmlElement(type = ResourceLocator.class, name = "testCycleLocator")
	private ResourceLocator	testCycleLocator;
	@XmlElement(required = true)
	private Integer			testCaseStatusId;
	@XmlElement(required = true)
	private String			name;
	@XmlElement(required = false)
	private String			description;
	@XmlElement(required = false)
	private Integer			majorVersion;
	@XmlElement(required = false)
	private Integer			minorVersion;
	@XmlElement(required = false)
	private String			latestVersion;
	@XmlElement(required = false)
	private Integer			approvalStatusId;
	@XmlElement(required = false)
	private Integer			approvedBy;
	@XmlElement(type = ResourceLocator.class, name = "approvedByLocator")
	private ResourceLocator	approvedByLocator;
	@XmlElement(required = false)
	private Date			approveDate;
	@XmlElement(required = false)
	private boolean			automated;
	@XmlElement(required = false)
	private String			automationUri;

	public Integer getTestCaseId()
	{
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId)
	{
		this.testCaseId = testCaseId;
	}

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}

	public Integer getMaxAttachmentSizeInMbytes()
	{
		return maxAttachmentSizeInMbytes;
	}

	public void setMaxAttachmentSizeInMbytes(Integer maxAttachmentSizeInMbytes)
	{
		this.maxAttachmentSizeInMbytes = maxAttachmentSizeInMbytes;
	}

	public Integer getMaxNumberOfAttachments()
	{
		return maxNumberOfAttachments;
	}

	public void setMaxNumberOfAttachments(Integer maxNumberOfAttachments)
	{
		this.maxNumberOfAttachments = maxNumberOfAttachments;
	}

	public Integer getTestCycleId()
	{
		return testCycleId;
	}

	public void setTestCycleId(Integer testCycleId)
	{
		this.testCycleId = testCycleId;
	}

	public boolean isAutomated()
	{
		return automated;
	}

	public void setAutomated(boolean automated)
	{
		this.automated = automated;
	}

	public String getAutomationUri()
	{
		return automationUri;
	}

	public void setAutomationUri(String automationUri)
	{
		this.automationUri = automationUri;
	}

	public Integer getTestCaseStatusId()
	{
		return testCaseStatusId;
	}

	public void setTestCaseStatusId(Integer testCaseStatusId)
	{
		this.testCaseStatusId = testCaseStatusId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Integer getMajorVersion()
	{
		return majorVersion;
	}

	public void setMajorVersion(Integer majorVersion)
	{
		this.majorVersion = majorVersion;
	}

	public Integer getMinorVersion()
	{
		return minorVersion;
	}

	public void setMinorVersion(Integer minorVersion)
	{
		this.minorVersion = minorVersion;
	}

	public String getLatestVersion()
	{
		return latestVersion;
	}

	public void setLatestVersion(String latestVersion)
	{
		this.latestVersion = latestVersion;
	}

	public Integer getApprovalStatusId()
	{
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId)
	{
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getApprovedBy()
	{
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy)
	{
		this.approvedBy = approvedBy;
	}

	public Date getApproveDate()
	{
		return approveDate;
	}

	public void setApproveDate(Date approveDate)
	{
		this.approveDate = approveDate;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setProductLocator(ResourceLocator productLocator)
	{
		this.productLocator = productLocator;
	}

	public ResourceLocator getProductLocator()
	{
		return productLocator;
	}

	public void setApprovedByLocator(ResourceLocator approvedByLocator)
	{
		this.approvedByLocator = approvedByLocator;
	}

	public ResourceLocator getApprovedByLocator()
	{
		return approvedByLocator;
	}

	public void setTestCycleLocator(ResourceLocator testCycleLocator)
	{
		this.testCycleLocator = testCycleLocator;
	}

	public ResourceLocator getTestCycleLocator()
	{
		return testCycleLocator;
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

}
