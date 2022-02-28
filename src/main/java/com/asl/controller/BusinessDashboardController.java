package com.asl.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Xusers;
import com.asl.entity.Zbusiness;
import com.asl.model.Business;
import com.asl.service.ZbusinessService;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Controller
@RequestMapping("/business")
public class BusinessDashboardController extends ASLAbstractController {

	private static final String OUTSIDE_USERS_NAME = "anonymousUser";

	@Autowired private ZbusinessService zbusinessService;

	@SuppressWarnings("unchecked")
	@GetMapping
	public String loadBusinessDashboard(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (!OUTSIDE_USERS_NAME.equalsIgnoreCase(username)) {
			return "redirect:/";
		}


		List<Xusers> list = null;
		if(sessionManager.getFromMap("FAKE_LOGIN_USER") != null) {
			list = (List<Xusers>) sessionManager.getFromMap("FAKE_LOGIN_USER");
		}
		if(list == null || list.isEmpty()) {
			return "redirect:/";
		}

		List<Business> businesses = new ArrayList<>();
		for(Xusers xus : list) {
			if(Boolean.FALSE.equals(xus.getZactive())) continue;
			Zbusiness zb = zbusinessService.findBById(xus.getZid());
			if(zb == null) continue;
			businesses.add(new Business(xus.getZemail(), xus.getXpassword(), zb.getZid(), zb.getZorg(), zb.getCentral(), zb.getBranch()));
		}

		model.addAttribute("centralAvailable", !businesses.stream().filter(f -> f.isCentral()).collect(Collectors.toList()).isEmpty());
		model.addAttribute("branchAvailable", !businesses.stream().filter(f -> f.isBranch()).collect(Collectors.toList()).isEmpty());
		model.addAttribute("businesses", businesses);

		return "pages/business/business-dashboard";
	}
}
