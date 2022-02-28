package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/qualification")
public class HRQualificationController extends ASLAbstractController {
	
	@GetMapping
	public String loadHRPMasterSetupPage() {
		
		return "pages/hr/qualification";
	}

}
