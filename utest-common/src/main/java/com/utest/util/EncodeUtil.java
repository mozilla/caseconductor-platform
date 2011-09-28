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
package com.utest.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncodeUtil
{

	private static String	ALGORITHM	= "SHA-512";

	public static String encode(final String plaintext)
	{
		try
		{
			final byte[] plain = plaintext.getBytes();
			byte[] hashed;
			final MessageDigest sha = MessageDigest.getInstance(ALGORITHM);
			sha.update(plain);
			hashed = sha.digest();
			final StringBuffer sb = new StringBuffer();
			final int length = hashed.length;
			for (int i = 0; i < (length) && i < hashed.length; i++)
			{
				sb.append(Character.forDigit((hashed[i] >>> 4) & 0xf, 16));
				sb.append(Character.forDigit(hashed[i] & 0xf, 16));
			}

			return sb.toString();

		}
		catch (final NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
}
