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

import com.asl.entity.AccountGroup;
import com.asl.entity.Acmst;
import com.asl.service.AccountGroupService;
import com.asl.service.AcmstService;

@Controller
@RequestMapping("/account/coa")
public class ChartOfAccountController extends ASLAbstractController {

	@Autowired private AcmstService acmstService;
	@Autowired private AccountGroupService accountGroupService;

	@GetMapping
	public String loadChartAccountrPage(Model model) {
		model.addAttribute("acmst", getDefaultAcmst());
		model.addAttribute("acmstlist", acmstService.getAllAcmst());
		return "pages/account/chartofaccount/chartofaccount";
	}

	private Acmst getDefaultAcmst() {
		Acmst acmst = new Acmst();
		acmst.setXaccusage("Ledger");
		return acmst;
	}

	@GetMapping("/{xacc}")
	public String loadChartAccountrPage(@PathVariable String xacc, Model model) {
		Acmst acmst = acmstService.findByXacc(xacc);
		if(acmst == null) return "redirect:/acount/coa";

		Map<String, AccountGroup> hkey = accountGroupService.getAccountGroupHirerkey(acmst.getXgroup());
		if(hkey != null) {
			if(hkey.get("LEVEL_1") != null) acmst.setXhrc1(hkey.get("LEVEL_1").getXagcode() + " - " + hkey.get("LEVEL_1").getXagname());
			if(hkey.get("LEVEL_2") != null) acmst.setXhrc2(hkey.get("LEVEL_2").getXagcode() + " - " + hkey.get("LEVEL_2").getXagname());
			if(hkey.get("LEVEL_3") != null) acmst.setXhrc3(hkey.get("LEVEL_3").getXagcode() + " - " + hkey.get("LEVEL_3").getXagname());
			if(hkey.get("LEVEL_4") != null) acmst.setXhrc4(hkey.get("LEVEL_4").getXagcode() + " - " + hkey.get("LEVEL_4").getXagname());
			if(hkey.get("LEVEL_5") != null) acmst.setXhrc5(hkey.get("LEVEL_5").getXagcode() + " - " + hkey.get("LEVEL_5").getXagname());
		}

		model.addAttribute("acmst", acmst);
		model.addAttribute("acmstlist", acmstService.getAllAcmst());

		return "pages/account/chartofaccount/chartofaccount";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Acmst acmst, BindingResult bindingResult){

		// validation
		if(StringUtils.isBlank(acmst.getXdesc())) {
			responseHelper.setErrorStatusAndMessage("Account name required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(acmst.getXgroup())) {
			responseHelper.setErrorStatusAndMessage("Please select a account group");
			return responseHelper.getResponse();
		}

		// if existing
		if(StringUtils.isNotBlank(acmst.getXacc())) {
			Acmst exist = acmstService.findByXacc(acmst.getXacc());
			if(exist == null) {
				responseHelper.setErrorStatusAndMessage("Account not found to do update");
				return responseHelper.getResponse();
			}

			BeanUtils.copyProperties(acmst, exist, "xacc", "xgroup", "xacctype");
			long count = acmstService.update(exist);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save this account");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Account updated successfully");
			responseHelper.setRedirectUrl("/account/coa/" + exist.getXacc());
			return responseHelper.getResponse();
		}

		// if new
		AccountGroup acgroup = accountGroupService.findByCode(acmst.getXgroup());
		if(acgroup == null) {
			responseHelper.setErrorStatusAndMessage("Account group not found in this system");
			return responseHelper.getResponse();
		}

		acmst.setXacctype(acgroup.getXagtype());
		long count = acmstService.save(acmst);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save this account");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Account created successfully");
		responseHelper.setRedirectUrl("/account/coa/" + acmst.getXacc());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xacc}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xacc){
		return doArchiveOrRestore(xacc, true);
	}

	public Map<String, Object> doArchiveOrRestore(String xacc, boolean archive){
		Acmst acmst = acmstService.findByXacc(xacc);
		if(acmst == null) {
			responseHelper.setErrorStatusAndMessage("Account not found to do delete");
			return responseHelper.getResponse();
		}


		long count = acmstService.delete(xacc);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Delete Account");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Account deleted successfully");
		responseHelper.setRedirectUrl("/account/coa");
		return responseHelper.getResponse();
	}
	

}
