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
@Service("RM0901Service")
public class RM0901ServiceImpl extends AbstractReportService {

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// Customer 
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Customer", "search/report/cus", "", false));
		
		// Project
		fieldsList.add(FormFieldBuilder.generateSearchField(3, "Project", "search/project", "", false));

		// Site
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Site", "search/site", "", false));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

}
