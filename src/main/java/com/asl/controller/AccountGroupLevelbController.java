package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accgroup/levelb")
public class AccountGroupLevelbController extends ASLAbstractController{
	
	@GetMapping
	public String LoadAccountGroupLevelbPage(){
		return "pages/accountgroup/level/levelb";
	}

}
