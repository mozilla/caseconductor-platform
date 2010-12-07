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
package com.utest.webservice.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RestUtil
{
	private static String[]	formats	= { "yyyy-MM-dd'T'HH:mm:ssz", //
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "MM/dd/yyyy", "MM/dd/yyyy h:mm a" };

	public static Date stringToDate(final String dateString)
	{
		Date ret = null;
		for (final String format : formats)
		{
			try
			{
				final SimpleDateFormat frm = new SimpleDateFormat(format);
				ret = frm.parse(dateString);
				break;
			}
			catch (final ParseException e)
			{
				continue;
			}
		}
		return ret;
	}

	public static byte[] downloadFile(final String url) throws Exception
	{
		final URL fileUrl = new URL(url);
		final InputStream in = fileUrl.openStream();
		final ByteArrayOutputStream buf = new ByteArrayOutputStream();
		final byte[] readBuf = new byte[1024];
		int read = in.read(readBuf);
		while (read > 0)
		{
			buf.write(readBuf, 0, read);
			read = in.read(readBuf);
		}
		return buf.toByteArray();
	}
}
