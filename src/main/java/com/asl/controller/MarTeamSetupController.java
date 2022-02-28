package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cateam")
public class MarTeamSetupController extends ASLAbstractController{


	@GetMapping
	public String loadMarTeamSetupPage() {
		
		return "pages/merchandising/cateam";
	}
}

