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
