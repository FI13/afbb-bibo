package de.afbb.bibo.share.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import de.afbb.bibo.properties.BiBoProperties;

public class PropertiesTest {

	@Test
	public void testLoad() throws IOException {
		Assert.assertEquals("root", BiBoProperties.get("USER_NAME"));
	}
}
