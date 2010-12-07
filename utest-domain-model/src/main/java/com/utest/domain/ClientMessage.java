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
