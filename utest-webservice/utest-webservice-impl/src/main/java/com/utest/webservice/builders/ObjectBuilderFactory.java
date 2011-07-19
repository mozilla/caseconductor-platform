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

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.service.StaticDataService;
import com.utest.webservice.model.v2.SearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface ObjectBuilderFactory
{
	<Ti, To> Ti toInfo(Class<Ti> clazz, To object, UriBuilder ub, final Object... uriBuilderArgs) throws Exception;

	<Ti, To> List<Ti> toInfo(final Class<Ti> clazz, List<To> objects, UriBuilder ub, Object... uriBuilderArgs) throws Exception;

	<Ti, To> To toObject(Class<To> clazz, Ti object) throws Exception;

	<Ti, To> SearchResultInfo<Ti> createResult(Class<Ti> clazz, Class<To> clazz1, UtestSearchRequest request, UtestSearchResult result, UriBuilder ub) throws Exception;

	<Ti, T extends UtestSearchRequest> UtestSearch createSearch(Class<Ti> objectClass, T request, UriInfo ui) throws Exception;

	StaticDataService getStaticDataService();

}
