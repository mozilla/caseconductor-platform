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
package com.utest.webservice.client.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.MessageBodyReader;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.cxf.jaxrs.provider.AegisElementProvider;

public class RestClient
{
	private static String	DEFAULT_ACCEPT	= "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5";
	private String			baseUrl			= "https://localhost";
	private String			servicePath		= "/tcm/services/v2/rest/";
	private static String	kestorepassword	= null;
	private static String	keyStoreFile	= null;
	private HttpClient		client;
	private InputStream		responseStream;

	private String			accept			= DEFAULT_ACCEPT;
	static Header			requestHeader;
	static Header			responseHeader;
	static Cookie[]			cookies;
	static HttpState		clientState		= new HttpState();

	public RestClient(String baseUrl, String servicePath)
	{
		this.baseUrl = baseUrl;
		this.servicePath = servicePath;
	}

	public InputStream getResponseStream()
	{
		return responseStream;
	}

	public String getAccept()
	{
		return accept;
	}

	public void setAccept(String accept)
	{
		this.accept = accept;
	}

	@SuppressWarnings("deprecation")
	public HttpClient getClient()
	{
		if (client == null)
		{
			client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000000);
			client.setState(clientState);
			// Protocol myhttps = new Protocol("https", new
			// MySSLSocketFactory(), 443);
			Protocol myhttps = null;
			if (keyStoreFile != null)
			{
				try
				{
					myhttps = new Protocol("https", new AuthSSLProtocolSocketFactory(null, null, new URL("file:" + keyStoreFile), kestorepassword), 443);
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
			}
			Protocol.registerProtocol("https", myhttps);
		}
		return client;
	}

	public void login(String login, String password) throws Exception
	{
		HttpClient httpClient = getClient();
		httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(login, password));
		executeMethod(createGet("users", "login", null));
	}

	public void logout() throws Exception
	{
		executeMethod(createGet("users", "logout", null));
	}

	public int executeMethod(HttpMethod method) throws HttpException, IOException
	{
		int result = client.executeMethod(method);
		processResponse(method);
		return result;
	}

	private void processResponse(HttpMethod method) throws IOException
	{
		method.getRequestHeader("Accept");
		responseStream = method.getResponseBodyAsStream();
	}

	@SuppressWarnings("unchecked")
	public <T> T getResponseObject(Class<T> type) throws WebApplicationException, IOException
	{
		MessageBodyReader reader = new AegisElementProvider();
		return (T) reader.readFrom(type, null, null, null, null, responseStream);
	}

	public static Cookie[] getCookies()
	{
		return cookies;
	}

	public static void setCookies(Cookie[] cookies)
	{
		RestClient.cookies = cookies;
	}

	private HttpMethod setHeader(HttpMethod method)
	{
		method.addRequestHeader("Accept", accept);
		return method;
	}

	public HttpMethod createGet(String service, String path, Map<String, Object> parameters)
	{
		final GetMethod get = new GetMethod(baseUrl + servicePath + service + "/" + path + paramsToString(parameters));
		setHeader(get);
		return get;
	}

	public HttpMethod createPut(String service, String path, Map<String, Object> parameters)
	{
		PutMethod put = new PutMethod(baseUrl + servicePath + service + "/" + path + paramsToString(parameters));
		setHeader(put);
		return put;
	}

	public HttpMethod createDelete(String service, String path, Map<String, Object> parameters)
	{
		DeleteMethod delete = new DeleteMethod(baseUrl + servicePath + service + "/" + path + paramsToString(parameters));
		setHeader(delete);
		return delete;
	}

	public HttpMethod createPost(String service, String path, Map<String, Object> parameters)
	{
		final PostMethod post = new PostMethod(baseUrl + servicePath + service + "/" + path);
		post.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		for (final Map.Entry<String, Object> param : parameters.entrySet())
		{
			post.addParameter(param.getKey(), (param.getValue() == null) ? "" : param.getValue().toString());
		}
		setHeader(post);
		return post;
	}

	private static String paramsToString(final Map<String, Object> params)
	{
		if (params == null)
			return "";
		String str = "";
		String separator = "?";
		for (final Map.Entry<String, Object> param : params.entrySet())
		{
			try
			{

				if (param.getValue() instanceof List)
				{
					for (Object l : (List) param.getValue())
					{
						str += separator + param.getKey() + "=" + URLEncoder.encode(l.toString(), "UTF-8");
						separator = "&";
					}
				}
				else
				{
					str += separator + param.getKey() + "=" + URLEncoder.encode(param.getValue().toString(), "UTF-8");
				}
			}
			catch (final UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			separator = "&";
		}
		return str;
	}

	public static void setKestorepassword(String kestorepassword)
	{
		RestClient.kestorepassword = kestorepassword;
	}

	public static void setKeyStoreFile(String keyStoreFile)
	{
		RestClient.keyStoreFile = keyStoreFile;
	}

}
