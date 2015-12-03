/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.afbb.bibo.servlet.Config;
import de.afbb.bibo.servlet.model.Session;

/**
 *
 * @author fi13.pendrulat
 */
public class SessionContainer {

	private static SessionContainer INSTANCE;

	private final Map<String, Session> connections;

	private SessionContainer() {
		connections = new HashMap<>();
	}

	public static SessionContainer getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SessionContainer();
		}
		return INSTANCE;
	}

	public String createNewConnection(final String name) {
		final String token = UUID.randomUUID().toString();
		final Session session = new Session(name, Calendar.getInstance());
		connections.put(token, session);
		return token;
	}

	private boolean isExpired(final Calendar created) throws NumberFormatException, IOException {
		final Calendar now = Calendar.getInstance();
		final long age = now.getTimeInMillis() - created.getTimeInMillis();
		return age > Config.getInstance().getTOKEN_EXPIRATION_TIME_IN_HOURS() * 3600000;
	}

	public boolean validate(final String token) throws NumberFormatException, IOException {
		final Session s = connections.get(token);
		if (s == null) {
			return false;
		} else if (isExpired(s.getDate())) {
			connections.remove(token);
			return false;
		} else {
			return true;
		}
	}

	public void invalidate(final String token) {
		connections.remove(token);
	}

	public Session getSession(final String token) {
		return connections.get(token);
	}
}
