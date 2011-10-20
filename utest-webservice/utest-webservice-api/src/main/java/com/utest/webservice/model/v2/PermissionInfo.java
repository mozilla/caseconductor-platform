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
package com.utest.webservice.model.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "permission")
public class PermissionInfo extends BaseInfo
{
	@XmlElement(required = true)
	private String	name;
	@XmlElement(required = true)
	private String	permissionCode;
	@XmlElement(required = true)
	private String	assignable;
	@XmlElement(required = true)
	private Integer	sortOrder	= 0;

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getPermissionCode()
	{
		return permissionCode;
	}

	public void setPermissionCode(final String permissionCode)
	{
		this.permissionCode = permissionCode;
	}

	public String getAssignable()
	{
		return assignable;
	}

	public void setAssignable(final String assignable)
	{
		this.assignable = assignable;
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
