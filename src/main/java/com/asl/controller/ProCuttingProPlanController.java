package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prcutplan")
public class ProCuttingProPlanController extends ASLAbstractController{

	@GetMapping
	public String loadProCuttingProPlanPage() {
		
		return "pages/landproduction/prcutplan";
	}
}

