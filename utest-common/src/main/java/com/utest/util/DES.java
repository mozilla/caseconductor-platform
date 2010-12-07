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

import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public final class DES
{

	/**
	 * The encryption algo
	 */
	private static final String	ALGORITHM			= "DESEde";

	/**
	 * The value used to pad the key - VMKTEST
	 */
	private static final byte	PADDING_VALUE		= 0x00;

	/**
	 * The length of the key
	 */
	private static final byte	KEY_LENGTH			= 24;

	/**
	 * The default key as string
	 */
	private static final String	DEFAULT_STRING_KEY	= "Hay mas cosas entre el cielo y la tierra, Horacio. Que las que sospecha tu filosofia.";

	/**
	 * The default key
	 */
	private static final byte[]	DEFAULT_KEY;

	// static initialization code to define the default key
	static
	{

		final String d = DEFAULT_STRING_KEY;
		final byte[] key = d.getBytes();

		DEFAULT_KEY = paddingKey(key, KEY_LENGTH, PADDING_VALUE);
	}

	private DES()
	{
		// avoid instantiation
	}

	/**
	 * Encrypts a byte array.
	 * 
	 * @param plainBytes
	 *            the bytes to encrypt
	 * @param encryptionKey
	 *            the key to use in the encryption phase
	 * @return the encrypted bytes.
	 */
	public static byte[] encrypt(final byte[] plainBytes, byte[] encryptionKey) throws Exception
	{

		if (plainBytes == null)
		{
			return null;
		}
		if (encryptionKey == null)
		{
			throw new IllegalArgumentException("the key can not be null");
		}

		byte[] cipherBytes = null;

		encryptionKey = paddingKey(encryptionKey, KEY_LENGTH, PADDING_VALUE);

		final KeySpec keySpec = new DESedeKeySpec(encryptionKey);
		final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		final Cipher cipher = Cipher.getInstance(ALGORITHM);

		final SecretKey key = keyFactory.generateSecret(keySpec);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		cipherBytes = cipher.doFinal(plainBytes);

		return cipherBytes;
	}

	/**
	 * Encrypts a byte array.
	 * 
	 * @return the encrypted bytes.
	 * @param plainText
	 *            the plain text
	 * @param key
	 *            the key to use
	 */
	public static String encrypt(final String plainText, final byte[] key) throws Exception
	{
		if (plainText == null)
		{
			return null;
		}
		if (key == null)
		{
			throw new IllegalArgumentException("the key can not be null");
		}

		return new String(Base64.encode(encrypt(plainText.getBytes(), key)));
	}

	/**
	 * Encrypts a byte array.
	 * 
	 * @param plainText
	 *            the plain text to encrypt.
	 * @return the encrypted bytes.
	 */
	public static String encrypt(final String plainText) throws Exception
	{
		return encrypt(plainText, DEFAULT_KEY);
	}

	/**
	 * Decrypt a text.
	 * 
	 * @return the original plain text
	 * @param encryptedBytes
	 *            the encrypted text
	 * @param encryptionKey
	 *            the key to used decrypting
	 */
	public static byte[] decrypt(final byte[] encryptedBytes, byte[] encryptionKey) throws Exception
	{

		if (encryptedBytes == null)
		{
			return null;
		}
		if (encryptionKey == null)
		{
			throw new IllegalArgumentException("The key can not be null");
		}
		byte[] plainBytes = null;
		encryptionKey = paddingKey(encryptionKey, KEY_LENGTH, PADDING_VALUE);

		final KeySpec keySpec = new DESedeKeySpec(encryptionKey);
		final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		final Cipher cipher = Cipher.getInstance(ALGORITHM);

		final SecretKey key = keyFactory.generateSecret(keySpec);
		cipher.init(Cipher.DECRYPT_MODE, key);

		plainBytes = cipher.doFinal(encryptedBytes);

		return plainBytes;
	}

	/**
	 * Decrypt a text.
	 * 
	 * @return the original plain text
	 * @param encryptedBytes
	 *            the encrypted bytes
	 */
	public static byte[] decrypt(final byte[] encryptedBytes) throws Exception
	{
		return decrypt(encryptedBytes, DEFAULT_KEY);
	}

	/**
	 * Decrypt a text.
	 * 
	 * @return the original, plain text password
	 * @param encryptedText
	 *            the encrypted text
	 */
	public static String decrypt(final String encryptedText) throws Exception
	{
		if (encryptedText == null)
		{
			return null;
		}
		return new String(decrypt(Base64.decode(encryptedText), DEFAULT_KEY));
	}

	/**
	 * Decrypt a text.
	 * 
	 * @return the original, plain text password
	 * @param encryptedText
	 *            if the encrypted text
	 * @param key
	 *            the key to use decrypting
	 */
	public static String decrypt(final String encryptedText, final byte[] key) throws Exception
	{
		if (encryptedText == null)
		{
			return null;
		}
		return new String(decrypt(Base64.decode(encryptedText), key));
	}

	/**
	 * Returns a padded copy of the given bytes array using to the given lenght
	 * using the given value.
	 * 
	 * @param b
	 *            the bytes array
	 * @param len
	 *            the required length
	 * @param paddingValue
	 *            the value to use padding the given bytes array
	 * @return the padded copy of the given bytes array
	 */
	private static byte[] paddingKey(final byte[] b, final int len, final byte paddingValue)
	{

		final byte[] newValue = new byte[len];

		if (b == null)
		{
			//
			// The given byte[] is null...returning a new byte[] with the
			// required
			// length
			//
			return newValue;
		}

		if (b.length >= len)
		{
			System.arraycopy(b, 0, newValue, 0, len);
			return newValue;
		}

		System.arraycopy(b, 0, newValue, 0, b.length);
		Arrays.fill(newValue, b.length, len, paddingValue);
		return newValue;

	}
}
