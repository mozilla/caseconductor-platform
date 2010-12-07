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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
		Cookie[] available = req.getCookies();
		String sessionId = null;
		if (available != null)
		{
			for (Cookie ck : available)
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
				Cookie cookie = new Cookie(SESSION_NAME, sessionId);
				context.getHttpServletResponse().addCookie(cookie);
			}
		}
		return sessionId;
	}

	@SuppressWarnings("unchecked")
	public static Authentication getAuthenticationTocken(Message message)
	{
		Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
		String tocken = null;
		for (javax.ws.rs.core.Cookie c : extractCookies(headers.get("cookie")))
		{
			if (AUTH_TOKEN.equalsIgnoreCase(c.getName()))
			{
				tocken = c.getValue();
				break;
			}
		}
		if (tocken == null)
		{
			return null;
		}
		try
		{
			return (Authentication) Base64.decodeToObject(tocken);
		}
		catch (Exception e)
		{
			return null;
		}

	}

	private static List<javax.ws.rs.core.Cookie> extractCookies(List<String> cookie)
	{
		List<javax.ws.rs.core.Cookie> cookies = new ArrayList<javax.ws.rs.core.Cookie>();
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

	private static javax.ws.rs.core.Cookie toCookie(String one)
	{
		try
		{
			javax.ws.rs.core.Cookie c = javax.ws.rs.core.Cookie.valueOf(one);
			System.out.println("Cookie name=" + c.getName() + "; value=[" + c.getValue() + "]");
			return c;
		}
		catch (Exception c)
		{
			return new javax.ws.rs.core.Cookie("", "");
		}

	}
}
