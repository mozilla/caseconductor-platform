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
 * @author Miguel Bautista
 *
 * copyright 2010 by uTest 
 */
package com.utest.domain;

import java.security.Principal;
import java.util.Date;
import java.io.Serializable;

public class AuthenticatedUserInfo implements Principal, Serializable
{
	private final Integer	userId;
	private final Integer	impsersonatedUserId;
	private final Date		authDate;
	private final String	userName;

	public AuthenticatedUserInfo(final Integer userId, final String userName, final Integer impsersonatedUserId)
	{
		this.userId = userId;
		this.userName = userName;
		this.impsersonatedUserId = impsersonatedUserId;
		this.authDate = new Date();
	}

	public AuthenticatedUserInfo(final Integer userId, final String userName)
	{
		this(userId, userName, null);
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId()
	{
		return userId;
	}

	/**
	 * @return the impsersonatedUserId
	 */
	public Integer getImpsersonatedUserId()
	{
		return impsersonatedUserId;
	}

	/**
	 * Returns either the <code>impsersonatedUserId</code> OR the
	 * <code>userId</code>. If <code>isImpersonating</code> then the
	 * <code>impsersonatedUserId</code> is returned. Otherwise the
	 * <code>userId</code> will be returned.
	 */
	public Integer getLoggedInUserId()
	{
		if (impersonating())
		{
			return impsersonatedUserId;
		}
		return userId;
	}

	public boolean impersonating()
	{
		return impsersonatedUserId != null;
	}

	/**
	 * @return the authDate
	 */
	public Date getAuthDate()
	{
		return authDate;
	}

	@Override
	public String getName()
	{
		return userName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authDate == null) ? 0 : authDate.hashCode());
		result = prime * result + ((impsersonatedUserId == null) ? 0 : impsersonatedUserId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof AuthenticatedUserInfo))
		{
			return false;
		}
		final AuthenticatedUserInfo other = (AuthenticatedUserInfo) obj;
		if (authDate == null)
		{
			if (other.authDate != null)
			{
				return false;
			}
		}
		else if (!authDate.equals(other.authDate))
		{
			return false;
		}
		if (impsersonatedUserId == null)
		{
			if (other.impsersonatedUserId != null)
			{
				return false;
			}
		}
		else if (!impsersonatedUserId.equals(other.impsersonatedUserId))
		{
			return false;
		}
		if (userId == null)
		{
			if (other.userId != null)
			{
				return false;
			}
		}
		else if (!userId.equals(other.userId))
		{
			return false;
		}
		if (userName == null)
		{
			if (other.userName != null)
			{
				return false;
			}
		}
		else if (!userName.equals(other.userName))
		{
			return false;
		}
		return true;
	}

}
