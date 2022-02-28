package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landeducation")
public class LandEducationController extends ASLAbstractController{
	
	@GetMapping
	public String loadLandEducationPage() {
		
		return "pages/landeducation";
	}

}
