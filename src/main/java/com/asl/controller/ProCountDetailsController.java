package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/countde")

public class ProCountDetailsController extends ASLAbstractController{

	@GetMapping
	public String loadProCountDetailsPage() {
		
		return "pages/landproduction/countde";
	}

}
