package de.afbb.bibo.share;

import de.afbb.bibo.share.model.Curator;

/**
 * holds the session token for the current session
 * 
 * @author dbecker
 */
public final class SessionHolder {

	private static final SessionHolder instance = new SessionHolder();
	private String sessionToken = null;
	private Curator curator = null;

	private SessionHolder() {
	}

	public Curator getCurator() {
		return curator;
	}

	public void setCurator(final Curator curator) {
		this.curator = curator;
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
