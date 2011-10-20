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
package com.utest.webservice.api.v2;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.CodeValue;

public interface StaticDataWebService
{
	List<CodeValue> getCodeValues(@Context final UriInfo ui_, String dataType) throws Exception;

	List<CodeValue> getParentCodeValues(@Context final UriInfo ui_, String dataType, Integer parentId) throws Exception;

	Map<String, List<CodeValue>> getParentMap(@Context final UriInfo ui_, String dataType) throws Exception;

	Set<String> getCodeKeys() throws Exception;

	List<CodeValue> getCodeValues(@Context final UriInfo ui_, String id, String locale) throws Exception;

	List<CodeValue> getLocales(UriInfo ui) throws Exception;

	Set<String> getErrorKeys() throws Exception;
}
