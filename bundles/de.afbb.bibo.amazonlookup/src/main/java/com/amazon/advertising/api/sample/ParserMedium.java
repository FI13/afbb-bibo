package com.amazon.advertising.api.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.afbb.bibo.properties.BiBoProperties;
import de.afbb.bibo.share.model.Medium;

public final class ParserMedium {

	/*
	 * Your AWS Access Key ID, as taken from the AWS Your Account page.
	 */
	private final String AWS_ACCESS_KEY_ID;

	/*
	 * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	 * Your Account page.
	 */
	private final String AWS_SECRET_KEY;

	/*
	 * Use the end-point according to the region you are interested in.
	 */
	private final String ENDPOINT;

	/*
	 * Use the end-point according to the region you are interested in.
	 */
	private final String ASSOCIATE_TAG;

	private static ParserMedium instance;

	private ParserMedium() throws IOException {
		AWS_ACCESS_KEY_ID = BiBoProperties.get("AWS_ACCESS_KEY_ID");
		AWS_SECRET_KEY = BiBoProperties.get("AWS_SECRET_KEY");
		ENDPOINT = BiBoProperties.get("ENDPOINT");
		ASSOCIATE_TAG = BiBoProperties.get("ASSOCIATE_TAG");
	}

	public static ParserMedium getInstance() throws IOException {
		if (instance == null) {
			instance = new ParserMedium();
		}
		return instance;
	}

	/**
	 * Bereitgestellte Funktion von Amazon
	 *
	 * @param isbn
	 * @return
	 */
	public String getUrl(final String isbn) {

		/*
		 * Set up the signed requests helper.
		 */
		SignedRequestsHelper helper;

		try {
			helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
		} catch (final Exception e) {
			e.printStackTrace();
			return "Error";
		}

		String requestUrl = null;

		final Map<String, String> params = new HashMap<String, String>();

		params.put("Service", "AWSECommerceService");
		params.put("Operation", "ItemLookup");
		params.put("AWSAccessKeyId", AWS_ACCESS_KEY_ID);
		params.put("AssociateTag", ASSOCIATE_TAG);
		params.put("ItemId", isbn);
		params.put("IdType", "ISBN");
		params.put("ResponseGroup", "ItemAttributes");
		params.put("SearchIndex", "Books");

		requestUrl = helper.sign(params);

		return requestUrl;
	}

	/**
	 * erstellt ein Objekt Medium, welches alle aus der Amazon AWS extrahierte
	 * Informationen enthält.
	 *
	 * @param isbn
	 *            - ISBN-Nummer (10 oder 13 stellig) für welches die
	 *            Datenabgerufen werden sollen
	 * @return Medium
	 */
	public Medium getMedium(final String isbn) {
		final Medium medium = new Medium();

		final String tempurl = getUrl(isbn);
		try

		{
			if (Integer.valueOf(BiBoProperties.get("USE_PROXY")) == 1) {
				System.setProperty("java.net.useSystemProxies", "true");
			}

			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			final URL url = new URL(tempurl);
			final InputStream stream = url.openStream();

			final Document doc = dBuilder.parse(stream);

			// optional, but recommended
			// read this -
			//
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			final NodeList nList = doc.getElementsByTagName("Item");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				final Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					final Element eElement = (Element) nNode;

					// Authors bestimmen
					String authorNames = "";
					for (int i = 0; i < eElement.getElementsByTagName("Author").getLength(); i++) {
						authorNames += eElement.getElementsByTagName("Author").item(i).getTextContent() + " ";
					}
					medium.setAuthor(authorNames);
					// Authors Ende
					medium.setLanguage(
							eElement.getElementsByTagName("Language").item(0).getFirstChild().getTextContent());

					medium.setPublisher(eElement.getElementsByTagName("Publisher").item(0).getTextContent());
					medium.setTitle(eElement.getElementsByTagName("Title").item(0).getTextContent());
					medium.setIsbn(isbn);

				}
			}
		} catch (final Exception e)

		{
			e.printStackTrace();
		}
		return medium;
	}

}
