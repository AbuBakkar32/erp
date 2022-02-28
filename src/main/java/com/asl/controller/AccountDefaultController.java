package com.asl.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Acdef;
import com.asl.service.AcdefService;

@Controller
@RequestMapping("/account/accdefault")
public class AccountDefaultController extends ASLAbstractController {

	@Autowired private AcdefService acdefService;

	@GetMapping
	public String LoadAccountDefaultPage(Model model) {
		Acdef acdef = acdefService.find();
		if(acdef != null) {
			acdef.setDataExist(true);
		} else {
			acdef = getDefaultAccountDefault();
		}

		model.addAttribute("acdef", acdef);
		return "pages/account/accountdefault/accountdefault";
	}

	private Acdef getDefaultAccountDefault() {
		Acdef acdef = new Acdef();
		acdef.setXaccrule("Open");
		acdef.setXlength(8);
		acdef.setXdateexp(new Date());
		return acdef;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Acdef acdef){
		
		// IF existing
		if(acdef.isDataExist()) {
			long  count = acdefService.update(acdef);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update account default");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Account default updated successfully");
			responseHelper.setRedirectUrl("/account/accdefault");
			return responseHelper.getResponse();
		}

		// if new
		long  count = acdefService.save(acdef);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save account default");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Account default saved successfully");
		responseHelper.setRedirectUrl("/account/accdefault");
		return responseHelper.getResponse();
	}

}
