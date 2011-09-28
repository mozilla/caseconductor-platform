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

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.ClientMessage;
import com.utest.domain.User;
import com.utest.domain.service.UserService;

/**
 * A <code>ApplicationListener</code> which listens to
 * <code>HttpSessionDestroyedEvent</code> and notifies the
 * <code>UserService</code> to mark the user as logged out when the event is
 * received.
 * 
 * @author Miguel Bautista
 */
public class HttpSessionDestroyedListener implements ApplicationListener<HttpSessionDestroyedEvent>
{
	private static final Logger		logger	= Logger.getLogger(HttpSessionDestroyedListener.class);

	private final UserService	userService;

	public HttpSessionDestroyedListener(final UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void onApplicationEvent(final HttpSessionDestroyedEvent event)
	{
		try
		{
			final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			final HttpSession session = (event).getSession();

			if ((auth != null) && (auth.getPrincipal() instanceof AuthenticatedUserInfo))
			{
				userService.logout((AuthenticatedUserInfo) auth.getPrincipal(), session.getId());
			}
		}
		catch (final Exception e)
		{
			logger.fatal("Failed to log user logout: " + e.toString());
		}
	}
}
