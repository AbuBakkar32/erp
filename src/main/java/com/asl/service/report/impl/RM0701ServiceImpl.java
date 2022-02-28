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
@Service("RM0701Service")
public class RM0701ServiceImpl extends AbstractReportService {
	
	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<Xcodes> groupList = xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE);
		List<DropdownOption> group = new ArrayList<>();
		group.add(new DropdownOption("", "-- Select --"));
		groupList.stream().forEach(x -> group.add(new DropdownOption(x.getXcode(), x.getXcode())));

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Land ID", "search/landId", "", false));
		
		fieldsList.add(FormFieldBuilder.generateInputField(3, "CS Dag", "" , false));

		fieldsList.add(FormFieldBuilder.generateInputField(4, "RS Dag", "" , false));
		
		fieldsList.add(FormFieldBuilder.generateInputField(5, "City Dag", "" , false));
		
		
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
