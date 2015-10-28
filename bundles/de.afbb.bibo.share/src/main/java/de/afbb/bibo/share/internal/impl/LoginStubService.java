package de.afbb.bibo.share.internal.impl;

import de.afbb.bibo.crypto.CryptoUtil;
import de.afbb.bibo.share.LoginService;

/**
 * stub implementation of {@link LoginService}
 * 
 * @author dbecker
 */
public class LoginStubService implements LoginService {

	private static final String USER = "Hugo";
	private static final String SALT = "salt";
	private static final String PASSWORD = "password123";
	private static final String HASH = CryptoUtil.hashPassword(PASSWORD, SALT);

	@Override
	public String requestSaltForUserName(final String userName) {
		return USER.equals(userName) ? SALT : null;
	}

	@Override
	public String requestSessionTokenForHash(final String userName, final String hashedPassword) {
		return USER.equals(userName) && HASH.equals(hashedPassword) ? "token" : null;
	}

}
