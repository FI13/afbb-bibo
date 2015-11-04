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
 *
 * @author fi13.pendrulat
 */
public class Config {
	private final String pASSWORD;
	private final String uSER_NAME;
	private final String uRL;
	private final String dATABASE_NAME;
	private final int tOKEN_EXPIRATION_TIME_IN_HOURS;
	private final static String path = "./server.properties";

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

		tOKEN_EXPIRATION_TIME_IN_HOURS = Integer.valueOf(get("TOKEN_EXPIRATION_TIME_IN_HOURS"));
		dATABASE_NAME = get("DATABASE_NAME");
		uRL = get("URL");
		uSER_NAME = get("USER_NAME");
		pASSWORD = get("PASSWORD");
	}
}
