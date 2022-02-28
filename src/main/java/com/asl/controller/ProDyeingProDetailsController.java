package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prdying")
public class ProDyeingProDetailsController extends ASLAbstractController {

	@GetMapping
	public String loadProDyeingProDetailsPage() {
		
		return "pages/landproduction/prdying";
	}
}

