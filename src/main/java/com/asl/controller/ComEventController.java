package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landcomevent")
public class ComEventController extends ASLAbstractController{

	@GetMapping
	public String loadLandComEvet() {
		
		return "pages/land/landcomevent";
	}
}
