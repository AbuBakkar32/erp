package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/cafiledetails")
public class MarFileDetailsController extends ASLAbstractController{


	@GetMapping
	public String loadMarFileDetailsPage() {
		
		return "pages/merchandising/cafiledetails";
	}
}

