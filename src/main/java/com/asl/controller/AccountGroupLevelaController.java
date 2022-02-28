package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accgroup/levela")
public class AccountGroupLevelaController extends ASLAbstractController{
	
	@GetMapping
	public String LoadAccountGroupLevelaPage(){
		return "pages/accountgroup/level/levela";
	}

}
