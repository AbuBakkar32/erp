package com.asl.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Xusers;
import com.asl.model.LoggedInUserDetails;
import com.asl.service.XusersService;

/**
 * @author Zubayer Ahamed
 * @since Jul 31, 2021
 */
@Controller
@RequestMapping("/userprofile")
public class UserProfileController extends ASLAbstractController {

	@Autowired private XusersService xusersService;

	@GetMapping
	public String loadUserpage(Model model) {
		LoggedInUserDetails loggedInUser = sessionManager.getLoggedInUserDetails();
		if(loggedInUser == null) {
			return "redirect:/";
		}

		Xusers user = xusersService.findByZemailAndZid(loggedInUser.getUsername(), sessionManager.getBusinessId());
		if(user == null) {
			return "redirect:/";
		}

		model.addAttribute("xusers", user);
		return "pages/userprofile/xusers";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> saveUser(Xusers xusers, BindingResult bindingResult){
		// validation
		if(StringUtils.isBlank(xusers.getXstaff())) {
			responseHelper.setErrorStatusAndMessage("Staff ID required");
			return responseHelper.getResponse();
		}

		// if existing user
		Xusers existUser = xusersService.findByZemailAndZid(xusers.getZemail(), sessionManager.getBusinessId());
		if(existUser == null) {
			responseHelper.setErrorStatusAndMessage("User not found in this system");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xusers.getXpassword())) xusers.setXpassword(existUser.getXpassword());

		existUser.setXpassword(xusers.getXpassword());
		long count = xusersService.update(existUser);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update user info");
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/userprofile");
		responseHelper.setSuccessStatusAndMessage("User info updated successfully");
		return responseHelper.getResponse();
	}
}
