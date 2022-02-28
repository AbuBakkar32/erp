package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accgroup/levele")
public class AccountGroupLeveleController extends ASLAbstractController{
	
	@GetMapping
	public String LoadAccountGroupLevelePage(){
		return "pages/accountgroup/level/levele";
	}

}
