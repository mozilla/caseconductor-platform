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
package com.utest.webservice.interceptors;

import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.utest.webservice.util.SessionUtil;

public class ServiceInterceptor extends AbstractPhaseInterceptor<Message>
{

	public ServiceInterceptor()
	{
		super(Phase.PRE_INVOKE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message message) throws Fault
	{
		Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
		String session = null;
		if (headers.get("cookie") != null)
		{
			for (String cook : headers.get("cookie"))
			{
				final String[] param = cook.split(";");
				for (String one : param)
				{
					if (one.indexOf(SessionUtil.SESSION_NAME) >= 0)
					{
						int ind = one.indexOf("=");
						if (ind > 0)
						{
							session = one.substring(ind + 1);
							break;
						}
					}
				}
			}
		}
		System.out.println("*******************************************");
		System.out.println("Service session=" + session);
		System.out.println("*******************************************");
		// message.get("HTTP.RESPONSE");
	}
}
