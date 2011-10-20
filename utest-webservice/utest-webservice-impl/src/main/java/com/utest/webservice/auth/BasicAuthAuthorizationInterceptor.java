/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
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
package com.utest.webservice.auth;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Map;

import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.utest.webservice.util.SessionUtil;

public class BasicAuthAuthorizationInterceptor extends SoapHeaderInterceptor
{
	AuthenticationProvider	authenticationProvider;

	public void setAuthenticationProvider(final AuthenticationProvider authenticationProvider)
	{
		this.authenticationProvider = authenticationProvider;
	}

	@Override
	public void handleMessage(final Message message) throws Fault
	{
		try
		{
			AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);
			Authentication authentication = SessionUtil.getAuthenticationToken(message);
			if (policy == null && authentication == null)
			{
				sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
				return;
			}
			if (authentication == null)
			{
				authentication = new UsernamePasswordAuthenticationToken(policy.getUserName(), policy.getPassword());
				((UsernamePasswordAuthenticationToken) authentication).setDetails(message.get("HTTP.REQUEST"));
				authentication = authenticationProvider.authenticate(authentication);
			}
			else
			{
				if (((UsernamePasswordAuthenticationToken) authentication).getDetails() == null)
				{
					((UsernamePasswordAuthenticationToken) authentication).setDetails(message.get("HTTP.REQUEST"));
				}
			}
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (final RuntimeException ex)
		{
			sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
			throw ex;
		}
	}

	@SuppressWarnings("unchecked")
	private void sendErrorResponse(final Message message, final int responseCode)
	{
		final Message outMessage = getOutMessage(message);
		outMessage.put(Message.RESPONSE_CODE, responseCode);
		// Set the response headers
		final Map responseHeaders = (Map) message.get(Message.PROTOCOL_HEADERS);
		if (responseHeaders != null)
		{
			responseHeaders.put("WWW-Authenticate", Arrays.asList(new String[] { "Basic realm=realm" }));
			responseHeaders.put("Content-Length", Arrays.asList(new String[] { "0" }));
		}
		message.getInterceptorChain().abort();
		try
		{
			getConduit(message).prepare(outMessage);
			close(outMessage);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	private Message getOutMessage(final Message inMessage)
	{
		final Exchange exchange = inMessage.getExchange();
		Message outMessage = exchange.getOutMessage();
		if (outMessage == null)
		{
			final Endpoint endpoint = exchange.get(Endpoint.class);
			outMessage = endpoint.getBinding().createMessage();
			exchange.setOutMessage(outMessage);
		}
		outMessage.putAll(inMessage);
		return outMessage;
	}

	private Conduit getConduit(final Message inMessage) throws IOException
	{
		final Exchange exchange = inMessage.getExchange();
		final EndpointReferenceType target = exchange.get(EndpointReferenceType.class);
		final Conduit conduit = exchange.getDestination().getBackChannel(inMessage, null, target);
		exchange.setConduit(conduit);
		return conduit;
	}

	private void close(final Message outMessage) throws IOException
	{
		final OutputStream os = outMessage.getContent(OutputStream.class);
		os.flush();
		os.close();
	}

}
