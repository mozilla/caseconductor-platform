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
package com.utest.domain;

import java.util.Date;

public interface TimelineVersionable
{
	public Integer getCreatedBy();

	public void setCreatedBy(final Integer createdBy);

	public Date getCreateDate();

	public void setCreateDate(final Date createDate);

	public Integer getLastChangedBy();

	public void setLastChangedBy(final Integer lastChangedBy);

	public Date getLastChangeDate();

	public void setLastChangeDate(final Date lastChangeDate);

	public void setVersion(Integer version);

	public Integer getVersion();

}
