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
package com.utest.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class ValidationException extends Exception
{
	private final List<String>	messageKeys	= new ArrayList<String>();

	public ValidationException()
	{
	}

	public ValidationException(final String message)
	{
		super(message);
	}

	public ValidationException(final Throwable cause)
	{
		super(cause);
	}

	public ValidationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public List<String> getMessageKeys()
	{
		return messageKeys;
	}

	public void addMessageKeys(final Collection<String> value)
	{
		messageKeys.addAll(value);
	}

	public ValidationException addMessageKey(final String value)
	{
		messageKeys.add(value);
		return this;
	}
}
