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

import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.model.report.RM0301;
import com.asl.model.report.RM0301Item;
import com.asl.model.report.RM0301PurchaseOrder;
import com.asl.model.report.RM0301Report;
import com.asl.model.report.RM0301Supplier;
import com.asl.service.PoordService;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0802Service")
public class RM0802ServiceImpl extends AbstractReportService {

	@Autowired
	private PoordService poordService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		//Voucher 
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Voucher Number", "search/report/xvoucher", "", true));


		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

}
