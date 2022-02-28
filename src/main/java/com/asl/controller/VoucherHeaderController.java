package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/voucherheader")
public class VoucherHeaderController extends ASLAbstractController{
	
	@GetMapping
	public String loadVoucherHeaderPage() {
		
		return"pages/accounts/voucherheader";
	}

}
