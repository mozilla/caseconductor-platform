/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
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
package com.utest.domain.view;

import com.utest.domain.CompanyDependable;
import com.utest.domain.Named;
import com.utest.domain.ParentDependable;
import com.utest.domain.TimelineEntity;

public class EnvironmentView extends TimelineEntity implements Named, ParentDependable, CompanyDependable
{
	private Integer	environmentTypeId;
	private Integer	companyId;
	private String	name;
	private String	localeCode;
	private Integer	sortOrder;

	public EnvironmentView()
	{
	}

	public Integer getEnvironmentTypeId()
	{
		return environmentTypeId;
	}

	public void setEnvironmentTypeId(Integer environmentTypeId)
	{
		this.environmentTypeId = environmentTypeId;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocaleCode()
	{
		return localeCode;
	}

	public void setLocaleCode(String localeCode)
	{
		this.localeCode = localeCode;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
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
	public void setParentId(Integer parentId)
	{
		setEnvironmentTypeId(parentId);
	}
}
