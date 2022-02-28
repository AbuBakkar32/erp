package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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

import com.asl.entity.Caitem;
import com.asl.entity.Cawhbudget;
import com.asl.entity.Cawhbudgetdt;
import com.asl.entity.Imissuedetail;
import com.asl.entity.Imissueheader;
import com.asl.entity.PoordDetail;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.CawhbudgetReport;
import com.asl.model.report.ImissueheaderReport;
import com.asl.service.CaitemService;
import com.asl.service.ImissueheaderService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/project/imissueheader")
public class ImissueheaderController extends ASLAbstractController{
	

	@Autowired private ImissueheaderService imissueheaderService;
	@Autowired private XcodesService xcodeService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadImissueheaderPage(Model model) {
		model.addAttribute("imissueheader", getDefaultImissueheader());
		model.addAttribute("allImissueheader", imissueheaderService.getAllImissueheader());
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_EXPENSE.getCode(), Boolean.TRUE));
		return "pages/project/imissue/imissueheader";
	}

	private Imissueheader getDefaultImissueheader() {
		Imissueheader issue = new Imissueheader();
		issue.setXtypetrn(TransactionCodeType.PROJECT_EXPENSE.getCode());
		issue.setXtrn(TransactionCodeType.PROJECT_EXPENSE.getdefaultCode());
		issue.setXstatus("Open");
		issue.setXstatusjv("Open");
		issue.setXtotamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		issue.setXwh(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? "" :sessionManager.getLoggedInUserDetails().getXwh());
		issue.setXperparer(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff());
		issue.setXpreparername(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getStaffname());
		issue.setStoreName(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? "" :sessionManager.getLoggedInUserDetails().getStorename());
		
		return issue;
	}

	@GetMapping("/{xissuenum}")
	public String loadCawhbudgetPage(@PathVariable String xissuenum, Model model) {
		Imissueheader data = imissueheaderService.findByImissueheader(xissuenum);
		if (data == null) data = getDefaultImissueheader();
		model.addAttribute("imissueheader", data);
		model.addAttribute("allImissueheader", imissueheaderService.getAllImissueheader());
		model.addAttribute("imissuedetailList", imissueheaderService.findByImissuedetail(xissuenum));
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_EXPENSE.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXproject(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("codes", list);
		
		List<Imissuedetail> details = imissueheaderService.findByImissuedetail(xissuenum);
		BigDecimal totalLineAmt = BigDecimal.ZERO;
		BigDecimal totalQty = BigDecimal.ZERO;
		if (details != null && !details.isEmpty()) {
			for (Imissuedetail pd : details) {
				totalLineAmt = totalLineAmt.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
				totalQty = totalQty.add(pd.getXqty() == null ? BigDecimal.ZERO : pd.getXqty());
			}
		}
		model.addAttribute("totalLineAmt", totalLineAmt);
		model.addAttribute("totalQty", totalQty);

		return "pages/project/imissue/imissueheader";
	}

	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imissueheader por, BindingResult bindingResult){
		if(por == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(StringUtils.isBlank(por.getXhwh())) {
			responseHelper.setErrorStatusAndMessage("Project required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(por.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff())) {
			responseHelper.setErrorStatusAndMessage("Preparer Can't be empty.");
			return responseHelper.getResponse();
		}

		// If existing
		Imissueheader data = imissueheaderService.findByImissueheader(por.getXissuenum());
		if(data != null) {
			BeanUtils.copyProperties(por, data, "xtypetrn", "xtrn");
			long count = imissueheaderService.updateImissueheader(data);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Work Order.");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Item Consumption Updated Successfully");
			responseHelper.setRedirectUrl("/project/imissueheader/" + data.getXissuenum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imissueheaderService.saveImissueheader(por);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Item Consumption ");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Item Consumption Saved Successfully");
		responseHelper.setRedirectUrl("/project/imissueheader/" + por.getXissuenum());
		return responseHelper.getResponse();
	}


	@PostMapping("/delete/{xissuenum}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xissuenum){
		Imissueheader data = imissueheaderService.findByImissueheader(xissuenum);
		if(data == null) {
			responseHelper.setErrorStatusAndMessage("Can't delete Item Consumption.");
			return responseHelper.getResponse();
		}

		List<Imissuedetail> details = imissueheaderService.findByImissuedetail(xissuenum);
		if(details != null && !details.isEmpty()) { 
			responseHelper.setErrorStatusAndMessage("Please delete all Consumption detail first."); 
			return responseHelper.getResponse(); 
		}
		
		responseHelper.setSuccessStatusAndMessage("Deleted Successfully");
		responseHelper.setRedirectUrl("/project/imissueheader");
		return responseHelper.getResponse();
	}
	
	
	@PostMapping("/confirm/{xissuenum}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xissuenum){
		Imissueheader header = imissueheaderService.findByImissueheader(xissuenum);
		if(header == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if("Confirmed".equalsIgnoreCase(header.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Consumption already confirmed");
			return responseHelper.getResponse();
		}
		
		List<Imissuedetail> details = imissueheaderService.findByImissuedetail(xissuenum);
		if(details.isEmpty()){
			responseHelper.setErrorStatusAndMessage("Please add detail!"); 
			return responseHelper.getResponse(); 
		}
		 
		/*
		 * if(header.getXtotamt().compareTo(BigDecimal.ZERO) == 0.00){
		 * responseHelper.setErrorStatusAndMessage("Total Amount should not <0.01");
		 * return responseHelper.getResponse(); }
		 */

		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(header.getXstatus())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			imissueheaderService.procIM_confirmProjectIssue(xissuenum, p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Consumption Confirmed successfully");
		responseHelper.setRedirectUrl("/project/imissueheader/" + xissuenum);
		return responseHelper.getResponse();
	}
	

	@PostMapping("/transfertogl/{xissuenum}")
	public @ResponseBody Map<String, Object> transfertogl(@PathVariable String xissuenum){
		Imissueheader header = imissueheaderService.findByImissueheader(xissuenum);
		if(header == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if("Confirmed".equalsIgnoreCase(header.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Consumption already Transferred to GL.");
			return responseHelper.getResponse();
		}
		
		List<Imissuedetail> details = imissueheaderService.findByImissuedetail(xissuenum);
		if(details.isEmpty()){
			responseHelper.setErrorStatusAndMessage("Please add detail!"); 
			return responseHelper.getResponse(); 
		}
		 
		if(header.getXtotamt().compareTo(BigDecimal.ZERO) == 0.00){
			responseHelper.setErrorStatusAndMessage("Total Amount should not <0.01");
			return responseHelper.getResponse();
		}

		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(header.getXstatusjv())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			imissueheaderService.procIM_confirmProjectIssueGL(xissuenum, p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Transferred to GL successfully");
		responseHelper.setRedirectUrl("/project/imissueheader/" + xissuenum);
		return responseHelper.getResponse();
	}

	@GetMapping("{xissuenum}/imissuedetail/{xrow}/show")
	public String openPoreqdetailModal(@PathVariable String xissuenum, @PathVariable String xrow, Model model) {
		Imissueheader header = imissueheaderService.findByImissueheader(xissuenum);
		if(header == null) return "redirect:/project/imissueheader";

		if("new".equalsIgnoreCase(xrow)) {
			Imissuedetail detail = new Imissuedetail();
			detail.setXissuenum(xissuenum);
			detail.setXqty(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			detail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			detail.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("imissuedetail", detail);
			model.addAttribute("purpose", xcodeService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
		} else {
			Imissuedetail detail = imissueheaderService.findImissuedetailByXissuenumAndXrow(xissuenum, Integer.parseInt(xrow));
			if(detail == null) {
				detail = new Imissuedetail();
				detail.setXissuenum(xissuenum);
				detail.setXqty(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				detail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				detail.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("imissuedetail", detail);
			model.addAttribute("purpose", xcodeService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
		}
		return "pages/project/imissue/imissuedetailmodal::imissuedetailmodal";
	}
	
	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
	
	@PostMapping("/imissuedetail/save")
	public @ResponseBody Map<String, Object> saveImissuedetail(Imissuedetail imissuedetail){
		if(imissuedetail == null || StringUtils.isBlank(imissuedetail.getXissuenum()) || StringUtils.isBlank(imissuedetail.getXitem())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(imissuedetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item");
			return responseHelper.getResponse();
		}
		/*
		 * if(imissuedetail.getXrate().compareTo(BigDecimal.ZERO) == 0.00 ) {
		 * responseHelper.setErrorStatusAndMessage("Unit Price should not <0.01");
		 * return responseHelper.getResponse(); }
		 */
		
		if(imissuedetail.getXqty().compareTo(BigDecimal.ZERO) == 0.00){
			responseHelper.setErrorStatusAndMessage("Quantity should not <0.01");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(imissuedetail.getXpurpose())) {
			responseHelper.setErrorStatusAndMessage("Please select any purpose.");
			return responseHelper.getResponse();
		}
		// Check item already exist in detail list
		if(imissuedetail.getXrow() == 0 && imissueheaderService.findImissuedetailByXissuenumAndXitem(imissuedetail.getXissuenum(), imissuedetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}
		
		// if existing
		Imissuedetail existDetail = imissueheaderService.findImissuedetailByXissuenumAndXrow(imissuedetail.getXissuenum(), imissuedetail.getXrow());
		if(existDetail != null) {
			
			BeanUtils.copyProperties(imissuedetail, existDetail, "xissuenum", "xrow");
			long count = imissueheaderService.updateImissuedetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("imissuedetailtable", "/project/imissueheader/imissuedetail/" + imissuedetail.getXissuenum());
			responseHelper.setSecondReloadSectionIdWithUrl("imissueheaderform", "/project/imissueheader/imissueheaderform/" + imissuedetail.getXissuenum());
			responseHelper.setThirdReloadSectionIdWithUrl("imissueheadertable", "/project/imissueheader/imissueheadertable");
			responseHelper.setSuccessStatusAndMessage("Consumption detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imissueheaderService.saveImissuedetail(imissuedetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		} 

		responseHelper.setReloadSectionIdWithUrl("imissuedetailtable", "/project/imissueheader/imissuedetail/" + imissuedetail.getXissuenum());
		responseHelper.setSecondReloadSectionIdWithUrl("imissueheaderform", "/project/imissueheader/imissueheaderform/" + imissuedetail.getXissuenum());
		responseHelper.setThirdReloadSectionIdWithUrl("imissueheadertable", "/project/imissueheader/imissueheadertable");
		responseHelper.setSuccessStatusAndMessage("Consumption detail saved successfully");
		return responseHelper.getResponse();
	}


	@GetMapping("/imissuedetail/{xissuenum}")
	public String reloadIssuedetailTable(@PathVariable String xissuenum, Model model) {
		List<Imissuedetail> issuedetail = imissueheaderService.findByImissuedetail(xissuenum);
		model.addAttribute("imissuedetailList", issuedetail);
		model.addAttribute("imissueheader", imissueheaderService.findByImissueheader(xissuenum));

		List<Imissuedetail> details = imissueheaderService.findByImissuedetail(xissuenum);
		BigDecimal totalLineAmt = BigDecimal.ZERO;
		BigDecimal totalQty = BigDecimal.ZERO;
		if (details != null && !details.isEmpty()) {
			for (Imissuedetail pd : details) {
				totalLineAmt = totalLineAmt.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
				totalQty = totalQty.add(pd.getXqty() == null ? BigDecimal.ZERO : pd.getXqty());
			}
		}
		model.addAttribute("totalLineAmt", totalLineAmt);
		model.addAttribute("totalQty", totalQty);

		return "pages/project/imissue/imissueheader::imissuedetailtable";
	}
	
	//delete
	@PostMapping("{xissuenum}/imissuedetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteImissuedetail(@PathVariable String xissuenum, @PathVariable String xrow, Model model) {
		Imissuedetail details = imissueheaderService.findImissuedetailByXissuenumAndXrow(xissuenum, Integer.parseInt(xrow));
		if(details == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imissueheaderService.deleteImissuedetail(details);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("imissuedetailtable", "/project/imissueheader/imissuedetail/" + xissuenum);
		responseHelper.setSecondReloadSectionIdWithUrl("imissueheaderform", "/project/imissueheader/imissueheaderform/" + xissuenum);
		responseHelper.setThirdReloadSectionIdWithUrl("imissueheadertable", "/project/imissueheader/imissueheadertable");
		return responseHelper.getResponse();
	}
	

	@GetMapping("/imissueheaderform/{xissuenum}")
	public String loadImissueheaderformPage(@PathVariable String xissuenum, Model model) {
		Imissueheader data = imissueheaderService.findByImissueheader(xissuenum);
		if (data == null) data = getDefaultImissueheader();
		
		model.addAttribute("imissueheader", data);
		model.addAttribute("allImissueheader", imissueheaderService.getAllImissueheader());
		model.addAttribute("imissuedetailList", imissueheaderService.findByImissuedetail(xissuenum));
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_EXPENSE.getCode(), Boolean.TRUE));
		return "pages/project/imissue/imissueheader:: imissueheaderform";
	}


	@GetMapping("/imissueheadertable")
	public String reloadImissueheaderTable(Model model) {
		model.addAttribute("allImissueheader", imissueheaderService.getAllImissueheader());
		return "pages/project/imissue/imissueheader:: imissueheadertable";
	}


	@GetMapping("/print/{xissuenum}")
	public ResponseEntity<byte[]> printCawhbudgetWithDetails(@PathVariable String xissuenum , HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Imissueheader data = imissueheaderService.findByImissueheader(xissuenum); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		ImissueheaderReport report=new ImissueheaderReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Consumption Info");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		report.setTotalAmount(data.getXtotamt() != null ? data.getXtotamt().setScale(2, RoundingMode.FLOOR).toString() : BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR).toString().toString());
	
		report.setXissuenum(data.getXissuenum());
		report.setXwh(data.getXwh());
		report.setXtotamt(data.getXtotamt());
		report.setSiteName(sessionManager.getLoggedInUserDetails().getStorename());
		if(data.getXdate()==null) {
			report.setXdate("");
		}
		if(data.getXdate()!=null)
		{
			report.setXdate(sdf.format(data.getXdate()));
		}
		if(data.getXprepdate()==null) {
			report.setXprepdate("");
		}
		if(data.getXprepdate()!=null)
		{
			report.setXprepdate(sdf.format(data.getXprepdate()));
		}
		report.setXref(data.getXref());
		report.setXnote(data.getXnote());
		report.setXstatus(data.getXstatus());
		report.setXperparer(data.getXperparer());
		report.setXstatusjv(data.getXstatusjv());
		report.setStoreName(data.getStoreName());
		report.setXpreparername(data.getXpreparername());
		
		List<Imissuedetail> issuedetails = imissueheaderService.findByImissuedetail(xissuenum);
		if(issuedetails != null && !issuedetails.isEmpty()) report.setIssuedetails(issuedetails);
	
		byte[] byt = getPDFByte(report, "imissueheaderreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for consumption info: " + xissuenum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}
	
	@GetMapping("/codesbyproject/{xhwh}")
	public @ResponseBody List<ProjectStoreView> getProjectstoreview(@PathVariable String xhwh){
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXproject(xhwh);
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		return list;
	}
}
