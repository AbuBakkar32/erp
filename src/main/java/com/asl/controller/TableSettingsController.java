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
@RequestMapping("/tablesettings")
public class TableSettingsController extends ASLAbstractController{
	
	@Autowired
	private POSSettingsService posSettingsService;
	@GetMapping
	public String loadTableSettingsPage(Model model) {
		
		POSSettings ps = posSettingsService.findByPOSSettings("Table");
		if(ps == null) ps = new POSSettings();
		ps.setXacc("Table");
		model.addAttribute("tables", ps);
		return"pages/tablesettings";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(POSSettings tableSettings, BindingResult bindingResult) {
		if (tableSettings == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		POSSettings ps = posSettingsService.findByPOSSettings("Table");
		if(ps != null) {
			BeanUtils.copyProperties(tableSettings, ps);
			long count = posSettingsService.update(ps);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update table");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Table updated successfully");
			responseHelper.setRedirectUrl("/tablesettings");
			return responseHelper.getResponse();
		}
		
		// If new
		long count = posSettingsService.save(tableSettings);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Table saved successfully");
		responseHelper.setRedirectUrl("/tablesettings");
		return responseHelper.getResponse();

	}

}
