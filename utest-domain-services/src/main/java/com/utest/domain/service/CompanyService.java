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
import com.utest.domain.Company;
import com.utest.domain.EnvironmentGroup;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

/**
 * Service to handle all domain operations related to TCM Companies.
 * 
 * @author Vadim Kisen
 */
public interface CompanyService extends BaseService
{
	// Company related methods
	Company getCompany(Integer companyId_) throws Exception;

	UtestSearchResult findCompanies(UtestSearch search_) throws Exception;

	Company addCompany(Integer countryId, String name, String address, String city, String zip, String url, String phone) throws Exception;

	Company saveCompany(Integer companyId, Integer countryId, String name, String address, String city, String zip, String url, String phone, Integer originalVersionId)
			throws Exception;

	void deleteCompany(Integer companyId, Integer originalVersionId) throws Exception;

	List<EnvironmentGroup> addGeneratedEnvironmentGroupsForCompany(Integer companyId, List<Integer> environmentIds, Integer originalVersionId) throws Exception;

	List<EnvironmentGroup> addGeneratedEnvironmentGroupsForCompany(Integer companyId, Integer environmentTypeId, List<Integer> environmentIds, Integer originalVersionId)
			throws Exception;

	List<Attachment> getAttachmentsForCompany(Integer companyId) throws Exception;

	Attachment addAttachmentForCompany(String name, String description, String url, Double size, Integer companyId, Integer attachmentTypeId) throws Exception;

	boolean deleteAttachment(Integer attachmentId, Integer entityId) throws Exception;
}
