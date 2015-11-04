/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servletclient.interfaces;

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
	 * @return true if log in was successfull. False othersise.
	 * @throws ConnectException
	 */
	boolean loginWithHash(String userName, String hashedPassword) throws ConnectException;

	/**
	 * invalidates the session token
	 * 
	 */
	void invalidateSession();

}

