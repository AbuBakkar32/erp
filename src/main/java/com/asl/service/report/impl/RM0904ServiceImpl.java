package com.asl.service.report.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.asl.model.FormFieldBuilder;

/**
 * @author Rakibul Islam

 */
@Service("RM0904Service")
public class RM0904ServiceImpl extends AbstractReportService {


	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
	
		
		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		// xcus - Customer / Supplier
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Customer", "search/report/cus", "", false));
		
		

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn)); 
		return fieldsList;
	}

}
