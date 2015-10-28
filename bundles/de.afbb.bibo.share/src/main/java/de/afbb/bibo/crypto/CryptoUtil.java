package de.afbb.bibo.crypto;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * utility class for cryptographic methods
 * 
 * @author dbecker
 */
public final class CryptoUtil {

	private CryptoUtil() {
	}

	/**
	 * hashes a password
	 * 
	 * @param password
	 *            should not be null
	 * @param salt
	 *            should not be null
	 * @return null when any parameter is null or empty, hash otherwise
	 */
	public static String hashPassword(final String password, final String salt) {
		if (password != null && !password.isEmpty() && salt != null && !salt.isEmpty()) {
			return DigestUtils.shaHex(password + salt);
		}
		return null;
	}

}
