package com.asl.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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

import com.asl.entity.Arhed;
import com.asl.entity.LandInfo;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ArhedService;
import com.asl.service.CacusService;
import com.asl.service.LandInfoService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/arentry")
public class ACCAREntryController extends ASLAbstractController{

	@Autowired private ArhedService arhedService;
	@Autowired private XcodesService xcodeService;	
	@Autowired private CacusService cacusService;
	@Autowired private LandInfoService landInfoService;

	@GetMapping
	public String loadCusOpeningPage(Model model) {

		model.addAttribute("arhed", getDefaultArhed());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.ACCOUNT_AR.getCode(),TransactionCodeType.ACCOUNT_AR.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("paymentModeList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(),Boolean.TRUE));
		model.addAttribute("purpose", xcodeService.findByXtype(CodeType.PURPOSE.getCode(), Boolean.TRUE));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtype(TransactionCodeType.ACCOUNT_AR.getdefaultCode()));
		model.addAttribute("findland", Collections.emptyList());
		return "pages/salesninvoice/arentry/arentry";
	}

	@GetMapping("/{xvoucher}")
	public String loadCustomerOpeningPage(@PathVariable String xvoucher, Model model) {
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		data.setXtypetrn(TransactionCodeType.ACCOUNT_AR.getCode());
		model.addAttribute("arhed", data);
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_AR.getCode()));
		model.addAttribute("paymentModeList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode()));
		model.addAttribute("purpose", xcodeService.findByXtype(CodeType.PURPOSE.getCode(), Boolean.TRUE));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtype(TransactionCodeType.ACCOUNT_AR.getdefaultCode()));
		List<LandInfo> land= landInfoService.getMemLand(data.getXcus());
		model.addAttribute("findland", land);
		return "pages/salesninvoice/arentry/arentry";
	}

	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();

		arhed.setXtrntype(TransactionCodeType.ACCOUNT_AR.getCode());
		arhed.setXdate(new Date());
		arhed.setXprime(BigDecimal.ZERO);
		arhed.setXstatus("Open");
		arhed.setXstaff(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());

		return arhed;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Arhed arhed, BindingResult bindingResult){
		// Validate
		if(StringUtils.isBlank(arhed.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Member/Director required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(arhed.getXland())) {
			responseHelper.setErrorStatusAndMessage("Land required");
			return responseHelper.getResponse();
		}
		/*
		 * if(StringUtils.isBlank(arhed.getXbank())) {
		 * responseHelper.setErrorStatusAndMessage("Bank required"); return
		 * responseHelper.getResponse(); }
		 */
		if(arhed.getXprime().compareTo(BigDecimal.ZERO) == -1 || arhed.getXprime().equals(BigDecimal.ZERO)) {
			responseHelper.setErrorStatusAndMessage("Invalid amount");
			return responseHelper.getResponse();
		}

		//Modify transaction codes for arhed
		arhed.setXstatusjv("Open");
		arhed.setXtypetrn("Sale");
		arhed.setXstatusjv("Open");
		arhed.setXbase(arhed.getXprime());
		arhed.setXsign(1); 
		arhed.setXstaff(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());

		
		// if existing record
		if(StringUtils.isNotBlank(arhed.getXvoucher())) {
			Arhed existArhed = arhedService.findArhedByXvoucher(arhed.getXvoucher());
			BeanUtils.copyProperties(arhed, existArhed, "xvoucher", "xtype");
			long count = arhedService.update(existArhed);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("AR updated successfully");
			responseHelper.setRedirectUrl("/arentry/" + arhed.getXvoucher());
			return responseHelper.getResponse();
		}

		// If new
		long count = arhedService.save(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("AR created successfully");
		responseHelper.setRedirectUrl("/arentry/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}

	
	@PostMapping("/delete/{xvoucher}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xvoucher, Model model){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("AR not found in this system");
			return responseHelper.getResponse();
		}

		long count = arhedService.deleteVoucher(xvoucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete AR");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("AR deleted successfully");
		responseHelper.setRedirectUrl("/arentry");
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xvoucher}")
	public @ResponseBody Map<String, Object> confirmMoneyReceipt(@PathVariable String xvoucher){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("AR is not found in this system");
			return responseHelper.getResponse();
		}

		voucher.setXstatus("Confirmed");
		long count = arhedService.update(voucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm AR");
			return responseHelper.getResponse();
		}
		
		responseHelper.setSuccessStatusAndMessage("AR confirmed successfully");
		responseHelper.setRedirectUrl("/arentry/" + voucher.getXvoucher());
		return responseHelper.getResponse();
	}
	
	@GetMapping("/landinfo/{xcus}")
	public @ResponseBody List<LandInfo> getLandInfo(@PathVariable String xcus, Model model) {
		List<LandInfo> land= landInfoService.getMemLand(xcus);
		model.addAttribute("findland", land);
		return land;
	}
	

}
