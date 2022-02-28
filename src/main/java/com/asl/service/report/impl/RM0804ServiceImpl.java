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
@Service("RM0804Service")
public class RM0804ServiceImpl extends AbstractReportService {
	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<Xcodes> groupList = xcodesService.findByXtypeAndXtypeobj(CodeType.STORE.getCode(),CodeType.BRANCH.getCode(), Boolean.TRUE);
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		groupList.stream().forEach(x -> options.add(new DropdownOption(x.getXcode(), x.getXcode())));

		
		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(1, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "To Date", new Date(), true));
		
		// Account
		fieldsList.add(FormFieldBuilder.generateSearchField(3, "Account Number", "search/account", "", true));
		
		// From Warehouse
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Project/Office", options, "", false));
		
		// Year
		fieldsList.add(FormFieldBuilder.generateInputField(5, "Year","2021", true));
		
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(6, sessionManager.getBusinessId()));
		
		// sub acc
		fieldsList.add(FormFieldBuilder.generateSearchField(7, "Sub Account", "search/report/xsub", "", false));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

}
