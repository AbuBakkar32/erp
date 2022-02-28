package com.asl.service.report.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import com.asl.model.FormFieldBuilder;


/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0504Service")
public class RM0504ServiceImpl extends AbstractReportService {

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {

		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// Item Code
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Raw Material", "search/report/caitemname", "", false));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

}
