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
package com.utest.util;

import java.math.BigInteger;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class CrytographicTool
{

	public enum CryptoAlgorithm
	{
		DES, BLOWFISH;
	}

	private CrytographicTool()
	{
		// avoid instantiation
	}

	/**
	 * Encrypts a text.
	 * 
	 * @param source
	 *            the plain text to encrypt.
	 * @return the encrypted text.
	 * @throws Exception
	 */
	public static String encrypt(final String source, final CryptoAlgorithm algorithm, final String blowfishKey) throws Exception
	{

		String result = "";

		if (CryptoAlgorithm.BLOWFISH.equals(algorithm))
		{
			// From hex to bytes
			final byte[] keyBytes = new BigInteger(blowfishKey, 16).toByteArray();

			final Key key = new SecretKeySpec(keyBytes, "Blowfish");

			final BASE64Encoder encoder = new BASE64Encoder();

			final Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// Encrypt tp blowfish
			final byte[] ciphertext = cipher.doFinal(source.getBytes("UTF8"));
			// Encode to 64 bits
			result = encoder.encode(ciphertext);

			return result;
		}

		if (CryptoAlgorithm.DES.equals(algorithm))
		{
			result = DES.encrypt(source);
		}
		return result;
	}

	/**
	 * Decrypt a text
	 * 
	 * @param source
	 *            the encrypted text
	 * @return the original plain text
	 */
	public static String decrypt(final String source, final CryptoAlgorithm algorithm, final String blowfishKey) throws Exception
	{

		String result = "";
		if (CryptoAlgorithm.DES.equals(algorithm))
		{
			result = DES.decrypt(source);
			return result;
		}

		if (CryptoAlgorithm.BLOWFISH.equals(algorithm))
		{
			// From hex to bytes
			final byte[] keyBytes = new BigInteger(blowfishKey, 16).toByteArray();

			final Key key = new SecretKeySpec(keyBytes, "Blowfish");

			final Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);

			final BASE64Decoder decoder = new BASE64Decoder();
			// the replace is due to a replace that the service is doing and
			// when we get that string from the server, we have some problems.
			final byte[] textToCipher = decoder.decodeBuffer(source.replace(" ", "+"));

			final byte[] ciphertext = cipher.doFinal(textToCipher);

			result = new String(ciphertext, "UTF8");
			return result;
		}

		return result;
	}

}
