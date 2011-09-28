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

// Generated Oct 7, 2009 11:18:35 AM by Hibernate Tools 3.2.4.GA

/**
 * AccessRole generated by hbm2java
 */
import java.util.Arrays;
import java.util.List;

public class AccessRole extends TimelineEntity implements Named
{

	public static final Integer		ROLE_ADMIN_ID			= new Integer(1);
	public static final Integer		ROLE_TM_ID				= new Integer(2);
	public static final Integer		ROLE_TESTER_ID			= new Integer(3);
	public static final Integer		ROLE_TM_ADMIN_ID		= new Integer(4);
	public static final Integer		ROLE_FINANCE_MGR_ID		= new Integer(5);
	public static final Integer		ROLE_PROJECT_MGR_ID		= new Integer(6);
	public static final Integer		ROLE_COMMUNITY_MGR_ID	= new Integer(7);
	// TODO -define roles for super admin and company admin
	// super admin role cannot be assigned to company level user
	public static final Integer[]	PROTECTED_SYSTEM_ROLES	= { 1 };
	private Integer					companyId;
	private String					name;
	private Integer					sortOrder;

	public AccessRole()
	{
	}

	public AccessRole(final Integer companyId, final String name, final Integer sortOrder)
	{
		this.companyId = companyId;
		this.name = name;
		this.sortOrder = sortOrder;
	}

	public AccessRole(final String name, final Integer sortOrder)
	{
		this.name = name;
		this.sortOrder = sortOrder;
	}

	public static List<Integer> getProtectedSystemRoleIds()
	{
		return Arrays.asList(PROTECTED_SYSTEM_ROLES);
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public Integer getSortOrder()
	{
		return this.sortOrder;
	}

	public void setSortOrder(final Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public void setCompanyId(final Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

}
