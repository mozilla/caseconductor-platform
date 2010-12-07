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

public class EnvironmentLocale extends TimelineEntity implements Named, LocaleDescriptable
{

	private Integer	environmentId;
	private String	name;
	private String	localeCode;
	private Integer	sortOrder;

	public EnvironmentLocale()
	{
	}

	public Integer getEnvironmentId()
	{
		return environmentId;
	}

	public void setEnvironmentId(final Integer environmentId)
	{
		this.environmentId = environmentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getLocaleCode()
	{
		return localeCode;
	}

	public void setLocaleCode(final String localeCode)
	{
		this.localeCode = localeCode;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(final Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Override
	public String getDescription()
	{
		return name;
	}

	@Override
	public Integer getEntityId()
	{
		return environmentId;
	}

}
