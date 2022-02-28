package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aradjustmententry")
public class ARAdjustmentEntryController extends ASLAbstractController{
	
	@GetMapping
	public String loadARAdjustmentEntryPage() {
		
		return"pages/accounts/aradjustmententry";
	}

}
