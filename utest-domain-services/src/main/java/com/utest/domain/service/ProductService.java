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

import com.utest.domain.AccessRole;
import com.utest.domain.Attachment;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.EnvironmentGroupExploded;
import com.utest.domain.Product;
import com.utest.domain.ProductComponent;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;
import com.utest.exception.UnsupportedEnvironmentSelectionException;

/**
 * Service to handle all domain operations related to Product.
 */
public interface ProductService extends BaseService
{
	Product addProduct(Integer companyId, String name, String description) throws Exception;

	Product getProduct(Integer productId) throws Exception;

	List<ProductComponent> getComponentsForProduct(Integer productId) throws Exception;

	UtestSearchResult findProducts(UtestSearch search, Integer teamMemberId, List<Integer> includedEnvironmentId_) throws Exception;

	UtestSearchResult findProductComponents(UtestSearch search) throws Exception;

	ProductComponent getProductComponent(Integer productComponentId) throws Exception;

	ProductComponent addProductComponent(Integer productId, String name, String description) throws Exception;

	List<EnvironmentGroup> getEnvironmentGroupsForProduct(Integer productId, List<Integer> includedEnvironmentId_) throws Exception;

	void saveEnvironmentGroupsForProduct(Integer productId, List<Integer> environmentGroupIds, Integer originalVesionId) throws UnsupportedEnvironmentSelectionException, Exception;

	Product saveProduct(Integer productId, String name, String description, Integer originalVersionId) throws Exception;

	ProductComponent saveProductComponent(Integer productComponentId, String name, String description, Integer originalVersionId) throws Exception;

	List<EnvironmentGroup> addGeneratedEnvironmentGroupsForProduct(Integer productId, List<Integer> environmentIds, Integer originalVersionId) throws Exception;

	List<EnvironmentGroup> addGeneratedEnvironmentGroupsForProduct(Integer productId, Integer environmentTypeId, List<Integer> environmentIds, Integer originalVersionId)
			throws Exception;

	void deleteProduct(Integer productId, Integer originalVersionId) throws Exception;

	void deleteProductComponent(Integer productComponentId, Integer originalVersionId) throws Exception;

	List<User> getTestingTeamForProduct(Integer productId) throws Exception;

	void saveTestingTeamForProduct(Integer productId, List<Integer> userIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException, Exception;

	void saveTestingTeamMemberRolesForProduct(Integer productId, Integer userId, List<Integer> roleIds, Integer originalVersionId) throws UnsupportedEnvironmentSelectionException,
			Exception;

	List<AccessRole> getTestingTeamMemberRolesForProduct(Integer productId, Integer userId) throws Exception;

	List<EnvironmentGroupExploded> getEnvironmentGroupsExplodedForProduct(Integer productId, List<Integer> includedEnvironmentId_) throws Exception;

	List<Attachment> getAttachmentsForProduct(Integer productId) throws Exception;

	Attachment addAttachmentForProduct(String name, String description, String url, Double size, Integer productId, Integer attachmentTypeId) throws Exception;

	boolean deleteAttachment(Integer attachmentId, Integer originalVersionId) throws Exception;
}
