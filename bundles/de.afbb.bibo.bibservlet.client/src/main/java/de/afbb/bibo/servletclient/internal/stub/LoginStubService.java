package de.afbb.bibo.servletclient.internal.stub;

import java.net.ConnectException;

import de.afbb.bibo.crypto.CryptoUtil;
import de.afbb.bibo.share.ILoginService;
import de.afbb.bibo.share.callback.EventListener;

/**
 * stub implementation of {@link ILoginService}
 *
 * @author David Becker
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
	public boolean loginWithHash(final String userName, final String hashedPassword) throws ConnectException {
		return USER.equals(userName) && HASH.equals(hashedPassword);
	}

	@Override
	public void invalidateSession() {
		// nothing to do in stub service
	}

	@Override
	public void register(final EventListener listener) {
		// nothing to do in stub service
	}

}
