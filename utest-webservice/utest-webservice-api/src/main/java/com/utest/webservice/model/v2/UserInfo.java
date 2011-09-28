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
@XmlType(name = "user")
public class UserInfo extends BaseInfo
{

	@XmlElement(required = false)
	private String			password;
	@XmlElement(required = false)
	private String			screenName;
	@XmlElement(required = false)
	private String			firstName;
	@XmlElement(required = false)
	private String			lastName;
	@XmlElement(required = true)
	private String			email;
	@XmlElement(required = false)
	private Integer			userStatusId;
	@XmlElement(required = false)
	private Integer			companyId;
	@XmlElement(type = ResourceLocator.class, name = "companyLocator")
	private ResourceLocator	companyLocator;

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Integer getUserStatusId()
	{
		return userStatusId;
	}

	public void setUserStatusId(Integer userStatusId)
	{
		this.userStatusId = userStatusId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public void setScreenName(String screenName)
	{
		this.screenName = screenName;
	}

	public String getScreenName()
	{
		return screenName;
	}

	public void setCompanyLocator(ResourceLocator companyLocator)
	{
		this.companyLocator = companyLocator;
	}

	public ResourceLocator getCompanyLocator()
	{
		return companyLocator;
	}

}
