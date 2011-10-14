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
package com.utest.webservice.api.v2;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentGroupSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.EnvironmentProfileExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentTypeInfo;
import com.utest.webservice.model.v2.EnvironmentTypeViewSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentViewSearchResultInfo;
import com.utest.webservice.model.v2.TagInfo;
import com.utest.webservice.model.v2.TagSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface EnvironmentWebService
{

	EnvironmentTypeInfo getEnvironmentType(UriInfo ui, Integer environmentTypeId) throws Exception;

	EnvironmentTypeViewSearchResultInfo findEnvironmentTypes(UriInfo ui, UtestSearchRequest request) throws Exception;

	EnvironmentInfo getEnvironment(UriInfo ui, Integer environmentId) throws Exception;

	EnvironmentViewSearchResultInfo findEnvironments(UriInfo ui, UtestSearchRequest request) throws Exception;

	EnvironmentGroupInfo getEnvironmentGroup(UriInfo ui, Integer environmentGroupId) throws Exception;

	Boolean updateEnvironmentGroupEnvironments(UriInfo ui, Integer environmentGroupId, ArrayList<Integer> environmentIds) throws Exception;

	List<EnvironmentInfo> getEnvironmentGroupEnvironments(UriInfo ui, Integer environmentGroupId, UtestSearchRequest request) throws Exception;

	List<EnvironmentInfo> getEnvironmentTypeEnvironments(UriInfo ui, Integer environmentTypeId, UtestSearchRequest request) throws Exception;

	TagInfo getTag(UriInfo ui, Integer tagId) throws Exception;

	TagSearchResultInfo findTags(UriInfo ui, UtestSearchRequest request) throws Exception;

	Boolean deleteEnvironment(UriInfo ui, Integer environmentId, Integer originalVersionId) throws Exception;

	Boolean deleteEnvironmentGroup(UriInfo ui, Integer environmentGroupId, Integer originalVersionId) throws Exception;

	Boolean deleteEnvironmentType(UriInfo ui, Integer environmentTypeId, Integer originalVersionId) throws Exception;

	EnvironmentTypeInfo updateEnvironmentType(UriInfo ui, Integer environmentTypeId, String name, String localeCode, Integer sortOrder) throws Exception;

	EnvironmentTypeInfo createEnvironmentType(UriInfo ui, Integer companyId, Integer parentEnvironmentTypeId, String name, String localeCode, Integer sortOrder, String groupType)
			throws Exception;

	EnvironmentInfo updateEnvironment(UriInfo ui, Integer environmentId, String name, String localeCode, Integer sortOrder) throws Exception;

	TagInfo updateTag(UriInfo ui, Integer tagId, String tag, Integer originalVersionId) throws Exception;

	EnvironmentInfo createEnvironment(UriInfo ui, Integer companyId, Integer environmentTypeId, String name, String localeCode, Integer sortOrder) throws Exception;

	TagInfo createTag(UriInfo ui, Integer companyId, String tag) throws Exception;

	Boolean deleteTag(UriInfo ui, Integer tagId, Integer originalVersionId) throws Exception;

	EnvironmentGroupInfo updateEnvironmentGroup(UriInfo ui, Integer environmentGroupId, String name, String description, Integer originalVersionId) throws Exception;

	EnvironmentGroupInfo createEnvironmentGroup(UriInfo ui, Integer companyId, Integer environmentTypeId, String name, String description) throws Exception;

	EnvironmentGroupExplodedInfo getEnvironmentGroupExploded(UriInfo ui, Integer environmentGroupId) throws Exception;

	EnvironmentProfileExplodedInfo getEnvironmentProfileExploded(UriInfo ui, Integer environmentProfileId) throws Exception;

	EnvironmentGroupSearchResultInfo findEnvironmentGroups(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

	EnvironmentGroupExplodedSearchResultInfo findEnvironmentGroupsExploded(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;
}
