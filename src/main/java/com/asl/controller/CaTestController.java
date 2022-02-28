package com.asl.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.PoordHeader;
import com.asl.entity.Test;
import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;
import com.asl.service.TestService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/test")
public class CaTestController extends ASLAbstractController{
	@Autowired TestService testService;
	@Autowired XtrnService xtrnService;
	@Autowired XcodesService xcodesService;
	
	
	@GetMapping
	public String loadTestPage(Model model) {
		model.addAttribute("test" ,getDefaultTest());
		model.addAttribute("allTest",testService.getAllTest());
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.TEST_NUMBER.getCode(),Boolean.TRUE));
		model.addAttribute("warehouse",xcodesService.findByXcode(CodeType.WAREHOUSE.getCode(),Boolean.TRUE));
		return "pages/test/test";
		
	}
	
	
	private Test getDefaultTest() {
		Test test = new Test();
		test.setXtypetrn(TransactionCodeType.TEST_NUMBER.getCode());
		test.setXstatuspor("Open");
		test.setXtotamt(BigDecimal.ZERO);
		test.setXtype("PO");
		
		return test;
	}

}
