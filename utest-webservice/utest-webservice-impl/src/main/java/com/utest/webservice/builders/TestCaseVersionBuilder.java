/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.beanutils.PropertyUtils;

import com.utest.domain.TestCaseVersion;
import com.utest.webservice.model.v2.BaseInfo;
import com.utest.webservice.model.v2.TestCaseVersionInfo;
import com.utest.webservice.model.v2.UtestResult;

public class TestCaseVersionBuilder<Ti, To> extends Builder<Ti, To>
{

	TestCaseVersionBuilder(final ObjectBuilderFactory factory, final Class<Ti> clazz, final Class<? extends UtestResult<Ti>> resultClass)
	{
		super(factory, clazz, resultClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ti toInfo(final To object, final UriBuilder ub, Object... uriBuilderArgs) throws Exception
	{
		Ti result = (Ti) new TestCaseVersionInfo();
		PropertyUtils.copyProperties(result, object);
		// need to copy some properties from TestCase
		((TestCaseVersionInfo) result).setName(((TestCaseVersion) object).getTestCase().getName());
		((TestCaseVersionInfo) result).setMaxAttachmentSizeInMbytes(((TestCaseVersion) object).getTestCase().getMaxAttachmentSizeInMbytes());
		((TestCaseVersionInfo) result).setMaxNumberOfAttachments(((TestCaseVersion) object).getTestCase().getMaxNumberOfAttachments());
		//
		populateIdentityAndTimeline((BaseInfo) result, object, ub, uriBuilderArgs);
		return result;
	}
}
