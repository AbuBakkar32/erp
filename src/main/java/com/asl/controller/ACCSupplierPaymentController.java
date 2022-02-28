package com.asl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.MoneyReceiptReport;
import com.asl.model.report.SupplierPaymentReport;
import com.asl.service.AcService;
import com.asl.service.ArhedService;
import com.asl.service.CabankService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/purchasing/supplierpayment")
public class ACCSupplierPaymentController extends ASLAbstractController{
	
	@Autowired private ArhedService arhedService;
	@Autowired private XcodesService xcodeService;
	@Autowired private AcService acService;
	@Autowired private CabankService cabankService;

	@GetMapping
	public String loadMoneyReceiptPage(Model model) {

		model.addAttribute("arhed", getDefaultArhed());
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.ACCOUNT_PAYMENT.getCode(),TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtype(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode()));
		
		return "pages/purchasing/supplierpayment/supplierpayment";
	}

	@GetMapping("/{xvoucher}")
	public String loadMoneyReceiptPage(@PathVariable String xvoucher, Model model) {
		Arhed data = arhedService.findArhedByXvoucher(xvoucher);
		if(data == null) data = getDefaultArhed();

		data.setXtypetrn(TransactionCodeType.ACCOUNT_PAYMENT.getCode());
		model.addAttribute("arhed", data);
		model.addAttribute("arhedprefix", xtrnService.findByXtypetrn(TransactionCodeType.ACCOUNT_PAYMENT.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("allArhed", arhedService.getAllArhedByXtype(TransactionCodeType.ACCOUNT_PAYMENT.getdefaultCode()));

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

		return "pages/purchasing/supplierpayment/supplierpayment";
	}

	private Arhed getDefaultArhed() {
		Arhed arhed = new Arhed();

		arhed.setXtrntype(TransactionCodeType.ACCOUNT_PAYMENT.getCode());
		arhed.setXdate(new Date());
		arhed.setXprime(BigDecimal.ZERO);
		arhed.setXstatus("Open");
		return arhed;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Arhed arhed, BindingResult bindingResult){
		// Validate
		if(StringUtils.isBlank(arhed.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
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
		arhed.setXsign(1);
		arhed.setXtypetrn("Purchase");
		arhed.setXpaymentterm("Cash");
		//arhed.setXwh("01");
		arhed.setXstatusbnk("Open");
		arhed.setXbase(arhed.getXprime());
		arhed.setXstatusjv("Open");

		// if existing record
		if(StringUtils.isNotBlank(arhed.getXvoucher())) {
			Arhed existArhed = arhedService.findArhedByXvoucher(arhed.getXvoucher());
			BeanUtils.copyProperties(arhed, existArhed, "xvoucher", "xtype");
			long count = arhedService.update(existArhed);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Supplier Payment updated successfully");
			responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + arhed.getXvoucher());
			return responseHelper.getResponse();
		}

		// If new
		long count = arhedService.save(arhed);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Supplier Payment created successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + arhed.getXvoucher());
		return responseHelper.getResponse();
	}

	
	@PostMapping("/delete/{xvoucher}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xvoucher, Model model){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Supplier Payment not found in this system");
			return responseHelper.getResponse();
		}

		long count = arhedService.deleteVoucher(xvoucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Supplier Payment voucher");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Supplier Payment deleted successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment");
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xvoucher}")
	public @ResponseBody Map<String, Object> confirmMoneyReceipt(@PathVariable String xvoucher){
		Arhed voucher = arhedService.findArhedByXvoucher(xvoucher);
		if(voucher == null) {
			responseHelper.setErrorStatusAndMessage("Supplier Payment not found in this system");
			return responseHelper.getResponse();
		}

		voucher.setXstatus("Confirmed");
		long count = arhedService.update(voucher);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm Supplier Payment");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Supplier Payment confirmed successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + voucher.getXvoucher());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/transfertogl/{xvoucher}")
	public @ResponseBody Map<String, Object> transferToGL(@PathVariable String xvoucher){
		
		Arhed arhed = arhedService.findArhedByXvoucher(xvoucher);
		if(arhed == null) {
			responseHelper.setErrorStatusAndMessage("Payment is not found in this system");
			return responseHelper.getResponse();
		}
		
		// Validate
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
			arhedService.AP_transferAPtoGL(xvoucher, p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Transferred to GL successfully");
		responseHelper.setRedirectUrl("/purchasing/supplierpayment/" + xvoucher);
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
			message = "Can't find supplier payment : " + xvoucher;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		SupplierPaymentReport report = new SupplierPaymentReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setReportName("Payment Receipt");
		report.setFromDate(sdf.format(data.getXdate()));
		
		report.setXvoucher(data.getXvoucher());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXpaymenttype(data.getXpaymentterm());
		report.setXref(data.getXref());
		report.setXprime(data.getXprime());
		report.setSpellword(data.getSpellword());
		report.setXprimeword(data.getXprimeword());
		report.setXcus(data.getXcus());
		report.setXorg(data.getXorg());
		report.setXmadd(data.getCustomeraddress());
		
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		
		byte[] byt = getPDFByte(report, "supplierpayment.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for supplier payment : " + xvoucher;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

}
