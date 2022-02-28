package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/cabom")
public class ProBOMEntryController extends ASLAbstractController{

	@GetMapping
	public String loadProBOMEntryPage() {
		
		return "pages/landproduction/cabom";
	}
}
