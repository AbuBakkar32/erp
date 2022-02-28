package com.asl.service.report.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0421Service")
public class RM0421ServiceImpl extends AbstractReportService {

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		options.add(new DropdownOption("Open", "Open"));
		options.add(new DropdownOption("Confirmed", "Confirmed"));
		options.add(new DropdownOption("SO Created", "SO Created"));
		
		
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));
		
		// xcus - Customer / Supplier
		fieldsList.add(FormFieldBuilder.generateHiddenField(4,sessionManager.getLoggedInUserDetails().getXcus()));
		
		// Item
		fieldsList.add(FormFieldBuilder.generateSearchField(5, "Item", "search/report/stock/xitem", "", false));

		// Status
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Status", options, "", false)); 

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

}
