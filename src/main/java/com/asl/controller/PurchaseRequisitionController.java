package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
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

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.DataList;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.LandInfo;
import com.asl.entity.Opdoheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.Poreqdetail;
import com.asl.entity.Poreqheader;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.PurchaseReqReport;
import com.asl.model.report.ServiceReqReport;
import com.asl.model.report.StoreRequisitionReport;
import com.asl.service.AcService;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.PoordService;
import com.asl.service.PoreqheaderService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/procurement/purchasereq")
public class PurchaseRequisitionController extends ASLAbstractController{
	@Autowired private PoreqheaderService poreqheaderService;
	@Autowired private XcodesService xcodeService;
	@Autowired private ImstockService imstockService;
	@Autowired private CaitemService caitemService;
	@Autowired private CacusService cacusService;
	@Autowired private PoordService poordService;
	@Autowired private ProjectStoreViewService projectstoreviewService;


	@GetMapping
	public String loadPurchaseRequisitionPage(Model model) {

		model.addAttribute("purchasereq", getDefaultPoreqheader());
		model.addAttribute("allPurchasereq",poreqheaderService.getPoreqheadersByXtypetrn((TransactionCodeType.PURCHASE_REQUISITION.getCode())));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_REQUISITION.getCode()));
		model.addAttribute("allcodes", Collections.emptyList());
		
		return "pages/procurement/purchasereq/purchasereq";
	}
	
	@GetMapping("/purchasereqlist")
	public String loadPoordListPage(Model model) {
		model.addAttribute("allPurchasereq",poreqheaderService.getPoreqheadersByXtypetrn((TransactionCodeType.PURCHASE_REQUISITION.getCode())));
		return "pages/procurement/purchasereq/purchasereqlist";
	}

	private Poreqheader getDefaultPoreqheader() {
		Poreqheader purchasereq = new Poreqheader();
		purchasereq.setXtypetrn(TransactionCodeType.PURCHASE_REQUISITION.getCode());
		purchasereq.setXtrn(TransactionCodeType.PURCHASE_REQUISITION.getdefaultCode());
		purchasereq.setXstatusreq("Open");
		purchasereq.setXtotamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		purchasereq.setXstatus("Open");
		return purchasereq;
	}

	@GetMapping("/{xporeqnum}")
	public String loadPoreqheaerPage(@PathVariable String xporeqnum, Model model) {
		Poreqheader data = poreqheaderService.findByPoreqheader(xporeqnum);
		if (data == null)
			return "redirect:/procurement/poreqheader";
		model.addAttribute("purchasereq", data);
		model.addAttribute("allPurchasereq",poreqheaderService.getPoreqheadersByXtypetrn((TransactionCodeType.PURCHASE_REQUISITION.getCode())));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_REQUISITION.getCode()));
		model.addAttribute("purchasereqdetailList", poreqheaderService.findPoreqdetailByXporeqnum(xporeqnum));
		model.addAttribute("polist", poordService.findPoordHeaderByXporeqnum(xporeqnum));

		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		List<Poreqdetail> details = poreqheaderService.findPoreqdetailByXporeqnum(xporeqnum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalNetAmount = BigDecimal.ZERO;
		if (details != null && !details.isEmpty()) {
			for (Poreqdetail pd : details) {
				totalQuantity = totalQuantity.add(pd.getXqtypur() == null ? BigDecimal.ZERO : pd.getXqtypur());
				totalNetAmount = totalNetAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalNetAmount", totalNetAmount);

		return "pages/procurement/purchasereq/purchasereq";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Poreqheader por, BindingResult bindingResult){
		if(por == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		por.setXstatus("Open");
		por.setXstatusreq("Open");
		// Validate
		if(StringUtils.isBlank(por.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}

		if (StringUtils.isBlank(por.getXhwh())) {
			responseHelper.setErrorStatusAndMessage("Project/Business required");
			return responseHelper.getResponse();
		}

		if (StringUtils.isBlank(por.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Site/Store required");
			return responseHelper.getResponse();
		}


		// If existing
		Poreqheader existPoreqheader = poreqheaderService.findByPoreqheader(por.getXporeqnum());
		if(existPoreqheader != null) {
			BeanUtils.copyProperties(por, existPoreqheader, "xtypetrn", "xtrn");
			long count = poreqheaderService.updatePoreqheader(existPoreqheader);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Purchase Requisition");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Purchase Requisition Updated Successfully");
			responseHelper.setRedirectUrl("/procurement/purchasereq/" + existPoreqheader.getXporeqnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = poreqheaderService.savePoreqheader(por);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Purchase Requisition");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase Requisition Created Successfully");
		responseHelper.setRedirectUrl("/procurement/purchasereq/" + por.getXporeqnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/delete/{xporeqnum}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xporeqnum){
		Poreqheader purchasereq = poreqheaderService.findByPoreqheader(xporeqnum);
		if(purchasereq == null) {
			responseHelper.setErrorStatusAndMessage("Can't delete Purchase Requisition");
			return responseHelper.getResponse();
		}

		List<Poreqdetail> details = poreqheaderService.findPoreqdetailByXporeqnum(xporeqnum);
		if(details != null && !details.isEmpty()) { 
			responseHelper.setErrorStatusAndMessage("Purchase requisition detail exist, please delete all Purchase requisition detail first."); 
			return responseHelper.getResponse(); 
		}

		responseHelper.setSuccessStatusAndMessage("Deleted Successfully");
		responseHelper.setRedirectUrl("/procurement/purchasereq");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm/{xporeqnum}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xporeqnum){
		Poreqheader poreqnumHeader = poreqheaderService.findByPoreqheader(xporeqnum);
		if(poreqnumHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		poreqnumHeader.setXsignatorydate1(null);

		// Validate
		if(("Confirmed".equalsIgnoreCase(poreqnumHeader.getXstatus())) & ("Approved".equalsIgnoreCase(poreqnumHeader.getXstatusreq()))) {
			responseHelper.setErrorStatusAndMessage("Purchase Requisition Order already confirmed");
			return responseHelper.getResponse();
		}

		List<Poreqdetail> details = poreqheaderService.findPoreqdetailByXporeqnum(xporeqnum);
		if(details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}

		if(poreqnumHeader.getXtotamt().compareTo(BigDecimal.ZERO) == 0.00){
			responseHelper.setErrorStatusAndMessage("Total Amount should not <0.01");
			return responseHelper.getResponse();
		}

		poreqnumHeader.setXstatus("Confirmed");
		poreqnumHeader.setXstatusreq("Approved");
		long count = poreqheaderService.updatePoreqheader(poreqnumHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm Purchase Requisition");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase Requisition Confirmed successfully");
		responseHelper.setRedirectUrl("/procurement/purchasereq/" + xporeqnum);
		return responseHelper.getResponse();
	}
	@PostMapping("/createpo/{xporeqnum}")
	public @ResponseBody Map<String, Object> createpo(@PathVariable String xporeqnum){
		Poreqheader poreqnumHeader = poreqheaderService.findByPoreqheader(xporeqnum);
		if(poreqnumHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if("PO Created".equalsIgnoreCase(poreqnumHeader.getXstatusreq())) {
			responseHelper.setErrorStatusAndMessage("Purchase Order already created.");
			return responseHelper.getResponse();
		}

		String p_seq;
		if(!"PO Created".equalsIgnoreCase(poreqnumHeader.getXstatusreq())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			poreqheaderService.procSP_CREATEPO_FROMPRQ(xporeqnum, "Purchase Requisition", p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Purchase Order created successfully");
		responseHelper.setRedirectUrl("/procurement/purchasereq/" + xporeqnum);
		return responseHelper.getResponse();
	}

	@GetMapping("{xporeqnum}/purchasereqdetail/{xrow}/show")
	public String openPoreqdetailModal(@PathVariable String xporeqnum, @PathVariable String xrow, Model model) {
		Poreqheader poreqheader = poreqheaderService.findByPoreqheader(xporeqnum);
		if(poreqheader == null) return "redirect:/procurement/purchasereq";

		if("new".equalsIgnoreCase(xrow)) {
			Poreqdetail poreqdetail = new Poreqdetail();
			poreqdetail.setXporeqnum(xporeqnum);
			poreqdetail.setXqtypur(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			poreqdetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			poreqdetail.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("purchasereqdetail", poreqdetail);
			model.addAttribute("availablestock", Collections.emptyList());
			model.addAttribute("purpose", xcodeService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
		} else {
			Poreqdetail poreqdetail = poreqheaderService.findPoreqdetailByXporeqnumAndXrow(xporeqnum, Integer.parseInt(xrow));
			if(poreqdetail == null) {
				poreqdetail = new Poreqdetail();
				poreqdetail.setXporeqnum(xporeqnum);
				poreqdetail.setXqtypur(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				poreqdetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				poreqdetail.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("purchasereqdetail", poreqdetail);
			model.addAttribute("availablestock", imstockService.findByXitem(poreqdetail.getXitem()));
			model.addAttribute("purpose", xcodeService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
		}
		return "pages/procurement/purchasereq/purchasereqdetailmodal::purchasereqdetailmodal";
	}

	@PostMapping("/purchasereqdetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(Poreqdetail poreqdetail){
		if(poreqdetail == null || StringUtils.isBlank(poreqdetail.getXporeqnum()) || StringUtils.isBlank(poreqdetail.getXitem())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(poreqdetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item");
			return responseHelper.getResponse();
		}

		if(poreqdetail.getXrate().compareTo(BigDecimal.ZERO) == 0.00 ) {
			responseHelper.setErrorStatusAndMessage("Unit Price should not <0.01");
			return responseHelper.getResponse();
		}

		if(poreqdetail.getXqtypur().compareTo(BigDecimal.ZERO) == 0.00){
			responseHelper.setErrorStatusAndMessage("Quantity should not <0.01");
			return responseHelper.getResponse();
		}
		
		

		// Check item already exist in detail list
		if(poreqdetail.getXrow() == 0 && poreqheaderService.findPoreqdetailByXporeqnumAndXitem(poreqdetail.getXporeqnum(), poreqdetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		// if existing
		Poreqdetail existDetail = poreqheaderService.findPoreqdetailByXporeqnumAndXrow(poreqdetail.getXporeqnum(), poreqdetail.getXrow());
		if(existDetail != null) {
			
			BeanUtils.copyProperties(poreqdetail, existDetail, "xporeqnum", "xrow");
			long count = poreqheaderService.updatePoreqdetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("purchasereqdetailtable", "/procurement/purchasereq/purchasereqdetail/" + poreqdetail.getXporeqnum());
			responseHelper.setSecondReloadSectionIdWithUrl("purchasereqform", "/procurement/purchasereq/purchasereqform/" + poreqdetail.getXporeqnum());
			responseHelper.setThirdReloadSectionIdWithUrl("purchasereqtable", "/procurement/purchasereq/purchasereqtable");
			responseHelper.setSuccessStatusAndMessage("Purchase Requisition detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = poreqheaderService.savePoreqdetail(poreqdetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("purchasereqdetailtable", "/procurement/purchasereq/purchasereqdetail/" + poreqdetail.getXporeqnum());
		responseHelper.setSecondReloadSectionIdWithUrl("purchasereqform", "/procurement/purchasereq/purchasereqform/" + poreqdetail.getXporeqnum());
		responseHelper.setThirdReloadSectionIdWithUrl("purchasereqtable", "/procurement/purchasereq/purchasereqtable");
		responseHelper.setSuccessStatusAndMessage("Purchase Requisition detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/purchasereqdetail/{xporeqnum}")
	public String reloadPoreqdetailTabble(@PathVariable String xporeqnum, Model model) {
		List<Poreqdetail> poreqdetails = poreqheaderService.findPoreqdetailByXporeqnum(xporeqnum);
		model.addAttribute("purchasereqdetailList", poreqdetails);
		model.addAttribute("purchasereq", poreqheaderService.findByPoreqheader(xporeqnum));

		List<Poreqdetail> details = poreqheaderService.findPoreqdetailByXporeqnum(xporeqnum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalNetAmount = BigDecimal.ZERO;
		if (details != null && !details.isEmpty()) {
			for (Poreqdetail pd : details) {
				totalQuantity = totalQuantity.add(pd.getXqtypur() == null ? BigDecimal.ZERO : pd.getXqtypur());
				totalNetAmount = totalNetAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalNetAmount", totalNetAmount);
		
		return "pages/procurement/purchasereq/purchasereq::purchasereqdetailtable";
	}

	@PostMapping("{xporeqnum}/purchasereqdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteporeqdetail(@PathVariable String xporeqnum, @PathVariable String xrow, Model model) {
		Poreqdetail detail = poreqheaderService.findPoreqdetailByXporeqnumAndXrow(xporeqnum, Integer.parseInt(xrow));
		if(detail == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = poreqheaderService.deletePoreqdetail(detail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("purchasereqdetailtable", "/procurement/purchasereq/purchasereqdetail/" + xporeqnum);
		responseHelper.setSecondReloadSectionIdWithUrl("purchasereqform", "/procurement/purchasereq/purchasereqform/" + xporeqnum);
		responseHelper.setThirdReloadSectionIdWithUrl("purchasereqtable", "/procurement/purchasereq/purchasereqtable");
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}

	@GetMapping("/purchasereqform/{xporeqnum}")
	public String loadPoreqheaderPage(@PathVariable String xporeqnum, Model model) {

		Poreqheader data = poreqheaderService.findByPoreqheader(xporeqnum);
		if (data == null) data = getDefaultPoreqheader();
		if (data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));

		model.addAttribute("purchasereq", data);
		model.addAttribute("allPurchasereq",poreqheaderService.getPoreqheadersByXtypetrn((TransactionCodeType.PURCHASE_REQUISITION.getCode())));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_REQUISITION.getCode()));
		model.addAttribute("purchasereqdetailList", poreqheaderService.findPoreqdetailByXporeqnum(xporeqnum));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

		return "pages/procurement/purchasereq/purchasereq:: purchasereqform";
	}

	@GetMapping("/purchasereqtable")
	public String reloadPoreqheaderHeaderTable(Model model) {
		model.addAttribute("allPurchasereq",poreqheaderService.getPoreqheadersByXtypetrn((TransactionCodeType.PURCHASE_REQUISITION.getCode())));
		return "pages/procurement/purchasereq/purchasereq::purchasereqtable";
	}

	@GetMapping("/print/{xporeqnum}")
	public ResponseEntity<byte[]> printPoreqnumHeaderWithDetails(@PathVariable String xporeqnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Poreqheader data = poreqheaderService.findByPoreqheader(xporeqnum);
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy, HH:mm:ss");
		SimpleDateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy");
		Cacus cacus = cacusService.findByXcus(data.getXcus());
		PurchaseReqReport report=new PurchaseReqReport();

		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Purchase Requisition");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		report.setXporeqnum(data.getXporeqnum());
		report.setXdate(df.format(data.getXdate()));
		report.setXcus(data.getXcus());
		report.setXorg(data.getXorg());
		report.setXhwh(data.getXhwh());
		report.setProjectName(data.getProjectName());
		report.setCusName(cacus.getXorg());
		report.setXwh(data.getXwh());
		report.setStoreName(data.getStoreName());
		report.setXquotnum(data.getXquotnum());
		report.setXref(data.getXref());
		report.setXpreparer(data.getXpreparer());
		report.setXpreparername(data.getXpreparername());
		report.setXnote(data.getXnote());
		report.setXsignatory1(data.getXsignatory1());
		report.setSignatoryName(data.getXsignatoryname());
		report.setXsignatorynote1(data.getXsignatorynote1());
		report.setXstatusreq(data.getXstatusreq());
		report.setXstatus(data.getXstatus());
		report.setXtotamt(data.getXtotamt());
		report.setTotalAmount(data.getXtotamt() != null ? data.getXtotamt().setScale(2, RoundingMode.FLOOR).toString() : BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR).toString().toString());
		report.setSpellword(data.getSpellword());
		report.setXprimeword(data.getXprimeword());
		
		if(data.getXprepdate()==null) {
			report.setXprepdate("");
		}
		else if(data.getXprepdate()!=null) {
			report.setXprepdate(sdf.format(data.getXprepdate()));
		}

		if(data.getXsignatorydate1()==null) {
			report.setXsignatorydate1("");
		}
		if(data.getXsignatorydate1()!=null)
		{
			report.setXsignatorydate1(sdf.format(data.getXsignatorydate1()));
		}

		List<Poreqdetail> items = poreqheaderService.findPoreqdetailByXporeqnum(data.getXporeqnum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXitemdesc());
				item.setItemQty(it.getXqtypur().toString());
				item.setRate(it.getXrate());
				item.setItemUnit(it.getXunitpur());
				item.setXcfpur(it.getXcfpur());
				item.setXnote(it.getXnote());
				item.setItemQty(it.getXqtypur() != null ? it.getXqtypur().toString() : BigDecimal.ZERO.toString());
				item.setLineamt(it.getXlineamt());
				item.setPurpose(it.getXpurpose());
				report.getItems().add(item);
			});
		}
				

		byte[] byt = getPDFByte(report, "purchasereqreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for this Purchase Requisition: " + xporeqnum;
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