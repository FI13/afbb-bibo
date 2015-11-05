package de.afbb.bibo.share.test;

import java.io.Serializable;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.CopyUtil;

public class CopyUtilTest {

	@Test
	public void test() {
		final Copy original = new Copy(1, "edition", "barcode", new Date(), "condition", new Date(), new Date(), 2, 3, 4, 5, 6, 7, "isbn",
				"title", "author", "language", 8, "publisher");
		final Object copy = CopyUtil.copy((Serializable) original);
		Assert.assertFalse(original == copy);
		Assert.assertTrue(original.equals(copy));
	}

}
