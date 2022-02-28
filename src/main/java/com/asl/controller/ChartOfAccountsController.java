package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/chartofaccounts")
public class ChartOfAccountsController extends ASLAbstractController {

	@GetMapping
	public String loadChartAccountrPage(Model model) {
		return "pages/accounts/chartofaccounts";
	}
}
