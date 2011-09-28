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
package com.utest.domain;

import java.util.Map;

public class ClientMessage implements java.io.Serializable
{
	public static final String DESTINATION_ID = "clientMessage";

	public static final Integer TYPE_CLIENT_LOGOUT = 1;
	public static final Integer TYPE_BUG_ACTION = 2;
	public static final Integer TYPE_TEST_CYCLE_ACTION = 3;

	private Integer messageType;
	private Map<String, ?> messageParameters;
	private String originatorId;

	public ClientMessage()
	{
	}

	public Map<String, ?> getMessageParameters()
	{
		return messageParameters;
	}

	public void setMessageParameters(final Map<String, ?> messageParameters)
	{
		this.messageParameters = messageParameters;
	}

	public void setMessageType(final Integer messageType)
	{
		this.messageType = messageType;
	}

	public Integer getMessageType()
	{
		return messageType;
	}

	public String getOriginatorId()
	{
		return originatorId;
	}

	public void setOriginatorId(final String originatorId)
	{
		this.originatorId = originatorId;
	}

}
