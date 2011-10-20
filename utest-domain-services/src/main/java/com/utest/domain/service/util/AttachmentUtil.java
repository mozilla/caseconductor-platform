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
 * @author Vadim Kisen
 *
 * copyright 2010 by uTest 
 */
package com.utest.domain.service.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttachmentUtil
{

	private static final String	boundary	= "AaB03x";
	// String dashes = "--";
	private static final String	twoHyphens	= "--";
	private static final String	newLine		= "\r\n";

	public boolean sendData(final byte[] binaryFileData, final String sentURL, final Map<String, String> params)
	{
		if (binaryFileData.length != 0)
		{
			try
			{
				final URL url = new URL(sentURL);
				final HttpURLConnection uc = (HttpURLConnection) url.openConnection();
				uc.setRequestMethod("POST");
				uc.setDoInput(true);
				uc.setDoOutput(true);
				uc.setUseCaches(false);
				uc.setDefaultUseCaches(false);
				uc.setRequestProperty("Accept", "image/pjpeg");
				uc.setRequestProperty("Connection", "Keep-Alive");
				uc.setRequestProperty("Content-type", "multipart/form-data; boundary=" + boundary);

				final DataOutputStream dos = new DataOutputStream(uc.getOutputStream());

				// Write all header stuff
				dos.writeBytes(twoHyphens + boundary + newLine);
				dos.writeBytes("Content-Disposition: form-data; name=\"thisFile\"; filename=\"c:\\new.jpg\"" + newLine);
				dos.writeBytes(newLine + twoHyphens);

				// Write actual file
				dos.write(binaryFileData, 0, binaryFileData.length);

				// Finish the output to the http handler
				if (params != null)
				{
					final Iterator<String> it = params.keySet().iterator();
					for (; it.hasNext();)
					{
						final String key = it.next();
						dos.writeBytes(makeFormData(key, params.get(key)));
					}
				}
				dos.writeBytes(newLine + twoHyphens + boundary + twoHyphens + newLine);

				dos.flush();
				dos.close();

				// Get whatever is returned
				final StringBuffer sb = new StringBuffer();
				final InputStreamReader in = new InputStreamReader(uc.getInputStream());
				int chr = in.read();
				while (chr != -1)
				{
					sb.append(String.valueOf((char) chr));
					chr = in.read();
				}
				in.close();
				return true;
			}
			catch (final Exception e)
			{
				// sb.append("No data sent back to Applet.");
				return false;
			}
		}
		return false;
	}

	private String makeFormData(final String name, final String value)
	{
		return newLine + twoHyphens + boundary + newLine + "Content-Disposition: form-data; name=\"" + name + "\"" + newLine + newLine + value + newLine + twoHyphens + boundary
				+ twoHyphens + newLine;
	}

	public static String getAbsolutePath(final String source, final Object... folders)
	{
		return getAbsolutePath(source, false, folders);
	}

	public static String getAbsolutePath(final String source, final boolean appendLastDash, final Object... folders)
	{
		final StringBuilder path = new StringBuilder();
		path.append(source);
		for (final Object folder : folders)
		{
			path.append("/").append(folder);
		}

		if (appendLastDash)
		{
			path.append("/");
		}

		return path.toString();
	}

	public static List<File> getFilesFiteredByExtension(final File parent, final List<String> extensions)
	{
		final List<File> res = new ArrayList<File>();
		if (!parent.isDirectory())
		{
			return res;
		}
		for (final File file : parent.listFiles())
		{
			if (matchByExtension(file, extensions))
			{
				res.add(file);
			}
		}
		return res;
	}

	private static boolean matchByExtension(final File file, final List<String> exts)
	{
		if (file.isDirectory())
		{
			return false;
		}

		final String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		for (final String extension : exts)
		{
			if (extension.equalsIgnoreCase(ext))
			{
				return true;
			}
		}
		return false;

	}

}
