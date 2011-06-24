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

// Generated Sep 3, 2010 1:57:10 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;

import com.utest.domain.CompanyDependable;
import com.utest.domain.EnvironmentDependable;
import com.utest.domain.ProductDependable;
import com.utest.domain.TimelineEntity;
import com.utest.domain.Versioned;

public class TestSuiteTestCaseView extends TimelineEntity implements ProductDependable, CompanyDependable, EnvironmentDependable, Versioned
{
	private Integer	testCaseId;
	private Integer	productId;
	private Integer	companyId;
	private String	name;
	private Integer	testCaseStatusId;
	private String	description;
	private Integer	majorVersion;
	private Integer	minorVersion;
	private boolean	latestVersion;
	private Integer	approvalStatusId;
	private Integer	approvedBy;
	private Date	approveDate;
	private boolean	automated;
	private String	automationUri;
	private Integer	environmentProfileId;
	private Integer	testCaseVersionId;
	private Integer	priorityId;
	private Integer	runOrder;
	private boolean	blocking;
	private Integer	testSuiteId;

	public TestSuiteTestCaseView()
	{
	}

	public Integer getTestCaseId()
	{
		return this.testCaseId;
	}

	public void setTestCaseId(final Integer testCaseId)
	{
		this.testCaseId = testCaseId;
	}

	public Integer getTestCaseStatusId()
	{
		return this.testCaseStatusId;
	}

	public void setTestCaseStatusId(final Integer testCaseStatusId)
	{
		this.testCaseStatusId = testCaseStatusId;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public Integer getMajorVersion()
	{
		return this.majorVersion;
	}

	public void setMajorVersion(final Integer majorVersion)
	{
		this.majorVersion = majorVersion;
	}

	public Integer getTestCaseVersionId()
	{
		return testCaseVersionId;
	}

	public void setTestCaseVersionId(Integer testCaseVersionId)
	{
		this.testCaseVersionId = testCaseVersionId;
	}

	public Integer getPriorityId()
	{
		return priorityId;
	}

	public void setPriorityId(Integer priorityId)
	{
		this.priorityId = priorityId;
	}

	public Integer getRunOrder()
	{
		return runOrder;
	}

	public void setRunOrder(Integer runOrder)
	{
		this.runOrder = runOrder;
	}

	public boolean isBlocking()
	{
		return blocking;
	}

	public void setBlocking(boolean blocking)
	{
		this.blocking = blocking;
	}

	public Integer getTestSuiteId()
	{
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId)
	{
		this.testSuiteId = testSuiteId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public Integer getMinorVersion()
	{
		return this.minorVersion;
	}

	public void setMinorVersion(final Integer minorVersion)
	{
		this.minorVersion = minorVersion;
	}

	public boolean isLatestVersion()
	{
		return this.latestVersion;
	}

	public void setLatestVersion(final boolean latestVersion)
	{
		this.latestVersion = latestVersion;
	}

	public Integer getApprovalStatusId()
	{
		return this.approvalStatusId;
	}

	public void setApprovalStatusId(final Integer approvalStatusId)
	{
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getApprovedBy()
	{
		return this.approvedBy;
	}

	public void setApprovedBy(final Integer approvedBy)
	{
		this.approvedBy = approvedBy;
	}

	public Date getApproveDate()
	{
		return this.approveDate;
	}

	public void setApproveDate(final Date approveDate)
	{
		this.approveDate = approveDate;
	}

	public Integer getEnvironmentProfileId()
	{
		return this.environmentProfileId;
	}

	public void setEnvironmentProfileId(final Integer environmentProfileId)
	{
		this.environmentProfileId = environmentProfileId;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setProductId(final Integer productId)
	{
		this.productId = productId;
	}

	public Integer getProductId()
	{
		return productId;
	}

	@Override
	public Integer getMainEntityIdentifier()
	{
		return testCaseId;
	}

	@Override
	public String getMainEntityIdentifierName()
	{
		return "testCaseId";
	}
}
