package com.asl.controller;

import java.util.Map;

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

import com.asl.entity.LandSurvey;
import com.asl.enums.ResponseStatus;
import com.asl.service.LandSurveyService;

@Controller
@RequestMapping("/landsurvey")
public class LandSurveyController extends ASLAbstractController{
	
	@Autowired 
	private LandSurveyService landSurveyService;
	
	@GetMapping
	public String loadLandSurveyPage(Model model) {
		model.addAttribute("survey", getDefaultLandsurvey());
		model.addAttribute("allSurvey", landSurveyService.getAllLandSurvey());
		return "pages/land/landsurvey";
	}
	
	private LandSurvey getDefaultLandsurvey() {
		LandSurvey ls  = new LandSurvey();
		ls.setNewData(true);
		return ls;
	}
	
	@GetMapping("/{xland}/{xrow}")
	public String loadRolePage(@PathVariable String xland, @PathVariable int xrow, Model model) {
		LandSurvey landsurvey = landSurveyService.findLandSurveydetailByXlandAndXrow(xland,xrow);
		if (landsurvey == null) return "redirect:/landsurvey";

		model.addAttribute("survey", landsurvey);
		model.addAttribute("allSurvey", landSurveyService.getAllLandSurvey());
		return "pages/land/landsurvey";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandSurvey LandSurvey, BindingResult bindingResult) {
		if (LandSurvey == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		LandSurvey exist = landSurveyService.findLandSurveydetailByXlandAndXrow(LandSurvey.getXland(), LandSurvey.getXrow());
		
		// if new
		if(LandSurvey.isNewData()) {
			if(exist != null) {
				responseHelper.setErrorStatusAndMessage("Land " + LandSurvey.getXland() + " with Row " + LandSurvey.getXrow() + " data already exist in this system");
				return responseHelper.getResponse();
			}
		
			long count = landSurveyService.save(LandSurvey);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save Land Survey info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Land Survey info saved successfully");
			responseHelper.setRedirectUrl("/landsurvey/" + LandSurvey.getXland()+"/"+LandSurvey.getXrow());
			return responseHelper.getResponse();
		}
		
		// if existing
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find data in this system");
			return responseHelper.getResponse();
		}
		
		BeanUtils.copyProperties(LandSurvey, exist);
		long count = landSurveyService.update(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Land Survey info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Land Survey info updated successfully");
		responseHelper.setRedirectUrl("/landsurvey/" + LandSurvey.getXland()+"/" + LandSurvey.getXrow());
		return responseHelper.getResponse();
	}

	@PostMapping("{xland}/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePersonDetail(@PathVariable String xland, @PathVariable String xrow, Model model) {
		LandSurvey ls = landSurveyService.findLandSurveydetailByXlandAndXrow(xland, Integer.parseInt(xrow));
		if(ls == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landSurveyService.deleteDetail(ls);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/landsurvey/" + xland + "/" + xrow);
		return responseHelper.getResponse();
	}
}
