package com.amazon.advertising.api.sample;

import de.afbb.bibo.share.model.Medium;

/*
 * This class shows how to make a simple authenticated call to the
 * Amazon Product Advertising API.
 *
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class Tester {

	public static void main(final String[] args) {

		/*
		 * Set up the signed requests helper.
		 */

		// System.out.println("Signed URL: \"" + requestUrl + "\"");
		Medium med = new Medium();
		med = ParserMedium.getMedium("9783804553804");
	}
}
