package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Acdetail;
import com.asl.entity.Arhed;
import com.asl.entity.Cabank;
import com.asl.entity.Cacus;
import com.asl.entity.LandInfo;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.MoneyReceiptReport;
import com.asl.service.AcService;
import com.asl.service.ArhedService;
import com.asl.service.CabankService;
import com.asl.service.CacusService;
import com.asl.service.LandInfoService;
import com.asl.service.XcodesService;

import lombok.var;

@Controller
@RequestMapping("/salesninvoice/moneyreceipt")
public class ACCMoneyReceiptController extends ASLAbstractController{

	@Autowired private ArhedService arhedService;
	@Autowired private XcodesService xcodeService;
	@Autowired private AcService acService;
	@Autowired private CabankService cabankService;
	@Autowired private CacusService cacusService;
	@Autowired private LandInfoService landInfoService;

	@GetMapping
	public String loadMoneyReceiptPage(Model model) {

		model.addAttribute("arhed", getDefaultArhed());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.MONEY_RECEIPTS.getCode(),TransactionCodeType.MONEY_RECEIPTS.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtype(TransactionCodeType.MONEY_RECEIPTS.getdefaultCode()));
		model.addAttribute("purpose", xcodeService.findByXtype(CodeType.PURPOSE.getCode(), Boolean.TRUE));
		model.addAttribute("findland", Collections.emptyList());
		return "pages/salesninvoice/moneyreceipt/arhed";
	}

	@GetMapping("/{xvoucher}")
	public String loadMoneyReceiptPage(@PathVariable String xvoucher, Model model) {
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		
		model.addAttribute("arhed", data);
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.MONEY_RECEIPTS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtype(TransactionCodeType.MONEY_RECEIPTS.getdefaultCode()));
		model.addAttribute("purpose", xcodeService.findByXtype(CodeType.PURPOSE.getCode(), Boolean.TRUE));
		List<LandInfo> land= landInfoService.getMemLand(data.getXcus());
		model.addAttribute("findland", land);
		
		List<Acdetail> acdetails = acService.findAcdetailsByXvoucher(data.getXdocnum());
		BigDecimal totalDebit = BigDecimal.ZERO;
		BigDecimal totalCredit = BigDecimal.ZERO;
		if(acdetails != null && !acdetails.isEmpty()) {
			for(Acdetail acd : acdetails) {
				totalDebit = totalDebit.add(acd.getXdebit() == null ? BigDecimal.ZERO : acd.getXdebit());
				totalCredit = totalCredit.add(acd.getXcredit() == null ? BigDecimal.ZERO : acd.getXcredit());
			}
		}
		model.addAttribute("acdetails", acdetails);
		model.addAttribute("totalDebit", totalDebit);
		model.addAttribute("totalCredit", totalCredit);

		return "pages/salesninvoice/moneyreceipt/arhed";
	}

	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();

		arhed.setXtype(TransactionCodeType.MONEY_RECEIPTS.getCode());
		arhed.setXtypetrn(TransactionCodeType.MONEY_RECEIPTS.getCode());
		arhed.setXtrntype(TransactionCodeType.MONEY_RECEIPTS.getCode());
		arhed.setXdate(new Date());
		arhed.setXprime(BigDecimal.ZERO);
		arhed.setXstatus("Open");
		//arhed.setXstaff(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());
		arhed.setXstaff(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff());
		arhed.setXstaffName(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getStaffname());

		return arhed;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Arhed arhed, BindingResult bindingResult){
		// Validate
		if(StringUtils.isBlank(arhed.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}
		Cacus cacus = cacusService.findByXcus(arhed.getXcus());
		
		if((cacus.getXgcus().equalsIgnoreCase("Member"))) {
			if(StringUtils.isBlank(arhed.getXland())) {
				responseHelper.setErrorStatusAndMessage("Land required");
				return responseHelper.getResponse();
			}
		}
		if(StringUtils.isBlank(arhed.getXbank())) {
			responseHelper.setErrorStatusAndMessage("Bank required");
			return responseHelper.getResponse();
		}
		if(arhed.getXprime().compareTo(BigDecimal.ZERO) == -1 || arhed.getXprime().equals(BigDecimal.ZERO)) {
			responseHelper.setErrorStatusAndMessage("Invalid amount");
			return responseHelper.getResponse();
		}

		//Modify transaction codes for arhed
		arhed.setXsign(-1);
		arhed.setXtypetrn("Sale");
		arhed.setXpaymentterm("Credit");
		arhed.setXstatusbnk("Open");
		//arhed.setXwh("01");
		arhed.setXbase(arhed.getXprime());
		arhed.setXstatusjv("Open");
		arhed.setXstaff(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff());
		arhed.setXstaffName(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getStaffname());
		//arhed.setXstaff(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());


		// if existing record
		if(StringUtils.isNotBlank(arhed.getXvoucher())) {
			Arhed existArhed = arhedService.findArhedByXvoucher(arhed.getXvoucher());
			BeanUtils.copyProperties(arhed, existArhed, "xvoucher", "xtype");
			long count = arhedService.update(existArhed);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Money Receipt updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt/" + arhed.getXvoucher());
			return responseHelper.getResponse();
		}

		// If new
		long count = arhedService.save(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Money Receipt created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}

	
	@PostMapping("/delete/{xvoucher}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xvoucher, Model model){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt not found in this system");
			return responseHelper.getResponse();
		}

		long count = arhedService.deleteVoucher(xvoucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Money Receipt");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Money Receipt deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt");
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xvoucher}")
	public @ResponseBody Map<String, Object> confirmMoneyReceipt(@PathVariable String xvoucher){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt not found in this system");
			return responseHelper.getResponse();
		}

		voucher.setXstatus("Confirmed");
		long count = arhedService.update(voucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm Money Receipt");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Money Receipt confirmed successfully");
		responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt/" + voucher.getXvoucher());
		return responseHelper.getResponse();
	}
	

	@PostMapping("/transfertogl/{xvoucher}")
	public @ResponseBody Map<String, Object> transferToGL(@PathVariable String xvoucher){
		
		Arhed arhed = arhedService.findArhedByXvoucher(xvoucher);
		if(arhed == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt is not found in this system");
			return responseHelper.getResponse();
		}
		//Problem in Bank Header
		// Validate
		if(arhed.getXprime().compareTo(BigDecimal.ZERO) == -1 || arhed.getXprime().equals(BigDecimal.ZERO)) {
			responseHelper.setErrorStatusAndMessage("Invalid amount");
			return responseHelper.getResponse();
		}
		if(!"Open".equalsIgnoreCase(arhed.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("MR is already confirmed"); 
			return responseHelper.getResponse(); 
		}
		
		if(StringUtils.isBlank(arhed.getXbank())) {
			responseHelper.setErrorStatusAndMessage("Cannot Proceed – Bank Required");
			return responseHelper.getResponse();
		}
		
		Cabank exist = cabankService.findCaBankByXbank(arhed.getXbank());
		if(StringUtils.isBlank(exist.getXacc())) {
			responseHelper.setErrorStatusAndMessage("Cannot Proceed – Account Head Not Set With Bank Code");
			return responseHelper.getResponse();
		}

		
		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(arhed.getXstatusjv())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			arhedService.procTransferARtoGL(xvoucher, p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}
		responseHelper.setSuccessStatusAndMessage("Transferred to GL successfully");
		responseHelper.setRedirectUrl("/salesninvoice/moneyreceipt/" + xvoucher);
		return responseHelper.getResponse();
	}



	@GetMapping("/print/{xvoucher}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String xvoucher, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) {
			message = "Can't find money receipt : " + xvoucher;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		MoneyReceiptReport report = new MoneyReceiptReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Money Receipt");
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());

		report.setXvoucher(data.getXvoucher());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXcus(data.getXcus());
		report.setXorg(data.getXorg());
		report.setXmadd(data.getCustomeraddress());
		report.setXprime(data.getXprime().setScale(2, RoundingMode.DOWN));
		report.setXbank(data.getXbank());
		report.setBankname(data.getXname());
		report.setXpaymenttype(data.getXpaymenttype());
		report.setXref(data.getXref());
		report.setSpellword(data.getSpellword());
		report.setXprimeword(data.getXprimeword());
		report.setXpurpose(data.getXpurpose());
		report.setXland(data.getXland());

		
		byte[] byt = getPDFByte(report, "moneyreceipt.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for voucher : " + xvoucher;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}
	
	@GetMapping("/landinfo/{xcus}")
	public @ResponseBody List<LandInfo> getLandInfo(@PathVariable String xcus, Model model) {
		List<LandInfo> land= landInfoService.getMemLand(xcus);
		model.addAttribute("findland", land);
		return land;
	}
}
