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

import com.asl.entity.Caproject;
import com.asl.entity.Cawh;
import com.asl.entity.Cawhbudget;
import com.asl.entity.Cawhbudgetdt;
import com.asl.entity.Imstock;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.LandDagDetails;
import com.asl.entity.LandDocument;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.Termstrn;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.CawhReport;
import com.asl.model.report.CawhbudgetReport;
import com.asl.service.CaprojectService;
import com.asl.service.CawhService;
import com.asl.service.CawhbudgetService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/project/cawhbudget")
public class CawhbudgetController extends ASLAbstractController{

	@Autowired private CawhbudgetService cawhbudgetService;
	@Autowired private CawhService cawhService;
	@Autowired private XcodesService xcodeService;

	@GetMapping
	public String loadCawhbudgetPage(Model model) {
		model.addAttribute("cawhbudget", getDefaultCawhbudget());
		model.addAttribute("allCawhbudget", cawhbudgetService.getAllCawhbudget());
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_BUDGET.getCode(), Boolean.TRUE));
		return "pages/project/cawhbudget/cawhbudget";
	}

	private Cawhbudget getDefaultCawhbudget() {
		Cawhbudget cp = new Cawhbudget();
		cp.setXtypetrn(TransactionCodeType.PROJECT_BUDGET.getCode());
		cp.setXtrn(TransactionCodeType.PROJECT_BUDGET.getdefaultCode());
		cp.setXstatus("Open");
		cp.setXtype("Budget");
		cp.setXtotamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		return cp;
	}

	@GetMapping("/{xbudget}")
	public String loadCawhbudgetPage(@PathVariable String xbudget, Model model) {
		Cawhbudget data = cawhbudgetService.findByCawhbudget(xbudget);
		if (data == null) data = getDefaultCawhbudget();
		model.addAttribute("cawhbudget", data);
		model.addAttribute("allCawhbudget", cawhbudgetService.getAllCawhbudget());
		model.addAttribute("cawhbudgetdtlist", cawhbudgetService.findByCawhbudgetdt(xbudget));
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_BUDGET.getCode(), Boolean.TRUE));
		
		List<Cawhbudgetdt> details = cawhbudgetService.findByCawhbudgetdt(xbudget);
		BigDecimal totalBudgetAmt = BigDecimal.ZERO;
		if (details != null && !details.isEmpty()) {
			for (Cawhbudgetdt pd : details) {
				totalBudgetAmt = totalBudgetAmt.add(pd.getXamount() == null ? BigDecimal.ZERO : pd.getXamount());
			}
		}
		model.addAttribute("totalBudgetAmt", totalBudgetAmt);

		return "pages/project/cawhbudget/cawhbudget";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cawhbudget cp, BindingResult bindingResult){
		if(cp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(cp.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Site required");
			return responseHelper.getResponse();
		}

		// If existing
		Cawhbudget data = cawhbudgetService.findByCawhbudget(cp.getXbudget());
		if(data != null) {
			BeanUtils.copyProperties(cp, data, "xtypetrn", "xtrn");
			long count = cawhbudgetService.updateCawhbudget(data);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Budget.");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Budget Updated Successfully");
			responseHelper.setRedirectUrl("/project/cawhbudget/" + data.getXbudget());
			return responseHelper.getResponse();
		}

		// If new
		long count = cawhbudgetService.saveCawhbudget(cp);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Save Budget.");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Budget Saved Successfully");
		responseHelper.setRedirectUrl("/project/cawhbudget/" + cp.getXbudget());
		return responseHelper.getResponse();
	}

	@PostMapping("/delete/{xbudget}")
	public @ResponseBody Map<String, Object> deleteProject(@PathVariable String xbudget,  Model model) {
		Cawhbudget cp = cawhbudgetService.findByCawhbudget(xbudget);
		if(cp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = cawhbudgetService.deleteCawhbudget(cp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/project/cawhbudget/" + xbudget );
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm/{xbudget}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xbudget){
		Cawhbudget cawhbudget = cawhbudgetService.findByCawhbudget(xbudget);
		if(cawhbudget == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(!"Open".equalsIgnoreCase(cawhbudget.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Budget already confirmed");
			return responseHelper.getResponse();
		}

		List<Cawhbudgetdt> details = cawhbudgetService.findByCawhbudgetdt(xbudget);
		if(details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}


		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(cawhbudget.getXstatus())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			cawhbudgetService.procIM_confirmProjectBudget(xbudget, p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Budget Confirmed successfully");
		responseHelper.setRedirectUrl("/project/cawhbudget/" + xbudget);
		return responseHelper.getResponse();
	}

	@GetMapping("/siteproject/{xwh}")
	public @ResponseBody Cawh getSiteDetail(@PathVariable String xwh){
		return cawhService.findByCawh(xwh);
	}

	@GetMapping("{xbudget}/cawhbudgetdt/{xrow}/show")
	public String openCawhbudgetdtModal(@PathVariable String xbudget, @PathVariable String xrow, Model model) {
		Cawhbudget cawhbudget = cawhbudgetService.findByCawhbudget(xbudget);
		if(cawhbudget == null) return "redirect:/project/cawhbudget/cawhbudgetdt";

		if("new".equalsIgnoreCase(xrow)) {
			Cawhbudgetdt dt = new Cawhbudgetdt();
			dt.setXbudget(xbudget);
			dt.setXamount(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			dt.setXtolerance(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("cawhbudgetdt", dt);
			model.addAttribute("purpose", xcodeService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
		} else {
			Cawhbudgetdt dt = cawhbudgetService.findCawhbudgetdtByXbudgetAndXrow(xbudget, Integer.parseInt(xrow));
			if(dt == null) {
				dt = new Cawhbudgetdt();
				dt.setXbudget(xbudget);
				dt.setXamount(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("cawhbudgetdt", dt);
			model.addAttribute("purpose", xcodeService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
		}
		return "pages/project/cawhbudget/cawhbudgetdtmodal::cawhbudgetdtmodal";
	}
	

	@PostMapping("/cawhbudgetdt/save")
	public @ResponseBody Map<String, Object> savecawhbudgetdt(Cawhbudgetdt cawhbudgetdt) {
		if (cawhbudgetdt == null || StringUtils.isBlank(cawhbudgetdt.getXbudget())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		//Validation
		if(StringUtils.isBlank(cawhbudgetdt.getXpurpose())) {
			responseHelper.setErrorStatusAndMessage("Please select any purpose.");
			return responseHelper.getResponse();
		}
		if(cawhbudgetdt.getXamount().compareTo(BigDecimal.ZERO) == 0.00 ) {
			responseHelper.setErrorStatusAndMessage("Budget Amount should not be <0.01");
			return responseHelper.getResponse();
		}

		// if existing
		Cawhbudgetdt exist = cawhbudgetService.findCawhbudgetdtByXbudgetAndXrow(cawhbudgetdt.getXbudget(), cawhbudgetdt.getXrow());
		if (exist != null) {
			BeanUtils.copyProperties(cawhbudgetdt, exist,"xbudget");
			long count = cawhbudgetService.updateCawhbudgetdt(exist);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("cawhbudgetdttable","/project/cawhbudget/cawhbudgetdt/" + cawhbudgetdt.getXbudget());
			responseHelper.setSecondReloadSectionIdWithUrl("cawhbudgetheaderform", "/project/cawhbudget/cawhbudgetheaderform/" + cawhbudgetdt.getXbudget());
			responseHelper.setThirdReloadSectionIdWithUrl("cawhbudgetheadertable", "/project/cawhbudget/cawhbudgetheadertable");
			responseHelper.setSuccessStatusAndMessage("Budget Details updated successfully");
			return responseHelper.getResponse();
		}

		
		// if new detail
		long count = cawhbudgetService.saveCawhbudgetdt(cawhbudgetdt);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("cawhbudgetdttable","/project/cawhbudget/cawhbudgetdt/" + cawhbudgetdt.getXbudget());
		responseHelper.setSecondReloadSectionIdWithUrl("cawhbudgetheaderform", "/project/cawhbudget/cawhbudgetheaderform/" + cawhbudgetdt.getXbudget());
		responseHelper.setThirdReloadSectionIdWithUrl("cawhbudgetheadertable", "/project/cawhbudget/cawhbudgetheadertable");
		responseHelper.setSuccessStatusAndMessage("Budget Details saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/cawhbudgetdt/{xbudget}")
	public String reloadCawhbudgetdtTable(@PathVariable String xbudget, Model model) {
		List<Cawhbudgetdt> budgetList = cawhbudgetService.findByCawhbudgetdt(xbudget);
		model.addAttribute("cawhbudgetdtlist", budgetList);
		model.addAttribute("cawhbudget", cawhbudgetService.findByCawhbudget(xbudget));

		List<Cawhbudgetdt> details = cawhbudgetService.findByCawhbudgetdt(xbudget);
		BigDecimal totalBudgetAmt = BigDecimal.ZERO;
		if (details != null && !details.isEmpty()) {
			for (Cawhbudgetdt pd : details) {
				totalBudgetAmt = totalBudgetAmt.add(pd.getXamount() == null ? BigDecimal.ZERO : pd.getXamount());
			}
		}
		model.addAttribute("totalBudgetAmt", totalBudgetAmt);

		return "pages/project/cawhbudget/cawhbudget::cawhbudgetdttable";
	}
	
	//delete
	@PostMapping("{xbudget}/cawhbudgetdt/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteLandDagDetails(@PathVariable String xbudget, @PathVariable String xrow, Model model) {
		Cawhbudgetdt details = cawhbudgetService.findCawhbudgetdtByXbudgetAndXrow(xbudget, Integer.parseInt(xrow));
		if(details == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = cawhbudgetService.deleteCawhbudgetdt(details);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("cawhbudgetdttable", "/project/cawhbudget/cawhbudgetdt/" + xbudget);
		responseHelper.setSecondReloadSectionIdWithUrl("cawhbudgetheaderform", "/project/cawhbudget/cawhbudgetheaderform/" + xbudget);
		responseHelper.setThirdReloadSectionIdWithUrl("cawhbudgetheadertable", "/project/cawhbudget/cawhbudgetheadertable");
		return responseHelper.getResponse();
	}
	

	@GetMapping("/cawhbudgetheaderform/{xbudget}")
	public String loadCawhbudgetheaerPage(@PathVariable String xbudget, Model model) {
		Cawhbudget data = cawhbudgetService.findByCawhbudget(xbudget);
		if (data == null) data = getDefaultCawhbudget();
		model.addAttribute("cawhbudget", data);
		model.addAttribute("allCawhbudget", cawhbudgetService.getAllCawhbudget());
		model.addAttribute("cawhbudgetdtlist", cawhbudgetService.findByCawhbudgetdt(xbudget));
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_BUDGET.getCode(), Boolean.TRUE));

		return "pages/project/cawhbudget/cawhbudget:: cawhbudgetheaderform";
	}


	@GetMapping("/cawhbudgetheadertable")
	public String reloadCawhbudgetheaderHeaderTable(Model model) {
		model.addAttribute("allCawhbudget", cawhbudgetService.getAllCawhbudget());
		return "pages/project/cawhbudget/cawhbudget:: cawhbudgetheadertable";
	}

	@GetMapping("/print/{xbudget}")
	public ResponseEntity<byte[]> printCawhbudgetWithDetails(@PathVariable String xbudget , HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Cawhbudget data = cawhbudgetService.findByCawhbudget(xbudget); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		CawhbudgetReport report=new CawhbudgetReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Budget Info");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		report.setTotalAmount(data.getXtotamt() != null ? data.getXtotamt().setScale(2, RoundingMode.FLOOR).toString() : BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR).toString().toString());
		
		report.setXbudget(data.getXbudget());
		report.setXwh(data.getXwh());
		report.setXproject(data.getXproject());
		report.setXcus(data.getXcus());
		report.setXtotamt(data.getXtotamt());
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
		report.setCusName(data.getXorg());
		report.setSiteName(data.getSiteName());
		report.setProjectName(data.getProjectName());
		
		
		List<Cawhbudgetdt> budgetdetails = cawhbudgetService.findByCawhbudgetdt(xbudget);
		if(budgetdetails != null && !budgetdetails.isEmpty()) 
			report.setBudgetdetails(budgetdetails);

		byte[] byt = getPDFByte(report, "cawhbudgetreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for budget info: " + xbudget;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}

}
