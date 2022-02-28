package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/pientry")
public class ProPIEntryController extends ASLAbstractController{

	@GetMapping
	public String loadProPIEntryPage() {
		
		return "pages/merchandising/pientry";
	}
}

