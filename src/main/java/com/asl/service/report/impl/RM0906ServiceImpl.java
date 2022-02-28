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
@Service("RM0906Service")
public class RM0906ServiceImpl extends AbstractReportService {


	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
	
		
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		// xhwh - Project/Business
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Project/Business", "search/project", "", false, "project", "report/search/store"));
				
		// xwh - store/site
		List<DropdownOption> site = new ArrayList<>();
		site.add(new DropdownOption("", "-- Select --"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(3, "Store/Site", site, "", false, "store"));	
		
		// xcus - Customer / Supplier
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Customer", "search/report/cus", "", false));
		
		

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn)); 
		return fieldsList;
	}

}
