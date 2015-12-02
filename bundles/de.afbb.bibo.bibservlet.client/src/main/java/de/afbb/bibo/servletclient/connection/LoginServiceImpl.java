/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servletclient.connection;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import de.afbb.bibo.share.ILoginService;

/**
 *
 * @author fi13.pendrulat
 */
public class LoginServiceImpl implements ILoginService {

	@Override
	public String requestSaltForUserName(final String userName) throws ConnectException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("name", userName);

		final HttpResponse resp = ServerConnection.getInstance().request("/login/requestSalt", "GET", params, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			return resp.getData();
		} else {
			throw new ConnectException("Wrong status code. Recieved was: " + resp.getStatus());
		}
	}

	@Override
	public boolean loginWithHash(final String userName, final String hashedPassword) throws ConnectException {
		return ServerConnection.getInstance().login(userName, hashedPassword);

	}

	@Override
	public void invalidateSession() {
		try {
			ServerConnection.getInstance().logout();
		} catch (final ConnectException ex) {
			Logger.getLogger(LoginServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
