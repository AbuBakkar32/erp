package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/batchentry")
public class ProBatchEntryController extends ASLAbstractController{

	@GetMapping
	public String loadProBatchEntryPage() {
		
		return "pages/landproduction/batchentry";
	}
}

