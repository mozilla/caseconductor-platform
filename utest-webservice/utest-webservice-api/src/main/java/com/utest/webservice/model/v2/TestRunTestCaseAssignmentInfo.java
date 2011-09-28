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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "testcaseassignment")
public class TestRunTestCaseAssignmentInfo extends BaseInfo
{
	@XmlElement(required = false)
	private Integer			testCycleId;
	@XmlElement(type = ResourceLocator.class, name = "testCycleLocator")
	private ResourceLocator	testCycleLocator;
	@XmlElement(required = false)
	private Integer			companyId;
	@XmlElement(type = ResourceLocator.class, name = "companyLocator")
	private ResourceLocator	companyLocator;
	@XmlElement(required = false)
	private Integer			testRunId;
	@XmlElement(type = ResourceLocator.class, name = "testRunLocator")
	private ResourceLocator	testRunLocator;
	@XmlElement(required = false)
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
	@XmlElement(required = false)
	private Integer			testCaseVersionId;
	@XmlElement(type = ResourceLocator.class, name = "testCaseVersionLocator")
	private ResourceLocator	testCaseVersionLocator;
	@XmlElement(required = false)
	private Integer			testSuiteId;
	@XmlElement(type = ResourceLocator.class, name = "testSuiteLocator")
	private ResourceLocator	testSuiteLocator;

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
