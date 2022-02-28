package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prswng")
public class ProSewingDetailEntryController extends ASLAbstractController{

	@GetMapping
	public String loadProSewingDetailEntryPage() {
		
		return "pages/landproduction/prswng";
	}
}


