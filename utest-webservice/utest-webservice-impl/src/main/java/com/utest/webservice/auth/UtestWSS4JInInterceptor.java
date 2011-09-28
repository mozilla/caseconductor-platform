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
 * copyright 2010 by uTest 
 */
package com.utest.webservice.auth;

import java.util.Map;
import java.util.Vector;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.handler.WSHandlerResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

public class UtestWSS4JInInterceptor extends WSS4JInInterceptor implements InitializingBean
{
	AuthenticationProvider	authenticationProvider;

	public UtestWSS4JInInterceptor()
	{
		super();
	}

	public UtestWSS4JInInterceptor(final Map<String, Object> properties)
	{
		super(properties);
	}

	public void setAuthenticationProvider(final AuthenticationProvider authenticationProvider)
	{
		this.authenticationProvider = authenticationProvider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(final SoapMessage message) throws Fault
	{
		try
		{
			super.handleMessage(message);
			final Vector<WSHandlerResult> result = (Vector<WSHandlerResult>) message.getContextualProperty(WSHandlerConstants.RECV_RESULTS);
			if ((result != null) && !result.isEmpty())
			{
				for (final WSHandlerResult res : result)
				{
					// loop through security engine results
					for (final WSSecurityEngineResult securityResult : (Vector<WSSecurityEngineResult>) res.getResults())
					{
						final int action = (Integer) securityResult.get(WSSecurityEngineResult.TAG_ACTION);
						// determine if the action was a username token
						if ((action & WSConstants.UT) > 0)
						{
							// get the principal object
							final WSUsernameTokenPrincipal principal = (WSUsernameTokenPrincipal) securityResult.get(WSSecurityEngineResult.TAG_PRINCIPAL);
							if (principal.getPassword() == null)
							{
								principal.setPassword("");
							}
							Authentication authentication = new UsernamePasswordAuthenticationToken(principal.getName(), principal.getPassword());
							authentication = authenticationProvider.authenticate(authentication);
							if (!authentication.isAuthenticated())
							{
								System.out.println("This user is not authentic.");
							}
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
					}
				}
			}
		}
		catch (final RuntimeException ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		Assert.notNull(authenticationProvider, "Authentication provider must be set");
		Assert.notNull(getProperties(), "Interceptor properties must be set, even if empty");
	}

}
