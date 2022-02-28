package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/batchdetails")
public class ProBatchDetailsController extends ASLAbstractController{

	@GetMapping
	public String loadProBatchDetailsPage() {
		
		return"pages/landproduction/batchdetails";
	}
}

