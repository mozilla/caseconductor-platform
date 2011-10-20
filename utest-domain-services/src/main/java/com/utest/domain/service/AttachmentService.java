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
import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

public interface AttachmentService
{
	Attachment addAttachment(final String name, final String description, final String url, final Double size, final Integer entityTypeId, final Integer entityId,
			final Integer attachmentTypeId) throws Exception;

	Attachment getAttachment(Integer attachmentId) throws Exception;

	UtestSearchResult findAttachments(UtestSearch search) throws Exception;

	boolean deleteAttachment(Integer attachmentId, Integer entityId, Integer entityTypeId) throws Exception;

	List<Attachment> getAttachmentsForEntity(Integer entityId, Integer entityTypeId) throws Exception;
}
