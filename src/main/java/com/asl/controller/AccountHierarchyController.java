package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/accounthierarchy")
public class AccountHierarchyController extends ASLAbstractController{
	
	@GetMapping
	public String loadAccountHierarchyPage(Model model) {
		return "pages/account/accounthierarchy/accounthierarchy";
	}

}
