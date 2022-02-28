package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prsewplan")
public class ProSewingPlanController extends ASLAbstractController{

	@GetMapping
	public String loadProSewingPlanPage() {
		
		return "pages/landproduction/prsewplan";
	}
}
