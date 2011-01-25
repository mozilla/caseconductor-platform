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

import javax.ws.rs.core.UriInfo;

import com.utest.webservice.model.v2.CompanyInfo;
import com.utest.webservice.model.v2.CompanySearchResultInfo;
import com.utest.webservice.model.v2.UtestSearchRequest;

public interface CompanyWebService
{
	CompanyInfo updateCompany(UriInfo ui, Integer companyId, CompanyInfo companyInfo) throws Exception;

	CompanySearchResultInfo findCompanies(UriInfo ui, UtestSearchRequest request) throws Exception;

	CompanyInfo createCompany(UriInfo ui, CompanyInfo companyInfo) throws Exception;

	CompanyInfo getCompany(UriInfo ui, Integer companyId) throws Exception;

	Boolean deleteCompany(UriInfo ui, Integer companyId, Integer originalVersionId) throws Exception;
}
