package com.asl.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Xusers;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.XusersService;

@Controller
@RequestMapping("/system/xusers")
public class XusersController extends ASLAbstractController{

	@Autowired private XusersService xusersService;

	@GetMapping
	public String loadUserpage(Model model) {
		model.addAttribute("xusers", new Xusers());
		model.addAttribute("xusersList", xusersService.getAllXusers());
		model.addAttribute("stores", xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		
		model.addAttribute("systemadminuser", sessionManager.getLoggedInUserDetails().getRoles().contains("SYSTEM_ADMIN"));
		
		return "pages/system/usersentry/xusers/xusers";
	}

	@GetMapping("/{zemail}")
	public String loadUserpage(@PathVariable String zemail, Model model) {
		Xusers xuser = xusersService.findUserByZemail(zemail);
		if(xuser == null) xuser = new Xusers();

		model.addAttribute("xusers", xuser);
		model.addAttribute("xusersList", xusersService.getAllXusers());
		model.addAttribute("stores", xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		
		model.addAttribute("systemadminuser", sessionManager.getLoggedInUserDetails().getRoles().contains("ROLE_SYSTEM_ADMIN"));
		return "pages/system/usersentry/xusers/xusers";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> saveUser(Xusers xusers, BindingResult bindingResult){
		if(xusers == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify zemail first
		xusers.setZemail(xusersService.modifyZemail(xusers.getZemail()));

		// validation
		if(StringUtils.isNotBlank(xusers.getXstaff())) {
			Xusers xs = xusersService.findUserByXstaff(xusers.getXstaff());
			if(xs != null && !xs.getZemail().equalsIgnoreCase(xusers.getZemail())) {
				responseHelper.setErrorStatusAndMessage("Staff ID alredy assigned with user : " + xs.getZemail());
				return responseHelper.getResponse();
			}
		}

		if(StringUtils.isNotBlank(xusers.getXcus())) {
			Xusers xs = xusersService.findUserByXcus(xusers.getXcus());
			if(xs != null && !xs.getZemail().equalsIgnoreCase(xusers.getZemail())) {
				responseHelper.setErrorStatusAndMessage("Party alredy assigned with user : " + xs.getZemail());
				return responseHelper.getResponse();
			}
		}

		// Validate xusers data
		modelValidator.validateXuser(xusers, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// password
		xusers.setXoldpass(xusers.getXpassword());

		// if existing user
		Xusers existUser = xusersService.findUserByZemail(xusers.getZemail());
		if(existUser != null) {
			if(StringUtils.isBlank(xusers.getXpassword())) {
				xusers.setXpassword(existUser.getXpassword());
				xusers.setXoldpass(existUser.getXoldpass());
			}
			BeanUtils.copyProperties(xusers, existUser);
			long count = xusersService.update(existUser);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update user");
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/system/xusers/" + xusers.getZemail());
			responseHelper.setSuccessStatusAndMessage("User updated successfully");
			return responseHelper.getResponse();
		}

		// if new user
		long count = xusersService.save(xusers);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save user");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/system/xusers/" + xusers.getZemail());
		responseHelper.setSuccessStatusAndMessage("User saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{zemail}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String zemail){
		Xusers user = xusersService.findUserByZemail(zemail);
		if(user == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = xusersService.delete(user);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete this user");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("User deleted successfully");
		responseHelper.setRedirectUrl("/system/xusers");
		return responseHelper.getResponse();
	}
}
