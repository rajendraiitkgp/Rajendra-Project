package com.icelero.ias.as.cryptography;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

public class CryptUtilities {
	private static PasswordEncryptor encryptor = new BasicPasswordEncryptor();

	private CryptUtilities() {
	}

	/**
	 * Encrypts (digests) a password.
	 * 
	 * @param password
	 *            the password to be encrypted.
	 * @return the resulting digest.
	 */
	public static String encryptPassword(String password) {
		return encryptor.encryptPassword(password);
	}

	/**
	 * Checks an unencrypted (plain) password against an encrypted one (a
	 * digest) to see if they match.
	 * 
	 * @param plainPassword
	 *            the plain password to check.
	 * @param encryptedPassword
	 *            the digest against which to check the password.
	 * @return true if passwords match, false if not.
	 * @see StandardStringDigester#matches(String, String)
	 */
	public static boolean checkPassword(final String plainPassword, final String encryptedPassword) {
		return encryptor.checkPassword(plainPassword, encryptedPassword);
	}
}
