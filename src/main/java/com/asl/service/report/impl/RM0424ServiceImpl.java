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
@Service("RM0424Service")
public class RM0424ServiceImpl extends AbstractReportService {

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
		options.add(new DropdownOption("GRN Created", "GRN Created"));
		options.add(new DropdownOption("Full Received", "Full Receipt"));
		
		List<Xcodes> storeList = xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE);
		List<DropdownOption> warehouse = new ArrayList<>();
		warehouse.add(new DropdownOption("", "-- Select --"));
		storeList.stream().forEach(x -> warehouse.add(new DropdownOption(x.getXcode(), x.getXcode() + "-" + x.getXlong())));
		
		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));
		
		// xcus - Customer / Supplier
		fieldsList.add(FormFieldBuilder.generateHiddenField(4,sessionManager.getLoggedInUserDetails().getXcus()));
		
		// Warehouse
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "To Warehouse", warehouse, "", false));
		
		// Status
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Status", options, "", false));


		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
