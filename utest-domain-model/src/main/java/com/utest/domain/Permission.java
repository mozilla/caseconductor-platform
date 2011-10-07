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

public class Permission extends Entity implements Named
{
	// TODO - define permissions to modify roles and assign permissions to roles
	// TODO - define super admin permissions and company level admin permissions
	public static final String	DELETED_ENTITY_VIEW				= "PERMISSION_DELETED_ENTITY_VIEW";
	public static final String	DELETED_ENTITY_UNDO				= "PERMISSION_DELETED_ENTITY_UNDO";
	public static final String	FEATURED_LIST_EDIT				= "PERMISSION_FEATURED_LIST_EDIT";
	public static final String	EXTERNAL_BUG_VIEW				= "PERMISSION_EXTERNAL_BUG_VIEW";
	public static final String	EXTERNAL_BUG_EDIT				= "PERMISSION_EXTERNAL_BUG_EDIT";
	public static final String	ATTACHMENT_VIEW					= "PERMISSION_ATTACHMENT_VIEW";
	public static final String	ATTACHMENT_ADD					= "PERMISSION_ATTACHMENT_ADD";
	public static final String	ATTACHMENT_EDIT					= "PERMISSION_ATTACHMENT_EDIT";
	public static final String	ANONYMOUS						= "PERMISSION_ANONYMOUS";
	public static final String	ROLE_EDIT						= "PERMISSION_ROLE_EDIT";
	public static final String	ROLE_PERMISSION_ASSIGN			= "PERMISSION_ROLE_PERMISSION_ASSIGN";
	public static final String	ENVIRONMENT_VIEW				= "PERMISSION_ENVIRONMENT_VIEW";
	public static final String	ENVIRONMENT_EDIT				= "PERMISSION_ENVIRONMENT_EDIT";
	public static final String	COMPANY_INFO_VIEW				= "PERMISSION_COMPANY_INFO_VIEW";
	public static final String	COMPANY_INFO_EDIT				= "PERMISSION_COMPANY_INFO_EDIT";
	public static final String	COMPANY_USERS_VIEW				= "PERMISSION_COMPANY_USERS_VIEW";
	public static final String	COMPANY_USERS_EDIT				= "PERMISSION_COMPANY_USERS_EDIT";
	public static final String	PRODUCT_VIEW					= "PERMISSION_PRODUCT_VIEW";
	public static final String	PRODUCT_EDIT					= "PERMISSION_PRODUCT_EDIT";
	public static final String	TEST_CASE_VIEW					= "PERMISSION_TEST_CASE_VIEW";
	public static final String	TEST_CASE_ADD					= "PERMISSION_TEST_CASE_ADD";
	public static final String	TEST_CASE_EDIT					= "PERMISSION_TEST_CASE_EDIT";
	public static final String	TEST_CASE_APPROVE				= "PERMISSION_TEST_CASE_APPROVE";
	public static final String	TEST_CASE_ACTIVATE				= "PERMISSION_TEST_CASE_ACTIVATE";
	public static final String	TEST_SUITE_VIEW					= "PERMISSION_TEST_SUITE_VIEW";
	public static final String	TEST_SUITE_EDIT					= "PERMISSION_TEST_SUITE_EDIT";
	public static final String	TEST_SUITE_APPROVE				= "PERMISSION_TEST_SUITE_APPROVE";
	public static final String	TEST_SUITE_ACTIVATE				= "PERMISSION_TEST_SUITE_ACTIVATE";
	public static final String	TEST_PLAN_VIEW					= "PERMISSION_TEST_PLAN_VIEW";
	public static final String	TEST_PLAN_EDIT					= "PERMISSION_TEST_PLAN_EDIT";
	public static final String	TEST_PLAN_APPROVE				= "PERMISSION_TEST_PLAN_APPROVE";
	public static final String	TEST_PLAN_ACTIVATE				= "PERMISSION_TEST_PLAN_ACTIVATE";
	public static final String	TEST_CYCLE_VIEW					= "PERMISSION_TEST_CYCLE_VIEW";
	public static final String	TEST_CYCLE_EDIT					= "PERMISSION_TEST_CYCLE_EDIT";
	public static final String	TEST_CYCLE_ACTIVATE				= "PERMISSION_TEST_CYCLE_ACTIVATE";
	public static final String	TEST_RUN_VIEW					= "PERMISSION_TEST_RUN_VIEW";
	public static final String	TEST_RUN_EDIT					= "PERMISSION_TEST_RUN_EDIT";
	public static final String	TEST_RUN_ACTIVATE				= "PERMISSION_TEST_RUN_ACTIVATE";
	public static final String	TEST_RUN_TEST_CASE_ASSIGN		= "PERMISSION_TEST_RUN_TEST_CASE_ASSIGN";
	public static final String	TEST_RUN_TEST_CASE_SELF_ASSIGN	= "PERMISSION_TEST_RUN_TEST_CASE_SELF_ASSIGN";
	public static final String	TEST_RUN_ASSIGNMENT_EXECUTE		= "PERMISSION_TEST_RUN_ASSIGNMENT_EXECUTE";
	public static final String	TEST_RUN_RESULT_APPROVE			= "PERMISSION_TEST_RUN_RESULT_APPROVE";
	public static final String	USER_ACCOUNT_VIEW				= "PERMISSION_USER_ACCOUNT_VIEW";
	public static final String	USER_ACCOUNT_EDIT				= "PERMISSION_USER_ACCOUNT_EDIT";
	public static final String	TEAM_VIEW						= "PERMISSION_TEAM_VIEW";
	public static final String	TEAM_EDIT						= "PERMISSION_TEAM_EDIT";

	private String				permissionCode;
	private String				name;
	private boolean				assignable;
	private Integer				sortOrder						= 0;

	public Permission()
	{
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setPermissionCode(final String permissionCode)
	{
		this.permissionCode = permissionCode;
	}

	public String getPermissionCode()
	{
		return permissionCode;
	}

	public void setAssignable(final boolean assignable)
	{
		this.assignable = assignable;
	}

	public boolean isAssignable()
	{
		return assignable;
	}

	public void setSortOrder(final Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

}
