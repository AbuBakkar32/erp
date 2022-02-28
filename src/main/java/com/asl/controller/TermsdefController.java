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

import com.asl.entity.Termsdef;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.TermsdefService;

@Controller
@RequestMapping("/mastersetup/termsdef")
public class TermsdefController extends ASLAbstractController{

	@Autowired private TermsdefService termsdefService;
	@GetMapping
	public String loadTermsdefPage(Model model) {
		Termsdef termsdef = new Termsdef();
		model.addAttribute("termsdef", termsdef);
		model.addAttribute("allTermsdef", termsdefService.getAllTermsdef());
		model.addAttribute("allTermsdef", termsdefService.getAllTermsdef());
		model.addAttribute("termsTypes", xcodesService.findByXtype(CodeType.TERMS_TYPE.getCode(), Boolean.TRUE));
		return "pages/mastersetup/termsdef/termsdef";
	}
	
	@GetMapping("/{xtermid}")
	public String loadTermsdefPage( @PathVariable int xtermid, Model model) {
		Termsdef termsdef = termsdefService.findbyXtermid(xtermid);
		if (termsdef == null)
			return "redirect:/termsdef";

		model.addAttribute("termsdef", termsdef);
		model.addAttribute("allTermsdef", termsdefService.getAllTermsdef());
		model.addAttribute("termsTypes", xcodesService.findByXtype(CodeType.TERMS_TYPE.getCode(), Boolean.TRUE));
		return "pages/mastersetup/termsdef/termsdef";
	}
	

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Termsdef termsdef,BindingResult bindingResult){
		if(termsdef == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		if(termsdef.getXtermid() != 0){
			Termsdef existing = termsdefService.findbyXtermid(termsdef.getXtermid());
			BeanUtils.copyProperties(termsdef, existing, "xtype","xterm","xtermid");
			long count = termsdefService.updateTermsdef(existing);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update terms info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Data updated successfully");
			responseHelper.setRedirectUrl("/mastersetup/termsdef/" + existing.getXtermid());
			return responseHelper.getResponse();
		}

		// If new
		if(termsdefService.findTermsdefByXtypeAndXterm(termsdef.getXtype(), termsdef.getXterm()) != null) {
			responseHelper.setErrorStatusAndMessage("Type " + termsdef.getXtype() + " with terms " + termsdef.getXterm() + " data already exist in this system");
			return responseHelper.getResponse();
		}
		long count = termsdefService.saveTermsdef(termsdef);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setRedirectUrl("/mastersetup/termsdef/" + termsdef.getXtermid() );
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xtermid}")
	public @ResponseBody Map<String, Object> deleteLandInfo(@PathVariable int xtermid,  Model model) {
		Termsdef li = termsdefService.findbyXtermid(xtermid);
		if(li == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = termsdefService.deleteTermsdef(li);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/mastersetup/termsdef/");
		return responseHelper.getResponse();
}

}
