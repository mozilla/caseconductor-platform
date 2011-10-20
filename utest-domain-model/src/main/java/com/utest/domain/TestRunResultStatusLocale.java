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

/**
 * TestRunResultStatusLocale generated by hbm2java
 */
public class TestRunResultStatusLocale implements LocaleDescriptable
{

	private TestRunResultStatusLocaleId	id;
	private String						name;
	private Integer						sortOrder;
	private boolean						deleted;

	public TestRunResultStatusLocale()
	{
	}

	public TestRunResultStatusLocale(TestRunResultStatusLocaleId id, String name, Integer sortOrder)
	{
		this.id = id;
		this.name = name;
		this.sortOrder = sortOrder;
	}

	public TestRunResultStatusLocaleId getId()
	{
		return this.id;
	}

	public void setId(TestRunResultStatusLocaleId id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getSortOrder()
	{
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Override
	public Integer getEntityId()
	{
		return getId().getTestRunResultStatusId();
	}

	@Override
	public String getLocaleCode()
	{
		return getId().getLocaleCode();
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
