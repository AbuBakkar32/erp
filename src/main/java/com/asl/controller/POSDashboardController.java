package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posdashboard")
public class POSDashboardController extends ASLAbstractController {
	
	@GetMapping
	public String loadPOSDashboardPage() {
		
		return"pages/posdashboard";
	}

}
