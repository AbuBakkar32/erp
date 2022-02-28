package com.asl.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.POSSettings;
import com.asl.enums.ResponseStatus;
import com.asl.service.POSSettingsService;

@Controller
@RequestMapping("/possettings")
public class POSSettingsController extends ASLAbstractController{

	@Autowired private POSSettingsService posSettingsService;

	@GetMapping
	public String loadPage(Model model) { 
		formDefaultValues(model);
		return "pages/possettings";
	}

	@GetMapping("/reloadform")
	public String reloadForm(Model model) { 
		formDefaultValues(model);
		return "pages/possettings::possettingform";
	}

	private void formDefaultValues(Model model) {
		POSSettings imageView = posSettingsService.findByPOSSettings("Image View");
		model.addAttribute("imageView", imageView != null ? imageView.getZactive() : false);

		POSSettings liveItemSearch = posSettingsService.findByPOSSettings("Live Item Search");
		model.addAttribute("liveItemSearch", liveItemSearch != null ? liveItemSearch.getZactive() : false);

		POSSettings livePromotion = posSettingsService.findByPOSSettings("Live Promotion");
		model.addAttribute("livePromotion", livePromotion != null ? livePromotion.getZactive() : false);

		POSSettings liveBackgroundColor = posSettingsService.findByPOSSettings("Live Background Color");
		model.addAttribute("liveBackgroundColor", liveBackgroundColor != null ? liveBackgroundColor.getZactive() : false);

		POSSettings liveManualToken = posSettingsService.findByPOSSettings("Live Manual Token");
		model.addAttribute("liveManualToken", liveManualToken != null ? liveManualToken.getZactive() : false);

		POSSettings liveKOTPrinter = posSettingsService.findByPOSSettings("Live KOT Printer");
		model.addAttribute("liveKOTPrinter", liveKOTPrinter != null ? liveKOTPrinter.getZactive() : false);

		POSSettings liveShortCode = posSettingsService.findByPOSSettings("Live Short Code");
		model.addAttribute("liveShortCode", liveShortCode != null ? liveShortCode.getZactive() : false);

		POSSettings liveHoldBill = posSettingsService.findByPOSSettings("Live Hold Bill");
		model.addAttribute("liveHoldBill", liveHoldBill != null ? liveHoldBill.getZactive() : false);
	}

	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(String name, boolean status){
		
		System.out.println(name + " - " + status);
		
		POSSettings ps = new POSSettings();
		ps.setXacc(name);
		ps.setZactive(status);
		
		POSSettings exps = posSettingsService.findByPOSSettings(name);
		if(exps == null) {
			long count = posSettingsService.save(ps);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
		} else {
			long count = posSettingsService.update(ps);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setReloadSectionIdWithUrl("possettingform", "/possettings/reloadform");
		return responseHelper.getResponse();
				
	}
}
