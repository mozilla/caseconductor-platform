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
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnyObject")
public class AnyObjectInfo
{
	@XmlElement(required = true)
	private Object	object;

	public AnyObjectInfo(final Object object)
	{
		this.object = object;
	}

	@XmlMixed
	// @XmlElementRefs( {
	// @XmlElementRef(name = "TestingManagerInfo", type =
	// TestingManagerInfo.class), @XmlElementRef(name = "TesterInfo", type =
	// TesterInfo.class),
	// @XmlElementRef(name = "UserInfo", type = UserInfo.class)
	//
	// })
	public Object getObject()
	{
		return object;
	}

	public void getObject(final Object obj)
	{
		object = obj;
	}
}
