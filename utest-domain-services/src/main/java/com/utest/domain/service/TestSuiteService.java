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
package com.utest.domain.service;

import java.util.List;

import com.utest.domain.Attachment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.TestSuite;
import com.utest.domain.TestSuiteTestCase;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.domain.view.TestSuiteTestCaseView;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to the Test Suite Management.
 */
public interface TestSuiteService extends BaseService
{
	// TestSuite related methods
	TestSuite addTestSuite(Integer productId, boolean useLatestVersiuons, String name, String description) throws Exception;

	TestSuite getTestSuite(Integer testSuiteId_) throws Exception;

	UtestSearchResult findTestSuites(UtestSearch search_, Integer includedInTestRunId_, Integer includedTestCaseId, Integer includedTestCaseVersionId,
			Integer includedEnvironmentId_) throws Exception;

	void deleteTestSuiteTestCase(Integer testSuiteTestCaseId_, Integer originalVersionId) throws Exception;

	List<TestSuiteTestCase> getTestSuiteTestCases(Integer testSuiteId) throws Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForTestSuite(Integer testSuiteId, Integer includedEnvironmentId_) throws Exception;

	TestSuiteTestCase getTestSuiteTestCase(Integer testSuiteTestCaseId) throws Exception;

	TestSuiteTestCase addTestSuiteTestCase(Integer testSuiteId, Integer testCaseVersionId) throws Exception;

	TestSuiteTestCase addTestSuiteTestCase(Integer testSuiteId, Integer testCaseVersionId, Integer priorityId, Integer runOrder, boolean blocking) throws Exception;

	void saveEnvironmentGroupsForTestSuite(Integer testSuiteId, List<Integer> environmentGroupIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	TestSuite saveTestSuite(Integer testSuiteId, String name, String description, Integer originalVersionId) throws Exception;

	TestSuite activateTestSuite(Integer testSuiteId, Integer originalVersionId) throws Exception;

	TestSuite lockTestSuite(Integer testSuiteId, Integer originalVersionId) throws Exception;

	TestSuiteTestCase saveTestSuiteTestCase(Integer includedTestCaseId, Integer priorityId, Integer runOrder, boolean blocking, Integer originalVersionId);

	void deleteTestSuite(Integer testSuiteId, Integer originalVersionId) throws Exception;

	TestSuite cloneTestSuite(Integer fromTestSuiteId) throws Exception;

	UtestSearchResult findTestSuiteTestCases(UtestSearch search, Integer includedEnvironmentId_) throws Exception;

	List<TestSuiteTestCaseView> getTestSuiteTestCasesViews(Integer testSuiteId) throws Exception;

	TestSuiteTestCaseView getTestSuiteTestCaseView(Integer testSuiteTestCaseId) throws Exception;

	List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForTestSuite(Integer testSuiteId, Integer includedEnvironmentId_) throws Exception;

	List<Attachment> getAttachmentsForTestSuite(Integer testSuiteId) throws Exception;

	Attachment addAttachmentForTestSuite(String name, String description, String url, Double size, Integer testSuiteId, Integer attachmentTypeId) throws Exception;

	boolean deleteAttachment(Integer attachmentId, Integer originalVersionId) throws Exception;

}
