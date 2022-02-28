package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Controller
@RequestMapping("/production")
public class ProductionController extends ASLAbstractController {

	@GetMapping
	public String loadProductionMenuPage(Model model) {
		return "pages/production/production";
	}

}
