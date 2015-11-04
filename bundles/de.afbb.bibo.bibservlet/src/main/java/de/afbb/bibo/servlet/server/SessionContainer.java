/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server;

import de.afbb.bibo.servlet.Config;
import de.afbb.bibo.servlet.model.Session;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public String createNewConnection(String name) {
        String token = UUID.randomUUID().toString();
        Session session = new Session(name, Calendar.getInstance());
        connections.put(token, session);
        return token;
    }

    private boolean isExpired(Calendar created) {
        Calendar now = Calendar.getInstance();
        long age = now.getTimeInMillis() - created.getTimeInMillis();
        return (age > Config.TOKEN_EXPIRATION_TIME_IN_HOURS * 3600000);
    }

    public boolean validate(String token) {
        Session s = connections.get(token);
        if (s == null) {
            return false;
        } else if (isExpired(s.getDate())) {
            connections.remove(token);
            return false;
        } else {
            return true;
        }
    }
    
    public void invalidate(String token) {
        connections.remove(token);
    }
    
    public Session getSession(String token) {
        return connections.get(token);
    }
}
