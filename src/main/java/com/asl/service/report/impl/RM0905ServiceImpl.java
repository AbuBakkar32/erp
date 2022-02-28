package com.asl.service.report.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;

/**
 * @author Rakibul Islam

 */
@Service("RM0905Service")
public class RM0905ServiceImpl extends AbstractReportService {


	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
	
		
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
		fieldsList.add(FormFieldBuilder.generateSearchField(6, "Customer", "search/report/cus", "", false));
		
		

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn)); 
		return fieldsList;
	}

}
