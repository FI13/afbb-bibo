package de.afbb.bibo.properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class BiBoProperties {
	private final static String path = "./bibo.properties";

	public static String get(final String search) {
		String s = null;
		Reader reader = null;
		try {
			reader = new FileReader(path);
			final Properties prop = new Properties();
			prop.load(reader);
			reader.close();
			s = prop.getProperty(search, "notFound");

		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
