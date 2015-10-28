package de.afbb.bibo.share;

/**
 * service interface to create a session with the server
 * 
 * @author dbecker
 */
public interface LoginService {

	/**
	 * requests the salt for given user
	 * 
	 * @param userName
	 *            name of the user
	 * @return password salt or empty String when user doesn't exist
	 */
	String requestSaltForUserName(String userName);

	/**
	 * request a session token for given user
	 * 
	 * @param userName
	 *            name of the user
	 * @param hashedPassword
	 *            hashed password for user
	 * @return session token or empty String when hashedPassword isn't valid for given userName
	 */
	String requestSessionTokenForHash(String userName, String hashedPassword);

}
