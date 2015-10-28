package de.afbb.bibo.share;

/**
 * holds the session token for the current session
 * 
 * @author dbecker
 */
public final class SessionHolder {

	private static final SessionHolder instance = new SessionHolder();
	private String sessionToken = null;

	private SessionHolder() {
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(final String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public static SessionHolder getInstance() {
		return instance;
	}

}
