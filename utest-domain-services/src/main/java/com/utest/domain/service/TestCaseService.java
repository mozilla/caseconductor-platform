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

import com.utest.domain.EnvironmentGroup;
import com.utest.domain.ProductComponent;
import com.utest.domain.Tag;
import com.utest.domain.TestCase;
import com.utest.domain.TestCaseStep;
import com.utest.domain.TestCaseVersion;
import com.utest.domain.VersionIncrement;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to the Test Cases.
 */
public interface TestCaseService
{
	TestCaseVersion getLatestTestCaseVersion(Integer testCaseId) throws Exception;

	TestCase getTestCase(Integer testCaseId) throws Exception;

	List<ProductComponent> getComponentsForTestCase(Integer testCaseId_) throws Exception;

	void saveProductComponentsForTestCase(Integer testCaseId_, List<Integer> productComponentIds_, Integer originalVersionId_) throws Exception;

	void deleteTestCaseStep(Integer testCaseStepId_, Integer originalVersionId_) throws Exception;

	void deleteTestCaseVersion(Integer testCaseVersionId_, Integer originalVersionId_) throws Exception;

	TestCaseVersion getTestCaseVersion(Integer testCaseVersionId_) throws Exception;

	UtestSearchResult findTestCaseVersions(UtestSearch search_) throws Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForTestCaseVersion(Integer testCaseVersionId_) throws Exception;

	TestCaseVersion getLastApprovedTestCaseVersion(Integer testCaseId) throws Exception;

	TestCase cloneTestCase(Integer testCaseId) throws Exception;

	TestCase addTestCase(Integer productId, Integer testCycleId, Integer maxAttachmentSizeInMbytes, Integer maxNumberOfAttachments, String name, String description)
			throws Exception;

	TestCase addTestCase(Integer productId, Integer maxAttachmentSizeInMbytes, Integer maxNumberOfAttachments, String name, String description) throws Exception;

	TestCaseVersion approveTestCaseVersion(Integer testCaseVersionId, Integer originalVersionId) throws Exception;

	TestCaseVersion rejectTestCaseVersion(Integer testCaseVersionId, Integer originalVersionId) throws Exception;

	TestCaseStep saveTestCaseStep(Integer testCaseStepId, String name, Integer stepNumber, String instruction, String expectedResult, Integer estimatedTimeInMin,
			Integer originalVersionId) throws Exception;

	TestCaseVersion lockTestCaseVersion(Integer testCaseVersionId, Integer originalVersionId) throws Exception;

	TestCaseStep addTestCaseStep(Integer testCaseVersionId, String name, Integer stepNumber, String instruction, String expectedResult, Integer estimatedTimeInMin)
			throws Exception;

	TestCaseVersion activateTestCaseVersion(Integer testCaseVersionId, Integer originalVersionId) throws Exception;

	List<TestCaseStep> getTestCaseVersionSteps(Integer testCaseVersionId) throws Exception;

	UtestSearchResult findLatestTestCaseVersions() throws Exception;

	UtestSearchResult findLatestTestCaseVersions(UtestSearch search) throws Exception;

	List<TestCaseVersion> getTestCaseVersions(Integer testCaseId) throws Exception;

	List<Tag> getTestCaseTags(Integer testCaseId) throws Exception;

	TestCaseStep getTestCaseStep(Integer testCaseStepId) throws Exception;

	TestCase saveTestCase(Integer testCaseId, String name, Integer maxAttachmentSizeInMbytes, Integer maxNumberOfAttachments, Integer originalVersionId_) throws Exception;

	TestCaseVersion saveTestCaseVersion(Integer testCaseVersionId, String description, Integer originalVersionId_, VersionIncrement versionIncrement) throws Exception;

	UtestSearchResult findTestCases(UtestSearch search) throws Exception;

	void deleteTestCaseTag(Integer testCaseId, Integer tagId);

	void addTestCaseTag(Integer testCaseId, Integer tagId) throws Exception;

	void saveTagsForTestCase(Integer testCaseId, List<Integer> tagIds, Integer originalVersionId_) throws Exception;

	void saveEnvironmentGroupsForTestCaseVersion(Integer testCaseVersionId, List<Integer> environmentGroupIds, Integer originalVersionId) throws ChangingActivatedEntityException,
			UnsupportedEnvironmentSelectionException, Exception;

	void deleteTestCase(Integer testCaseId, Integer originalVersionId) throws Exception;

	UtestSearchResult findTestCaseVersionsBySteps(UtestSearch search) throws Exception;
}
