package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/prdyingr")
public class ProDyeingTransReController extends ASLAbstractController{

	@GetMapping
	public String loadProDyeingTransRePage() {
		
		return "pages/landproduction/prdyingr";
	}
}
