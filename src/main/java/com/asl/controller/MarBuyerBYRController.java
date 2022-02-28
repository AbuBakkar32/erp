package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/caagent")
public class MarBuyerBYRController extends ASLAbstractController{

	@GetMapping
	public String loadMarBuyerBYRPage() {
		
		return "pages/merchandising/caagent";
	}
}


