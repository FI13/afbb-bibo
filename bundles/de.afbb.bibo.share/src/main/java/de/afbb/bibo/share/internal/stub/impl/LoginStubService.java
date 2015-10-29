package de.afbb.bibo.share.internal.stub.impl;

import java.net.ConnectException;

import de.afbb.bibo.crypto.CryptoUtil;
import de.afbb.bibo.share.ILoginService;

/**
 * stub implementation of {@link ILoginService}
 * 
 * @author dbecker
 */
public class LoginStubService implements ILoginService {

	private static final String USER = "Hugo";
	private static final String SALT = "salt";
	private static final String PASSWORD = "password123";
	private static final String HASH = CryptoUtil.hashPassword(PASSWORD, SALT);

	@Override
	public String requestSaltForUserName(final String userName) throws ConnectException {
		return USER.equals(userName) ? SALT : null;
	}

	@Override
	public String requestSessionTokenForHash(final String userName, final String hashedPassword) throws ConnectException {
		return USER.equals(userName) && HASH.equals(hashedPassword) ? "token" : null;
	}

	@Override
	public void invalidateSession(final String sessionToken) {
		// nothing to do in stub service
	}

}
