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

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class FileUploadUtil
{

	public static void uploadFile(final File targetFile, final String targetURL, final String targerFormFieldName_) throws Exception
	{
		final PostMethod filePost = new PostMethod(targetURL);
		filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		filePost.addRequestHeader("X-Atlassian-Token", "no-check");
		try
		{
			final FilePart fp = new FilePart(targerFormFieldName_, targetFile);
			fp.setTransferEncoding(null);
			final Part[] parts = { fp };
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			final HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			final int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK)
			{
				Logger.getLogger(FileUploadUtil.class).info("Upload complete, response=" + filePost.getResponseBodyAsString());
			}
			else
			{
				Logger.getLogger(FileUploadUtil.class).info("Upload failed, response=" + HttpStatus.getStatusText(status));
			}
		}
		catch (final Exception ex)
		{
			Logger.getLogger(FileUploadUtil.class).error("ERROR: " + ex.getClass().getName() + " " + ex.getMessage(), ex);
			throw ex;
		}
		finally
		{
			Logger.getLogger(FileUploadUtil.class).debug(new String(filePost.getResponseBody()));
			filePost.releaseConnection();
		}

	}

}
