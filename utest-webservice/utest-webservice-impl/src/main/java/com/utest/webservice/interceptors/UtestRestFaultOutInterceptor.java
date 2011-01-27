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

import java.net.HttpURLConnection;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.binding.xml.interceptor.XMLFaultOutInterceptor;
import org.apache.cxf.common.i18n.BundleUtils;
import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.hibernate.StaleObjectStateException;
import org.hibernate.StaleStateException;

import com.utest.exception.ActivatingIncompleteEntityException;
import com.utest.exception.ActivatingNotApprovedEntityException;
import com.utest.exception.ApprovingIncompleteEntityException;
import com.utest.exception.ChangingActivatedEntityException;
import com.utest.exception.DeletingActivatedEntityException;
import com.utest.exception.DeletingUsedEntityException;
import com.utest.exception.DomainException;
import com.utest.exception.DuplicateNameException;
import com.utest.exception.DuplicateTestCaseStepException;
import com.utest.exception.EmailInUseException;
import com.utest.exception.IncludingMultileVersionsOfSameEntityException;
import com.utest.exception.IncludingNotActivatedEntityException;
import com.utest.exception.InvalidParentChildEnvironmentException;
import com.utest.exception.NotFoundException;
import com.utest.exception.ScreenNameInUseException;
import com.utest.exception.TestCaseExecutionBlockedException;
import com.utest.exception.TestCaseExecutionWithoutRestartException;
import com.utest.exception.UnsupportedEnvironmentSelectionException;
import com.utest.exception.ValidationException;

public class UtestRestFaultOutInterceptor extends AbstractOutDatabindingInterceptor
{
	private static final ResourceBundle	BUNDLE		= BundleUtils.getBundle(XMLFaultOutInterceptor.class);
	private static final int			TYPE_XML	= 1;
	private static final int			TYPE_JSON	= 2;
	private static final int			TYPE_TEXT	= 3;

	public UtestRestFaultOutInterceptor()
	{
		super(Phase.MARSHAL);
	}

	public UtestRestFaultOutInterceptor(final String phase)
	{
		super(phase);
	}

	@Override
	public void handleMessage(final Message message) throws Fault
	{
		try
		{
			final java.io.OutputStream outOriginal = message.getContent(java.io.OutputStream.class);
			outOriginal.write(buildResponse(message).getBytes());
			outOriginal.flush();
			outOriginal.close();
			message.getInterceptorChain().abort();
		}
		catch (final Exception e)
		{
			throw new Fault(new org.apache.cxf.common.i18n.Message("XML_WRITE_EXC", BUNDLE), e);
		}

		return;

	}

	@Override
	public void handleFault(final Message message) throws Fault
	{
	}

	public String buildResponse(final Message message)
	{
		final StringBuffer sb = new StringBuffer();
		final Fault fault = (Fault) message.getContent(Exception.class);
		final String accept = (String) message.getExchange().getInMessage().get(Message.ACCEPT_CONTENT_TYPE);
		int type = TYPE_TEXT;
		int responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
		if (accept.contains(MediaType.APPLICATION_XML))
		{
			type = TYPE_XML;
			message.put(Message.CONTENT_TYPE, MediaType.APPLICATION_XML);
		}
		else if (accept.contains(MediaType.APPLICATION_JSON))
		{
			type = TYPE_JSON;
			message.put(Message.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		}
		else
		{
			message.put(Message.CONTENT_TYPE, MediaType.TEXT_PLAIN);
		}
		sb.append(getStart(type));
		if (fault.getCause() instanceof ValidationException)
		{
			final ValidationException ve = (ValidationException) fault.getCause();
			for (final String errmsg : ve.getMessageKeys())
			{
				sb.append(createError(type, fault.getCause(), errmsg));
			}
		}
		else
		{
			sb.append(createError(type, fault.getCause(), fault.getCause().getMessage()));
		}

		if ((fault.getCause() instanceof org.apache.cxf.interceptor.security.AccessDeniedException)
				|| (fault.getCause() instanceof org.springframework.security.access.AccessDeniedException))
		{
			responseCode = HttpURLConnection.HTTP_FORBIDDEN;// Access deny
		}
		else if (fault.getCause() instanceof NotFoundException)
		{
			responseCode = HttpURLConnection.HTTP_NOT_FOUND;// Not found
		}
		else if ((fault.getCause() instanceof StaleObjectStateException) || (fault.getCause() instanceof StaleStateException))
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// conflict
		}
		else if (fault.getCause() instanceof EmailInUseException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// conflict
		}
		else if (fault.getCause() instanceof ScreenNameInUseException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// conflict
		}
		else if (fault.getCause() instanceof InvalidParentChildEnvironmentException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// conflict
		}
		else if (fault.getCause() instanceof DuplicateTestCaseStepException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// conflict
		}
		else if (fault.getCause() instanceof DuplicateNameException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// conflict
		}
		else if (fault.getCause() instanceof DeletingActivatedEntityException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// not allowed
		}
		else if (fault.getCause() instanceof DeletingUsedEntityException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// not allowed
		}
		else if (fault.getCause() instanceof ActivatingIncompleteEntityException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// not allowed
		}
		else if (fault.getCause() instanceof UnsupportedEnvironmentSelectionException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// not allowed
		}
		else if (fault.getCause() instanceof ApprovingIncompleteEntityException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// not allowed
		}
		else if (fault.getCause() instanceof ActivatingNotApprovedEntityException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// not allowed
		}
		else if (fault.getCause() instanceof ChangingActivatedEntityException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// not allowed
		}
		else if (fault.getCause() instanceof IncludingMultileVersionsOfSameEntityException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// not allowed
		}
		else if (fault.getCause() instanceof IncludingNotActivatedEntityException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// Not allowed
		}
		else if (fault.getCause() instanceof TestCaseExecutionBlockedException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// Not allowed
		}
		else if (fault.getCause() instanceof TestCaseExecutionWithoutRestartException)
		{
			responseCode = HttpURLConnection.HTTP_CONFLICT;// Not allowed
		}

		message.put(Message.RESPONSE_CODE, responseCode);
		sb.append(getEnd(type));
		return sb.toString();
	}

	private String createError(final int type, Throwable error, String msg)
	{
		msg = translateError(error, msg);
		switch (type)
		{
			case TYPE_XML:
				return "<error>" + msg + "</error>";
			case TYPE_JSON:
				return "{\"error\":\"" + msg + "\"}";
			default:
				return msg + "\n";
		}
	}

	// {"ns1.user":[{"@xsi.type":"ns1:user"
	private String getStart(final int type)
	{
		switch (type)
		{
			case TYPE_XML:
				return "<errors>";
			case TYPE_JSON:
				return "{\"errors\":[";
			default:
				return "";
		}
	}

	private String getEnd(final int type)
	{
		switch (type)
		{
			case TYPE_XML:
				return "</errors>";
			case TYPE_JSON:
				return "]}";
			default:
				return "";
		}
	}

	private String translateError(Throwable error, final String message)
	{

		if (error instanceof DomainException)
		{
			return ((DomainException) error).getErrorMessageKey();
		}

		if (message == null)
		{
			if (error != null)
			{
				return error.getClass().getSimpleName();
			}
			else
			{
				return null;
			}
		}
		else
		{
			return message;
		}

	}
}
