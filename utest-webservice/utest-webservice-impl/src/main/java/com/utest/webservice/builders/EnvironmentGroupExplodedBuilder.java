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
package com.utest.webservice.builders;

import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.utest.domain.EnvironmentGroupExploded;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.SearchResultInfo;

public class EnvironmentGroupExplodedBuilder<Ti, To> extends Builder<Ti, To>
{

	EnvironmentGroupExplodedBuilder(final ObjectBuilderFactory factory, final Class<Ti> clazz, final Class<? extends SearchResultInfo<Ti>> resultClass)
	{
		super(factory, clazz, resultClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void populateExtendedProperties(Ti result, To object, UriBuilder ub, Object[] uriBuilderArgs) throws Exception
	{
		// need to populate contained environments
		EnvironmentGroupExplodedInfo groupInfo = (EnvironmentGroupExplodedInfo) result;
		List<?> environments = factory.toInfo(EnvironmentInfo.class, ((EnvironmentGroupExploded) object).getEnvironments(), ub);
		groupInfo.setEnvironments((List<EnvironmentInfo>) environments);
	}
}
