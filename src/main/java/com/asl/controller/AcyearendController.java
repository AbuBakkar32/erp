package com.asl.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.Potoapi;
import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;
import com.asl.model.Acyearend;
import com.asl.service.AcyearendService;
import com.asl.service.PogrnService;

@Controller
@RequestMapping("/system/acyearend")
public class AcyearendController extends ASLAbstractController{

	@Autowired private AcyearendService acyearendService;
	@GetMapping
	public String loadAcyearendControllerPage(Model model) {
		Acyearend acyearend = new Acyearend();
		model.addAttribute("acyearend", acyearend);
		return "pages/system/acyearend";
	}
	
	@PostMapping("/process")
	public @ResponseBody Map<String, Object> procrss(Acyearend acyearend) {
		
		String p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
		acyearendService.acYearEnd(acyearend.getXyear(), acyearend.getXdate(), p_seq); //.procInventory(pogrnHeader.getXgrnnum(), pogrnHeader.getXpornum(), p_seq);
		String em = getProcedureErrorMessages(p_seq);
		if (StringUtils.isNotBlank(em)) {
			responseHelper.setErrorStatusAndMessage(em);
			return responseHelper.getResponse();
		}

		

		responseHelper.setSuccessStatusAndMessage("Processed successfully");
		responseHelper.setRedirectUrl("/system/acyearend");
		return responseHelper.getResponse();
	}
}