package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchasing")
public class PurchasingController extends ASLAbstractController {

	@GetMapping
	public String loadItemMasterMenuPage(Model model) {
		return "pages/purchasing/purchasing";
	}
}
