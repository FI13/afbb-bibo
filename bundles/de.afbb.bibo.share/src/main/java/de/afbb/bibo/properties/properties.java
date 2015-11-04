package de.afbb.bibo.properties;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class properties {
	private final static String path = "h:/client.properties";

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
}
