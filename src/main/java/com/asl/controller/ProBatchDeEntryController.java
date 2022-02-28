package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/batchdetailsentry")
public class ProBatchDeEntryController extends ASLAbstractController{

	@GetMapping
	public String loadBatchDeEntryPage() {
		
		return "pages/landproduction/batchdetailsentry";
	}
}


