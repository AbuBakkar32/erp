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
@Service("RM0309Service")
public class RM0309ServiceImpl extends AbstractReportService {

	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {

		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		options.add(new DropdownOption("Open", "Open"));
		options.add(new DropdownOption("Confirmed", "Confirmed"));
		
		List<Xcodes> CusgroupList = xcodesService.findByXtype(CodeType.SUPPLIER_GROUP.getCode(), Boolean.TRUE);
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
		
		// Supplier Group
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Supplier Group", group, "", false));
		
		// xcus - Supplier 
		fieldsList.add(FormFieldBuilder.generateSearchField(5, "Supplier", "search/report/sup", "", false));
		
		// project
		fieldsList.add(FormFieldBuilder.generateSearchField(6, "Project/Business", "search/project", "", false));

		// Payment Type
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Payment Type", paytype, "", false));
		
		// bank
		fieldsList.add(FormFieldBuilder.generateSearchField(8, "From Bank", "search/bank", "", false));
		
		// Payment By
		fieldsList.add(FormFieldBuilder.generateSearchField(9, "Payment By", "search/report/xstaff", "", false));
		
		//Status
		fieldsList.add(FormFieldBuilder.generateDropdownField(10, "Status", options, "", false));
				
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}


}
