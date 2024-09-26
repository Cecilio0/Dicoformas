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
	public static Element readXML(String fileRoute) throws ParserConfigurationException, IOException, SAXException {
		
		// Load the XML file from resources
		File xmlFile = new File(XMLReader.class.getClassLoader()
				.getResource(fileRoute)
				.getFile());
		
		// Create a DocumentBuilderFactory
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		// Parse the XML file and load it into a Document
		Document doc = dBuilder.parse(xmlFile);
		return doc.getDocumentElement();
	}
}
