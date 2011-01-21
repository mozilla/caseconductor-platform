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

import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentGroupSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.EnvironmentSearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentTypeInfo;
import com.utest.webservice.model.v2.EnvironmentTypeSearchResultInfo;
import com.utest.webservice.model.v2.TagInfo;
import com.utest.webservice.model.v2.TagSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface EnvironmentWebService
{

	Boolean updateEnvironmentType(UriInfo ui, Integer environmentTypeId, EnvironmentTypeInfo environmentTypeInfo) throws Exception;

	EnvironmentTypeInfo createEnvironmentType(UriInfo ui, EnvironmentTypeInfo environmentTypeInfo) throws Exception;

	EnvironmentTypeInfo getEnvironmentType(UriInfo ui, Integer environmentTypeId) throws Exception;

	EnvironmentTypeSearchResultInfo findEnvironmentTypes(UriInfo ui, UtestSearchRequest request) throws Exception;

	Boolean updateEnvironment(UriInfo ui, Integer environmentId, EnvironmentInfo environmentInfo) throws Exception;

	EnvironmentInfo createEnvironment(UriInfo ui, EnvironmentInfo environmentInfo) throws Exception;

	EnvironmentInfo getEnvironment(UriInfo ui, Integer environmentId) throws Exception;

	EnvironmentSearchResultInfo findEnvironments(UriInfo ui, UtestSearchRequest request) throws Exception;

	Boolean updateEnvironmentGroup(UriInfo ui, Integer environmentGroupId, EnvironmentGroupInfo environmentGroupInfo) throws Exception;

	EnvironmentGroupInfo createEnvironmentGroup(UriInfo ui, EnvironmentGroupInfo environmentGroupInfo) throws Exception;

	EnvironmentGroupInfo getEnvironmentGroup(UriInfo ui, Integer environmentGroupId) throws Exception;

	EnvironmentGroupSearchResultInfo findEnvironmentGroups(UriInfo ui, UtestSearchRequest request) throws Exception;

	Boolean updateEnvironmentGroupEnvironments(UriInfo ui, Integer environmentGroupId, ArrayList<Integer> environmentIds) throws Exception;

	List<EnvironmentInfo> getEnvironmentGroupEnvironments(UriInfo ui, Integer environmentGroupId, UtestSearchRequest request) throws Exception;

	Boolean deleteEnvironmentType(UriInfo ui, Integer environmentTypeId) throws Exception;

	Boolean deleteEnvironment(UriInfo ui, Integer environmentId) throws Exception;

	Boolean deleteEnvironmentGroup(UriInfo ui, Integer environmentGroupId) throws Exception;

	List<EnvironmentInfo> getEnvironmentTypeEnvironments(UriInfo ui, Integer environmentTypeId, UtestSearchRequest request) throws Exception;

	List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(UriInfo ui, Integer companyId, ArrayList<Integer> environmentIds) throws Exception;

	List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(UriInfo ui, Integer companyId, Integer environmentTypeId, ArrayList<Integer> environmentIds)
			throws Exception;

	Boolean updateParentDependableEnvironments(UriInfo ui, Integer companyId, Integer parentEnvironmentId, ArrayList<Integer> environmentIds) throws Exception;

	List<EnvironmentInfo> getParentDependableEnvironments(UriInfo ui, Integer companyId, Integer parentEnvironmentId, UtestSearchRequest request) throws Exception;

	TagInfo createTag(UriInfo ui, TagInfo tagInfo) throws Exception;

	Boolean deleteTag(UriInfo ui, Integer tagId) throws Exception;

	TagInfo getTag(UriInfo ui, Integer tagId) throws Exception;

	TagSearchResultInfo findTags(UriInfo ui, UtestSearchRequest request) throws Exception;
}
