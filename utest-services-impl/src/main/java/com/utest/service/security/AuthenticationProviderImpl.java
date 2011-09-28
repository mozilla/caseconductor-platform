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

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.utest.domain.User;
import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.Permission;
import com.utest.domain.service.UserService;
import com.utest.util.EncodeUtil;

/**
 * An implementation of <code>AuthenticationProvider</code> that uses the
 * <code>User</code> domain authenticate a user. This
 * <code>AuthenticationProvider</code> only supports
 * <code>UsernamePasswordAuthenticationToken</code>.
 * 
 * @author Miguel Bautista
 */
public class AuthenticationProviderImpl implements AuthenticationProvider
{
	private static String			BAD_CREDENTIALS		= "bad.credentials";
	private static String			EMAIL_NOT_CONFIRMED	= "email.not.confirmed";
	private static String			ACCOUNT_DISABLED	= "account.disabled";

	private final UserService	userService;

	public AuthenticationProviderImpl(final UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException
	{
		final AuthenticationRequest request = new AuthenticationRequest(authentication);

		final User user;
		if (request.wasExternallyAuthenticated())
		{
			user = authenticatedExternally(request.getUsercode());
		}
		else
		{
			user = login(request.getUserEmail(), request.getPassword());
		}

		final List<GrantedAuthority> grantedAuthorities;
		final AuthenticatedUserInfo authUser;
		authUser = new AuthenticatedUserInfo(user.getId(), request.getUsername());
		grantedAuthorities = getGrantedAuthority(user);
		return new UsernamePasswordAuthenticationToken(authUser, null, grantedAuthorities);
	}

	private User login(final String email, final String password)
	{
		if ((email == null) || (email.length() == 0) || (password == null) || (password.length() == 0))
		{
			throw new BadCredentialsException(BAD_CREDENTIALS);
		}
		final User user = userService.getUserByEmail(email);
		if (user == null)
		{
			throw new BadCredentialsException(BAD_CREDENTIALS);
		}
		else if (!EncodeUtil.encode(password).equals(user.getPassword()))
		{
			throw new BadCredentialsException(BAD_CREDENTIALS);
		}
		verifyUser(user);
		return user;
	}

	private User authenticatedExternally(final String usercode)
	{
		final User user = userService.getUserByCode(usercode);
		if (user == null)
		{
			throw new BadCredentialsException(BAD_CREDENTIALS);
		}
		verifyUser(user);
		return user;
	}

	private void verifyUser(final User user)
	{
		if (!user.isEnabled())
		{
			if (!user.isConfirmedEmail())
			{
				throw new AuthenticationServiceException(EMAIL_NOT_CONFIRMED);
			}
			else
			{
				throw new DisabledException(ACCOUNT_DISABLED);
			}
		}
	}

	private List<GrantedAuthority> getGrantedAuthority(final User user)
	{
		if (user == null)
		{
			throw new IllegalArgumentException();
		}
		final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		final List<Permission> permissions = user.getPermissions();
		if ((permissions != null) && (!permissions.isEmpty()))
		{
			for (int i = 0; i < permissions.size(); i++)
			{
				final Permission permission = permissions.get(i);
				final GrantedAuthority authority = new GrantedAuthorityImpl(permission.getPermissionCode());
				grantedAuthorities.add(authority);
			}
		}
		return grantedAuthorities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(final Class authentication)
	{
		return authentication == UsernamePasswordAuthenticationToken.class;
	}
}
