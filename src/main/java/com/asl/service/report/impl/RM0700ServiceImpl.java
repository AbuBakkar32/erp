package com.asl.service.report.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.asl.entity.Cacus;
import com.asl.entity.Imstock;
import com.asl.entity.LandDagDetails;
import com.asl.entity.LandDocument;
import com.asl.entity.LandEvents;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;
import com.asl.entity.LandSurvey;
import com.asl.entity.LandSurveyor;
import com.asl.entity.Zbusiness;
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.model.report.RM0301;
import com.asl.model.report.STOCKLReport;
import com.asl.service.CacusService;
import com.asl.service.LandDocumentService;
import com.asl.service.LandInfoService;
import com.asl.service.LandPersonService;
import com.asl.service.LandSurveyService;
import com.asl.service.LandSurveyorService;
import com.asl.model.report.LandInfoReport;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0700Service")
public class RM0700ServiceImpl extends AbstractReportService {
	
	@Autowired private LandInfoService landService;
	@Autowired private CacusService cacusService;
	@Autowired private LandPersonService landPersonService;
	@Autowired private LandSurveyorService landSurveyorService;
	@Autowired private LandSurveyService landSurveyService;
	@Autowired private LandDocumentService landDocumentService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// Land
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Land ID", "search/landId", "", true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {
		
		String xland = (String) reportParams.get("LAND");
		
		LandInfo list = landService.findByLandInfo(xland);
		if(list == null) return new byte[0];
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		Zbusiness zb = sessionManager.getZbusiness();

		LandInfoReport report = new LandInfoReport();
		report.setBusinessName(zb.getZorg());
		report.setBusinessAddress(zb.getXmadd());
		report.setReportName("Land Info Report : " + xland);
		report.setPrintDate(sdf.format(new Date()));
		report.setFromDate(sdf.format(new Date()));
		report.setXmemname(list.getXmemname());
		report.setXcus(list.getXcus());
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setXtotded(list.getXtotded());
		report.setXtotrem(list.getXtotrem());

		
		BeanUtils.copyProperties(list, report);


		List<LandOwner> owners = landService.findByLandOwner(xland);
		if(owners != null && !owners.isEmpty()) {
			for(LandOwner owner : owners) {
				if(StringUtils.isBlank(owner.getXperson())) continue;
				LandPerson lp = landPersonService.findByLandPerson(owner.getXperson());
				if(lp != null) {
					owner.setXperson(owner.getXperson() + " - " + lp.getXname());
				}
			}
			report.setOwners(owners);
		}

		List<LandDagDetails> dagList = landService.findByLandDagDetails(xland);
		if(dagList != null && !dagList.isEmpty()) report.setDags(dagList);

		List<LandSurvey> surveyList = landSurveyService.findByXlandSurvey(xland);
		if(surveyList != null && !surveyList.isEmpty()) {
			for(LandSurvey ls : surveyList) {
				if(StringUtils.isBlank(ls.getXsurveyor())) continue;
				LandSurveyor lsy = landSurveyorService.findByLandSurveyor(ls.getXsurveyor());
				if(lsy != null) {
			 		ls.setXsurveyor(ls.getXsurveyor() + " - " + lsy.getXname());
			 		ls.setFormatedDate(sdf.format(ls.getXdate()));
				}
			}
			report.setSurveys(surveyList);
		}

		List<LandDocument> documents = landDocumentService.findByAllLandDocument(xland);
		if(documents != null && !documents.isEmpty()) {
			for(LandDocument documet : documents) {
				if(StringUtils.isBlank(documet.getXdoc())) continue;
				LandDocument lp = landDocumentService.findByLandDocument(documet.getXdoc());
				if(lp != null) {
					documet.setXdoc(documet.getXdoc());
				}
			}
			report.setDocuments(documents);
		}
		List<LandEvents> activities = landService.findByLandEvents(xland);
		if(activities != null && !activities.isEmpty()) {
			for(LandEvents activity : activities) {
				if(StringUtils.isBlank(activity.getXperson())) continue;
				LandPerson lp = landPersonService.findByLandPerson(activity.getXperson());
				
				if(lp != null) {
					activity.setXperson(activity.getXperson() + " - " + lp.getXname());
					activity.setFormatedDate(sdf.format(activity.getXdate()));
				}
			}
			report.setActivities(activities);
		}

		String xml = printingService.parseXMLString(report);
		if (StringUtils.isBlank(xml))
			return new byte[0];

		Document doc = printingService.getDomSourceForXML(xml);
		if (doc == null)
			return new byte[0];

		ByteArrayOutputStream baos = printingService.transfromToPDFBytes(doc, templatePath, ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
		if (baos == null)
			return new byte[0];

		return baos.toByteArray();
	}

}
