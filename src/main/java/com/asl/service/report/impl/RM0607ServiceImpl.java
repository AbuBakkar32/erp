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
@Service("RM0607Service")
public class RM0607ServiceImpl extends AbstractReportService {

	@Autowired
	private XcodesService xcodesService;
	
	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<Xcodes> groupList = xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE);
		List<DropdownOption> Itemgroups = new ArrayList<>();
		Itemgroups.add(new DropdownOption("", "-- Select --"));
		groupList.stream().forEach(x -> Itemgroups.add(new DropdownOption(x.getXcode(), x.getXcode())));

		List<Xcodes> categoryList = xcodesService.findByXtype(CodeType.ITEM_CATEGORY.getCode(), Boolean.TRUE);
		List<DropdownOption> ItemCategory = new ArrayList<>();
		ItemCategory.add(new DropdownOption("", "-- Select --"));
		categoryList.stream().forEach(x -> ItemCategory.add(new DropdownOption(x.getXcode(), x.getXcode())));

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// Item Group
		fieldsList.add(FormFieldBuilder.generateDropdownField(2, "Item Group", Itemgroups, "", false));
		
		// Item Category
		fieldsList.add(FormFieldBuilder.generateDropdownField(3, "Item Category", ItemCategory, "", false));
		
		// caitem
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Item", "search/caitem", "", true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
