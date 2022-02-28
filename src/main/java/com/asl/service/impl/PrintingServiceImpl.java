package com.asl.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.asl.service.PrintingService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 *
 */
@Slf4j
@Service
public class PrintingServiceImpl extends AbstractGenericService implements PrintingService{

	@Override
	public String parseXMLString(Object ob) throws JAXBException {
		log.info("Start parsing object to xml string");
		JAXBContext jaxbContext = JAXBContext.newInstance(ob.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter result = new StringWriter();
		jaxbMarshaller.marshal(ob, result);
		log.info(result.toString());
		return result.toString();
	}

	@SuppressWarnings("unused")
	private void saveXmlToFile(String xml) {
		String path = appConfig.getSavedXmlPath();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss");
		
	}

	@Override
	public Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException {
		log.info("Start creationg document object from xml");
		InputSource is = new InputSource(new StringReader(xml));
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	}

	@Override
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template, HttpServletRequest request)
			throws TransformerFactoryConfigurationError, TransformerException, FOPException {
		log.info("Start creating pdf bytes using xml doc and template");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File file = new File(template);

		Source xslSrc = new StreamSource(file);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslSrc);
		if (transformer == null) {
			throw new TransformerException("Template File not found: " + template);
		}

		//for image path setting
		String serverPath = request.getSession().getServletContext().getRealPath("/");

		FopFactory fopFactory = FopFactory.newInstance(new File(serverPath).toURI());
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
		// Make sure the XSL transformation's result is piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());
		// Start the transformation and rendering process
		transformer.transform(new DOMSource(doc), res);
		return out;
	}

	@Override
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {
		
		return null;
	}

	@Override
	public byte[] getPDFReportByte(Object ob, String template, HttpServletRequest request) throws JAXBException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, ParseException {
		ByteArrayOutputStream out = getPDFReportByteAttayOutputStream(ob, template, request);
		if(out == null) return null;
		return out.toByteArray();
	}

	@Override
	public ByteArrayOutputStream getPDFReportByteAttayOutputStream(Object ob, String template, HttpServletRequest request)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {
		if(ob == null || StringUtils.isBlank(template)) return null;

		String xml = parseXMLString(ob);
		if(StringUtils.isBlank(xml)) {
			log.error(ERROR, "Cant't generate xml string from object");
			return null;
		}

		Document doc = getDomSourceForXML(xml);
		if(doc == null) {
			log.error(ERROR, "Cant't generate document object from xml string");
			return null;
		}

		ByteArrayOutputStream out = transfromToPDFBytes(doc, template, request);
		if(out == null) return null;

		return out;
	}

	
}
