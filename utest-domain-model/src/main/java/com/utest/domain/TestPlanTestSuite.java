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
 * TestPlanTestSuite generated by hbm2java
 */
public class TestPlanTestSuite extends TimelineEntity
{

	private Integer	testPlanId;
	private Integer	testSuiteId;
	private Integer	runOrder;

	public TestPlanTestSuite()
	{
		super();
	}

	public TestPlanTestSuite(final Integer testSuiteId, final Integer runOrder)
	{
		super();
		this.testSuiteId = testSuiteId;
		this.runOrder = runOrder;
	}

	public Integer getTestSuiteId()
	{
		return this.testSuiteId;
	}

	public void setTestSuiteId(final Integer testSuiteId)
	{
		this.testSuiteId = testSuiteId;
	}

	public Integer getRunOrder()
	{
		return this.runOrder;
	}

	public void setRunOrder(final Integer runOrder)
	{
		this.runOrder = runOrder;
	}

	public void setTestPlanId(Integer testPlanId)
	{
		this.testPlanId = testPlanId;
	}

	public Integer getTestPlanId()
	{
		return testPlanId;
	}

}
