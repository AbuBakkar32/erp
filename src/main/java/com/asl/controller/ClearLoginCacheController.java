package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * Jan 12, 2021
 */
@Controller
@RequestMapping("/clearlogincache")
public class ClearLoginCacheController extends ASLAbstractController {

	@GetMapping
	public String clearCache() {
		if(sessionManager.getFromMap("FAKE_LOGIN_USER") != null) {
			sessionManager.removeFromMap("FAKE_LOGIN_USER");
		}
		return "redirect:/login";
	}
}
