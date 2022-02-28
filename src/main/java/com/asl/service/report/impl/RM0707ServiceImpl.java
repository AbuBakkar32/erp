package com.asl.service.report.impl;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0707Service")
public class RM0707ServiceImpl extends AbstractReportService {

	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {

		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<Xcodes> CusgroupList = xcodesService.findByXtype(CodeType.CUSTOMER_GROUP.getCode(), Boolean.TRUE);
		List<DropdownOption> group = new ArrayList<>();
		group.add(new DropdownOption("", "-- Select --"));
		CusgroupList.stream().forEach(x -> group.add(new DropdownOption(x.getXcode(), x.getXcode())));
		
		List<Xcodes> paymentType = xcodesService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE);
		List<DropdownOption> paytype = new ArrayList<>();
		paytype.add(new DropdownOption("", "-- Select --"));
		paymentType.stream().forEach(x -> paytype.add(new DropdownOption(x.getXcode(), x.getXcode())));

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		
		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));
		
		// Item Group
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Customer Group", group, "", false));

		// Payment Type
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "Payment Type", paytype, "", false));
		
		// Payment By
		fieldsList.add(FormFieldBuilder.generateSearchField(6, "Received By", "search/report/xstaff", "", false));
				
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}


}
