/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
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
@XmlType(name = "includedtestcase")
public class TestRunTestCaseInfo extends BaseInfo
{
	@XmlElement(required = false)
	private String			name;
	@XmlElement(required = false)
	private Integer			testCaseStatusId;
	@XmlElement(required = false)
	private String			description;
	@XmlElement(required = false)
	private Integer			majorVersion;
	@XmlElement(required = false)
	private Integer			minorVersion;
	@XmlElement(required = false)
	private boolean			latestVersion;
	@XmlElement(required = false)
	private Integer			approvalStatusId;
	@XmlElement(required = false)
	private Integer			approvedBy;
	@XmlElement(required = false)
	private Date			approveDate;
	@XmlElement(required = false)
	private boolean			automated;
	@XmlElement(required = false)
	private String			automationUri;
	@XmlElement(required = false)
	private Integer			testCycleId;
	@XmlElement(type = ResourceLocator.class, name = "testCycleLocator")
	private ResourceLocator	testCycleLocator;
	@XmlElement(required = false)
	private Integer			productId;
	@XmlElement(type = ResourceLocator.class, name = "productLocator")
	private ResourceLocator	productLocator;
	@XmlElement(required = false)
	private Integer			companyId;
	@XmlElement(type = ResourceLocator.class, name = "testCaseLocator")
	private ResourceLocator	companyLocator;
	@XmlElement(required = false)
	private Integer			testCaseId;
	@XmlElement(type = ResourceLocator.class, name = "testCaseLocator")
	private ResourceLocator	testCaseLocator;
	@XmlElement(required = true)
	private Integer			testCaseVersionId;
	@XmlElement(type = ResourceLocator.class, name = "testCaseVersionLocator")
	private ResourceLocator	testCaseVersionLocator;
	@XmlElement(required = false)
	private Integer			priorityId	= 0;
	@XmlElement(required = false)
	private Integer			runOrder	= 0;
	@XmlElement(required = false)
	private String			blocking	= "false";
	@XmlElement(required = false)
	private Integer			testRunId;
	@XmlElement(type = ResourceLocator.class, name = "testRunLocator")
	private ResourceLocator	testRunLocator;
	@XmlElement(required = false)
	private Integer			testSuiteId;
	@XmlElement(type = ResourceLocator.class, name = "testSuiteLocator")
	private ResourceLocator	testSuiteLocator;

	public Integer getPriorityId()
	{
		return priorityId;
	}

	public Integer getRunOrder()
	{
		return runOrder;
	}

	public String getBlocking()
	{
		return blocking;
	}

	public void setPriorityId(Integer priorityId)
	{
		this.priorityId = priorityId;
	}

	public void setRunOrder(Integer runOrder)
	{
		this.runOrder = runOrder;
	}

	public void setBlocking(String blocking)
	{
		this.blocking = blocking;
	}

	public Integer getTestCaseId()
	{
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId)
	{
		this.testCaseId = testCaseId;
	}

	public Integer getTestCaseVersionId()
	{
		return testCaseVersionId;
	}

	public void setTestCaseVersionId(Integer testCaseVersionId)
	{
		this.testCaseVersionId = testCaseVersionId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public boolean isLatestVersion()
	{
		return latestVersion;
	}

	public void setLatestVersion(boolean latestVersion)
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

	public void setTestCaseLocator(ResourceLocator testCaseLocator)
	{
		this.testCaseLocator = testCaseLocator;
	}

	public ResourceLocator getTestCaseLocator()
	{
		return testCaseLocator;
	}

	public void setTestCaseVersionLocator(ResourceLocator testCaseVersionLocator)
	{
		this.testCaseVersionLocator = testCaseVersionLocator;
	}

	public ResourceLocator getTestCaseVersionLocator()
	{
		return testCaseVersionLocator;
	}

	public Integer getTestRunId()
	{
		return testRunId;
	}

	public void setTestRunId(Integer testRunId)
	{
		this.testRunId = testRunId;
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

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}

	public ResourceLocator getProductLocator()
	{
		return productLocator;
	}

	public void setProductLocator(ResourceLocator productLocator)
	{
		this.productLocator = productLocator;
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

	public ResourceLocator getTestRunLocator()
	{
		return testRunLocator;
	}

	public void setTestRunLocator(ResourceLocator testRunLocator)
	{
		this.testRunLocator = testRunLocator;
	}

	public Integer getTestSuiteId()
	{
		return testSuiteId;
	}

	public void setTestSuiteId(Integer testSuiteId)
	{
		this.testSuiteId = testSuiteId;
	}

	public ResourceLocator getTestSuiteLocator()
	{
		return testSuiteLocator;
	}

	public void setTestSuiteLocator(ResourceLocator testSuiteLocator)
	{
		this.testSuiteLocator = testSuiteLocator;
	}
}
