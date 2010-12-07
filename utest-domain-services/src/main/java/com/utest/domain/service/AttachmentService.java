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

import java.io.File;
import java.util.List;

import com.utest.domain.Attachment;
import com.utest.domain.User;
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

public interface AttachmentService
{
	// Attachment related methods
	Attachment uploadAttachment(User auth_, String localPath_, Integer entityId_, Integer entityTypeId_) throws Exception;

	List<File> downloadAttachments(Integer entityId, Integer entityTypeId, Integer attachmentTypeId) throws Exception;

	Attachment getAttachment(User auth_, Integer attachmentId_) throws Exception;

	List<Attachment> getAttachments(Integer entityId, Integer entityTypeId, Integer attachmentTypeId) throws Exception;

	UtestSearchResult findAttachments(User auth_, UtestSearch search_) throws Exception;

}
