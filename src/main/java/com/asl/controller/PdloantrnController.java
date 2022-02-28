package com.asl.controller;

import static org.hamcrest.CoreMatchers.nullValue;

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

import com.asl.entity.Caproject;
import com.asl.entity.Caprojectplan;
import com.asl.entity.Cawh;
import com.asl.entity.Cawhfact;
import com.asl.entity.LandDocument;
import com.asl.entity.Pdloantrn;
import com.asl.entity.Pdloanwriteoff;
import com.asl.entity.Pdmst;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.CaprojectReport;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.PdloantrnReport;
import com.asl.service.PdloantrnService;
import com.asl.service.PdmstService;

@Controller
@RequestMapping("/payroll/pdloantrn")
public class PdloantrnController extends ASLAbstractController{

	@Autowired private PdloantrnService pdloantrnService;
	@Autowired private PdmstService pdmstService;
	
	@GetMapping
	public String loadPdloantrnControllerPage(Model model) {
		model.addAttribute("loantrn", getDefaultPdloantrn());
		model.addAttribute("allLoantrns", pdloantrnService.getAllPdloantrn());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LOAN_NUMBER.getCode(), Boolean.TRUE));
		return "pages/payroll/pdloantrn/pdloantrn";
	}


	
	@GetMapping("/{xvoucher}")
	public String loadPdloantrnPage(@PathVariable String xvoucher, Model model) {
		Pdloantrn data = pdloantrnService.findByPdloantrn(xvoucher);
		if(data == null) return "redirect:/payroll/pdloantrn";

		model.addAttribute("loantrn", data);
		model.addAttribute("allLoantrns", pdloantrnService.getAllPdloantrn());
		model.addAttribute("lwolist", pdloantrnService.findByPdloanwriteoff(xvoucher));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LOAN_NUMBER.getCode(), Boolean.TRUE));
		return "pages/payroll/pdloantrn/pdloantrn";
	}

	private Pdloantrn getDefaultPdloantrn() {
		Pdloantrn pdloantrn = new Pdloantrn();

		pdloantrn.setXtype(TransactionCodeType.LOAN_NUMBER.getCode());
		pdloantrn.setXtypetrn(TransactionCodeType.LOAN_NUMBER.getCode());
		pdloantrn.setXdate(new Date());
		pdloantrn.setXstatus("Open");
		pdloantrn.setXpaid(BigDecimal.ZERO);

		return pdloantrn;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pdloantrn pdloantrn, BindingResult bindingResult) {
		if (pdloantrn == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		//Validation
		if(StringUtils.isBlank(pdloantrn.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Cannot Proceed-Loan Type Is Blank");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pdloantrn.getXstaff())) {
			responseHelper.setErrorStatusAndMessage("Please Provide Employee");
			return responseHelper.getResponse();
		}
		
		pdloantrn.setXlastinsamt(BigDecimal.ZERO);
		pdloantrn.setXpaid(BigDecimal.ZERO);
		pdloantrn.setXstatus("Open");
		
		// if existing
		if(StringUtils.isNotBlank(pdloantrn.getXvoucher())) {
			
			//Validation
			if(StringUtils.isBlank(pdloantrn.getXtype())) {
				responseHelper.setErrorStatusAndMessage("Cannot Proceed-Loan Type Is Blank");
				return responseHelper.getResponse();
			}
			if(StringUtils.isBlank(pdloantrn.getXstaff())) {
				responseHelper.setErrorStatusAndMessage("Please Provide Employee");
				return responseHelper.getResponse();
			}
			if (!"Open".equalsIgnoreCase(pdloantrn.getXstatus())) {
				responseHelper.setErrorStatusAndMessage("Schedule Already Generated!");
				return responseHelper.getResponse();
			}
			
			
			Pdloantrn exit= pdloantrnService.findByPdloantrn(pdloantrn.getXvoucher());
			BeanUtils.copyProperties(pdloantrn, exit,"xtypetrn","xtrn","xvoucher");
			long count = pdloantrnService.updatePdloantrn(exit);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update loan info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Loan info updated successfully");
			responseHelper.setRedirectUrl("/payroll/pdloantrn/" + pdloantrn.getXvoucher());
			return responseHelper.getResponse();
		}

		// if new
		long count = pdloantrnService.savePdloantrn(pdloantrn);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save loan info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Loan info saved successfully");
		responseHelper.setRedirectUrl("/payroll/pdloantrn/" + pdloantrn.getXvoucher());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xvoucher}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xvoucher,  Model model) {
		Pdloantrn pdloantrn = pdloantrnService.findByPdloantrn(xvoucher);
		if(pdloantrn == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pdloantrnService.deletePdloantrn(pdloantrn);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/payroll/pdloantrn/" + xvoucher );
		return responseHelper.getResponse();
}
	
	//start of LoanWriteOff
	
		@GetMapping("/{xvoucher}/loanwriteoff/{xrow}/show")
		public String loadLoanWriteOffModal(@PathVariable String xvoucher, @PathVariable String xrow, Model model) {
			if("new".equalsIgnoreCase(xrow)) {
				Pdloanwriteoff lwo = new Pdloanwriteoff();
				lwo.setXvoucher(xvoucher);
				lwo.setXstatus("Open");
				model.addAttribute("lwo", lwo);
			}
			else {
				Pdloanwriteoff lwo = pdloantrnService.findPdloanwriteoffByXvoucherAndXrow(xvoucher, Integer.parseInt(xrow));
				if(lwo==null) {
					lwo = new Pdloanwriteoff();
					
				}
				model.addAttribute("lwo", lwo);
			}
			
			return "pages/payroll/pdloantrn/loanwriteoffmodal::loanwriteoffmodal";
		}
		
		@PostMapping("/loanwriteoff/save")
		public @ResponseBody Map<String, Object> saveLoanWriteOff(Pdloanwriteoff lwo) {
			if (lwo == null || StringUtils.isBlank(lwo.getXvoucher())) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			// if existing
			Pdloanwriteoff exist = pdloantrnService.findPdloanwriteoffByXvoucherAndXrow(lwo.getXvoucher(), lwo.getXrow());
			if (exist != null) {
				BeanUtils.copyProperties(lwo, exist, "xvoucher");
				long count = pdloantrnService.updatePdloanwriteoff(exist);
				if (count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
				responseHelper.setReloadSectionIdWithUrl("loanwriteofftable","/payroll/pdloantrn/loanwriteoff/" + lwo.getXvoucher());
				responseHelper.setSuccessStatusAndMessage("Loan Write Off updated successfully");
				return responseHelper.getResponse();
			}

			// if new detail
			long count = pdloantrnService.savePdloanwriteoff(lwo);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("loanwriteofftable","/payroll/pdloantrn/loanwriteoff/" + lwo.getXvoucher());
			responseHelper.setSuccessStatusAndMessage("Loan Write Off saved successfully");
			return responseHelper.getResponse();
		}
		
		@GetMapping("/loanwriteoff/{xvoucher}")
		public String reloadTable(@PathVariable String xvoucher, Model model) {
			List<Pdloanwriteoff>lwoist = pdloantrnService.findByPdloanwriteoff(xvoucher);
			model.addAttribute("lwolist", lwoist);
			model.addAttribute("loantrn", pdloantrnService.findByPdloantrn(xvoucher));
			return "pages/payroll/pdloantrn/pdloantrn::loanwriteofftable";
		}
		
		@PostMapping("{xvoucher}/loanwriteoff/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteDetail(@PathVariable String xvoucher, @PathVariable String xrow,
				Model model) {
			Pdloanwriteoff lpe = pdloantrnService.findPdloanwriteoffByXvoucherAndXrow(xvoucher, Integer.parseInt(xrow));
			if (lpe == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = pdloantrnService.deletePdloanwriteoff(lpe);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("loanwriteofftable", "/payroll/pdloantrn/loanwriteoff/" + xvoucher);
			return responseHelper.getResponse();
		}
		
		@GetMapping("/print/{xvoucher}")
		public ResponseEntity<byte[]> printLoanWithDetails(@PathVariable String xvoucher , HttpServletRequest request) {
			String message;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("text", "html"));
			headers.add("X-Content-Type-Options", "nosniff");
			
			Pdloantrn data = pdloantrnService.findByPdloantrn(xvoucher);
			
			Pdmst pdmst=pdmstService.findAllPdmst(data.getXstaff());
			
			SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
			
			PdloantrnReport report=new PdloantrnReport();
			
			report.setBusinessName(sessionManager.getZbusiness().getZorg());
			report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
			report.setReportName("Loans & Advance Entry");
			report.setReportLogo(sessionManager.getZbusiness().getXbimage());
			report.setFromDate(sdf.format(data.getXdate()));
			report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy").format(new Date()));
			report.setPhone(sessionManager.getZbusiness().getXphone());
			report.setFax(sessionManager.getZbusiness().getXfax());
			
			report.setXvoucher(data.getXvoucher());
			if(data.getXdate()==null) {
				report.setXdate("");
			}
			if(data.getXdate()!=null)
			{
				report.setXdate(sdf.format(data.getXdate()));
			}
			report.setXstaff(data.getXstaff());
			report.setXstaffName(pdmst.getXname());
			report.setXtype(data.getXtype());
			report.setXloanamt(data.getXloanamt() != null ? data.getXloanamt() : BigDecimal.ZERO);
			report.setXinstallment(data.getXinstallment());
			report.setXinsamt(data.getXinsamt() != null ? data.getXinsamt():BigDecimal.ZERO);
			if(data.getXdateeff() == null) 
			{
				report.setXdateeff("");
			}
			if(data.getXdateeff() != null)
			{
				report.setXdateeff(sdf.format(data.getXdateeff()));
			}
			report.setXlastinsamt(data.getXlastinsamt() != null ? data.getXlastinsamt():BigDecimal.ZERO);
			report.setXpaid(data.getXpaid() != null ? data.getXpaid() : BigDecimal.ZERO);
			report.setXstatus(data.getXstatus());
			report.setXstatustag(data.getXstatustag());
			report.setXamount(data.getXamount() != null ? data.getXamount() : BigDecimal.ZERO);
			
			List<Pdloanwriteoff>items = pdloantrnService.findByPdloanwriteoff(data.getXvoucher());
			if(items != null && !items.isEmpty()) {
				items.stream().forEach(it -> {
					ItemDetails item = new ItemDetails();
					item.setXdate(sdf.format(it.getXdate()));
					item.setXloanamt(it.getXloanamt() != null ? it.getXloanamt():BigDecimal.ZERO);
					item.setXstatus(it.getXstatus());
					item.setXstatustag(it.getXstatustag());
					item.setXnote(it.getXnote());
					
					report.getItems().add(item);
			});
			};
			byte[] byt = getPDFByte(report, "pdloantrnreport.xsl", request);
			if(byt == null) {
				message = "Can't generate pdf for Loans & Advance Entry: " + xvoucher;
				return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			headers.setContentType(new MediaType("application", "pdf"));
			return new ResponseEntity<>(byt, headers, HttpStatus.OK);
			
		}
		
		
		
		
		
}
