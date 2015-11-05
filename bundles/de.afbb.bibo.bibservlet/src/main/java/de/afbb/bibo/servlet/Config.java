/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.afbb.bibo.servlet;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 * @author fi13.pendrulat
 */
public class Config {

	public static String PASSWORD;
	public static String USER_NAME;
	public static String URL;
	public static String DATABASE_NAME;
	public static int TOKEN_EXPIRATION_TIME_IN_HOURS;
	public static String path = "./server.properties";

	public static String get(final String search) throws IOException {
		String s = null;
		Reader reader = null;
		reader = new FileReader(path);
		final Properties prop = new Properties();
		prop.load(reader);
		reader.close();
		s = prop.getProperty(search, "notFound");
		return s;
	}

	public Config() throws IOException {
		TOKEN_EXPIRATION_TIME_IN_HOURS = Integer.valueOf(get("TOKEN_EXPIRATION_TIME_IN_HOURS"));
		DATABASE_NAME = get("DATABASE_NAME");
		URL = get("URL");
		USER_NAME = get("USER_NAME");
		PASSWORD = get("PASSWORD");
	}
}
