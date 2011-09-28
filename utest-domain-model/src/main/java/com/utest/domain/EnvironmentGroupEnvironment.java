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

public class EnvironmentGroupEnvironment extends TimelineEntity
{

	private Integer	environmentGroupId;
	private Integer	environmentId;
	private boolean	optional	= false;
	private Integer	weightIndex	= 0;

	public EnvironmentGroupEnvironment()
	{
	}

	public EnvironmentGroupEnvironment(final Integer environmentGroupId, final Integer environmentId, final boolean optional, final Integer weightIndex)
	{
		this.environmentGroupId = environmentGroupId;
		this.environmentId = environmentId;
		this.optional = optional;
		this.weightIndex = weightIndex;
	}

	public Integer getEnvironmentGroupId()
	{
		return this.environmentGroupId;
	}

	public void setEnvironmentGroupId(final Integer environmentGroupId)
	{
		this.environmentGroupId = environmentGroupId;
	}

	public Integer getEnvironmentId()
	{
		return this.environmentId;
	}

	public void setEnvironmentId(final Integer environmentId)
	{
		this.environmentId = environmentId;
	}

	public boolean isOptional()
	{
		return this.optional;
	}

	public void setOptional(final boolean optional)
	{
		this.optional = optional;
	}

	public Integer getWeightIndex()
	{
		return this.weightIndex;
	}

	public void setWeightIndex(final Integer weightIndex)
	{
		this.weightIndex = weightIndex;
	}

}
