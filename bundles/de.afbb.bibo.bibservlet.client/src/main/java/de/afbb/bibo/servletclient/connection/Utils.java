package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.afbb.bibo.exception.BadGatewayException;
import de.afbb.bibo.exception.InvalidSessionException;
import de.afbb.bibo.share.beans.BeanExclusionStrategy;

/**
 *
 * @author David Becker
 *
 */
final class Utils {

	// used to only throw exceptions once
	private static final Set<String> invalidatedSessionTokens = new HashSet<String>();

	private Utils() {
	}

	public static final Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new BeanExclusionStrategy())
			.setDateFormat("yyyyMMddHHmmss").create();

	/**
	 * converts HTTP error codes into exceptions
	 *
	 * @param code
	 * @throws ConnectException
	 */
	public static ConnectException createExceptionForCode(final int code) {
		if (HttpServletResponse.SC_OK == code || HttpServletResponse.SC_NOT_FOUND == code) {
			return null;
		}
		if (HttpServletResponse.SC_UNAUTHORIZED == code) {
			String sessionToken;
			try {
				sessionToken = ServerConnection.getInstance().getSessionToken();
				if (!isInvalidToken(sessionToken)) {
					addInvalidSessionToken(sessionToken);
					return new InvalidSessionException();
				}
			} catch (final ConnectException e) {
				// should never happen at this point...
			}
			// don't throw an other exception, just swallow this incident
			return null;
		} else if (HttpServletResponse.SC_BAD_GATEWAY == code) {
			return new BadGatewayException();
		}
		return new ConnectException("Wrong status code. Recieved was: " + code);
	}

	private static boolean isInvalidToken(final String token) {
		synchronized (invalidatedSessionTokens) {
			return invalidatedSessionTokens.contains(token);
		}
	}

	private static void addInvalidSessionToken(final String token) {
		synchronized (invalidatedSessionTokens) {
			invalidatedSessionTokens.add(token);
		}
	}

}
