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
 * @author Rakibul Islam
 */
@Service("RM0909Service")
public class RM0909ServiceImpl extends AbstractReportService {
	
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
		
		
		List<Xcodes> itemCat = xcodesService.findByXtype(CodeType.ITEM_CATEGORY.getCode(), Boolean.TRUE);
		List<DropdownOption> itemCats = new ArrayList<>();
		itemCats.add(new DropdownOption("", "-- Select --"));
		itemCat.stream().forEach(x -> itemCats.add(new DropdownOption(x.getXcode(), x.getXcode())));
		
		
		List<Xcodes> itemGroup = xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE);
		List<DropdownOption> itemGroups = new ArrayList<>();
		itemGroups.add(new DropdownOption("", "-- Select --"));
		itemGroup.stream().forEach(x -> itemGroups.add(new DropdownOption(x.getXcode(), x.getXcode())));
		
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));
		
		// xhwh - Project/Business
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Project/Business", "search/project", "", false, "project", "report/search/store"));
				
		// xwh - store/site
		List<DropdownOption> site = new ArrayList<>();
		site.add(new DropdownOption("", "-- Select --"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "Store/Site", site, "", false, "store"));	
		
		// xcus - Customer / Supplier
		fieldsList.add(FormFieldBuilder.generateSearchField(6, "Supplier", "search/report/sup", "", false));
		
		// xpornum
		fieldsList.add(FormFieldBuilder.generateInputField(7, "Requisition No", "", false));
		
		// xitem
		fieldsList.add(FormFieldBuilder.generateSearchField(8, "Item", "search/report/stock/xitem", "", false));
				
		// xstatuspor
		fieldsList.add(FormFieldBuilder.generateDropdownField(9, "Status", options, "", false));
		
		// xgitem
		fieldsList.add(FormFieldBuilder.generateDropdownField(10, "Item Group", itemGroups, "", false));

		// xcatitem
		fieldsList.add(FormFieldBuilder.generateDropdownField(11, "Item Category", itemCats, "", false));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn)); 
		return fieldsList;
	}

}