package com.asl.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Xusers;
import com.asl.model.FakeLoginUser;
import com.asl.service.XusersService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since 
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController extends ASLAbstractController {

	private static final String LOGAIN_PAGE_PATH = "pages/login/fakelogin";
	private static final String DIRECT_LOGIN_PAGE_PATH = "pages/login/directlogin";
	private static final String OUTSIDE_USERS_NAME = "anonymousUser";

	@Autowired private XusersService userService;

	@GetMapping
	public String loadLoginPage(Model model, @RequestParam(required = false) String device) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (OUTSIDE_USERS_NAME.equalsIgnoreCase(username)) {
			if(sessionManager.getFromMap("FAKE_LOGIN_USER") != null) {
				return "redirect:/business";
			}

			model.addAttribute("pageTitle", "Login");
			log.debug("Login page called at {}", new Date());
			return isDirectlogin() ? DIRECT_LOGIN_PAGE_PATH : LOGAIN_PAGE_PATH;
		}

		return "redirect:/";
	}

	@PostMapping("/fakelogin")
	public @ResponseBody Map<String, Object> doFakeLogin(FakeLoginUser fakeLoginUser){
		if(fakeLoginUser == null || StringUtils.isBlank(fakeLoginUser.getUsername()) || StringUtils.isBlank(fakeLoginUser.getPassword())) {
			responseHelper.setErrorStatusAndMessage("Username or password is empty");
			return responseHelper.getResponse();
		}

		List<Xusers> users = userService.findByZemailAndXpassword(fakeLoginUser.getUsername(), fakeLoginUser.getPassword());
		if(users == null || users.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("User not found in the system, please try again with appropriate username and password");
			return responseHelper.getResponse();
		}

		users = users.stream().filter(f -> Boolean.TRUE.equals(f.getZactive())).collect(Collectors.toList());
		sessionManager.addToMap("FAKE_LOGIN_USER", users);

		responseHelper.setSuccessStatusAndMessage("Logged in successfully");
		responseHelper.setRedirectUrl("/business");
		return responseHelper.getResponse();
	}
}
