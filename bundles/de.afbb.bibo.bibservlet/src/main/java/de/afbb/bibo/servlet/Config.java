/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet;

import java.io.IOException;

import de.afbb.bibo.properties.BiBoProperties;

/**
 * loads the property file out of the jar file (I'm lazy...)
 *
 * @author fi13.pendrulatz
 */
public final class Config {

	private final String PASSWORD;
	private final String USER_NAME;
	private final String MYSQL_URL;
	private final String DATABASE_NAME;
	private final int TOKEN_EXPIRATION_TIME_IN_HOURS;
	private final int PORT;
	private final int MYSQL_PORT;

	private static Config instance;

	private Config() throws NumberFormatException, IOException {
		TOKEN_EXPIRATION_TIME_IN_HOURS = Integer.valueOf(BiBoProperties.get("TOKEN_EXPIRATION_TIME_IN_HOURS"));
		PORT = Integer.valueOf(BiBoProperties.get("SERVER_PORT"));
		DATABASE_NAME = BiBoProperties.get("DATABASE_NAME");
		MYSQL_URL = BiBoProperties.get("MYSQL_URL");
		MYSQL_PORT = Integer.valueOf(BiBoProperties.get("MYSQL_PORT"));
		USER_NAME = BiBoProperties.get("USER_NAME");
		PASSWORD = BiBoProperties.get("PASSWORD");
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

	public String getMYSQL_URL() {
		return MYSQL_URL;
	}

	public String getDATABASE_NAME() {
		return DATABASE_NAME;
	}

	public int getTOKEN_EXPIRATION_TIME_IN_HOURS() {
		return TOKEN_EXPIRATION_TIME_IN_HOURS;
	}

	/**
	 * @return the pORT
	 */
	public int getPORT() {
		return PORT;
	}

	/**
	 * @return the mYSQL_PORT
	 */
	public int getMYSQL_PORT() {
		return MYSQL_PORT;
	}

}
