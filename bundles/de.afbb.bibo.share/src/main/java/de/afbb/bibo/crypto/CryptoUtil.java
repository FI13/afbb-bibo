package de.afbb.bibo.crypto;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * utility class for cryptographic methods
 * 
 * @author dbecker
 */
public final class CryptoUtil {

	private static final Random RANDOM = new SecureRandom();

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

	public static String generateSalt() {
		final byte[] salt = new byte[50];
		RANDOM.nextBytes(salt);
		return DigestUtils.shaHex(new String(salt));
	}

	// TODO Remove
//	public static void main(final String[] args) {
//		System.out.println("Started");
//		final String s = generateSalt();
//		System.out.println("Salt: " + s);
//		final String p = hashPassword("...", s);
//		System.out.println("Pass: " + p);
//	}

}
