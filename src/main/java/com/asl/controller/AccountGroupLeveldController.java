package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accgroup/leveld")
public class AccountGroupLeveldController extends ASLAbstractController{
	
	@GetMapping
	public String LoadAccountGroupLeveldPage(){
		return "pages/accountgroup/level/leveld";
	}

}
