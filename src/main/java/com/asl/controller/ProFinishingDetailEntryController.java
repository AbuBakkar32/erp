package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prfinshng")
public class ProFinishingDetailEntryController extends ASLAbstractController{

	@GetMapping
	public String loadProFinishingDetailEntryPage() {
		
		return "pages/landproduction/prfinshng";
	}
}

