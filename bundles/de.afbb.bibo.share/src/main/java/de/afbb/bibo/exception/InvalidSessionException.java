package de.afbb.bibo.exception;

import java.net.ConnectException;

/**
 * exception to indicate that the given session was rejected by the servlet
 * 
 * @author David Becker
 *
 */
public class InvalidSessionException extends ConnectException {

	private static final long serialVersionUID = -6036512773011286257L;

}
