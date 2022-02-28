package com.asl.service.report.impl;

import java.util.ArrayList;
import java.util.Comparator;
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
@Service("RM0311Service")
public class RM0311ServiceImpl extends AbstractReportService {

	@Autowired
	private XcodesService xcodesService;
	
	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		List<Xcodes> statusList = xcodesService.findByXtype(CodeType.SUPPLIER_GROUP.getCode(), Boolean.TRUE);
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		statusList.stream().forEach(x -> options.add(new DropdownOption(x.getXcode(), x.getXcode())));
		
		
		List<DropdownOption> active = new ArrayList<>();
		active.add(new DropdownOption("", "-- Select --"));
		active.add(new DropdownOption("1", "Yes"));
		active.add(new DropdownOption("0", "No"));
		
		
		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// xcus
		fieldsList.add(FormFieldBuilder.generateDropdownField(2, "Supplier Group", options, "", false));
		
		// Status
		fieldsList.add(FormFieldBuilder.generateDropdownField(3, "Active Status", active, "", false));

//		// xorg
//		fieldsList.add(FormFieldBuilder.generateInputField(3, "XORG", "CP", true));
//
//		// xphone
//		fieldsList.add(FormFieldBuilder.generateInputField(4, "XPHONE", "01515634889", false));
//
//		// xgcus
//		fieldsList.add(FormFieldBuilder.generateInputField(5, "XGCUS", "Corporate", true));
//
//		// xstatuscus
//		fieldsList.add(FormFieldBuilder.generateInputField(6, "XSTATUSCUS", "Active", true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
