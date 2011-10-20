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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "companyLocator")
public class CompanyLocator // extends ResourceLocator
{
	private final Integer	id;
	private String			url;

	public CompanyLocator(Integer id)
	{
		super();
		this.id = id;
	}

	@XmlTransient
	public String getResourcePath()
	{
		// TODO - replace with a Constant
		return "/companies/{id}";
	}

	@XmlAttribute(name = "id", required = true)
	public Integer getId()
	{
		return id;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@XmlAttribute(name = "url", required = true)
	public String getUrl()
	{
		return url;
	}
}
