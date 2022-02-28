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
@Service("RM0708Service")
public class RM0708ServiceImpl extends AbstractReportService {

	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {

		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<Xcodes> CusgroupList = xcodesService.findByXtype(CodeType.PURPOSE.getCode(), Boolean.TRUE);
		List<DropdownOption> group = new ArrayList<>();
		group.add(new DropdownOption("", "-- Select --"));
		CusgroupList.stream().forEach(x -> group.add(new DropdownOption(x.getXcode(), x.getXcode())));
		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		
		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));
		
		// Customer
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Member/Director", "search/memberdir", "", false));
		
		// Land ID
		fieldsList.add(FormFieldBuilder.generateSearchField(5, "Land", "search/landId", "", false));

		// purpose Type
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Purpose", group, "", false));
		
				
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}


}
