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
package com.utest.webservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Cookie;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.message.Message;
import org.jboss.util.Base64;
import org.springframework.security.core.Authentication;

public class SessionUtil
{
	public static final String	SESSION_NAME	= "JSESSIONID";
	public static final String	AUTH_TOKEN		= "USERTOKEN";

	public static String extractSession(MessageContext context, boolean insertToResponze)
	{
		HttpServletRequest req = context.getHttpServletRequest();
		javax.servlet.http.Cookie[] available = req.getCookies();
		String sessionId = null;
		if (available != null)
		{
			for (javax.servlet.http.Cookie ck : available)
			{
				if (SESSION_NAME.equalsIgnoreCase(ck.getName()))
				{
					sessionId = ck.getValue();
				}
			}
		}
		if (sessionId == null)
		{
			HttpSession session = req.getSession();
			sessionId = session.getId();
			if (insertToResponze)
			{
				javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(SESSION_NAME, sessionId);
				context.getHttpServletResponse().addCookie(cookie);
			}
		}
		return sessionId;
	}

	@SuppressWarnings("unchecked")
	public static Authentication getAuthenticationToken(Message message)
	{
		Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
		String token = null;
		for (Cookie c : extractCookies(headers.get("cookie")))
		{
			if (AUTH_TOKEN.equalsIgnoreCase(c.getName()))
			{
				token = c.getValue();
				break;
			}
		}
		if (token == null)
		{
			return null;
		}
		try
		{
			return (Authentication) Base64.decodeToObject(token);
		}
		catch (Exception e)
		{
			return null;
		}

	}

	private static List<Cookie> extractCookies(List<String> cookie)
	{
		List<Cookie> cookies = new ArrayList<Cookie>();
		if (cookie == null)
			return cookies;
		for (String one : cookie)
		{
			if (one.contains(";"))
			{
				for (String o : one.split(";"))
				{
					cookies.add(toCookie(o));
				}
			}
			else
			{
				cookies.add(toCookie(one));
			}
		}
		return cookies;
	}

	private static Cookie toCookie(String one)
	{
		try
		{
			Cookie c = Cookie.valueOf(one);
			System.out.println("Cookie name=" + c.getName() + "; value=[" + c.getValue() + "]");
			return c;
		}
		catch (Exception c)
		{
			return new Cookie("", "");
		}

	}
}
