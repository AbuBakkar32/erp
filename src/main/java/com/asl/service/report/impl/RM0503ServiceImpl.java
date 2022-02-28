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
@Service("RM0503Service")
public class RM0503ServiceImpl extends AbstractReportService {

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
		
		//From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));
		
		// Batch No
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Batch No", "search/report/xbatch", "", false));
		
		// Item Group
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "Item Group", Itemgroups, "", false));
		
		// Item Category
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Item Category", ItemCategory, "", false));
		
		// Item
		fieldsList.add(FormFieldBuilder.generateSearchField(7, "Item", "search/caitem", "", true));
		

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
