package de.afbb.bibo.properties;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class propertiesTester {

	public static void main(final String[] args) throws IOException {
		Writer writer = null;
		Reader reader = null;

		writer = new FileWriter("h:/properties.txt");

		final Properties prop1 = new Properties(System.getProperties());
		prop1.setProperty("MeinNameIst", "Forrest Gump");
		prop1.store(writer, "Eine Insel mit zwei Bergen");

		reader = new FileReader("h:/properties.txt");

		final Properties prop2 = new Properties();
		prop2.load(reader);
		prop2.list(System.out);

	}

}
