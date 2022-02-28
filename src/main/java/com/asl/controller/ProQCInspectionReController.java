package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prqcre")
public class ProQCInspectionReController extends ASLAbstractController{

	@GetMapping
	public String loadProQCInspectionRePage() {
		
		return "pages/landproduction/prqcre";
	}
}

