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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.web.context.request.RequestContextHolder;

import com.utest.domain.AuthenticatedUserInfo;
import com.utest.domain.service.UserService;

/**
 * A <code>ApplicationListener</code> which listens to
 * <code>AuthenticationSuccessEvent</code> and notifies the
 * <code>UserService</code> to mark the user as logged in when the event is
 * received.
 * 
 * @author Miguel Bautista
 */
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent>
{
	private static final Logger		logger	= Logger.getLogger(AuthenticationSuccessListener.class);

	private final UserService	userService;

	public AuthenticationSuccessListener(final UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent event)
	{
		try
		{
			final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
			final AuthenticatedUserInfo authInfo = (AuthenticatedUserInfo) (event).getAuthentication().getPrincipal();
			userService.login(authInfo, sessionId);
		}
		catch (final Exception e)
		{
			logger.fatal("Failed to log user login: " + e.toString());
		}
	}

}
