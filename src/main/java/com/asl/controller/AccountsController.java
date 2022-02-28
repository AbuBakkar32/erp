package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountsController extends ASLAbstractController {
	
	@GetMapping
	public String loadAccountMenuPage(Model model) {
		return "pages/account/account";
	}
	
}
