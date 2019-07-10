package com.hy.wf.common.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utils - XMLParser
 */
public class XMLParser {

	public static Map<String, Object> fromXML(String xml) {
		Document document = null;
		InputStream inputStream = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			inputStream = getStringStream(xml);
			document = documentBuilder.parse(inputStream);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
		
		Map<String, Object> map = Collections.<String, Object> emptyMap();
		if (document != null) {
			map = new HashMap<String, Object>();
			// 获取到document里面的全部结点
			Node node;
			NodeList allNodes = document.getFirstChild().getChildNodes();
			map = new HashMap<String, Object>();
			int i = 0;
			while (i < allNodes.getLength()) {
				node = allNodes.item(i);
				if (node instanceof Element) {
					map.put(node.getNodeName(), node.getTextContent());
				}
				i++;
			}
		}
		
		return map;
	}

	public static InputStream getStringStream(String sInputString) {
		ByteArrayInputStream tInputStringStream = null;
		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				tInputStringStream = new ByteArrayInputStream(sInputString.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return tInputStringStream;
	}

}

