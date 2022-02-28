package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prdyfin")
public class ProDyeFinishingProController extends ASLAbstractController{

	@GetMapping
	public String loadProDyeFinishingProPage() {
		
		return "pages/landproduction/prdyfin";
	}
}
