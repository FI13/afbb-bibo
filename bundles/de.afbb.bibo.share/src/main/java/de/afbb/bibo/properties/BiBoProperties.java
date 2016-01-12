package de.afbb.bibo.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BiBoProperties {

	private final static String PROPRTY_FILE = "bibo.properties";//$NON-NLS-1$
	private static Properties prop = null;

	public static String get(final String search) throws IOException {
		if (prop == null) {
			final InputStream inputStream = BiBoProperties.class.getClassLoader().getResourceAsStream(PROPRTY_FILE);

			if (inputStream != null) {
				prop = new Properties();
				prop.load(inputStream);
				inputStream.close();
			}
		}
		return prop.getProperty(search, "");
	}
}
