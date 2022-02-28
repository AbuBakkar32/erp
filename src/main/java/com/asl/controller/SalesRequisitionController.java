package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.asl.entity.Caitem;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opreqheader;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesRequsitionReport;
import com.asl.service.OpreqService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XtrnService;


import com.asl.service.CaitemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/salesninvoice/opreq")
public class SalesRequisitionController extends ASLAbstractController {

	@Autowired private OpreqService opreqService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProjectStoreViewService projectstoreviewService;


	@GetMapping
	public String loadSalesOrderPage(Model model) {
		model.addAttribute("opreqheader", getDefaultOpordHeader(model));
		model.addAttribute("allOpenOpreqHeader", opreqService.getAllStatusOpenOpreqheader());
		model.addAttribute("opreqprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode(),Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());
		
		return "pages/salesninvoice/reqorder/reqorder";
	}
	
	@GetMapping("/reqorderlist")
	public String loadreqorderlistPage(Model model) {
		model.addAttribute("allOpreqHeader", opreqService.getAllOpreqheader());
		return "pages/salesninvoice/reqorder/reqorderlist";
	}

	@GetMapping("/{xdoreqnum}")
	public String loadSalesOrderPage(@PathVariable String xdoreqnum, Model model) {
		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if (data == null) return "redirect:/salesninvoice/opreq";

		model.addAttribute("opreqheader", data);
		model.addAttribute("allOpenOpreqHeader", opreqService.getAllStatusOpenOpreqheader());
		model.addAttribute("opreqDetailsList", opreqService.findOpreqDetailByXdoreqnum(xdoreqnum));
		model.addAttribute("AllSOCreatedList", opreqService.getAllSOCreated(xdoreqnum));
		model.addAttribute("opreqprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode(),Boolean.TRUE));

		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		List<Opreqdetail> opreqDetails = opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(opreqDetails != null && !opreqDetails.isEmpty()) {
			for(Opreqdetail pd : opreqDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		
		return "pages/salesninvoice/reqorder/reqorder";
	}

	private Opreqheader getDefaultOpordHeader(Model model) {
		Opreqheader opreqheader = new Opreqheader();
		model.addAttribute("AllSOCreatedList", opreqService.getAllSOCreated(opreqheader.getXdoreqnum()));
		opreqheader.setXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode());
		opreqheader.setXtrn(TransactionCodeType.SALES_REQUESTION_ORDER.getdefaultCode());
		opreqheader.setXstatus("Open");
		opreqheader.setXamount(BigDecimal.ZERO);

		return opreqheader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opreqheader opreqHeader, BindingResult bindingResult) {
		if (opreqHeader == null || StringUtils.isBlank(opreqHeader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if (StringUtils.isBlank(opreqHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please select a customer.");
			return responseHelper.getResponse();
		}
		
		if (StringUtils.isBlank(opreqHeader.getXhwh())) {
			responseHelper.setErrorStatusAndMessage("Project/Business required.");
			return responseHelper.getResponse();
		}
		
		if (StringUtils.isBlank(opreqHeader.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Store/Site required.");
			return responseHelper.getResponse();
		}

		// if existing record
		Opreqheader existOpreqHeader = opreqService.findOpreqHeaderByXdoreqnum(opreqHeader.getXdoreqnum());
		if (existOpreqHeader != null) {
			BeanUtils.copyProperties(opreqHeader, existOpreqHeader, "xdoreqnum", "xtypetrn", "xtrn");
			long count = opreqService.updateOpreqheader(existOpreqHeader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Sales Requisition order updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/opreq/" + opreqHeader.getXdoreqnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = opreqService.saveOpreqheader(opreqHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Sales Requisition order created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opreq/" + opreqHeader.getXdoreqnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xdoreqnum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xdoreqnum){
		Opreqheader opreqheader = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if(opreqheader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find sales requisition : " + xdoreqnum);
			return responseHelper.getResponse();
		}

		long count = opreqService.deleteOpreqheader(xdoreqnum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't find sales requisition : " + xdoreqnum);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Sales requisition order deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opreq/");
		return responseHelper.getResponse();
	}

	
	@GetMapping("/{xdoreqnum}/opreqdetail/{xrow}/show")
	public String openOpordDetailModal(@PathVariable String xdoreqnum, @PathVariable String xrow, Model model) {

		if ("new".equalsIgnoreCase(xrow)) {
			Opreqdetail opreqdetails = new Opreqdetail();
			opreqdetails.setXdoreqnum(xdoreqnum);
			opreqdetails.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			opreqdetails.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			model.addAttribute("opreqdetail", opreqdetails);
		} else {
			Opreqdetail opreqdetails = opreqService.findOpreqdetailByXordernumAndXrow(xdoreqnum, Integer.parseInt(xrow));
			if (opreqdetails == null) {
				opreqdetails = new Opreqdetail();
				opreqdetails.setXdoreqnum(xdoreqnum);
				opreqdetails.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				opreqdetails.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("opreqdetail", opreqdetails);
		}
		return "pages/salesninvoice/reqorder/opreqdetailmodal::opreqdetailmodal";
	}
	
	@PostMapping("/opreqdetail/save")
	public @ResponseBody Map<String, Object> saveOporddetail(Opreqdetail opreqdetail, BindingResult bindingResult) {
		if (opreqdetail == null || StringUtils.isBlank(opreqdetail.getXdoreqnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(opreqdetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item not selected! Please select an item");
			return responseHelper.getResponse();
		}

		Caitem caitem = caitemService.findByXitem(opreqdetail.getXitem());
		if(caitem == null) {
			responseHelper.setErrorStatusAndMessage("Item not found in the system");
			return responseHelper.getResponse();
		}

		if(opreqdetail.getXqtyord() == null || opreqdetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Item quantity can't be less then zero");
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if (opreqdetail.getXrow() == 0 && opreqService.findOpreqdetailByXdoreqnumAndXitem(opreqdetail.getXdoreqnum(), opreqdetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		// modify line amount
		opreqdetail.setXitemdesc(caitem.getXdesc());
		opreqdetail.setXcatitem(caitem.getXcatitem());
		opreqdetail.setXgitem(caitem.getXgitem());
		
		opreqdetail.setXlineamt(opreqdetail.getXqtyord().multiply(opreqdetail.getXrate()).setScale(2, RoundingMode.DOWN));

		// if existing
		Opreqdetail existDetail = opreqService.findOpreqdetailByXordernumAndXrow(opreqdetail.getXdoreqnum(), opreqdetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(opreqdetail, existDetail, "xdoreqnum", "xrow");
			long count = opreqService.updateOpreqdetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("opreqdetailtable", "/salesninvoice/opreq/opreqdetail/" + opreqdetail.getXdoreqnum());
			responseHelper.setSecondReloadSectionIdWithUrl("opreqheaderform", "/salesninvoice/opreq/opreqheaderform/" + opreqdetail.getXdoreqnum());
			responseHelper.setSuccessStatusAndMessage("Sales Order item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opreqService.saveOpreqdetail(opreqdetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("opreqdetailtable", "/salesninvoice/opreq/opreqdetail/" + opreqdetail.getXdoreqnum());
		responseHelper.setSecondReloadSectionIdWithUrl("opreqheaderform", "/salesninvoice/opreq/opreqheaderform/" + opreqdetail.getXdoreqnum());
		responseHelper.setSuccessStatusAndMessage("Sales Order item saved successfully");
		return responseHelper.getResponse();
	}
	
	 
	@GetMapping("/opreqdetail/{xdoreqnum}")
	public String reloadOpordDetailTable(@PathVariable String xdoreqnum, Model model) {
		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if (data == null) return "redirect:/salesninvoice/opreq";
		List<Opreqdetail> opreqDetails = opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		model.addAttribute("opreqheader", data);
		model.addAttribute("opreqDetailsList", opreqDetails);
		model.addAttribute("AllSOCreatedList", opreqService.getAllSOCreated(xdoreqnum));
		
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(opreqDetails != null && !opreqDetails.isEmpty()) {
			for(Opreqdetail pd : opreqDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		return "pages/salesninvoice/reqorder/reqorder::opreqdetailtable";
	}

	@GetMapping("/opreqheaderform/{xdoreqnum}")
	public String reloadOpdoheaderform(@PathVariable String xdoreqnum, Model model) {
		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		
		if (data == null) return "redirect:/salesninvoice/opord";
		model.addAttribute("opreqheader", data);
		model.addAttribute("opreqprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode(), Boolean.TRUE));;
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		return "pages/salesninvoice/reqorder/reqorder::opreqheaderform";
	}
	
	@PostMapping("{xdoreqnum}/opreqdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpreqDetail(@PathVariable String xdoreqnum, @PathVariable String xrow, Model model) {
		Opreqdetail pd = opreqService.findOpreqdetailByXordernumAndXrow(xdoreqnum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setErrorStatusAndMessage("Detail item can't found to do delete");
			return responseHelper.getResponse();
		}

		long count = opreqService.deleteOpreqdetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("opreqdetailtable", "/salesninvoice/opreq/opreqdetail/" + xdoreqnum);
		responseHelper.setSecondReloadSectionIdWithUrl("opreqheaderform", "/salesninvoice/opreq/opreqheaderform/" + xdoreqnum);
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xdoreqnum}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xdoreqnum){

		Opreqheader oh = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if(oh == null) {
			responseHelper.setErrorStatusAndMessage("Sales Requisition order " + xdoreqnum + " not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(oh.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Sales Requisition order " + xdoreqnum + " is not Open");
			return responseHelper.getResponse();
		}

		// check sales order has details
		List<Opreqdetail> details = opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Sales Requisition order details is empty");
			return responseHelper.getResponse();
		}

		oh.setXstatus("Confirmed");
		oh.setXstatusreq("Open");
		long count = opreqService.updateOpreqheader(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Confirmed sales Requisition order " + xdoreqnum);
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/opreq/" + xdoreqnum);
		responseHelper.setSuccessStatusAndMessage("Order Confirmed successfully");
		return responseHelper.getResponse();
	}
	
	@PostMapping("/createso/{xdoreqnum}")
	public @ResponseBody Map<String, Object> soconfirm(@PathVariable String xdoreqnum){

		Opreqheader oh = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if(oh == null) {
			responseHelper.setErrorStatusAndMessage("Sales Requisition order " + xdoreqnum + " not found");
			return responseHelper.getResponse();
		}

		if(!"Confirmed".equalsIgnoreCase(oh.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Sales Requisition order " + xdoreqnum + " is not Open");
			return responseHelper.getResponse();
		}
		
		if("SO Created".equalsIgnoreCase(oh.getXstatusreq())) {
			responseHelper.setErrorStatusAndMessage("Sales Order already created.");
			return responseHelper.getResponse();
		}

		// check sales order has details
		List<Opreqdetail> details = opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Sales Requisition order details is empty");
			return responseHelper.getResponse();
		}

		String p_seq;
		if(!"SO Created".equalsIgnoreCase(oh.getXstatusreq())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opreqService.procCreateSOFromSRQ(xdoreqnum, p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setRedirectUrl("/salesninvoice/opreq/" + xdoreqnum);
		responseHelper.setSuccessStatusAndMessage("Sales Requisition Order Create Successfully");
		return responseHelper.getResponse();
	}


	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		Caitem centralCaitem = caitemService.findByXitem(xitem);
		return centralCaitem;
	}
	
	@GetMapping("/print/{xdoreqnum}")
	public ResponseEntity<byte[]> printSalesReqWithDetails(@PathVariable String xdoreqnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		SalesRequsitionReport report=new SalesRequsitionReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Sales Requsition");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
		report.setXdoreqnum(data.getXdoreqnum());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXcus(data.getXcus());
		report.setXmadd(data.getCustomeraddress());
		report.setXorg(data.getXorg());
		report.setXhwh(data.getXhwh());
		report.setProjectName(data.getProjectName());
		report.setXwh(data.getXwh());
		report.setStoreName(data.getStoreName());
		report.setXref(data.getXref());
		report.setXstatus(data.getXstatus());
		report.setXamount(data.getXamount());
		report.setXstatusreq(data.getXstatusreq());
		report.setXnote(data.getXnote());
		report.setSpellword(data.getSpellword());
		report.setXprimeword(data.getXprimeword());
		
		List<Opreqdetail> items = opreqService.findOpreqDetailByXdoreqnum(data.getXdoreqnum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXdesc());
				item.setItemQty(it.getXqtyord().toString());
				item.setRate(it.getXrate());
				item.setItemUnit(it.getXunit());
				item.setXunitsel(it.getXunitsel());
				item.setXcfsel(it.getXcfsel());
				item.setXnote(it.getXnote());
				item.setLineamt(it.getXlineamt());

				report.getItems().add(item);
			});
		}
		
		byte[] byt = getPDFByte(report, "salesreqreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for sales requisition : " + xdoreqnum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}
	
	@GetMapping("/allcodesbyproject/{xhwh}")
	public @ResponseBody List<ProjectStoreView> getProjectstoreview(@PathVariable String xhwh){
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(xhwh);
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		return list;
	}

}
