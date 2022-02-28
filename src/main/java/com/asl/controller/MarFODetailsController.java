package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pofodesnd")
public class MarFODetailsController extends ASLAbstractController{

	@GetMapping
	public String loadMarFODetailsPage() {
		
		return "pages/merchandising/pofodesnd";
	}
}

