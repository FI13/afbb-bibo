/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import de.afbb.bibo.share.ILoginService;
import de.afbb.bibo.share.callback.EventListener;
import de.afbb.bibo.share.model.NavigationTreeNodeType;

/**
 * HTTP service that manages the session with the server
 *
 * @author fi13.pendrulat
 * @author David Becker
 */
public class LoginServiceImpl implements ILoginService {

	private final Set<EventListener> listeners = new HashSet<EventListener>();

	@Override
	public String requestSaltForUserName(final String userName) throws ConnectException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("name", userName);

		final HttpResponse resp = ServerConnection.getInstance().request("/login/requestSalt", "GET", params, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			return resp.getData();
		} else if (resp.getStatus() != HttpServletResponse.SC_NOT_FOUND) {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		return null;
	}

	@Override
	public boolean loginWithHash(final String userName, final String hashedPassword) throws ConnectException {
		final boolean successfull = ServerConnection.getInstance().login(userName, hashedPassword);
		if (successfull) {
			notifyListener(NavigationTreeNodeType.ROOT);
		}
		return successfull;

	}

	@Override
	public void invalidateSession() {
		try {
			ServerConnection.getInstance().logout();
		} catch (final ConnectException ex) {
			Logger.getLogger(LoginServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void register(final EventListener listener) {
		listeners.add(listener);
	}

	private void notifyListener(final NavigationTreeNodeType type) {
		for (final EventListener eventListener : listeners) {
			eventListener.invalidate(type);
		}
	}

}
