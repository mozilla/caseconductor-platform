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
package com.utest.domain;

// Generated Sep 3, 2010 1:57:10 PM by Hibernate Tools 3.2.4.GA

/**
 * TestRunTestCaseAssignment generated by hbm2java
 */
public class TestRunTestCaseAssignment extends TimelineEntity implements ProductDependable, EnvironmentDependable
{

	private Integer	testRunTestCaseId;
	private Integer	testCycleId;
	private Integer	productId;
	private Integer	companyId;
	private Integer	testRunId;
	private Integer	testCaseId;
	private Integer	testCaseVersionId;
	private Integer	testSuiteId;
	private Integer	testerId;
	private Integer	environmentProfileId;

	public TestRunTestCaseAssignment()
	{
	}

	public TestRunTestCaseAssignment(final Integer testRunTestCaseId, final Integer productId, final Integer testRunId, final Integer testCaseId, final Integer testCaseVersionId,
			final Integer testerId)
	{
		super();
		this.testRunTestCaseId = testRunTestCaseId;
		this.productId = productId;
		this.testRunId = testRunId;
		this.testCaseId = testCaseId;
		this.testCaseVersionId = testCaseVersionId;
		this.testerId = testerId;
	}

	public TestRunTestCaseAssignment(final Integer testRunTestCaseId, final Integer productId, final Integer testRunId, final Integer testCaseId, final Integer testCaseVersionId,
			final Integer testerId, final Integer environmentProfileId)
	{
		super();
		this.testRunTestCaseId = testRunTestCaseId;
		this.productId = productId;
		this.testRunId = testRunId;
		this.testCaseId = testCaseId;
		this.testCaseVersionId = testCaseVersionId;
		this.testerId = testerId;
		this.environmentProfileId = environmentProfileId;
	}

	public Integer getTestRunTestCaseId()
	{
		return this.testRunTestCaseId;
	}

	public void setTestRunTestCaseId(final Integer testRunTestCaseId)
	{
		this.testRunTestCaseId = testRunTestCaseId;
	}

	public Integer getProductId()
	{
		return this.productId;
	}

	public void setProductId(final Integer productId)
	{
		this.productId = productId;
	}

	public Integer getTestRunId()
	{
		return this.testRunId;
	}

	public void setTestRunId(final Integer testRunId)
	{
		this.testRunId = testRunId;
	}

	public Integer getTestCaseId()
	{
		return this.testCaseId;
	}

	public void setTestCaseId(final Integer testCaseId)
	{
		this.testCaseId = testCaseId;
	}

	public Integer getTestCaseVersionId()
	{
		return this.testCaseVersionId;
	}

	public void setTestCaseVersionId(final Integer testCaseVersionId)
	{
		this.testCaseVersionId = testCaseVersionId;
	}

	public Integer getTestCycleId()
	{
		return testCycleId;
	}

	public void setTestCycleId(Integer testCycleId)
	{
		this.testCycleId = testCycleId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getTesterId()
	{
		return this.testerId;
	}

	public void setTesterId(final Integer testerId)
	{
		this.testerId = testerId;
	}

	public Integer getEnvironmentProfileId()
	{
		return this.environmentProfileId;
	}

	public void setEnvironmentProfileId(final Integer environmentProfileId)
	{
		this.environmentProfileId = environmentProfileId;
	}

	public void setTestSuiteId(Integer testSuiteId)
	{
		this.testSuiteId = testSuiteId;
	}

	public Integer getTestSuiteId()
	{
		return testSuiteId;
	}

}
