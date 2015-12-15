package de.afbb.bibo.amazon;

import java.io.IOException;

import de.afbb.bibo.share.model.Medium;

/*
 * This class shows how to make a simple authenticated call to the
 * Amazon Product Advertising API.
 *
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class Tester {

	public static void main(final String[] args) throws IOException {

		/*
		 * Set up the signed requests helper.
		 */

		// System.out.println("Signed URL: \"" + requestUrl + "\"");
		Medium med = new Medium();
		med = ParserMedium.getInstance().getMedium("9783804553804");
		System.out.println(med);
		System.out.println("ISBN-Check: " + ISBN.checkISBN("3128010498"));
	}
}
