package com.fb.zk.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


public class XMLParser {
	private final static Logger logger = LoggerFactory.getLogger(XMLParser.class);

	private static XMLParser sInstance = null;

	private final static String XMLSTR = "xml";

	private XMLParser() {

	}

	public static XMLParser getInstance() {
		if (sInstance == null) {
			sInstance = new XMLParser();
		}
		return sInstance;
	}

	public Element getRoot(File target) {
		if (target == null) {
			return null;
		}
		String fileName = target.getName();
		String[] token = fileName.split("\\.");
		String pf = token[1];

		if (!XMLSTR.equals(pf)) {
			return null;
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(target);
			Element root = doc.getDocumentElement();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}


	}


}
