package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prqc")
public class ProDyeFinishingQCController extends ASLAbstractController{

	@GetMapping
	public String loadProDyeFinishingQCPage() {
		
		return "pages/landproduction/prqc";
	}
}

