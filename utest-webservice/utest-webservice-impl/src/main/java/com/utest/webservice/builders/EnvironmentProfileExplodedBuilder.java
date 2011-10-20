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

import com.utest.domain.EnvironmentProfileExploded;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.EnvironmentProfileExplodedInfo;
import com.utest.webservice.model.v2.SearchResultInfo;

public class EnvironmentProfileExplodedBuilder<Ti, To> extends Builder<Ti, To>
{

	EnvironmentProfileExplodedBuilder(final ObjectBuilderFactory factory, final Class<Ti> clazz, final Class<? extends SearchResultInfo<Ti>> resultClass)
	{
		super(factory, clazz, resultClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void populateExtendedProperties(Ti result, To object, UriBuilder ub, Object[] uriBuilderArgs) throws Exception
	{
		// need to populate contained environments
		EnvironmentProfileExplodedInfo profileInfo = (EnvironmentProfileExplodedInfo) result;
		List<?> environments = factory.toInfo(EnvironmentInfo.class, ((EnvironmentProfileExploded) object).getEnvironments(), ub);
		List<?> environmentGroups = factory.toInfo(EnvironmentGroupExplodedInfo.class, ((EnvironmentProfileExploded) object).getEnvironmentGroups(), ub);
		profileInfo.setEnvironments((List<EnvironmentInfo>) environments);
		profileInfo.setEnvironmentGroups((List<EnvironmentGroupExplodedInfo>) environmentGroups);
	}
}
