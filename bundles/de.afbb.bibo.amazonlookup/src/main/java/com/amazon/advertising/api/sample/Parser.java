package com.amazon.advertising.api.sample;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.afbb.bibo.share.model.Medium;

public class Parser {

	public static void main(final String[] args) {
		final Medium test = new Medium();
		try {

			final File fXmlFile = new File("h:/ama.xml");
			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			final Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

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
					test.setAuthor(authorNames);
					// Authors Ende
					test.setLanguage(eElement.getElementsByTagName("Language").item(0).getFirstChild().getNextSibling()
							.getTextContent());

					test.setPublisher(eElement.getElementsByTagName("Publisher").item(0).getTextContent());
					test.setTitle(eElement.getElementsByTagName("Title").item(0).getTextContent());

					// Ausgabe des Testobjects
					System.out.println("Ausgabe Objekt: ");
					System.out.println("Author: " + test.getAuthor());
					System.out.println("ISBN: " + test.getIsbn());
					System.out.println("Language: " + test.getLanguage());
					System.out.println("Publisher: " + test.getPublisher());
					System.out.println("Title: " + test.getTitle());

				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
