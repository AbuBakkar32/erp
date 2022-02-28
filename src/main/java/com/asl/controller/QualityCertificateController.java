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

import com.asl.entity.Acdetail;
import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.ProjectStoreView;
import com.asl.entity.Termstrn;
import com.asl.enums.CodeType;
import com.asl.enums.Modules;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.QCReport;
import com.asl.model.report.WorkOrderReport;
import com.asl.service.AcService;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.PoordService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/procurement/qcheader")
public class QualityCertificateController extends ASLAbstractController{
	

	@Autowired private PogrnService pogrnService;
	@Autowired private PocrnService pocrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CacusService cacusService;
	@Autowired private PoordService poordService;
	@Autowired private AcService acService;
	@Autowired private ImstockService imstockService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadQCPage(Model model) {

		model.addAttribute("qcheader", getDefaultPogrnHeader());
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.QC_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allQcHeader", pogrnService.getAllQCPogrnHeader());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());

		return "pages/procurement/qcheader/qcheader";
	}

	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (data == null) data = getDefaultPogrnHeader();
		if (data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO);

		model.addAttribute("qcheader", data);
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.QC_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allQcHeader", pogrnService.getAllQCPogrnHeader());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("qcheaderdetailList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));

		List<Termstrn> poorddetails = poordService.findTermstrnByXdocnum(data.getXpornum());
		model.addAttribute("allTermsdef", poorddetails);

		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

		List<PogrnDetail> details = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		BigDecimal totalQuantityReceived = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		BigDecimal totalVatAmount = BigDecimal.ZERO;
		if(details != null && !details.isEmpty()) {
			for(PogrnDetail pd : details) {
				totalQuantityReceived = totalQuantityReceived.add(pd.getXqtypur() == null ? BigDecimal.ZERO : pd.getXqtypur());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
				totalVatAmount = totalVatAmount.add(pd.getXvatamt() == null ? BigDecimal.ZERO : pd.getXvatamt());
			}
		}
		model.addAttribute("totalQuantityReceived", totalQuantityReceived);
		model.addAttribute("totalLineAmount", totalLineAmount);
		model.addAttribute("totalVatAmount", totalVatAmount);

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
		
		return "pages/procurement/qcheader/qcheader";
	}
	
	private PogrnHeader getDefaultPogrnHeader() {
		PogrnHeader pogrn = new PogrnHeader();
		pogrn.setXtypetrn(TransactionCodeType.QC_NUMBER.getCode());
		pogrn.setXstatusgrn("Open");
		pogrn.setXstatus("Open");
		pogrn.setXtype("Other");
		pogrn.setXtotamt(BigDecimal.ZERO.setScale(1, RoundingMode.DOWN));
		pogrn.setXdisc(BigDecimal.ZERO.setScale(1, RoundingMode.DOWN));
		pogrn.setXvatamt(BigDecimal.ZERO.setScale(1, RoundingMode.DOWN));
		pogrn.setXaitamt(BigDecimal.ZERO.setScale(1, RoundingMode.DOWN));
		pogrn.setXamtother(BigDecimal.ZERO.setScale(1, RoundingMode.DOWN));
		pogrn.setXdategl(pogrn.getXdate());
		pogrn.setXpreparer(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff());
		pogrn.setXpreparername(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getStaffname());
		return pogrn;
	}

	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PogrnHeader por, BindingResult bindingResult){
		if(por == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		por.setXstatusap("Open");
		por.setXstatusjv("Open");
		por.setXstatusprjtrn("Open");
		por.setXtype("Other");

		// Validate
		if(StringUtils.isBlank(por.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(por.getXinvnum())) {
			responseHelper.setErrorStatusAndMessage("Invoice Number required");
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff())) {
			responseHelper.setErrorStatusAndMessage("Preparer Can't be empty.");
			return responseHelper.getResponse();
		}
		
		/*
		 * if(StringUtils.isBlank(por.getXnote())) {
		 * responseHelper.setErrorStatusAndMessage("Preparer Note required"); return
		 * responseHelper.getResponse(); }
		 */
		
		/*
		 * if(StringUtils.isBlank(por.getXwh())) {
		 * responseHelper.setErrorStatusAndMessage("Store required"); return
		 * responseHelper.getResponse(); }
		 */

		// If existing
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(por.getXgrnnum());
		if(data != null) {
			BeanUtils.copyProperties(por, data, "xtypetrn", "xtrn");
			long count = pogrnService.update(data);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Quality Certificate info.");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Quality Certificate info Updated Successfully");
			responseHelper.setRedirectUrl("/procurement/qcheader/" + data.getXgrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = pogrnService.save(por);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Quality Certificate info");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Quality Certificate info Created Successfully");
		responseHelper.setRedirectUrl("/procurement/qcheader/" + por.getXgrnnum());
		return responseHelper.getResponse();
	}
	

	@PostMapping("/delete/{xgrnnum}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xgrnnum){
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(data == null) {
			responseHelper.setErrorStatusAndMessage("Can't delete Quality Certificate info.");
			return responseHelper.getResponse();
		}

		List<PogrnDetail> details = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		if(details != null && !details.isEmpty()) { 
			responseHelper.setErrorStatusAndMessage("Please delete all Quality Certificate info detail first."); 
			return responseHelper.getResponse(); 
		}
		
		responseHelper.setSuccessStatusAndMessage("Deleted Successfully");
		responseHelper.setRedirectUrl("/procurement/qcheader");
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xgrnnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xgrnnum) {
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(pogrnHeader == null) {
			responseHelper.setErrorStatusAndMessage("GRN not found in this system");
			return responseHelper.getResponse();
		}

		// Validate
		if(pogrnHeader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("GRN date required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Site/Store required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXinvnum())) {
			responseHelper.setErrorStatusAndMessage("Invoice No. required");
			return responseHelper.getResponse();
		}
		if (StringUtils.isBlank(pogrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Purchase Type Required");
			return responseHelper.getResponse();
		}
		if (!"Open".equalsIgnoreCase(pogrnHeader.getXstatusgrn())) {
			responseHelper.setErrorStatusAndMessage("GRN already confirmed");
			return responseHelper.getResponse();
		}

		List<PogrnDetail> pogrnDetailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		if (pogrnDetailList == null || pogrnDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("GRN has no item details");
			return responseHelper.getResponse();
		}

		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusgrn())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.procPO_confirmGRN(pogrnHeader.getXgrnnum(), p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		if(isIdeal()) {
			if (!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusprjtrn())) {
				p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
				pogrnService.procPO_confirmProjectQC(pogrnHeader.getXgrnnum(), p_seq);
				String em = getProcedureErrorMessages(p_seq);
				if (StringUtils.isNotBlank(em)) {
					responseHelper.setErrorStatusAndMessage(em);
					return responseHelper.getResponse();
				}
			}
		}

	

		responseHelper.setSuccessStatusAndMessage("Quality Certificate Confirmed successfully");
		responseHelper.setRedirectUrl("/procurement/qcheader/" + xgrnnum);
		return responseHelper.getResponse();
	}

	@PostMapping("/transferap/{xgrnnum}")
	public @ResponseBody Map<String, Object> transferap(@PathVariable String xgrnnum){
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(pogrnHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		
		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusap())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.procTransferPOtoAP(pogrnHeader.getXgrnnum(), p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Transfered to AP successfully");
		responseHelper.setRedirectUrl("/procurement/qcheader/" + xgrnnum);
		return responseHelper.getResponse();
	}

	@PostMapping("/transfergl/{xgrnnum}")
	public @ResponseBody Map<String, Object> transfergl(@PathVariable String xgrnnum){
		PogrnHeader pogrndHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(pogrndHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		String p_seq;
		if(isModuleActive(Modules.ACCOUNTING)) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.poTransferPOtoGL(xgrnnum, "pogrnheaderac", p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}
		responseHelper.setSuccessStatusAndMessage("Transfered to GL successfully");
		responseHelper.setRedirectUrl("/procurement/qcheader/" + xgrnnum);
		return responseHelper.getResponse();
	}

	@GetMapping("{xgrnnum}/qcheaderdetail/{xrow}/show")
	public String openQCModal(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {
		PogrnHeader pogrnheader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(pogrnheader == null) return "redirect:/procurement/qcheader";

		if("new".equalsIgnoreCase(xrow)) {
			PogrnDetail pogrnDetail = new PogrnDetail();
			pogrnDetail.setXgrnnum(xgrnnum);
			pogrnDetail.setXqtypur(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXvatrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXvatamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXait(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXaitamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXdisc(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXdiscf(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXamtother(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrnDetail.setXcfpur(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("qcheaderdetail", pogrnDetail);
			model.addAttribute("availablestock", Collections.emptyList());
			model.addAttribute("purpose", xcodeService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
		} else {
			PogrnDetail pogrnDetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
			if(pogrnDetail == null) {
				pogrnDetail = new PogrnDetail();
				pogrnDetail.setXgrnnum(xgrnnum);
				pogrnDetail.setXqtypur(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXvatrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXvatamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXait(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXaitamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXdisc(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXdiscf(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXamtother(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrnDetail.setXcfpur(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("qcheaderdetail", pogrnDetail);
			model.addAttribute("availablestock", imstockService.findByXitem(pogrnDetail.getXitem()));
			model.addAttribute("purpose", xcodeService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
		}
		return "pages/procurement/qcheader/qcheaderdetailmodal::qcheaderdetailmodal";
	}
	
	@PostMapping("/qcheaderdetail/save")
	public @ResponseBody Map<String, Object> savePogrndetail(PogrnDetail pogrndetail){
		if(pogrndetail == null || StringUtils.isBlank(pogrndetail.getXgrnnum()) || StringUtils.isBlank(pogrndetail.getXitem())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(pogrndetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item");
			return responseHelper.getResponse();
		}
		if(pogrndetail.getXrate().compareTo(BigDecimal.ZERO) == 0.00 ) {
			responseHelper.setErrorStatusAndMessage("Unit Price should not <0.01");
			return responseHelper.getResponse();
		}
		
		if(pogrndetail.getXqtypur().compareTo(BigDecimal.ZERO) == 0.00){
			responseHelper.setErrorStatusAndMessage("Quantity should not <0.01");
			return responseHelper.getResponse();
		}
		
		/*
		 * if(pogrndetail.getXcfpur().compareTo(BigDecimal.ZERO) == 0.00 ) {
		 * responseHelper.
		 * setErrorStatusAndMessage("Conv. Factor-Purchase should not <0.01"); return
		 * responseHelper.getResponse(); }
		 */
		// Check item already exist in detail list
		if(pogrndetail.getXrow() == 0 && poordService.findPoorddetailByXpornumAndXitem(pogrndetail.getXgrnnum(), pogrndetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}
		
		Caitem caitem = caitemService.findByXitem(pogrndetail.getXitem());
		/*
		 * if(caitem ==null) { pogrndetail.setXaitamt(BigDecimal.ZERO.setScale(2,
		 * RoundingMode.DOWN)); } if(caitem !=null) {
		 * pogrndetail.setXvatamt((pogrndetail.getXlineamt().multiply(caitem.getXvatrate
		 * ())).divide(BigDecimal.valueOf(100))); }
		 */
		// if existing
		PogrnDetail existDetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pogrndetail.getXgrnnum(), pogrndetail.getXrow());
		if(existDetail != null) {
			
			BeanUtils.copyProperties(pogrndetail, existDetail, "xgrnnum", "xrow");
			long count = pogrnService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("qcheaderdetailtable", "/procurement/qcheader/qcheaderdetail/" + pogrndetail.getXgrnnum());
			responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurement/qcheader/pogrnheaderform/" + pogrndetail.getXgrnnum());
			responseHelper.setThirdReloadSectionIdWithUrl("qcheadertable", "/procurement/qcheader/qcheadertable");
			responseHelper.setSuccessStatusAndMessage("Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pogrnService.saveDetail(pogrndetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("qcheaderdetailtable", "/procurement/qcheader/qcheaderdetail/" + pogrndetail.getXgrnnum());
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurement/qcheader/pogrnheaderform/" + pogrndetail.getXgrnnum());
		responseHelper.setThirdReloadSectionIdWithUrl("qcheadertable", "/procurement/qcheader/qcheadertable");
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/qcheaderdetail/{xgrnnum}")
	public String reloadPoreqdetailTabble(@PathVariable String xgrnnum, Model model) {
		List<PogrnDetail> pogrndetails = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		model.addAttribute("qcheaderdetailList", pogrndetails);
		model.addAttribute("qcheader", pogrnService.findPogrnHeaderByXgrnnum(xgrnnum));
		
		List<PogrnDetail> details = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		BigDecimal totalQuantityReceived = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		BigDecimal totalVatAmount = BigDecimal.ZERO;
		if(details != null && !details.isEmpty()) {
			for(PogrnDetail pd : details) {
				totalQuantityReceived = totalQuantityReceived.add(pd.getXqtypur() == null ? BigDecimal.ZERO : pd.getXqtypur());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
				totalVatAmount = totalVatAmount.add(pd.getXvatamt() == null ? BigDecimal.ZERO : pd.getXvatamt());
			}
		}
		model.addAttribute("totalQuantityReceived", totalQuantityReceived);
		model.addAttribute("totalLineAmount", totalLineAmount);
		model.addAttribute("totalVatAmount", totalVatAmount);
		
		return "pages/procurement/qcheader/qcheader::qcheaderdetailtable";
	}

	@PostMapping("{xgrnnum}/qcheaderdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePogrnDetail(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {
		PogrnDetail detail = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
		if(detail == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pogrnService.deleteDetail(detail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("qcheaderdetailtable", "/procurement/qcheader/qcheaderdetail/" + xgrnnum);
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurement/qcheader/pogrnheaderform/" + xgrnnum);
		responseHelper.setThirdReloadSectionIdWithUrl("qcheadertable", "/procurement/qcheader/qcheadertable");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
	

	@GetMapping("/pogrnheaderform/{xgrnnum}")
	public String loadOCPage(@PathVariable String xgrnnum, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (data == null) data = getDefaultPogrnHeader();
		if (data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO);

		model.addAttribute("qcheader", data);
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.QC_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allQcHeader", pogrnService.getAllQCPogrnHeader());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("qcheaderdetailList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

		return "pages/procurement/qcheader/qcheader:: pogrnheaderform";
	}

	@GetMapping("/qcheadertable")
	public String reloadPoreqheaderHeaderTable(Model model) {
		model.addAttribute("allQcHeader", pogrnService.getAllQCPogrnHeader());
		return "pages/procurement/qcheader/qcheader::qcheadertable";
	}

	@GetMapping("/print/{xgrnnum}")
	public ResponseEntity<byte[]> printPoreqnumHeaderWithDetails(@PathVariable String xgrnnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy, HH:mm:ss");
		SimpleDateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy");
		Cacus cacus = cacusService.findByXcus(data.getXcus());
		QCReport report=new QCReport();

		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Quality Certificate");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		if(data.getXdate()==null) {
			report.setFromDate("");
		}
		else if(data.getXdate()!=null) {
			report.setFromDate(sdf.format(data.getXdate()));
		}
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		report.setXgrnnum(data.getXgrnnum());
		if(data.getXdate()==null) {
			report.setXdate("");
		}
		else if(data.getXdate()!=null) {
			report.setXdate(df.format(data.getXdate()));
		}
		report.setXcus(data.getXcus());
		report.setCusName(cacus.getXorg());
		report.setXwh(data.getXwh());
		report.setStoreName(xcodesService.findByXtypesAndXcodes(CodeType.STORE.getCode(), data.getXwh(), Boolean.TRUE).getXlong());
		report.setXinvnum(data.getXinvnum());
		report.setXpornum(data.getXpornum());
		report.setXporeqnum(data.getXporeqnum());
		report.setXquotnum(data.getXquotnum());
		report.setXref(data.getXref());
		report.setXpreparer(data.getXpreparer());
		report.setPreparerName(data.getXpreparername());
		report.setXnote(data.getXnote());
		report.setXvoucher(data.getXvoucher());
		report.setXstatusgrn(data.getXstatusgrn());
		report.setXstatus(data.getXstatus());
		report.setXtotamt(data.getXtotamt());
		report.setXstatusap(data.getXstatusap());
		report.setXstatusjv(data.getXstatusjv());
		report.setXvatamt(data.getXvatamt());
		report.setXaitamt(data.getXaitamt());
		report.setXdisc(data.getXdisc());
		report.setXamtother(data.getXamtother());
		report.setZactive(data.getZactive());

		if(data.getXprepdate()==null) {
			report.setXprepdate("");
		}
		else if(data.getXprepdate()!=null) {
			report.setXprepdate(sdf.format(data.getXprepdate()));
		}

		List<PoordDetail> items = poordService.findPoorddetailByXpornum(data.getXpornum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getItemname());
				item.setItemQty(it.getXqtypur().toString());
				item.setRate(it.getXrate());
				item.setItemUnit(it.getXunitpur());
				item.setItemQty(it.getXqtypur() != null ? it.getXqtypur().toString() : BigDecimal.ZERO.toString());
				item.setLineamt(it.getXlineamt());
				item.setPurpose(it.getXpurpose());
				report.getItems().add(item);
			});
		}

		List<Termstrn> terms = poordService.findTermstrnByXdocnum(data.getXpornum());
		if(terms != null && !terms.isEmpty()) report.setDfterms(terms);

		byte[] byt = getPDFByte(report, "qcreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for this Quality Certificate: " + xgrnnum;
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
