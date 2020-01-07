package com.javaxxz.core.toolbox.kit;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.javaxxz.core.exception.ToolBoxException;


public class XmlKit {


	public final static String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";

	// -------------------------------------------------------------------------------------- Read

	public static Document readXML(File file) {
		if (file == null) {
			throw new NullPointerException("Xml file is null !");
		}
		if (file.exists() == false) {
			throw new ToolBoxException("File [" + file.getAbsolutePath() + "] not a exist!");
		}
		if (file.isFile() == false) {
			throw new ToolBoxException("[" + file.getAbsolutePath() + "] not a file!");
		}

		try {
			file = file.getCanonicalFile();
		} catch (IOException e) {
		}

		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = dbf.newDocumentBuilder();
			return builder.parse(file);
		} catch (Exception e) {
			throw new ToolBoxException("Parse xml file [" + file.getAbsolutePath() + "] error!", e);
		}
	}


	public static Document readXML(String absoluteFilePath) {
		return readXML(new File(absoluteFilePath));
	}


	public static Document parseXml(String xmlStr) {
		if (StrKit.isBlank(xmlStr)) {
			throw new ToolBoxException("Xml content string is empty !");
		}
		xmlStr = cleanInvalid(xmlStr);

		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = dbf.newDocumentBuilder();
			return builder.parse(new InputSource(StrKit.getReader(xmlStr)));
		} catch (Exception e) {
			throw new ToolBoxException("Parse xml file [" + xmlStr + "] error!", e);
		}
	}

	// -------------------------------------------------------------------------------------- Write

	public static String toStr(Document doc) {
		return toStr(doc, CharsetKit.UTF_8);
	}


	public static String toStr(Document doc, String charset) {
		try {
			StringWriter writer = StrKit.getWriter();

			final Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, charset);
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.transform(new DOMSource(doc), new StreamResult(writer));

			return writer.toString();
		} catch (Exception e) {
			throw new ToolBoxException("Trans xml document to string error!", e);
		}
	}


	public static void toFile(Document doc, String absolutePath) {
		toFile(doc, absolutePath, null);
	}


	public static void toFile(Document doc, String absolutePath, String charset) {
		if (StrKit.isBlank(charset)) {
			charset = doc.getXmlEncoding();
		}
		if (StrKit.isBlank(charset)) {
			charset = CharsetKit.UTF_8;
		}

		BufferedWriter writer = null;
		try {
			writer = FileKit.getWriter(absolutePath, charset, false);
			Source source = new DOMSource(doc);
			final Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, charset);
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.transform(source, new StreamResult(writer));
		} catch (Exception e) {
			throw new ToolBoxException("Trans xml document to string error!", e);
		} finally {
			IoKit.close(writer);
		}
	}

	// -------------------------------------------------------------------------------------- Create

	public static Document createXml(String rootElementName) {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (Exception e) {
			throw new ToolBoxException("Create xml document error!", e);
		}
		Document doc = builder.newDocument();
		doc.appendChild(doc.createElement(rootElementName));

		return doc;
	}

	// -------------------------------------------------------------------------------------- Function

	public static String cleanInvalid(String xmlContent) {
		if (xmlContent == null) return null;
		return xmlContent.replaceAll(INVALID_REGEX, "");
	}


	public static List<Element> getElements(Element element, String tagName) {
		final NodeList nodeList = element.getElementsByTagName(tagName);
		return transElements(element, nodeList);
	}


	public static Element getElement(Element element, String tagName) {
		final NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList == null || nodeList.getLength() < 1) {
			return null;
		}
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element childEle = (Element) nodeList.item(i);
			if (childEle == null || childEle.getParentNode() == element) {
				return childEle;
			}
		}
		return null;
	}


	public static String elementText(Element element, String tagName) {
		Element child = getElement(element, tagName);
		return child == null ? null : child.getTextContent();
	}


	public static String elementText(Element element, String tagName, String defaultValue) {
		Element child = getElement(element, tagName);
		return child == null ? defaultValue : child.getTextContent();
	}


	public static List<Element> transElements(NodeList nodeList) {
		return transElements(null, nodeList);
	}


	public static List<Element> transElements(Element parentEle, NodeList nodeList) {
		final ArrayList<Element> elements = new ArrayList<Element>();
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element element = (Element) nodeList.item(i);
			if (parentEle == null || element.getParentNode() == parentEle) {
				elements.add(element);
			}
		}

		return elements;
	}


	public static <T> void writeObjectAsXml(File dest, T t) throws IOException {
		FileOutputStream fos = null;
		XMLEncoder xmlenc = null;
		try {
			fos = new FileOutputStream(dest);
			xmlenc = new XMLEncoder(new BufferedOutputStream(fos));
			xmlenc.writeObject(t);
		} finally {
			IoKit.close(fos);
			if (xmlenc != null) {
				xmlenc.close();
			}
		}
	}


	@SuppressWarnings("unchecked")
	public static <T> T readObjectFromXml(File source) throws IOException {
		Object result = null;
		FileInputStream fis = null;
		XMLDecoder xmldec = null;
		try {
			fis = new FileInputStream(source);
			xmldec = new XMLDecoder(new BufferedInputStream(fis));
			result = xmldec.readObject();
		} finally {
			IoKit.close(fis);
			if (xmldec != null) {
				xmldec.close();
			}
		}
		return (T) result;
	}
	// ---------------------------------------------------------------------------------------- Private method start
	// ---------------------------------------------------------------------------------------- Private method end

}
