package com.asl.service.report.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.service.XcodesService;


/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0808Service")
public class RM0808ServiceImpl extends AbstractReportService {

	@Autowired private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<DropdownOption> status = new ArrayList<>();
		status.add(new DropdownOption("", "-- Select --"));
		status.add(new DropdownOption("Balanced", "Balanced"));
		status.add(new DropdownOption("Suspended", "Suspended"));
		status.add(new DropdownOption("Posted", "Posted"));
		
		List<DropdownOption> Aprovestatus = new ArrayList<>();
		Aprovestatus.add(new DropdownOption("", "-- Select --"));
		Aprovestatus.add(new DropdownOption("Open", "Open"));
		Aprovestatus.add(new DropdownOption("Approved", "Approved"));

		
		 
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));
		
		// Status
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Voucher Status", status , "", false));
		
		// Approved Status
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "Approved Status", Aprovestatus , "", false));
		
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

}
