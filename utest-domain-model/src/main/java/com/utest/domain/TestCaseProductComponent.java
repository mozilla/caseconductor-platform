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

import java.util.Date;

/**
 * TestCaseProductComponent generated by hbm2java
 */
public class TestCaseProductComponent extends TimelineEntity
{

	private Integer	testCaseId;
	private Integer	productComponentId;

	public TestCaseProductComponent()
	{
		super();
	}

	public TestCaseProductComponent(final Integer testCaseId, final Integer productComponentId, final Integer createdBy, final Date createDate, final Integer lastChangedBy,
			final Date lastChangeDate)
	{
		super(createdBy, createDate, lastChangedBy, lastChangeDate);
		this.testCaseId = testCaseId;
		this.productComponentId = productComponentId;
	}

	public Integer getTestCaseId()
	{
		return this.testCaseId;
	}

	public void setTestCaseId(final Integer testCaseId)
	{
		this.testCaseId = testCaseId;
	}

	public Integer getProductComponentId()
	{
		return this.productComponentId;
	}

	public void setProductComponentId(final Integer productComponentId)
	{
		this.productComponentId = productComponentId;
	}

}
