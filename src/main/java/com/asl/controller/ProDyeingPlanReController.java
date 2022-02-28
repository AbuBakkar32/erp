package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prplandyr")
public class ProDyeingPlanReController  extends ASLAbstractController{

	@GetMapping
	public String loadProDyeingPlanRePage() {
		
		return "pages/landproduction/prplandyr";
	}

}


