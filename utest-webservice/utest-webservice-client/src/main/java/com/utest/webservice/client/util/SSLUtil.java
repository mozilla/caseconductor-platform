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
