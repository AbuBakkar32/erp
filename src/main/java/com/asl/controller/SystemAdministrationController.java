package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class SystemAdministrationController extends ASLAbstractController {

	@GetMapping("/usersentry")
	public String loadUsersEntrypage(Model model) {
		
		return "pages/system/usersentry/usersentry";
	}
}
