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
package com.utest.domain.service;

import java.util.List;

import com.utest.domain.Environment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentLocale;
import com.utest.domain.EnvironmentProfile;
import com.utest.domain.EnvironmentType;
import com.utest.domain.EnvironmentTypeLocale;
import com.utest.domain.Tag;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to the Environments, Groups
 * and Profiles Groups.
 */
public interface EnvironmentService
{
	// Environment Types related methods

	EnvironmentType getEnvironmentType(Integer environmentTypeId_) throws Exception;

	UtestSearchResult findEnvironmentTypes(UtestSearch search_) throws Exception;

	// Environments related methods
	EnvironmentLocale saveEnvironmentLocale(Integer environmentId_, String description_, String localeCode_, Integer sortOrder_) throws Exception;

	Environment getEnvironment(Integer environmentId_) throws Exception;

	List<Environment> getEnvironmentsForType(Integer environmentTypeId_) throws Exception;

	UtestSearchResult findEnvironments(UtestSearch search_) throws Exception;

	// Environment Groups related methods

	EnvironmentGroup saveEnvironmentsForGroup(Integer environmentGroupId_, List<Integer> environmentIds_) throws Exception;

	EnvironmentGroup getEnvironmentGroup(Integer environmentGroupId_) throws Exception;

	List<Environment> getEnvironmentsForGroup(Integer environmentGroupId_) throws Exception;

	UtestSearchResult findEnvironmentGroups(UtestSearch search_) throws Exception;

	// Environment Profile related methods
	EnvironmentProfile saveEnvironmentGroupsForProfile(Integer environmentProfileId_, List<Integer> environmentGroupIds_) throws Exception;

	// EnvironmentProfile saveEnvironmentProfile(EnvironmentProfile
	// environmentProfile_) throws Exception;

	EnvironmentProfile getEnvironmentProfile(Integer environmentProfileId_) throws Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForProfile(Integer environmentProfileId_) throws Exception;

	UtestSearchResult findEnvironmentProfiles(UtestSearch search_) throws Exception;

	EnvironmentTypeLocale saveEnvironmentTypeLocale(Integer environmentTypeId_, String description_, String localeCode_, Integer sortOrder_) throws Exception;

	EnvironmentGroup addEnvironmentGroup(Integer companyId, Integer environmentTypeId, String name, String description, List<Integer> environmentIds) throws Exception;

	EnvironmentGroup addEnvironmentGroup(Integer companyId, String name, String description, List<Integer> environmentIds) throws Exception;

	EnvironmentProfile addEnvironmentProfile(Integer companyId, String name, String description, List<Integer> environmentGroupIds) throws Exception;

	Environment addEnvironment(Integer environmentTypeId, String description) throws Exception;

	Environment addEnvironment(Integer companyId, Integer environmentTypeId, String description, String localeCode) throws Exception;

	EnvironmentType addEnvironmentType(Integer parentEnvironmentTypeId, String description, boolean isGroupType) throws Exception;

	EnvironmentType addEnvironmentType(Integer companyId, Integer parentEnvironmentTypeId, String description, boolean isGroupType, String localeCode) throws Exception;

	/**
	 * Group all possible combinations of different environment types into
	 * distinct groups.
	 * 
	 * @Example: if 6 environments of 3 different types are passed in - type 1
	 *           [1, 2], type 2 [3, 4], type 3 [5, 6], then result will be as
	 *           follows: group 1 [1, 3, 5], group 2 [1, 4, 5], group 3 [1, 3,
	 *           6], group 4 [1, 4, 6], group 5 [2, 3, 5], group 6 [2, 4, 5],
	 *           group 7 [2, 3, 6], group 8 [2, 4, 6]
	 * 
	 * @param auth_
	 *            - user performing operation
	 * @param companyId_
	 *            - company owning these groups
	 * @param environmentIds_
	 *            - environments to be grouped
	 * @return List<EnvironmentGroup> - auto-generated groups of environments
	 * @throws Exception
	 */
	List<EnvironmentGroup> addGeneratedEnvironmentGroups(Integer companyId, List<Integer> environmentIds) throws Exception;

	List<EnvironmentGroup> addGeneratedEnvironmentGroups(Integer companyId, Integer environmentTypeId, List<Integer> environmentIds) throws Exception;

	boolean isValidEnvironmentGroupSelectionForProfile(Integer parentEnvironmentProfile, List<Integer> environmentGroupIds) throws UnsupportedEnvironmentSelectionException,
			Exception;

	EnvironmentGroup saveEnvironmentGroup(Integer environmentGroupId, String name, String description, Integer originalVersionId) throws Exception;

	void saveParentDependableEnvironments(Integer companyId, Integer parentEnvironmentId, List<Integer> environmentIds) throws Exception;

	List<Environment> getParentDependableEnvironments(Integer companyId, Integer parentEnvironmentId) throws Exception;

	Tag addTag(Integer companyId, String tag) throws Exception;

	UtestSearchResult findTags(UtestSearch search) throws Exception;

	Tag getTag(Integer tagId) throws Exception;

	void deleteEnvironment(Integer environmentId, Integer originalVersionId) throws Exception;

	void deleteEnvironmentGroup(Integer environmentGroupId, Integer originalVersionId) throws Exception;

	void deleteEnvironmentProfile(Integer environmentProfileId, Integer originalVersionId) throws Exception;

	void deleteEnvironmentType(Integer environmentTypeId, Integer originalVersionId) throws Exception;

	void deleteTag(Integer tagId, Integer originalVersionId) throws Exception;

	Tag saveTag(Integer tagId, String tag, Integer originalVersionId) throws Exception;

	EnvironmentProfile intersectEnvironmentProfiles(Integer profileId1, Integer profileId2) throws Exception;

}
