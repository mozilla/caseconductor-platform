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
 * copyright 2010 by uTest 
 */
package com.utest.webservice.client.rest;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.cxf.aegis.databinding.AegisDatabinding;
import org.apache.cxf.databinding.DataReader;

public class AegisEntityProvider // implements EntityProvider<Object>
{

	@SuppressWarnings("unchecked")
	AegisUnmarshaller createUnmarshaller()
	{
		AegisDatabinding binding = new AegisDatabinding();
		DataReader reader = binding.createReader(XMLStreamReader.class);
		AegisUnmarshaller u = new AegisUnmarshaller(reader);
		return u;
	}

	public Object readFrom(Class<Object> type, MediaType m, MultivaluedMap<String, String> headers, InputStream is) throws XMLStreamException, FactoryConfigurationError
	{
		return createUnmarshaller().unmarshal(is, "");
	}

}
