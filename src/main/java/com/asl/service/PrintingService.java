package com.asl.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Zubayer Ahamed
 *
 */
public interface PrintingService {

	// FOP
	public String parseXMLString(Object ob) throws JAXBException;

	public Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException;

	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template, HttpServletRequest request)
			throws TransformerFactoryConfigurationError, TransformerException, FOPException;

	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException;

	public byte[] getPDFReportByte(Object ob, String template, HttpServletRequest request) throws JAXBException, ParserConfigurationException,
			SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, ParseException;

	public ByteArrayOutputStream getPDFReportByteAttayOutputStream(Object ob, String template, HttpServletRequest request)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException;
}
