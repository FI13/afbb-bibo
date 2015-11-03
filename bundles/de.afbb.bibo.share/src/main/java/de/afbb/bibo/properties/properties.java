package de.afbb.bibo.properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class properties {
public static String get (final String search) throws FileNotFoundException{
	String s = null;
	Reader reader = null;
	reader = new FileReader("h:/client.properties");
	final Properties prop = new Properties();
	s=prop.getProperty(search, "notFound")


	return s;
}
}
