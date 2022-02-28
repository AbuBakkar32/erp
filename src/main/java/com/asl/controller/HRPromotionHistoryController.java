package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/promohistory")
public class HRPromotionHistoryController extends ASLAbstractController{

	@GetMapping
	public String loadHRPersonalInfoPage() {
		
		return "pages/hr/promohistory";
	}
}
