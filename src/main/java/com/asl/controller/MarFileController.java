package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cafile")
public class MarFileController extends ASLAbstractController{

	@GetMapping
	public String loadMarFilePage() {
		
		return "pages/merchandising/cafile";
	}
}
