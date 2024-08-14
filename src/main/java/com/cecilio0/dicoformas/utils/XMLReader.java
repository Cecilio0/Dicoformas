package com.cecilio0.dicoformas.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLReader {
	public static Element readXML(String fileRoute, String tagName) throws ParserConfigurationException, IOException, SAXException {
		
		// Load the XML file from resources
		File xmlFile = new File(XMLReader.class.getClassLoader()
				.getResource(fileRoute)
				.getFile());
		
		// Create a DocumentBuilderFactory
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		// Parse the XML file and load it into a Document
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		
		// Get a list of all elements in the XML
		NodeList nList = doc.getElementsByTagName(tagName);
		
		// Return the first element
		System.out.println(nList.getLength());
		return (Element) nList.item(0);
		
		//Read elements by doing:
		//node.getElementsByTagName("tagName").item(0).getTextContent();
	}
}
