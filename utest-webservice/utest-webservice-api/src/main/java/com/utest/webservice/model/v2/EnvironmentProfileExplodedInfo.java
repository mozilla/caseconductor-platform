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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environmentprofile")
public class EnvironmentProfileExplodedInfo extends BaseInfo
{
	@XmlElement(required = false)
	private Integer								companyId;
	@XmlElement(type = ResourceLocator.class, name = "companyLocator")
	private ResourceLocator						companyLocator;
	@XmlElement(required = true)
	private String								name;
	@XmlElement(required = false)
	private String								description;
	@XmlElement(required = false)
	private List<EnvironmentInfo>				environments;
	@XmlElement(required = false)
	private List<EnvironmentGroupExplodedInfo>	environmentGroups;

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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setCompanyLocator(ResourceLocator companyLocator)
	{
		this.companyLocator = companyLocator;
	}

	public ResourceLocator getCompanyLocator()
	{
		return companyLocator;
	}

	public void setEnvironments(List<EnvironmentInfo> environments)
	{
		this.environments = environments;
	}

	public List<EnvironmentInfo> getEnvironments()
	{
		return environments;
	}

	public void setEnvironmentGroups(List<EnvironmentGroupExplodedInfo> environmentGroups)
	{
		this.environmentGroups = environmentGroups;
	}

	public List<EnvironmentGroupExplodedInfo> getEnvironmentGroups()
	{
		return environmentGroups;
	}

}
