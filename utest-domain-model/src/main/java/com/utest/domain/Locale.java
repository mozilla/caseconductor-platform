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
 * Antivirus generated by hbm2java
 */

public class Locale implements java.io.Serializable
{
	public static final String	DEFAULT_LOCALE		= "en_US";
	public static final Integer	DEFAULT_SORT_ORDER	= 0;

	private String				code;
	private String				name;
	private Integer				sortOrder;
	private boolean				deleted;

	public Locale()
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

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public void setSortOrder(final Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public boolean isDeleted()
	{
		return deleted;
	}
}
