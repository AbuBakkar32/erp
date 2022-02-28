package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accgroup/levelc")
public class AccountGroupLevelcController extends ASLAbstractController{
	
	@GetMapping
	public String LoadAccountGroupLevelcPage(){
		return "pages/accountgroup/level/levelc";
	}

}
