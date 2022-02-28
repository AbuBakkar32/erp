package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recipemaster")
public class ProRecipeMasterController extends ASLAbstractController{

	@GetMapping
	public String loadProRecipeMasterPage() {
		
		return "pages/landproduction/recipemaster";
	}
}

