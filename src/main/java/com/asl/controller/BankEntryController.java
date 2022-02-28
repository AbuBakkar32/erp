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

import com.asl.entity.Cabank;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CabankService;

@Controller
@RequestMapping("/account/bankentry")
public class BankEntryController extends ASLAbstractController {

	@Autowired private CabankService cabankService;

	@GetMapping
	public String loadBankEntryPage(Model model) {

		model.addAttribute("cabank", getDefaultCabank());
		model.addAttribute("bankprefix", xtrnService.findByXtypetrn(TransactionCodeType.BANK_CODE.getCode(), Boolean.TRUE));
		model.addAttribute("cabanklist", cabankService.getAllCaBank());

		return"pages/account/bankentry/bankentry";
	}

	private Cabank getDefaultCabank() {
		Cabank cabank = new Cabank();
		cabank.setXtypetrn(TransactionCodeType.BANK_CODE.getCode());
		return cabank;
	}

	@GetMapping("/{xbank}")
	public String loadBankEntryPage(@PathVariable String xbank, Model model) {
		Cabank cabank = cabankService.findCaBankByXbank(xbank);
		if(cabank == null) return "redirect:/account/bankentry";

		model.addAttribute("cabank", cabank);
		model.addAttribute("bankprefix", xtrnService.findByXtypetrn(TransactionCodeType.BANK_CODE.getCode(), Boolean.TRUE));
		model.addAttribute("cabanklist", cabankService.getAllCaBank());

		return"pages/account/bankentry/bankentry";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cabank cabank, BindingResult bindingResult){
		// validation
		if(StringUtils.isBlank(cabank.getXname())) {
			responseHelper.setErrorStatusAndMessage("Bank name required");
			return responseHelper.getResponse();
		}
		
		//if exisiting
		if(StringUtils.isNotBlank(cabank.getXbank())) {
			Cabank exist = cabankService.findCaBankByXbank(cabank.getXbank());
			if(exist == null) {
				responseHelper.setErrorStatusAndMessage("Can't find bank in this system");
				return responseHelper.getResponse();
			}

			BeanUtils.copyProperties(cabank, exist);
			long count = cabankService.update(exist);

			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update bank");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Bank updated successfully");
			responseHelper.setRedirectUrl("/account/bankentry/" + exist.getXbank());
			return responseHelper.getResponse();
		}
		
		// if new
		long count = cabankService.save(cabank);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save bank");
			return responseHelper.getResponse();
		}
		
		responseHelper.setSuccessStatusAndMessage("Bank saved successfully");
		responseHelper.setRedirectUrl("/account/bankentry/" + cabank.getXbank());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xbank}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xbank){
		return doArchiveOrRestore(xbank, true);
	}

	private Map<String, Object> doArchiveOrRestore(String xbank, boolean archive){
		Cabank cabank = cabankService.findCaBankByXbank(xbank);
		if(cabank == null) {
			responseHelper.setErrorStatusAndMessage("Bank not found in this system");
			return responseHelper.getResponse();
		}

		long count = cabankService.delete(cabank.getXbank());
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete bank");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Bank deleted successfully");
		responseHelper.setRedirectUrl("/account/bankentry");
		return responseHelper.getResponse();
	}
}
