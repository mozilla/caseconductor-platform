/*
Case Conductor is a Test Case Management system.
Copyright (C) 2011 uTest Inc.

This file is part of Case Conductor.

Case Conductor is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Case Conductor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Case Conductor.  If not, see <http://www.gnu.org/licenses/>.

*/
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
