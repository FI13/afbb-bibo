/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servletclient.connection;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.afbb.bibo.properties.BiBoProperties;
import de.afbb.bibo.share.SessionHolder;
import de.afbb.bibo.share.model.Curator;

/**
 *
 * @author fi13.pendrulat
 */
public class ServerConnection {

	private final String HOST;

	private final String URL;
	private final int PORT;

	private String sessionToken;
	private boolean isLoggedIn;

	private static ServerConnection INSTANCE;

	private ServerConnection() throws NumberFormatException, IOException {
		isLoggedIn = false;
		URL = BiBoProperties.get("SERVER_URL");
		PORT = Integer.valueOf(BiBoProperties.get("SERVER_PORT"));
		HOST = "http://" + URL + ":" + PORT;
	}

	public static ServerConnection getInstance() throws ConnectException {
		if (INSTANCE == null) {
			try {
				INSTANCE = new ServerConnection();
			} catch (NumberFormatException | IOException e) {
				throw new ConnectException("Can't load configuration");
			}
		}
		return INSTANCE;
	}

	/**
	 * Make a Server request.
	 *
	 * @param url
	 *            The part of the url after the HOST:PORT part (for Example
	 *            http://localhost:8080 will automatically be appended to the
	 *            request.)
	 * @param method
	 *            The request method. The server understands "GET" and "POST".
	 * @param params
	 *            The request parameters, put in a map.
	 * @param json
	 *            An object parsed to JSON which will be sent to the server.
	 * @return HttpResponse
	 * @throws java.net.ConnectException
	 */
	public HttpResponse request(final String url, final String method, final Map<String, String> params,
			final String json) throws ConnectException {
		// Check for logged in
		if (!isLoggedIn && !url.startsWith("/login")) {
			throw new ConnectException("trying to access data without logging in first.");
		}

		// create request url
		URL servletURL;
		String request = HOST + url + "?";
		if (params != null) {
			for (final String key : params.keySet()) {
				request += key + "=" + params.get(key) + "&";
			}
		}
		request = request.substring(0, request.length() - 1);
		try {
			servletURL = new URL(request);
		} catch (final MalformedURLException ex) {
			throw new ConnectException("MalformedURLException: " + ex.getMessage());
		}

		// create connection
		try {
			final HttpURLConnection connection = (HttpURLConnection) servletURL.openConnection();
			if (isLoggedIn) {
				connection.setRequestProperty("sessionId", getSessionToken());
			}
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			if (json != null) {
				final OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				out.write(json);
				out.close();
			}
			connection.connect();

			return new HttpResponse(connection);
		} catch (final IOException ex) {
			throw new ConnectException("IOException: " + ex.getMessage());
		}
	}

	/**
	 * Log in to the server. The session token will be saved automatically
	 *
	 * @param user
	 *            the user name
	 * @param hash
	 * @return true if the login was successfull.
	 * @throws java.net.ConnectException
	 *             if there was an error with the request or if the recieved
	 *             status code was wrong.
	 */
	protected boolean login(final String user, final String hash) throws ConnectException {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("name", user);
		params.put("hash", hash);
		final HttpResponse resp = request("/login/login", "GET", params, null);
		if (resp.getStatus() == HttpServletResponse.SC_OK) {
			isLoggedIn = true;
			final String[] split = resp.getData().split("\n");
			setSessionToken(split[0]);
			try {
				SessionHolder.getInstance().setCurator(new Gson().fromJson(split[1], Curator.class));
			} catch (JsonSyntaxException e) {
				throw Utils.createExceptionForCode(HttpServletResponse.SC_BAD_GATEWAY);
			}
			return true;
		} else if (resp.getStatus() != HttpServletResponse.SC_UNAUTHORIZED) {
			final ConnectException exception = Utils.createExceptionForCode(resp.getStatus());
			if (exception != null) {
				throw exception;
			}
		}
		return false;
	}

	/**
	 * Log out from the server. The session token will be deleted afterwards.
	 *
	 * @throws java.net.ConnectException
	 */
	protected void logout() {
		if (isLoggedIn) {
			final Map<String, String> params = new HashMap<String, String>();
			params.put("sessionId", getSessionToken());
			try {
				request("/login/logout", "GET", params, null);
			} catch (final ConnectException e) {
				// nothing to do, application is closing anyway
			}
		}
	}

	String getSessionToken() {
		synchronized (sessionToken) {
			return sessionToken;
		}
	}

	private void setSessionToken(final String sessionToken) {
		synchronized (sessionToken) {
			this.sessionToken = sessionToken;
		}
	}
}
