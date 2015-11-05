package de.afbb.bibo.share;

import java.net.ConnectException;

/**
 * service interface to create a session with the server
 * 
 * @author dbecker
 */
public interface ILoginService {

	/**
	 * requests the salt for given user
	 * 
	 * @param userName
	 *            name of the user
	 * @return password salt or empty String when user doesn't exist
	 * @throws ConnectException
	 */
	String requestSaltForUserName(String userName) throws ConnectException;

	/**
	 * request a session token for given user
	 * 
	 * @param userName
	 *            name of the user
	 * @param hashedPassword
	 *            hashed password for user
	 * @return session token or empty String when hashedPassword isn't valid for given userName
	 * @throws ConnectException
	 */
	boolean loginWithHash(String userName, String hashedPassword) throws ConnectException;

	/**
	 * invalidates the given session token
	 * 
	 */
	void invalidateSession();

}
