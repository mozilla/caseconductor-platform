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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;

@WebFault(name = "InvalidRequest", targetNamespace = "http://utest.com/service/v2")
@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvalidRequest")
public class InvalidRequest extends Exception
{
	private static final long	serialVersionUID	= 1020908408763868746L;

	public InvalidRequest()
	{
		super();
	}

	public InvalidRequest(final String message)
	{
		super(message);
	}

	public InvalidRequest(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
