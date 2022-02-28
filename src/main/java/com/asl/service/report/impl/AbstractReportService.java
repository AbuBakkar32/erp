package com.asl.service.report.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.asl.config.AppConfig;
import com.asl.service.ASLSessionManager;
import com.asl.service.PrintingService;
import com.asl.service.report.ReportFieldService;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
@Component
public abstract class AbstractReportService implements ReportFieldService {

	@Autowired protected ASLSessionManager sessionManager;
	@Autowired protected PrintingService printingService;
	@Autowired protected Environment env;
	@Autowired protected AppConfig appConfig;
	

	public boolean isConventionCenter() {
		return Arrays.asList(env.getActiveProfiles()).contains("convention");
	}

	public boolean isKhanas() {
		return Arrays.asList(env.getActiveProfiles()).contains("khanas");
	}

	public boolean isTCC() {
		return Arrays.asList(env.getActiveProfiles()).contains("tcc");
	}

	public boolean isBoshila() {
		return Arrays.asList(env.getActiveProfiles()).contains("BOSHILA");
	}

	public boolean isDev() {
		return Arrays.asList(env.getActiveProfiles()).contains("dev");
	}

	protected static final SimpleDateFormat SDF = new SimpleDateFormat("E, dd-MMM-yyyy");

	@Override
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams) throws JAXBException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, ParseException{
		return null;
	}

	protected String parseXMLString(Object ob) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ob.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter result = new StringWriter();
		jaxbMarshaller.marshal(ob, result);
		return result.toString();
	}

	protected Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException {
		InputSource is = new InputSource(new StringReader(xml));
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	}

	protected ByteArrayOutputStream transfromToPDFBytes(Document doc, String template) throws TransformerFactoryConfigurationError, TransformerException, FOPException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File file = new File(template);

		Source xslSrc = new StreamSource(file);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslSrc);
		if (transformer == null) {
			throw new TransformerException("Template File not found: " + template);
		}

		//for image path setting
		//String serverPath = request.getSession().getServletContext().getRealPath("/");

		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
		// Make sure the XSL transformation's result is piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());
		// Start the transformation and rendering process
		transformer.transform(new DOMSource(doc), res);
		return out;
	}
}
