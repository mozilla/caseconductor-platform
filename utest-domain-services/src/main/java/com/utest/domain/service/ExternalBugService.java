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

import com.utest.domain.EntityExternalBug;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

public interface ExternalBugService
{
	EntityExternalBug getEntityExternalBug(Integer attachmentId) throws Exception;

	UtestSearchResult findEntityExternalBugs(UtestSearch search) throws Exception;

	boolean deleteEntityExternalBug(Integer attachmentId, Integer entityId, Integer entityTypeId) throws Exception;

	List<EntityExternalBug> getEntityExternalBugsForEntity(Integer entityId, Integer entityTypeId) throws Exception;

	EntityExternalBug addEntityExternalBug(String externalIdentifier, String url, Integer entityTypeId, Integer entityId) throws Exception;

	EntityExternalBug saveEntityExternalBug(Integer entityExternalBugId, Integer entityId, Integer entityTypeId, String externalIdentifier, String url) throws Exception;
}
