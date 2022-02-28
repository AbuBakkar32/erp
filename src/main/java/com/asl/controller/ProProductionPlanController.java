package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/proplan")
public class ProProductionPlanController extends ASLAbstractController{

	@GetMapping
	public String loadProProductionPlanPage() {
		
		return "pages/landproduction/proplan";
	}

}
