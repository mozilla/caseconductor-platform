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
package com.utest.webservice.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.cxf.configuration.jsse.TLSClientParameters;

public class SSLUtil
{
	public static TLSClientParameters getEnableSSl()
	{

		final TLSClientParameters sslTlc = new TLSClientParameters()
		{

			@Override
			public TrustManager[] getTrustManagers()
			{
				return new TrustManager[] {
					new X509TrustManager()
					{

						public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException
						{
						}

						public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException
						{
						}

						public X509Certificate[] getAcceptedIssuers()
						{
							return null;
						}
					}
				};
			}

		};
		sslTlc.setSecureSocketProtocol("SSL");
		sslTlc.setDisableCNCheck(true);
		return sslTlc;
	}

	public static TLSClientParameters getTLSconfiguration(String keyStoreFile, String kestorepassword) throws Exception
	{
		final TLSClientParameters tlsParams = new TLSClientParameters();

		final KeyStore trustStore = KeyStore.getInstance("JKS");
		final File truststoreFile = new File(keyStoreFile);
		trustStore.load(new FileInputStream(truststoreFile), kestorepassword.toCharArray());
		final TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustFactory.init(trustStore);
		final TrustManager[] tm = trustFactory.getTrustManagers();
		tlsParams.setTrustManagers(tm);
		tlsParams.setSecureSocketProtocol("SSL");
		// DisableCNCheck could be removed after final certificate for
		// production will be established
		tlsParams.setDisableCNCheck(true);
		return tlsParams;
	}

}
