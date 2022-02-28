package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/salaryde")
public class PaySalaryDetailsController extends ASLAbstractController{
	
	@GetMapping
	public String loadPaySalaryDetailsPage() {
		
		return "pages/hr/salaryde";
	}

}
