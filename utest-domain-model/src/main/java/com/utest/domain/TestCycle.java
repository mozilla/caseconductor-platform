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
 * TestCycle generated by hbm2java
 */
public class TestCycle extends TimelineEntity implements ProductDependable, EnvironmentDependable, TeamDependable, Named
{

	private String	name;
	private String	description;
	private Integer	testCycleStatusId;
	private Integer	productId;
	private Date	startDate;
	private Date	endDate;
	private boolean	communityAuthoringAllowed;
	private boolean	communityAccessAllowed;
	private Integer	environmentProfileId;
	private Integer	teamId;

	public TestCycle()
	{
		super();
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public Integer getTestCycleStatusId()
	{
		return testCycleStatusId;
	}

	public void setTestCycleStatusId(final Integer testCycleStatusId)
	{
		this.testCycleStatusId = testCycleStatusId;
	}

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(final Integer productId)
	{
		this.productId = productId;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	public boolean isCommunityAuthoringAllowed()
	{
		return communityAuthoringAllowed;
	}

	public void setCommunityAuthoringAllowed(final boolean communityAuthoringAllowed)
	{
		this.communityAuthoringAllowed = communityAuthoringAllowed;
	}

	public boolean isCommunityAccessAllowed()
	{
		return communityAccessAllowed;
	}

	public void setCommunityAccessAllowed(final boolean communityAccessAllowed)
	{
		this.communityAccessAllowed = communityAccessAllowed;
	}

	public Integer getEnvironmentProfileId()
	{
		return environmentProfileId;
	}

	public void setEnvironmentProfileId(final Integer environmentProfileId)
	{
		this.environmentProfileId = environmentProfileId;
	}

	public void setTeamId(Integer teamId)
	{
		this.teamId = teamId;
	}

	public Integer getTeamId()
	{
		return teamId;
	}

}
