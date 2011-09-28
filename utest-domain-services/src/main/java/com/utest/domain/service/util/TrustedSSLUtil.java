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
package com.utest.domain.service.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * Utility to override SSL exceptions thrown. Should be called before trying to
 * access other applications via HTTPS protocol when no valid certificate is
 * available.
 * 
 * @author VKISEN
 * 
 */
public class TrustedSSLUtil implements ProtocolSocketFactory
{

	public static void initialize()
	{
		Protocol.registerProtocol("https", new Protocol("https", new TrustedSSLUtil(), 443));
	}

	private SSLContext			sslcontext		= null;

	private static TrustManager	trustAllCerts	= new X509TrustManager()
												{
													public java.security.cert.X509Certificate[] getAcceptedIssuers()
													{
														return null;
													}

													public void checkClientTrusted(final java.security.cert.X509Certificate[] certs, final String authType)
													{
													}

													public void checkServerTrusted(final java.security.cert.X509Certificate[] certs, final String authType)
													{
													}
												};

	/**
	 * Constructor for TrustedSSLUtil.
	 */
	private TrustedSSLUtil()
	{
		super();
	}

	private static SSLContext createSSLContext()
	{
		try
		{
			final SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { trustAllCerts }, null);
			return context;
		}
		catch (final Exception e)
		{
			throw new HttpClientError(e.toString());
		}
	}

	private SSLContext getSSLContext()
	{
		if (this.sslcontext == null)
		{
			this.sslcontext = createSSLContext();
		}
		return this.sslcontext;
	}

	public Socket createSocket(final String host, final int port, final InetAddress clientHost, final int clientPort) throws IOException, UnknownHostException
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
		final int timeout = params.getConnectionTimeout();
		final SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0)
		{
			return socketfactory.createSocket(host, port, localAddress, localPort);
		}
		else
		{
			final Socket socket = socketfactory.createSocket();
			final SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
			final SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}

	public Socket createSocket(final String host, final int port) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	@Override
	public boolean equals(final Object obj)
	{
		return ((obj != null) && obj.getClass().equals(TrustedSSLUtil.class));
	}

	@Override
	public int hashCode()
	{
		return TrustedSSLUtil.class.hashCode();
	}
}
