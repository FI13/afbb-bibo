/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet;

import java.io.IOException;

import de.afbb.bibo.properties.BiBoProperties;

/**
 * Die config-file muss "bibo.properties" heissen und im gleichen Verzeichnis
 * wie die ausführbare Datei liegen
 * 
 * @author fi13.pendrulatz
 */
public class Config {

	public static String PASSWORD;
	public static String USER_NAME;
	public static String URL;
	public static String DATABASE_NAME;
	public static int TOKEN_EXPIRATION_TIME_IN_HOURS;

	public Config() throws NumberFormatException, IOException {
		TOKEN_EXPIRATION_TIME_IN_HOURS = Integer.valueOf(BiBoProperties.get("TOKEN_EXPIRATION_TIME_IN_HOURS"));
		DATABASE_NAME = BiBoProperties.get("DATABASE_NAME");
		URL = BiBoProperties.get("URL");
		USER_NAME = BiBoProperties.get("USER_NAME");
		PASSWORD = BiBoProperties.get("PASSWORD");
	}
}
