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

// Generated Oct 7, 2009 11:18:35 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;

public class SignInFact extends Entity
{

	private Integer	userId;
	private Integer	impersonatedUserId;
	private Date	startDate;
	private Date	endDate;
	private String	sessionId;

	public SignInFact()
	{
	}

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(final Integer userId)
	{
		this.userId = userId;
	}

	public Integer getImpersonatedUserId()
	{
		return impersonatedUserId;
	}

	public void setImpersonatedUserId(final Integer impersonatedUserId)
	{
		this.impersonatedUserId = impersonatedUserId;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	public void setSessionId(final String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getSessionId()
	{
		return sessionId;
	}

}
