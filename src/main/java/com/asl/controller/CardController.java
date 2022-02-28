package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/card")
public class CardController extends ASLAbstractController{
	
	@GetMapping
	public String loadCardPage() {
		
		return"pages/card";
	}

}
