package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/voucherdetails")
public class AccountVoucherDetailsController extends ASLAbstractController{
	
	@GetMapping
	public String loadVoucherDetailsPage() {
		return "pages/account/voucher/voucherdetails";
	}

}
