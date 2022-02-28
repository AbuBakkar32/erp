package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventory")
public class InventoryController extends ASLAbstractController {

	@GetMapping
	public String loadInventoryMenuPage(Model model) {
		return "pages/inventory/inventory";
	}

}

