package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bankvoucherentry")
public class BankVoucherEntryController extends ASLAbstractController{
	
	@GetMapping
	public String loadBankVoucherEntryPage() {
		
		return"pages/accounts/bankvoucherentry";
	}

}
