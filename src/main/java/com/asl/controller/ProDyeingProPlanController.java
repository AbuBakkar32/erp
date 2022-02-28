package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prplandy")
public class ProDyeingProPlanController extends ASLAbstractController{

	@GetMapping
	public String loadProDyeingProPlanPage() {
		
		return "pages/landproduction/prplandy";
	}
}

