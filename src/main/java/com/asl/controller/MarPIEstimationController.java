package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/piestimtn")
public class MarPIEstimationController extends ASLAbstractController{

	@GetMapping
	public String loadMarPIEstimationPage() {
		
		return "pages/merchandising/piestimtn";
	}
}

