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

public class EnvironmentType extends LocalizedEntity implements TimelineVersionable, CompanyDependable
{

	/**
	 * optional id of parent type
	 */
	private Integer	parentEnvironmentTypeId	= null;
	private Integer	companyId;
	private boolean	groupType;
	private Integer	createdBy;
	private Date	createDate;
	private Integer	lastChangedBy;
	private Date	lastChangeDate;
	private Integer	version;

	public Integer getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(final Integer createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(final Date createDate)
	{
		this.createDate = createDate;
	}

	public Integer getLastChangedBy()
	{
		return lastChangedBy;
	}

	public void setLastChangedBy(final Integer lastChangedBy)
	{
		this.lastChangedBy = lastChangedBy;
	}

	public Date getLastChangeDate()
	{
		return lastChangeDate;
	}

	public void setLastChangeDate(final Date lastChangeDate)
	{
		this.lastChangeDate = lastChangeDate;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(final Integer version)
	{
		this.version = version;
	}

	public EnvironmentType()
	{
	}

	public void setParentEnvironmentTypeId(final Integer parentEnvironmentTypeId)
	{
		this.parentEnvironmentTypeId = parentEnvironmentTypeId;
	}

	public Integer getParentEnvironmentTypeId()
	{
		return parentEnvironmentTypeId;
	}

	public void setCompanyId(final Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setGroupType(boolean groupType)
	{
		this.groupType = groupType;
	}

	public boolean isGroupType()
	{
		return groupType;
	}

}
