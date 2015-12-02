package de.afbb.bibo.share.test;

import org.junit.Test;

import de.afbb.bibo.share.model.Copy;
import junit.framework.Assert;

public class EqualsTest {

	@Test
	public void test() {
		final Copy copyA = new Copy();
		final Copy copyB = new Copy();
		Assert.assertTrue(copyA.equals(copyB));
		copyA.setBarcode(null);
		copyB.setBarcode(null);
		Assert.assertTrue(copyA.equals(copyB));
		copyA.setBarcode(null);
		copyB.setBarcode("");
		Assert.assertTrue(copyA.equals(copyB));
		copyA.setBarcode("");
		copyB.setBarcode(null);
		Assert.assertTrue(copyA.equals(copyB));
		copyA.setBarcode("");
		copyB.setBarcode("");
		Assert.assertTrue(copyA.equals(copyB));
	}

}
