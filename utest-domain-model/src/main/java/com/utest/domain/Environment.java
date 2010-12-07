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

import java.util.Date;

public class Environment extends LocalizedEntity implements TimelineVersionable, ParentDependable, CompanyDependable
{
	private Integer	environmentTypeId;
	private Integer	companyId;
	private Integer	createdBy;
	private Date	createDate;
	private Integer	lastChangedBy;
	private Date	lastChangeDate;
	private Integer	version;

	public Environment()
	{
	}

	public void setEnvironmentTypeId(final Integer environmentTypeId)
	{
		this.environmentTypeId = environmentTypeId;
	}

	public Integer getEnvironmentTypeId()
	{
		return environmentTypeId;
	}

	@Override
	public Integer getChildId()
	{
		return getId();
	}

	@Override
	public Integer getParentId()
	{
		return getEnvironmentTypeId();
	}

	@Override
	public void setParentId(final Integer parentId)
	{
		setEnvironmentTypeId(parentId);

	}

	public void setCompanyId(final Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public Integer getCreatedBy()
	{
		return this.createdBy;
	}

	public void setCreatedBy(final Integer createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getCreateDate()
	{
		return this.createDate;
	}

	public void setCreateDate(final Date createDate)
	{
		this.createDate = createDate;
	}

	public Integer getLastChangedBy()
	{
		return this.lastChangedBy;
	}

	public void setLastChangedBy(final Integer lastChangedBy)
	{
		this.lastChangedBy = lastChangedBy;
	}

	public Date getLastChangeDate()
	{
		return this.lastChangeDate;
	}

	public void setLastChangeDate(final Date lastChangeDate)
	{
		this.lastChangeDate = lastChangeDate;
	}

	public void setVersion(final Integer version)
	{
		this.version = version;
	}

	public Integer getVersion()
	{
		return version;
	}

}
