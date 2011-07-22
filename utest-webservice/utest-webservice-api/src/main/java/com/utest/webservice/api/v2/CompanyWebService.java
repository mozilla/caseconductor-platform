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

import com.utest.webservice.model.v2.AttachmentInfo;
import com.utest.webservice.model.v2.CompanyInfo;
import com.utest.webservice.model.v2.CompanySearchResultInfo;
import com.utest.webservice.model.v2.EnvironmentGroupInfo;
import com.utest.webservice.model.v2.EnvironmentInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface CompanyWebService
{
	CompanySearchResultInfo findCompanies(UriInfo ui, UtestSearchRequest request) throws Exception;

	CompanyInfo getCompany(UriInfo ui, Integer companyId) throws Exception;

	Boolean deleteCompany(UriInfo ui, Integer companyId, Integer originalVersionId) throws Exception;

	List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(UriInfo ui, Integer companyId, Integer environmentTypeId, ArrayList<Integer> environmentIds,
			Integer originalVesionId) throws Exception;

	List<EnvironmentGroupInfo> generateEnvironmentGroupFromEnvironments(UriInfo ui, Integer companyId, ArrayList<Integer> environmentIds, Integer originalVesionId)
			throws Exception;

	Boolean updateParentDependableEnvironments(UriInfo ui, Integer companyId, Integer parentEnvironmentId, ArrayList<Integer> environmentIds) throws Exception;

	List<EnvironmentInfo> getParentDependableEnvironments(UriInfo ui, Integer companyId, Integer parentEnvironmentId) throws Exception;

	CompanyInfo createCompany(UriInfo ui, Integer countryId, String name, String address, String city, String zip, String url, String phone) throws Exception;

	CompanyInfo updateCompany(UriInfo ui, Integer companyId, Integer countryId, String name, String address, String city, String zip, String url, String phone,
			Integer originalVersionId) throws Exception;

	List<AttachmentInfo> getCompanyAttachments(UriInfo ui, Integer companyId) throws Exception;

	AttachmentInfo createAttachment(UriInfo ui, Integer companyId, String name, String description, String url, Double size, Integer attachmentTypeId) throws Exception;

	Boolean deleteAttachment(UriInfo ui, Integer companyId, Integer attachmentId, Integer originalVersionId) throws Exception;
}
