package com.asl.controller;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.POSSettings;
import com.asl.enums.ResponseStatus;
import com.asl.service.POSSettingsService;

@Controller
@RequestMapping("/servicecharge")
public class ServiceChargeContoller extends ASLAbstractController{
	
	@Autowired
	private POSSettingsService posSettingsService;
	@GetMapping
	public String loadServiceChargePage(Model model) {
		
		POSSettings ps = posSettingsService.findByPOSSettings("Service");
		if(ps == null) ps = new POSSettings();
		ps.setXacc("Service");
		model.addAttribute("services", ps);
		
		return"pages/servicecharge";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(POSSettings serviceCharge, BindingResult bindingResult) {
		if (serviceCharge == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		// If Exist?
		POSSettings ps = posSettingsService.findByPOSSettings("Service");
		if(ps != null) {
			BeanUtils.copyProperties(serviceCharge, ps);
			long count = posSettingsService.update(ps);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Service Charge");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Service Charge updated successfully");
			responseHelper.setRedirectUrl("/servicecharge");
			return responseHelper.getResponse();
		}
		
		// If new
		long count = posSettingsService.save(serviceCharge);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Service Charge saved successfully");
		responseHelper.setRedirectUrl("/servicecharge");
		return responseHelper.getResponse();

	}

}
