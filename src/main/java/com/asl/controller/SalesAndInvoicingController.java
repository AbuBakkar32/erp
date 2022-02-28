package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @since Mar 9, 2021
 */
@Controller
@RequestMapping("/salesninvoice")
public class SalesAndInvoicingController extends ASLAbstractController {

	@GetMapping
	public String loadSalesAndInvoicingMenuPage(Model Model) {
		return "pages/salesninvoice/salesninvoice";
	}
}
