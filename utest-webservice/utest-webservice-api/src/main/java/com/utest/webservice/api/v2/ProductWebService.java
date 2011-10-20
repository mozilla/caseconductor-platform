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
 * @author Greg Zheng
 *
 * copyright 2010 by uTest
 */
package com.utest.webservice.api.v2;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.EnvironmentGroupExplodedInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.ProductComponentInfo;
import com.utest.webservice.model.v2.ProductComponentSearchResultInfo;
import com.utest.webservice.model.v2.ProductInfo;
import com.utest.webservice.model.v2.ProductSearchResultInfo;
import com.utest.webservice.model.v2.RoleInfo;
import com.utest.webservice.model.v2.UserInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface ProductWebService
{
	ProductInfo getProduct(UriInfo ui, Integer productId) throws Exception;

	ProductComponentInfo getProductComponent(UriInfo ui, Integer productComponentId) throws Exception;

	ProductComponentSearchResultInfo findProductComponents(UriInfo ui, UtestSearchRequest request) throws Exception;

	List<ProductComponentInfo> getProductComponents(UriInfo ui, Integer productId) throws Exception;

	Boolean updateProductEnvironmentGroups(UriInfo ui, Integer productId, ArrayList<Integer> environmentGroupIds, Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(UriInfo ui, Integer productId, ArrayList<Integer> environmentIds, Integer originalVesionId)
			throws Exception;

	List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(UriInfo ui, Integer productId, Integer environmentTypeId, ArrayList<Integer> environmentIds,
			Integer originalVesionId) throws Exception;

	Boolean deleteProduct(UriInfo ui, Integer productId, Integer originalVersionId) throws Exception;

	Boolean deleteProductComponent(UriInfo ui, Integer productComponentId, Integer originalVersionId) throws Exception;

	ProductComponentInfo createProductComponent(UriInfo ui, Integer productId, String name, String description) throws Exception;

	ProductInfo createProduct(UriInfo ui, Integer companyId, String name, String description) throws Exception;

	ProductInfo updateProduct(UriInfo ui, Integer productId, String name, String description, Integer originalVersionId) throws Exception;

	ProductComponentInfo updateProductComponent(UriInfo ui, Integer productComponentId, String name, String description, Integer originalVersionId) throws Exception;

	Boolean updateProductTeamMembers(UriInfo ui, Integer productId, ArrayList<Integer> userIds, Integer originalVesionId) throws Exception;

	List<UserInfo> getProductTeamMembers(UriInfo ui, Integer productId) throws Exception;

	List<RoleInfo> getProductTeamMemberRoles(UriInfo ui, Integer productId, Integer userId) throws Exception;

	Boolean updateProductTeamMemberRoles(UriInfo ui, Integer productId, Integer userId, ArrayList<Integer> roleIds, Integer originalVersionId) throws Exception;

	List<AttachmentInfo> getProductAttachments(UriInfo ui, Integer productId) throws Exception;

	AttachmentInfo createAttachment(UriInfo ui, Integer productId, String name, String description, String url, Double size, Integer attachmentTypeId) throws Exception;

	Boolean deleteAttachment(UriInfo ui, Integer productId, Integer attachmentId, Integer originalVersionId) throws Exception;

	ProductSearchResultInfo findProducts(UriInfo ui, Integer includedEnvironmentId, Integer teamMemberId, UtestSearchRequest request) throws Exception;

	Boolean importMultiStepTestCasesFromCsv(MultipartBody body, Integer productId) throws Exception;

	Boolean importSingleStepTestCasesFromCsv(MultipartBody body, Integer productId) throws Exception;

	Boolean undeleteProduct(UriInfo ui, Integer productId, Integer originalVersionId) throws Exception;

	ProductSearchResultInfo findDeletedProducts(UriInfo ui, Integer includedEnvironmentId, Integer teamMemberId, UtestSearchRequest request) throws Exception;

	List<EnvironmentGroupInfo> getProductEnvironmentGroups(UriInfo ui, Integer productId, Integer includedEnvironmentId) throws Exception;

	List<EnvironmentGroupExplodedInfo> getProductEnvironmentGroupsExploded(UriInfo ui, Integer productId, Integer includedEnvironmentId) throws Exception;

}
