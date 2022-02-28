package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/defaultset")
public class HRPMasterSetupController extends ASLAbstractController{

	@GetMapping
	public String loadHRPMasterSetupPage() {
		
		return "pages/hr/defaultset";
	}
}
