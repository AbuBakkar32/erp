package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.taskdefs.TempFile;
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
import com.asl.entity.Cacus;
import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.PurchaseReturnReport;
import com.asl.service.AcService;
import com.asl.service.CacusService;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/procurements/purchasereturn")
public class PurchaseReturnController extends ASLAbstractController {

	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private PocrnService pocrnService;
	@Autowired private PogrnService pogrnService;
	@Autowired private AcService acService;
	@Autowired private CacusService cacusService;

	@GetMapping
	public String loadGrnReturnPage(Model model) {
		model.addAttribute("pocrnheader", getDefaultPocrnHeader());
		model.addAttribute("allPocrnHeader", pocrnService.getAllPocrnheader());

		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_RETURN.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));

		return "pages/procurement/grnreturn/pocrn";
	}

	@GetMapping("/{xcrnnum}")
	public String loadGrnReturnPage(@PathVariable String xcrnnum, Model model) {
		Pocrnheader data = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum);
		if (data == null) return "redirect:/procurements/purchasereturn";

		model.addAttribute("pocrnheader", data);
		model.addAttribute("allPocrnHeader", pocrnService.getAllPocrnheader());
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("pocrnDetailsList", pocrnService.findPocrnDetailByXcrnnum(xcrnnum));
		
		List<Pocrndetail> details = pocrnService.findPocrnDetailByXcrnnum(xcrnnum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(details != null && !details.isEmpty()) {
			for(Pocrndetail pd : details) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);

		List<Acdetail> acdetails = acService.findAcdetailsByXvoucher(data.getXvoucher());
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

		return "pages/procurement/grnreturn/pocrn";
	}

	private Pocrnheader getDefaultPocrnHeader() {
		Pocrnheader pocrn = new Pocrnheader();
		pocrn.setXtypetrn(TransactionCodeType.PURCHASE_RETURN.getCode());
		pocrn.setXtype(TransactionCodeType.PURCHASE_RETURN.getdefaultCode());
		pocrn.setXstatuscrn("Open");
		pocrn.setXdate(new Date());
		pocrn.setXpaymenttype("Other");
		return pocrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pocrnheader pocrnHeader, BindingResult bindingResult) {

		// Validate
		if(StringUtils.isBlank(pocrnHeader.getXgrnnum())) {
			responseHelper.setErrorStatusAndMessage("GRN number selection required");
			return responseHelper.getResponse();
		}
		// Search supplier number now
		PogrnHeader pgh = pogrnService.findPogrnHeaderByXgrnnum(pocrnHeader.getXgrnnum());
		if(pgh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN number in this system");
			return responseHelper.getResponse();
		}
		if("Open".equalsIgnoreCase(pgh.getXstatusgrn())) {
			responseHelper.setErrorStatusAndMessage("GRN not confirmed");
			return responseHelper.getResponse();
		}
		pocrnHeader.setXcus(pgh.getXcus());
		if(StringUtils.isBlank(pocrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier not found from this GRN");
			return responseHelper.getResponse();
		}
		if(pocrnHeader.getXdate().before(pgh.getXdate())) {
			responseHelper.setErrorStatusAndMessage("Return date can't be before GRN date");
			return responseHelper.getResponse();
		}
		if(StringUtils.isNotBlank(pgh.getXtype())) pocrnHeader.setXpaymenttype(pgh.getXtype());
		pocrnHeader.setXhwh(pgh.getXhwh());
		pocrnHeader.setXwh(pgh.getXwh());
		pocrnHeader.setXtotamt(pgh.getXtotamt());

		// HIDDEN DATA
		pocrnHeader.setXstatusap("Open");
		pocrnHeader.setXstatusjv("Open");

		// if existing record
		if (StringUtils.isNotBlank(pocrnHeader.getXcrnnum())) {
			Pocrnheader exist = pocrnService.findPocrnHeaderByXcrnnum(pocrnHeader.getXcrnnum());
			BeanUtils.copyProperties(pocrnHeader, exist, "xcrnnum", "xgrnnum", "xtype", "xcus");
			long count = pocrnService.update(exist);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update purchase return");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Purchase return updated successfully");
			responseHelper.setRedirectUrl("/procurements/purchasereturn/" + exist.getXcrnnum());
			return responseHelper.getResponse();
		}

		// if new record
		return pocrnService.save(responseHelper, pocrnHeader, pgh);
	}

	@PostMapping("/archive/{xcrnnum}")
	public  @ResponseBody Map<String, Object> archive(@PathVariable String xcrnnum) {
		Pocrnheader pch = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum);
		if(pch == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Purchase return");
			return responseHelper.getResponse();
		}

		List<Pocrndetail> crndetails = pocrnService.findPocrnDetailByXcrnnum(xcrnnum);
		if(crndetails != null && !crndetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete purchase return details first");
			return responseHelper.getResponse();
		}

		long hcount = pocrnService.deletePocrnHeader(xcrnnum);
		if(hcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Delete Purchase Return");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase Return Deleted Successfully");
		responseHelper.setRedirectUrl("/procurements/purchasereturn");
		return responseHelper.getResponse();
	}

	@GetMapping("{xcrnnum}/pocrndetail/{xrow}/show")
	public String openPocrnDetailModal(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {

		if ("new".equalsIgnoreCase(xrow)) {
			Pocrndetail pocrndetail = new Pocrndetail();
			pocrndetail.setXcrnnum(xcrnnum);
			pocrndetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pocrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pocrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			// pocrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("pocrndetail", pocrndetail);
		} else {
			Pocrndetail pocrndetail = pocrnService.findPocrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
			pocrndetail.setPrevqty(pocrndetail.getXqtyord() == null ? BigDecimal.ZERO : pocrndetail.getXqtyord());
			model.addAttribute("pocrndetail", pocrndetail);
		}

		return "pages/procurement/grnreturn/pocrndetailmodal::pocrndetailmodal";
	}

	@PostMapping("/pocrndetail/save")
	public @ResponseBody Map<String, Object> savePocrndetail(Pocrndetail pocrnDetail) {
		if (pocrnDetail == null || StringUtils.isBlank(pocrnDetail.getXcrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(pocrnDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item selection required");
			return responseHelper.getResponse();
		}

		if(pocrnDetail.getXqtyord() == null || BigDecimal.ZERO.equals(pocrnDetail.getXqtyord()) || pocrnDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Return quantity should be greater then 0");
			return responseHelper.getResponse();
		}

		// validate return qty
		Pocrnheader pch = pocrnService.findPocrnHeaderByXcrnnum(pocrnDetail.getXcrnnum());
		if(pch == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Purchase return");
			return responseHelper.getResponse();
		}
		PogrnDetail pogrndetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pch.getXgrnnum(), pocrnDetail.getXdocrow());
		if(pogrndetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Puchase order detail for this item row");
			return responseHelper.getResponse();
		}
		// calculate and update poordetail xqtygrn 
		if(pocrnDetail.getXrow() != 0) {
			BigDecimal diff1 = pocrnDetail.getPrevqty().subtract(pocrnDetail.getXqtyord());
			BigDecimal diff2 = pogrndetail.getXqtyprn().subtract(diff1);
			if(diff2.compareTo(pogrndetail.getXqtygrn()) == 1) {
				responseHelper.setErrorStatusAndMessage("Return quantity can't be greater then GRN quantity");
				return responseHelper.getResponse();
			}
			pogrndetail.setXqtyprn(diff2);
		} else {
			pogrndetail.setXqtyprn(pogrndetail.getXqtyprn().add(pocrnDetail.getXqtyord()));
			if(pogrndetail.getXqtyprn().compareTo(pogrndetail.getXqtygrn()) == 1) {
				responseHelper.setErrorStatusAndMessage("Purchase return quantity can't be greater then GRN quantity");
				return responseHelper.getResponse();
			}
		}

		long count2 = pogrnService.updateDetail(pogrndetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update purchase return qty to GRN detail");
			return responseHelper.getResponse();
		}

		pocrnDetail.setXitem(pocrnDetail.getXitem().split("\\|")[0]);
		pocrnDetail.setXlineamt(pocrnDetail.getXqtyord().multiply(pocrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));


		// if existing
		Pocrndetail existDetail = pocrnService.findPocrnDetailByXcrnnumAndXrow(pocrnDetail.getXcrnnum(),pocrnDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(pocrnDetail, existDetail, "xcrnnum", "xrow");
			long count = pocrnService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update return item");
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("pocrndetailtable", "/procurements/purchasereturn/pocrndetail/" + pocrnDetail.getXcrnnum());
			responseHelper.setSuccessStatusAndMessage("Return item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pocrnService.saveDetail(pocrnDetail);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save return item");
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("pocrndetailtable", "/procurements/purchasereturn/pocrndetail/" + pocrnDetail.getXcrnnum());
		responseHelper.setSuccessStatusAndMessage("Return item saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/pocrndetail/{xcrnnum}")
	public String reloadPocrnDetailTable(@PathVariable String xcrnnum, Model model) {
		List<Pocrndetail> details = pocrnService.findPocrnDetailByXcrnnum(xcrnnum);
		model.addAttribute("pocrnDetailsList", details);
		model.addAttribute("pocrnheader", pocrnService.findPocrnHeaderByXcrnnum(xcrnnum));
		
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(details != null && !details.isEmpty()) {
			for(Pocrndetail pd : details) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		return "pages/procurement/grnreturn/pocrn::pocrndetailtable";
	}

	@PostMapping("{xcrnnum}/pocrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePocrnDetail(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {
		Pocrndetail pd = pocrnService.findPocrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		Pocrnheader pch = pocrnService.findPocrnHeaderByXcrnnum(pd.getXcrnnum());
		if(pch == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Purchase return");
			return responseHelper.getResponse();
		}
		PogrnDetail pogrndetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pch.getXgrnnum(), pd.getXdocrow());
		if(pogrndetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Puchase order detail for this item row");
			return responseHelper.getResponse();
		}
		pogrndetail.setXqtyprn(pogrndetail.getXqtyprn().subtract(pd.getXqtyord()));
		long count2 = pogrnService.updateDetail(pogrndetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Return qty to GRN detail");
			return responseHelper.getResponse();
		}

		long count = pocrnService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("pocrndetailtable", "/procurements/purchasereturn/pocrndetail/" + pd.getXcrnnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/confirmcrn/{xcrnnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xcrnnum) {
		Pocrnheader pocrnHeader = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum);
		if(pocrnHeader == null) {
			responseHelper.setErrorStatusAndMessage("GRN not found in this system");
			return responseHelper.getResponse();
		}

		// validate
		if (StringUtils.isBlank(pocrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}
		if (!"Open".equalsIgnoreCase(pocrnHeader.getXstatuscrn())) {
			responseHelper.setErrorStatusAndMessage("Purchase Return already confirmed");
			return responseHelper.getResponse();
		}

		List<Pocrndetail> pocrndetailsList = pocrnService.findPocrnDetailByXcrnnum(xcrnnum);
		if(pocrndetailsList == null || pocrndetailsList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Purchase Return has no item details");
			return responseHelper.getResponse();
		}

		try {
			pocrnService.confirmCRN(pocrnHeader);
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}
 
		responseHelper.setSuccessStatusAndMessage("Purchase return confirmed successfully");
		responseHelper.setRedirectUrl("/procurements/purchasereturn/" + xcrnnum);
		return responseHelper.getResponse();
	}
	
	@GetMapping("/print/{xcrnnum}")
	public ResponseEntity<byte[]> printSalseOrderWithDetails(@PathVariable String xcrnnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Pocrnheader data = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum);
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
	
		PurchaseReturnReport report = new PurchaseReturnReport();
		Cacus cacus = cacusService.findByXcus(data.getXcus());
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Purchase Return");
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());

		report.setXcrnnum(data.getXcrnnum());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXgrnnum(data.getXgrnnum());
		report.setXcus(data.getXcus());
		report.setXorg(data.getXorg());
		report.setXhwh(data.getXhwh());
		report.setProjectName(data.getProjectName());
		report.setXwh(data.getXwh());
		report.setStoreName(data.getStoreName());
		report.setXvoucher(data.getXvoucher());
		report.setSpellword(data.getSpellword());
		report.setXnote(data.getXnote());
		report.setXmadd(cacus.getXmadd());
		
		report.setXpaymenttype(data.getXpaymenttype());
		report.setXstatusgrn(data.getXstatuscrn());
		report.setTotalQty(BigDecimal.ZERO);
		report.setTotalAmount(BigDecimal.ZERO);
		report.setXprimeword(data.getXprimeword());
		
		List<Pocrndetail> items = pocrnService.findPocrnDetailByXcrnnum(data.getXcrnnum());	
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXitemname());
				item.setItemQty(it.getXqtyord().toString());
				item.setItemTotalAmount(it.getXlineamt().toString());
				item.setRate(it.getXrate());
				item.setXunit(it.getXunit());
				item.setXcfpur(it.getXcfpur());
				item.setXlong(it.getXlong());
				
				item.setLineamt(it.getXlineamt());
				item.setItemQty(it.getXqtyord() != null ? it.getXqtyord().toString() : BigDecimal.ZERO.toString());
				item.setItemTotalAmount(it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());


				report.getItems().add(item);
				report.setTotalQty(report.getTotalQty().add(BigDecimal.valueOf(Double.valueOf(item.getItemQty()))));
				report.setTotalAmount(report.getTotalAmount().add(BigDecimal.valueOf(Double.valueOf(item.getItemTotalAmount()))));
				
			});
		}
		
		
		byte[] byt = getPDFByte(report, "purchasereturn.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for voucher : " + xcrnnum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}
}

