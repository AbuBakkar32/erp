package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/batchplan")
public class ProBatchPlanningController extends ASLAbstractController{
	
	@GetMapping
	public String loadProBatchPlanningPage() {
		
		return"pages/landproduction/batchplan";
	}

}
