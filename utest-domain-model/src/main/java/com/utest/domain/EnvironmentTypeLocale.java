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
package com.utest.domain;

// Generated Oct 7, 2009 11:18:35 AM by Hibernate Tools 3.2.4.GA

public class EnvironmentTypeLocale extends TimelineEntity implements Named, LocaleDescriptable
{

	private Integer	environmentTypeId;
	private String	name;
	private String	localeCode;
	private Integer	sortOrder;

	public EnvironmentTypeLocale()
	{
	}

	public Integer getEnvironmentTypeId()
	{
		return environmentTypeId;
	}

	public void setEnvironmentTypeId(final Integer environmentTypeId)
	{
		this.environmentTypeId = environmentTypeId;
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

	// @Override
	// public String getDescription()
	// {
	// return name;
	// }

	@Override
	public Integer getEntityId()
	{
		return environmentTypeId;
	}

}
