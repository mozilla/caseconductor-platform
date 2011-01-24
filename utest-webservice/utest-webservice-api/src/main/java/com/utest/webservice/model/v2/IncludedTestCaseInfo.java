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
@XmlType(name = "includedtestcase")
public class IncludedTestCaseInfo extends BaseInfo
{
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
	private boolean			blocking	= false;
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

	public boolean isBlocking()
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

	public void setBlocking(boolean blocking)
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
