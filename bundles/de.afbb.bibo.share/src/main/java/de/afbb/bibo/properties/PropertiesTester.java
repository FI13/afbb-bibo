package de.afbb.bibo.properties;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class PropertiesTester {

	public static void main(final String[] args) throws IOException {
		final Writer writer = null;
		Reader reader = null;

		reader = new FileReader("h:/client.properties");

		final Properties prop2 = new Properties();
		prop2.load(reader);
		prop2.list(System.out);

		final String s = BiBoProperties.get("MeinNameIst");
		System.out.println("Output: " + s);

	}

}
