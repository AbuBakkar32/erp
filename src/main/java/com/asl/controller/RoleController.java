package com.asl.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Xroles;
import com.asl.enums.ResponseStatus;
import com.asl.service.XRolesService;

@Controller
@RequestMapping("/role")
public class RoleController extends ASLAbstractController {
	@Autowired
	private XRolesService xRolesService;

	@GetMapping
	public String loadRolePage(Model model) {
		Xroles xroles = new Xroles();
		xroles.setXmaxdisc(BigDecimal.ZERO);
		xroles.setXmaxdiscf(BigDecimal.ZERO);
		xroles.setNewdata(true);

		model.addAttribute("roles", xroles);
		model.addAttribute("allRole", xRolesService.getAllXroles());
		return "pages/role";
	}

	@GetMapping("/{xrole}")
	public String loadRolePage(@PathVariable String xrole, Model model) {
		Xroles xroles = xRolesService.findByXrole(xrole);
		if(xroles == null) return "redirect:/role";

		xroles.setNewdata(false);
		model.addAttribute("roles", xroles);
		model.addAttribute("allRole", xRolesService.getAllXroles());
		return "pages/role";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Xroles xroles, BindingResult bindingResult){
		if(xroles == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		} 

		if(StringUtils.isBlank(xroles.getXrole())) {
			responseHelper.setErrorStatusAndMessage("Role name required");
			return responseHelper.getResponse();
		}

		// modify role  name
		String xr = xroles.getXrole().trim();
		xr = xr.toUpperCase();
		xr = xr.replace(" ", "_");
		xroles.setXrole(xr);

		// check existing if want to save new role
		if(xroles.isNewdata()) {
			Xroles existingXrole = xRolesService.findByXrole(xroles.getXrole());
			if(existingXrole != null) {
				responseHelper.setErrorStatusAndMessage("Role name already exist");
				return responseHelper.getResponse();
			}
		}

		if (xroles.getXmaxdisc().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Discount should be Positive Value");
			return responseHelper.getResponse();
		}

		if (xroles.getXmaxdiscf().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("SP Discount should be Positive Value");
			return responseHelper.getResponse();
			
		}

		// save or update
		long count = xroles.isNewdata() ? xRolesService.save(xroles) : xRolesService.update(xroles);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data "+ (xroles.isNewdata() ? "saved" : "updated") +" successfully");
		responseHelper.setRedirectUrl("/role/" + xroles.getXrole());
		return responseHelper.getResponse();

	}
}