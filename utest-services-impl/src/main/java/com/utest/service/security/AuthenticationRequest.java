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
package com.utest.service.security;

import org.springframework.security.core.Authentication;

/**
 * Takes in a <code>org.springframework.security.Authentication</code> parses it
 * into a authentication request.
 * 
 * @author Miguel Bautista
 */
public class AuthenticationRequest
{
	public static String IMPERSINATION_TOKEN = "/";
	public static String EXTERANAL_AUTH_TOKEN = "__EXTERNAL_AUTH__";

	private final String userEmail;
	private final String impersonatingEmail;
	private final String password;
	private final String username;
	private final String usercode;

	public AuthenticationRequest(final Authentication authentication)
	{
		if (authentication == null)
		{
			throw new IllegalArgumentException("Authentication is required");
		}

		final Object principal = authentication.getPrincipal();
		if (!(principal instanceof String))
		{
			throw new UnsupportedOperationException(
					"principal is not of type String");
		}
		username = (String) principal;
		if (username.startsWith(EXTERANAL_AUTH_TOKEN))
		{
			// user was externally authenticated
			usercode = username.substring(EXTERANAL_AUTH_TOKEN.length());
			userEmail = password = impersonatingEmail = null;
		}
		else
		{
			final int imperinationIdx = username.indexOf(IMPERSINATION_TOKEN);
			if (imperinationIdx == -1)
			{
				userEmail = username;
				impersonatingEmail = null;
			}
			else
			{
				userEmail = username.substring(0, imperinationIdx);
				impersonatingEmail = username.substring(imperinationIdx + 1);
			}
			usercode = null;

			final Object credentials = authentication.getCredentials();
			if (!(credentials instanceof String))
			{
				throw new UnsupportedOperationException(
						"credentials is not of type String");
			}
			password = (String) credentials;
		}

	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail()
	{
		return userEmail;
	}

	/**
	 * @return the impersonatingEmail
	 */
	public String getImpersonatingEmail()
	{
		return impersonatingEmail;
	}

	public boolean impersonating()
	{
		return impersonatingEmail != null;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	public String getUsername()
	{
		return username;
	}

	public String getUsercode()
	{
		return usercode;
	}

	public boolean wasExternallyAuthenticated()
	{
		return usercode != null;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((impersonatingEmail == null) ? 0 : impersonatingEmail
						.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result
				+ ((usercode == null) ? 0 : usercode.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof AuthenticationRequest))
		{
			return false;
		}
		final AuthenticationRequest other = (AuthenticationRequest) obj;
		if (impersonatingEmail == null)
		{
			if (other.impersonatingEmail != null)
			{
				return false;
			}
		}
		else if (!impersonatingEmail.equals(other.impersonatingEmail))
		{
			return false;
		}
		if (password == null)
		{
			if (other.password != null)
			{
				return false;
			}
		}
		else if (!password.equals(other.password))
		{
			return false;
		}
		if (userEmail == null)
		{
			if (other.userEmail != null)
			{
				return false;
			}
		}
		else if (!userEmail.equals(other.userEmail))
		{
			return false;
		}
		if (usercode == null)
		{
			if (other.usercode != null)
			{
				return false;
			}
		}
		else if (!usercode.equals(other.usercode))
		{
			return false;
		}
		if (username == null)
		{
			if (other.username != null)
			{
				return false;
			}
		}
		else if (!username.equals(other.username))
		{
			return false;
		}
		return true;
	}
}
