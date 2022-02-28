package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesReturnReport;
import com.asl.service.AcService;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.OpcrnService;
import com.asl.service.OpdoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/salesninvoice/salesreturn")
public class SalesReturnController extends ASLAbstractController {

	@Autowired private OpdoService opdoService;
	@Autowired private OpcrnService opcrnService;
	@Autowired private AcService acService;
	@Autowired private CacusService cacusService;
	@Autowired private CaitemService caitemService;

	@GetMapping
	public String loadGrnReturnPage(Model model) {
		model.addAttribute("opcrnheader", getDefaultOpcrnHeader());
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_RETURN.getCode(), Boolean.TRUE));
		model.addAttribute("allOpcrnHeader", opcrnService.getAllOpcrnheader());
		model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		return "pages/salesninvoice/salesreturn/opcrn";
	}

	@GetMapping("/{xcrnnum}")
	public String loadGrnReturnPage(@PathVariable String xcrnnum, Model model) {
		Opcrnheader data = opcrnService.findOpcrnHeaderByXcrnnum(xcrnnum);
		if (data == null) return "redirect:/salesninvoice/salesreturn";

		model.addAttribute("opcrnheader", data);
		model.addAttribute("allOpcrnHeader", opcrnService.getAllOpcrnheader());
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_RETURN.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("opcrnDetailsList", opcrnService.findOpcrnDetailByXcrnnum(xcrnnum));
		
		List<Opcrndetail> invoiceDetails = opcrnService.findOpcrnDetailByXcrnnum(xcrnnum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(invoiceDetails != null && !invoiceDetails.isEmpty()) {
			for(Opcrndetail pd : invoiceDetails) {
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

		return "pages/salesninvoice/salesreturn/opcrn";
	}

	private Opcrnheader getDefaultOpcrnHeader() {
		Opcrnheader opcrn = new Opcrnheader();

		opcrn.setXtypetrn(TransactionCodeType.SALES_RETURN.getCode());
		opcrn.setXtype(TransactionCodeType.SALES_RETURN.getdefaultCode());
		opcrn.setXdate(new Date());
		opcrn.setXwh("01");
		opcrn.setXpaymenttype("Other");
		opcrn.setXstatuscrn("Open");

		return opcrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opcrnheader opcrnHeader, BindingResult bindingResult) {

		// Validate
		if(StringUtils.isBlank(opcrnHeader.getXdornum())) {
			responseHelper.setErrorStatusAndMessage("Invoice number selection required");
			return responseHelper.getResponse();
		}
		
		// Search invoice number now
		Opdoheader odh = opdoService.findOpdoHeaderByXdornum(opcrnHeader.getXdornum());
		if(odh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Invoice number in this system");
			return responseHelper.getResponse();
		}
		if("Open".equalsIgnoreCase(odh.getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Invoice not confirmed");
			return responseHelper.getResponse();
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(odh.getXdate());
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		if(opcrnHeader.getXdate().before(cal1.getTime())) {
			responseHelper.setErrorStatusAndMessage("Return date can't be before invoice date");
			return responseHelper.getResponse();
		}
		opcrnHeader.setXcus(odh.getXcus());
		if(StringUtils.isBlank(opcrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer not found from this Invoice");
			return responseHelper.getResponse();
		}
		if (StringUtils.isBlank(odh.getXhwh())) {
			responseHelper.setErrorStatusAndMessage("Project/Business not found from this Invoice.");
			return responseHelper.getResponse();
		}
		
		if (StringUtils.isBlank(odh.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Store/Site not found from this Invoice.");
			return responseHelper.getResponse();
		}

		// HIDDEN DATA
		opcrnHeader.setXstatusar("Open");
		opcrnHeader.setXstatusjv("Open");
		opcrnHeader.setXordernum(opcrnHeader.getXdornum());
		opcrnHeader.setXhwh(odh.getXhwh());
		opcrnHeader.setXwh(odh.getXwh());
		opcrnHeader.setXpaymenttype(StringUtils.isBlank(odh.getXpaymenttype()) ? "Other" : odh.getXpaymenttype());

		// if existing record
		if (StringUtils.isNotBlank(opcrnHeader.getXcrnnum())) {
			Opcrnheader exist = opcrnService.findOpcrnHeaderByXcrnnum(opcrnHeader.getXcrnnum());
			BeanUtils.copyProperties(opcrnHeader, exist, "xcrnnum", "xdornum", "xtype", "xcus");
			long count = opcrnService.update(exist);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update sales return");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Sales return updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/salesreturn/" + exist.getXcrnnum());
			return responseHelper.getResponse();
		}

		// If new
		return opcrnService.save(responseHelper, opcrnHeader, odh);
	}

	@PostMapping("/archive/{xcrnnum}")
	public  @ResponseBody Map<String, Object> archive(@PathVariable String xcrnnum) {
		Opcrnheader pch = opcrnService.findOpcrnHeaderByXcrnnum(xcrnnum);
		if(pch == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Sales return");
			return responseHelper.getResponse();
		}

		List<Opcrndetail> crndetails = opcrnService.findOpcrnDetailByXcrnnum(xcrnnum);
		if(crndetails != null && !crndetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete sales return details first");
			return responseHelper.getResponse();
		}

		long hcount = opcrnService.deleteOpcrnHeader(pch);
		if(hcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Delete Sales Return");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Sales Return Deleted Successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesreturn");
		return responseHelper.getResponse();
	}

	@GetMapping("{xcrnnum}/opcrndetail/{xrow}/show")
	public String openOpcrnDetailModal(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {

		if ("new".equalsIgnoreCase(xrow)) {
			Opcrndetail opcrndetail = new Opcrndetail();
			opcrndetail.setXcrnnum(xcrnnum);
			opcrndetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			opcrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			// pocrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("opcrndetail", opcrndetail);
		} else {
			Opcrndetail opcrndetail = opcrnService.findOpcrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
			opcrndetail.setPrevqty(opcrndetail.getXqtyord() == null ? BigDecimal.ZERO : opcrndetail.getXqtyord());
			model.addAttribute("opcrndetail", opcrndetail);
		}

		return "pages/salesninvoice/salesreturn/opcrndetailmodal::opcrndetailmodal";
	}

	@PostMapping("/opcrndetail/save")
	public @ResponseBody Map<String, Object> saveOpcrndetail(Opcrndetail opcrnDetail) {
		if (opcrnDetail == null || StringUtils.isBlank(opcrnDetail.getXcrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		
		if(StringUtils.isBlank(opcrnDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item selection required");
			return responseHelper.getResponse();
		}

		if(opcrnDetail.getXqtyord() == null || BigDecimal.ZERO.equals(opcrnDetail.getXqtyord()) || opcrnDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Return quantity should be greater then 0");
			return responseHelper.getResponse();
		}

		// validate return qty
		Opcrnheader pch = opcrnService.findOpcrnHeaderByXcrnnum(opcrnDetail.getXcrnnum());
		if(pch == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Sales return");
			return responseHelper.getResponse();
		}
		
		Opdodetail opdoDetail = opdoService.findOpdoDetailByXdornumAndXrow(pch.getXdornum(), opcrnDetail.getXdocrow());
		if(opdoDetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Invoice detail for this item row");
			return responseHelper.getResponse();
		}

		// calculate and update poordetail xqtygrn 
		if(opcrnDetail.getXrow() != 0) {
			BigDecimal diff1 = opcrnDetail.getPrevqty().subtract(opcrnDetail.getXqtyord());
			BigDecimal diff2 = opdoDetail.getXqtycrn().subtract(diff1);
			if(diff2.compareTo(opdoDetail.getXqtyord()) == 1) {
				responseHelper.setErrorStatusAndMessage("Sales Return quantity can't be greater then sales quantity");
				return responseHelper.getResponse();
			}
			opdoDetail.setXqtycrn(diff2);
		} else {
			opdoDetail.setXqtycrn(opdoDetail.getXqtycrn().add(opcrnDetail.getXqtyord()));
			if(opdoDetail.getXqtycrn().compareTo(opdoDetail.getXqtyord()) == 1) {
				responseHelper.setErrorStatusAndMessage("Sales return quantity can't be greater then sales quantity");
				return responseHelper.getResponse();
			}
		}

		long count2 = opdoService.updateDetail(opdoDetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update sales return qty to sales detail");
			return responseHelper.getResponse();
		}

		opcrnDetail.setXitem(opcrnDetail.getXitem().split("\\|")[0]);
		opcrnDetail.setXlineamt(opcrnDetail.getXqtyord().multiply(opcrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		Opcrndetail existDetail = opcrnService.findOpcrnDetailByXcrnnumAndXrow(opcrnDetail.getXcrnnum(), opcrnDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(opcrnDetail, existDetail, "xcrnnum", "xrow");
			long count = opcrnService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update return item");
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("opcrndetailtable", "/salesninvoice/salesreturn/opcrndetail/" + opcrnDetail.getXcrnnum());
			responseHelper.setSecondReloadSectionIdWithUrl("opcrnheaderform", "/salesninvoice/salesreturn/opcrnheaderform/" + opcrnDetail.getXcrnnum());
			responseHelper.setSuccessStatusAndMessage("Return item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opcrnService.saveDetail(opcrnDetail);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save return item");
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("opcrndetailtable", "/salesninvoice/salesreturn/opcrndetail/" + opcrnDetail.getXcrnnum());
		responseHelper.setSuccessStatusAndMessage("Return item saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/opcrndetail/{xcrnnum}")
	public String reloadOpcrnDetailTable(@PathVariable String xcrnnum, Model model) {
		model.addAttribute("opcrnDetailsList", opcrnService.findOpcrnDetailByXcrnnum(xcrnnum));
		model.addAttribute("opcrnheader", opcrnService.findOpcrnHeaderByXcrnnum(xcrnnum));
		
		List<Opcrndetail> invoiceDetails = opcrnService.findOpcrnDetailByXcrnnum(xcrnnum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(invoiceDetails != null && !invoiceDetails.isEmpty()) {
			for(Opcrndetail pd : invoiceDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		return "pages/salesninvoice/salesreturn/opcrn::opcrndetailtable";
	}

	@PostMapping("{xcrnnum}/opcrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpcrnDetail(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {
		Opcrndetail pd = opcrnService.findOpcrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		Opcrnheader pch = opcrnService.findOpcrnHeaderByXcrnnum(pd.getXcrnnum());
		if(pch == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Sales return");
			return responseHelper.getResponse();
		}
		
		Opdodetail opdoDetail = opdoService.findOpdoDetailByXdornumAndXrow(pch.getXdornum(), pd.getXdocrow());
		if(opdoDetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Puchase order detail for this item row");
			return responseHelper.getResponse();
		}
		opdoDetail.setXqtycrn(opdoDetail.getXqtycrn().subtract(pd.getXqtyord()));
		long count2 = opdoService.updateDetail(opdoDetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Return qty to Sales detail");
			return responseHelper.getResponse();
		}

		long count = opcrnService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("opcrndetailtable", "/salesninvoice/salesreturn/opcrndetail/" + pd.getXcrnnum());
		responseHelper.setSecondReloadSectionIdWithUrl("opcrnheaderform", "/salesninvoice/salesreturn/opcrnheaderform/" + xcrnnum);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirmsrn/{xcrnnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xcrnnum) {
		// Get PocrnHeader record by Xcrnnum
		Opcrnheader opcrnHeader = opcrnService.findOpcrnHeaderByXcrnnum(xcrnnum);
		if(opcrnHeader == null) {
			responseHelper.setErrorStatusAndMessage("Sales return not found in this system");
			return responseHelper.getResponse();
		}

		// validate
		if (StringUtils.isBlank(opcrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}
		if (!"Open".equalsIgnoreCase(opcrnHeader.getXstatuscrn())) {
			responseHelper.setErrorStatusAndMessage("Sales Return already confirmed");
			return responseHelper.getResponse();
		}

		List<Opcrndetail> opcrnHeaderList = opcrnService.findOpcrnDetailByXcrnnum(xcrnnum);
		if(opcrnHeaderList == null || opcrnHeaderList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Sales Return has no item details");
			return responseHelper.getResponse();
		}

		try {
			opcrnService.confirmCRN(opcrnHeader);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Sales Return Confirmed successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesreturn/" + xcrnnum);
		return responseHelper.getResponse();
	}
	
	@GetMapping("/print/{xcrnnum}")
	public ResponseEntity<byte[]> printSalesReturnWithDetails(@PathVariable String xcrnnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Opcrnheader data = opcrnService.findOpcrnHeaderByXcrnnum(xcrnnum);
		Cacus cacus = cacusService.findByXcus(data.getXcus());
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		SalesReturnReport report=new SalesReturnReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Sales Return");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
		report.setXcrnnum(data.getXcrnnum());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXdornum(data.getXdornum());
		report.setXcus(data.getXcus());
		report.setXmadd(cacus.getXmadd());
		report.setXorg(data.getXorg());
		report.setXwh(data.getXwh());
		report.setXhwh(data.getXhwh());
		report.setProjectName(data.getProjectName());
		report.setStoreName(data.getStoreName());
		report.setXpaymenttype(data.getXpaymenttype());
		report.setXnote(data.getXnote());
		report.setXvoucher(data.getXvoucher());
		report.setXstatucgrn(data.getXstatuscrn());
		report.setXtotamt(data.getXtotamt() == null ? BigDecimal.ZERO:data.getXtotamt());
		report.setXvatamt(data.getXvatamt() == null ? BigDecimal.ZERO:data.getXvatamt());
		report.setXaitamt(data.getXaitamt() == null ? BigDecimal.ZERO:data.getXaitamt());
		report.setXdiscamt(data.getXdiscamt() == null ? BigDecimal.ZERO:data.getXdiscamt());
		report.setTotalQty(BigDecimal.ZERO);
		report.setTotalAmount(BigDecimal.ZERO);
		report.setSpellword(data.getSpellword());
		report.setXprimeword(data.getXprimeword());
		
		List<Opcrndetail> items = opcrnService.findOpcrnDetailByXcrnnum(data.getXcrnnum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXitemname());
				item.setItemQty(it.getXqtyord().toString());
				item.setItemTotalAmount(it.getXlineamt().toString());
				item.setRate(it.getXrate());
				item.setItemUnit(it.getXunit());
				item.setXvatamt(it.getXvatamt() == null ? BigDecimal.ZERO:it.getXvatamt());
				item.setXcfsel(it.getXcfsel());
				//item.setXlong(it.getXlong());
				item.setLineamt(it.getXlineamt());
				item.setItemQty(it.getXqtyord() != null ? it.getXqtyord().toString() : BigDecimal.ZERO.toString());
				item.setItemTotalAmount(it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());

				report.getItems().add(item);
				report.setTotalQty(report.getTotalQty().add(BigDecimal.valueOf(Double.valueOf(item.getItemQty()))));
				report.setTotalAmount(report.getTotalAmount().add(BigDecimal.valueOf(Double.valueOf(item.getItemTotalAmount()))));
				
			});
		}
		
		byte[] byt = getPDFByte(report, "salesreturnreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for sales return : " + xcrnnum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}

	@GetMapping("/opcrnheaderform/{xcrnnum}")
	public String loadReturnPage(@PathVariable String xcrnnum, Model model) {
		Opcrnheader data = opcrnService.findOpcrnHeaderByXcrnnum(xcrnnum);

		model.addAttribute("opcrnheader", data);
		model.addAttribute("allOpcrnHeader", opcrnService.getAllOpcrnheader());
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_RETURN.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("opcrnDetailsList", opcrnService.findOpcrnDetailByXcrnnum(xcrnnum));

		return "pages/salesninvoice/salesreturn/opcrn:: opcrnheaderform";
	}
	
	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
	
}
