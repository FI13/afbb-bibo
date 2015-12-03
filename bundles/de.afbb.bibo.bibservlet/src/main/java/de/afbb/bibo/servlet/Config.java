/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet;

import java.io.IOException;

/**
 * Die config-file muss "bibo.properties" heissen und im gleichen Verzeichnis
 * wie die ausführbare Datei liegen
 *
 * @author fi13.pendrulatz
 */
public final class Config {

	private final String PASSWORD;
	private final String USER_NAME;
	private final String URL;
	private final String DATABASE_NAME;
	private final int TOKEN_EXPIRATION_TIME_IN_HOURS;

	private static Config instance;

	private Config() throws NumberFormatException, IOException {
		// FIXME doesn't work
		// TOKEN_EXPIRATION_TIME_IN_HOURS =
		// Integer.valueOf(BiBoProperties.get("TOKEN_EXPIRATION_TIME_IN_HOURS"));
		// DATABASE_NAME = BiBoProperties.get("DATABASE_NAME");
		// URL = BiBoProperties.get("URL");
		// USER_NAME = BiBoProperties.get("USER_NAME");
		// PASSWORD = BiBoProperties.get("PASSWORD");
		TOKEN_EXPIRATION_TIME_IN_HOURS = 8;
		DATABASE_NAME = "afbbbibo";
		URL = "127.0.0.1";
		USER_NAME = "root";
		PASSWORD = "";
	}

	public static Config getInstance() throws NumberFormatException, IOException {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public String getURL() {
		return URL;
	}

	public String getDATABASE_NAME() {
		return DATABASE_NAME;
	}

	public int getTOKEN_EXPIRATION_TIME_IN_HOURS() {
		return TOKEN_EXPIRATION_TIME_IN_HOURS;
	}

}
