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
@XmlType(name = "testresult")
public class TestRunResultInfo extends BaseInfo
{

	@XmlElement(required = true)
	private Integer			testRunId;
	@XmlElement(type = ResourceLocator.class, name = "testRunLocator")
	private ResourceLocator	testRunLocator;
	@XmlElement(required = false)
	private Integer			testRunResultStatusId;
	@XmlElement(required = false)
	private Date			runDate;
	@XmlElement(required = false)
	private String			actualResult;
	@XmlElement(required = false)
	private Integer			failedStepNumber;
	@XmlElement(required = false)
	private Integer			actualTimeInMin;
	@XmlElement(required = false)
	private String			comment;
	@XmlElement(required = false)
	private Integer			approvalStatusId;
	@XmlElement(required = false)
	private Integer			approvedBy;
	@XmlElement(type = ResourceLocator.class, name = "approvedByLocator")
	private ResourceLocator	approvedByLocator;
	@XmlElement(required = false)
	private Date			approveDate;
	@XmlElement(required = true)
	private Integer			productId;
	@XmlElement(type = ResourceLocator.class, name = "productLocator")
	private ResourceLocator	productLocator;
	@XmlElement(required = true)
	private Integer			testerId;
	@XmlElement(type = ResourceLocator.class, name = "testerLocator")
	private ResourceLocator	testerLocator;
	@XmlElement(required = false)
	private Integer			testCaseId;
	@XmlElement(type = ResourceLocator.class, name = "testCaseLocator")
	private ResourceLocator	testCaseLocator;
	@XmlElement(required = true)
	private Integer			testCaseVersionId;
	@XmlElement(type = ResourceLocator.class, name = "testCaseVersionLocator")
	private ResourceLocator	testCaseVersionLocator;

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

	public Integer getTesterId()
	{
		return testerId;
	}

	public void setTesterId(Integer testerId)
	{
		this.testerId = testerId;
	}

	public ResourceLocator getTesterLocator()
	{
		return testerLocator;
	}

	public void setTesterLocator(ResourceLocator testerLocator)
	{
		this.testerLocator = testerLocator;
	}

	public Integer getTestCaseId()
	{
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId)
	{
		this.testCaseId = testCaseId;
	}

	public ResourceLocator getTestCaseLocator()
	{
		return testCaseLocator;
	}

	public void setTestCaseLocator(ResourceLocator testCaseLocator)
	{
		this.testCaseLocator = testCaseLocator;
	}

	public Integer getTestCaseVersionId()
	{
		return testCaseVersionId;
	}

	public void setTestCaseVersionId(Integer testCaseVersionId)
	{
		this.testCaseVersionId = testCaseVersionId;
	}

	public ResourceLocator getTestCaseVersionLocator()
	{
		return testCaseVersionLocator;
	}

	public void setTestCaseVersionLocator(ResourceLocator testCaseVersionLocator)
	{
		this.testCaseVersionLocator = testCaseVersionLocator;
	}

	public Integer getTestRunId()
	{
		return testRunId;
	}

	public void setTestRunId(Integer testRunId)
	{
		this.testRunId = testRunId;
	}

	public ResourceLocator getTestRunLocator()
	{
		return testRunLocator;
	}

	public void setTestRunLocator(ResourceLocator testRunLocator)
	{
		this.testRunLocator = testRunLocator;
	}

	public Integer getTestRunResultStatusId()
	{
		return testRunResultStatusId;
	}

	public void setTestRunResultStatusId(Integer testRunResultStatusId)
	{
		this.testRunResultStatusId = testRunResultStatusId;
	}

	public Date getRunDate()
	{
		return runDate;
	}

	public void setRunDate(Date runDate)
	{
		this.runDate = runDate;
	}

	public String getActualResult()
	{
		return actualResult;
	}

	public void setActualResult(String actualResult)
	{
		this.actualResult = actualResult;
	}

	public Integer getFailedStepNumber()
	{
		return failedStepNumber;
	}

	public void setFailedStepNumber(Integer failedStepNumber)
	{
		this.failedStepNumber = failedStepNumber;
	}

	public Integer getActualTimeInMin()
	{
		return actualTimeInMin;
	}

	public void setActualTimeInMin(Integer actualTimeInMin)
	{
		this.actualTimeInMin = actualTimeInMin;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
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

	public void setApprovedByLocator(ResourceLocator approvedByLocator)
	{
		this.approvedByLocator = approvedByLocator;
	}

	public ResourceLocator getApprovedByLocator()
	{
		return approvedByLocator;
	}

}
