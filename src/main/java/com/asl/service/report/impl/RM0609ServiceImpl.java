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
@Service("RM0609Service")
public class RM0609ServiceImpl extends AbstractReportService {
	
	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<Xcodes> statusList = xcodesService.findByXtype(CodeType.DEPARTMENT.getCode(), Boolean.TRUE);
		List<DropdownOption> dpt = new ArrayList<>();
		dpt.add(new DropdownOption("", "-- Select --"));
		statusList.stream().forEach(x -> dpt.add(new DropdownOption(x.getXcode(), x.getXcode())));
		
		
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		options.add(new DropdownOption("Normal", "Normal"));
		options.add(new DropdownOption("Resigned", "Resigned"));
		options.add(new DropdownOption("Closed", "Closed"));
		options.add(new DropdownOption("Terminated", "Terminated"));
		
		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// Department
		fieldsList.add(FormFieldBuilder.generateDropdownField(2, "Department", dpt, "", false));

		// Status
		fieldsList.add(FormFieldBuilder.generateDropdownField(3, "Status", options, "", false));


		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
