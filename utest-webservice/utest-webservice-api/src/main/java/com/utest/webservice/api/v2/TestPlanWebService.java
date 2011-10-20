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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.IncludedTestSuiteInfo;
import com.utest.webservice.model.v2.TestPlanInfo;
import com.utest.webservice.model.v2.TestPlanSearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface TestPlanWebService
{

	Boolean updateTestPlanEnvironmentGroups(UriInfo ui, Integer testPlanId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<IncludedTestSuiteInfo> getTestPlanTestSuites(UriInfo ui, Integer testPlanId) throws Exception;

	TestPlanInfo getTestPlan(UriInfo ui, Integer testPlanId) throws Exception;

	TestPlanInfo activateTestPlan(UriInfo ui, Integer testPlanId, Integer originalVesionId) throws Exception;

	TestPlanInfo deactivateTestPlan(UriInfo ui, Integer testPlanId, Integer originalVesionId) throws Exception;

	Boolean deleteTestPlan(UriInfo ui, Integer testPlanId, Integer originalVesionId) throws Exception;

	Boolean deleteTestPlanTestSuite(UriInfo ui, Integer includedTestSuiteId, Integer originalVesionId) throws Exception;

	IncludedTestSuiteInfo getTestPlanTestSuite(UriInfo ui, Integer includedTestSuiteId) throws Exception;

	TestPlanInfo createTestPlan(UriInfo ui, Integer productId, String name, String description) throws Exception;

	IncludedTestSuiteInfo updateTestPlanTestSuite(UriInfo ui, Integer includedTestSuiteId, Integer runOrder, Integer originalVesionId) throws Exception;

	IncludedTestSuiteInfo createTestPlanTestSuite(UriInfo ui, Integer testPlanId, Integer testSuiteId, Integer runOrder) throws Exception;

	TestPlanInfo updateTestPlan(UriInfo ui, Integer testPlanId, String name, String description, Integer originalVesionId) throws Exception;

	List<AttachmentInfo> getTestPlanAttachments(UriInfo ui, Integer testPlanId) throws Exception;

	AttachmentInfo createAttachment(UriInfo ui, Integer testPlanId, String name, String description, String url, Double size, Integer attachmentTypeId) throws Exception;

	Boolean deleteAttachment(UriInfo ui, Integer testPlanId, Integer attachmentId, Integer originalVersionId) throws Exception;

	TestPlanSearchResultInfo findTestPlans(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

	Boolean undeleteTestPlan(UriInfo ui, Integer testPlanId, Integer originalVesionId) throws Exception;

	TestPlanSearchResultInfo findDeletedTestPlans(UriInfo ui, Integer includedEnvironmentId, UtestSearchRequest request) throws Exception;

	List<EnvironmentGroupInfo> getTestPlanEnvironmentGroups(UriInfo ui, Integer testPlanId, Integer includedEnvironmentId) throws Exception;

	List<EnvironmentGroupExplodedInfo> getTestPlanEnvironmentGroupsExploded(UriInfo ui, Integer productId, Integer includedEnvironmentId) throws Exception;

}
