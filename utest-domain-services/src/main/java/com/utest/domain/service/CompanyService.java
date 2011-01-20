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

import com.utest.domain.Company;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

/**
 * Service to handle all domain operations related to TCM Companies.
 * 
 * @author Vadim Kisen
 */
public interface CompanyService
{
	// Company related methods
	Company getCompany(Integer companyId_) throws Exception;

	UtestSearchResult findCompanies(UtestSearch search_) throws Exception;

	Company addCompany(Integer countryId, String name, String address, String city, String zip, String url, String phone) throws Exception;

	Company saveCompany(Integer companyId, Integer countryId, String name, String address, String city, String zip, String url, String phone, Integer originalVersionId)
			throws Exception;

	void deleteCompany(Integer companyId, Integer originalVersionId) throws Exception;

}
