package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fodetails")
public class ProFODetailsController extends ASLAbstractController{

	@GetMapping
	public String loadProFODetailsPage() {
		
		return "pages/landproduction/fodetails";
	}

}
