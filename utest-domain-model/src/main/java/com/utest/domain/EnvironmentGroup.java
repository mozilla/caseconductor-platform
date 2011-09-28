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

public class EnvironmentGroup extends TimelineEntity implements CompanyDependable, Named
{

	private boolean	executed;
	private boolean	deprecated;
	private Integer	environmentTypeId;
	private String	externalIdentifier;
	private Integer	companyId;
	private String	name;
	private String	description;

	public EnvironmentGroup()
	{
	}

	public EnvironmentGroup(final String name)
	{
		this.name = name;
	}

	public EnvironmentGroup(final String name, final String description)
	{
		this.name = name;
		this.description = description;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public void setCompanyId(final Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public boolean isExecuted()
	{
		return executed;
	}

	public void setExecuted(final boolean executed)
	{
		this.executed = executed;
	}

	public boolean isDeprecated()
	{
		return deprecated;
	}

	public void setDeprecated(final boolean deprecated)
	{
		this.deprecated = deprecated;
	}

	public void setEnvironmentTypeId(Integer environmentTypeId)
	{
		this.environmentTypeId = environmentTypeId;
	}

	public Integer getEnvironmentTypeId()
	{
		return environmentTypeId;
	}

	public void setExternalIdentifier(String externalIdentifier)
	{
		this.externalIdentifier = externalIdentifier;
	}

	public String getExternalIdentifier()
	{
		return externalIdentifier;
	}

}
