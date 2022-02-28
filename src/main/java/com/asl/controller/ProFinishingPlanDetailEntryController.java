package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prplanfin")
public class ProFinishingPlanDetailEntryController extends ASLAbstractController{

	@GetMapping
	public String loadProFinishingPlanDetailEntryPage() {
		
		return "pages/landproduction/prplanfin";
	}
}

