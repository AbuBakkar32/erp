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
@RequestMapping("/shiftsettings")
public class ShiftSettingsController extends ASLAbstractController {

	@Autowired
	private POSSettingsService posSettingsService;

	@GetMapping
	public String loadShiftSettingsPage(Model model) {
		POSSettings ps = posSettingsService.findByPOSSettings("Shift");
		if(ps == null) ps = new POSSettings();
		ps.setXacc("Shift");
		model.addAttribute("shifts", ps);
		return "pages/shiftsettings";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(POSSettings shiftSettings, BindingResult bindingResult) {
		if (shiftSettings == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		POSSettings ps = posSettingsService.findByPOSSettings("Shift");
		if(ps != null) {
			BeanUtils.copyProperties(shiftSettings, ps);
			long count = posSettingsService.update(ps);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update shift");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Shift updated successfully");
			responseHelper.setRedirectUrl("/shiftsettings");
			return responseHelper.getResponse();
		}
		
		// If new
		long count = posSettingsService.save(shiftSettings);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Shift saved successfully");
		responseHelper.setRedirectUrl("/shiftsettings");
		return responseHelper.getResponse();

	}
}
