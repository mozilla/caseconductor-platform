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

/**
 * TestRunResultStatusLocaleId generated by hbm2java
 */

public class TestRunResultStatusLocaleId implements java.io.Serializable
{

	private Integer	testRunResultStatusId;
	private String	localeCode;

	public TestRunResultStatusLocaleId()
	{
	}

	public TestRunResultStatusLocaleId(Integer testRunResultStatusId, String localeCode)
	{
		this.testRunResultStatusId = testRunResultStatusId;
		this.localeCode = localeCode;
	}

	public Integer getTestRunResultStatusId()
	{
		return this.testRunResultStatusId;
	}

	public void setTestRunResultStatusId(Integer testRunResultStatusId)
	{
		this.testRunResultStatusId = testRunResultStatusId;
	}

	public String getLocaleCode()
	{
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	@Override
	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TestRunResultStatusLocaleId))
			return false;
		TestRunResultStatusLocaleId castOther = (TestRunResultStatusLocaleId) other;

		return ((this.getTestRunResultStatusId() == castOther.getTestRunResultStatusId()) || (this.getTestRunResultStatusId() != null
				&& castOther.getTestRunResultStatusId() != null && this.getTestRunResultStatusId().equals(castOther.getTestRunResultStatusId())))
				&& ((this.getLocaleCode() == castOther.getLocaleCode()) || (this.getLocaleCode() != null && castOther.getLocaleCode() != null && this.getLocaleCode().equals(
						castOther.getLocaleCode())));
	}

	@Override
	public int hashCode()
	{
		int result = 17;

		result = 37 * result + (getTestRunResultStatusId() == null ? 0 : this.getTestRunResultStatusId().hashCode());
		result = 37 * result + (getLocaleCode() == null ? 0 : this.getLocaleCode().hashCode());
		return result;
	}

}
