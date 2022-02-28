package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/machinemas")
public class ProMachineMasterController extends ASLAbstractController{
	
	@GetMapping
	public String loadProMachineMasterPage() {
		
		return "pages/landproduction/machinemas";
	}

}
