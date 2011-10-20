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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

//@XmlRootElement()
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "resourceIdentity")
public class ResourceIdentity
{
	public ResourceIdentity()
	{
		super();
	}

	@XmlElement(required = true)
	private Integer	id;
	@XmlElement(required = true)
	private String	url;
	@XmlElement(required = true)
	private Integer	version	= 0;

	@XmlAttribute(name = "id")
	public Integer getId()
	{
		return id;
	}

	public void setId(final Integer id)
	{
		this.id = id;
	}

	@XmlAttribute(name = "version")
	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(final Integer version)
	{
		this.version = version;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	@XmlAttribute(name = "url")
	public String getUrl()
	{
		return url;
	}
}
