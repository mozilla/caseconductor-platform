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
 * copyright 2010 by uTest 
 */
package com.utest.webservice.client.rest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

public class MySSLSocketFactory implements SecureProtocolSocketFactory
{

	private SSLContext	sslcontext	= null;

	public MySSLSocketFactory()
	{
		super();
	}

	private static SSLContext createEasySSLContext()
	{
		try
		{
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { new EasyX509TrustManager(null) }, null);
			return context;
		}
		catch (Exception e)
		{
			throw new HttpClientError(e.toString());
		}
	}

	private SSLContext getSSLContext()
	{
		if (this.sslcontext == null)
		{
			this.sslcontext = createEasySSLContext();
		}
		return this.sslcontext;
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int,java.net.InetAddress,int)
	 */
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException
	{

		return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort, final HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException
	{
		if (params == null)
		{
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0)
		{
			return socketfactory.createSocket(host, port, localAddress, localPort);
		}
		else
		{
			Socket socket = socketfactory.createSocket();
			SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
			SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}

	public Socket createSocket(String host, int port) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	@Override
	public boolean equals(Object obj)
	{
		return ((obj != null) && obj.getClass().equals(MySSLSocketFactory.class));
	}

	@Override
	public int hashCode()
	{
		return MySSLSocketFactory.class.hashCode();
	}

}