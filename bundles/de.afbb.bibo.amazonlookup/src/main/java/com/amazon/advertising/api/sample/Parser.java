package com.amazon.advertising.api.sample;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

public class Parser {

	public static void main(String[] args) {
	    try {

	    	File fXmlFile = new File("h:/ama.xml");
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(fXmlFile);
	    			
	    	//optional, but recommended
	    	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	    	doc.getDocumentElement().normalize();

	    	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	    			
	    	NodeList nList = doc.getElementsByTagName("Item");
	    		    	
	    	for (int temp = 0; temp < nList.getLength(); temp++) {

	    		Node nNode = nList.item(temp);
	    				
	    		System.out.println("\nCurrent Element :" + nNode.getNodeName());
	    				
	    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

	    			Element eElement = (Element) nNode;

	    			System.out.println("Staff id : " + eElement.getAttribute("id"));
	    			System.out.println("Laenge Author: "+eElement.getElementsByTagName("Author").getLength());
	    			for (int i=0; i<eElement.getElementsByTagName("Author").getLength(); i++){
	    				
	    			System.out.println("Author : " + eElement.getElementsByTagName("Author").item(i).getTextContent());
	    			//System.out.println("Author : " + eElement.getElementsByTagName("Author").item(1).getTextContent());
	    			}
	    			System.out.println("Language : " + eElement.getElementsByTagName("Language").item(0).getTextContent());
	    			System.out.println("Publisher : " + eElement.getElementsByTagName("Publisher").item(0).getTextContent());
	    			System.out.println("Title : " + eElement.getElementsByTagName("Title").item(0).getTextContent());

	    		}
	    	}
	        } catch (Exception e) {
	    	e.printStackTrace();
	        }
	}
}
