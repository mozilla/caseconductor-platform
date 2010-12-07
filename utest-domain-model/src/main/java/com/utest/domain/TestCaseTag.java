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
 * TestCaseTag generated by hbm2java
 */
public class TestCaseTag extends TimelineEntity implements Named
{

	private Integer	testCaseId;
	private String	name;

	public TestCaseTag()
	{
	}

	public TestCaseTag(final Integer testCaseId, final String name, final Integer createdBy, final Date createDate, final Integer lastChangedBy, final Date lastChangeDate)
	{
		super(createdBy, createDate, lastChangedBy, lastChangeDate);
		this.testCaseId = testCaseId;
		this.setName(name);
	}

	public Integer getTestCaseId()
	{
		return this.testCaseId;
	}

	public void setTestCaseId(final Integer testCaseId)
	{
		this.testCaseId = testCaseId;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

}
