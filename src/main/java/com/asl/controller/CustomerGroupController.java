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

import com.asl.entity.Xcodes;
import com.asl.enums.ResponseStatus;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/account/cusgroup")
public class CustomerGroupController  extends ASLAbstractController{
	
	@Autowired private XcodesService xcodesService;
	
	@GetMapping
	public String loadXcodePage(Model model) {
		model.addAttribute("xcodes", new Xcodes());
		model.addAttribute("xcodesList", xcodesService.getAllXcodesByCusGroup());
		
		return "pages/account/customergroup/customergroup";
	}
	
	@GetMapping("/{xtype}/{xcode}")
	public String loadXcodePage(@PathVariable String xtype, @PathVariable String xcode, Model model) {
		Xcodes x = xcodesService.findByXtypesAndXcodesForCus(xtype,xcode);
		if(x == null) return "redirect:/account/cusgroup";

		model.addAttribute("xcodes", x);
		model.addAttribute("xcodesList", xcodesService.getAllXcodesByCusGroup());
		return "pages/account/customergroup/customergroup";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Xcodes xcodes, BindingResult bindingResult){
		if(xcodes == null || StringUtils.isBlank(xcodes.getXtype()) || StringUtils.isBlank(xcodes.getXcode())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing record
		Xcodes existXcodes = xcodesService.findByXtypesAndXcodesForCus(xcodes.getXtype(), xcodes.getXcode());
		if(existXcodes != null) {
			BeanUtils.copyProperties(xcodes, existXcodes, "xtype", "xcode");
			long count = xcodesService.update(existXcodes);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Customer Group updated successfully");
			responseHelper.setRedirectUrl("/account/cusgroup/" + xcodes.getXtype() + "/" + xcodes.getXcode());
			return responseHelper.getResponse();
		}

		// If new xtrn
		long count = xcodesService.save(xcodes);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Customer Group saved successfully");
		responseHelper.setRedirectUrl("/account/cusgroup/" + xcodes.getXtype() + "/" + xcodes.getXcode());
		return responseHelper.getResponse();
	}
}
