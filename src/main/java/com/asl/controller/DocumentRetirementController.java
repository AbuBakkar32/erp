package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.GRNOrder;
import com.asl.model.report.GrnReport;
import com.asl.model.report.ItemDetails;
import com.asl.service.AcService;
import com.asl.service.CacusService;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.PoordService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/cost/pogrnheader")
public class DocumentRetirementController extends ASLAbstractController {

	@Autowired private PogrnService pogrnService;
	@Autowired private PocrnService pocrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CacusService cacusService;
	@Autowired private PoordService poordService;
	@Autowired private AcService acService;

	@GetMapping
	public String loadGRNPage(Model model) {

		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllDocumentPogrnHeader());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("branchs", xcodesService.findByXtype(CodeType.BRANCH.getCode(), Boolean.TRUE));

		return "pages/cost/pogrn/pogrn";
	}

	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (data == null) data = getDefaultPogrnHeader();
		if (data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO);

		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllDocumentPogrnHeader());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("branchs", xcodesService.findByXtype(CodeType.BRANCH.getCode(), Boolean.TRUE));
		
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));
		
		List<PogrnDetail> details = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		BigDecimal totalQuantityReceived = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(details != null && !details.isEmpty()) {
			for(PogrnDetail pd : details) {
				totalQuantityReceived = totalQuantityReceived.add(pd.getXqtygrn() == null ? BigDecimal.ZERO : pd.getXqtygrn());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantityReceived", totalQuantityReceived);
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
		
		return "pages/cost/pogrn/pogrn";
	}

	private PogrnHeader getDefaultPogrnHeader() {
		PogrnHeader pogrn = new PogrnHeader();
		pogrn.setXtypetrn(TransactionCodeType.GRN_NUMBER.getCode());
		pogrn.setXdate(new Date());
		pogrn.setXstatusgrn("Open");
		pogrn.setXtotamt(BigDecimal.ZERO);
		pogrn.setXstatusap("Open");
		pogrn.setXstatusjv("Open");
		pogrn.setXtype("LC");
		return pogrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PogrnHeader pogrnHeader, BindingResult bindingResult) {

		// Validate
		
		
		// Hidden data
		pogrnHeader.setXdategl(pogrnHeader.getXdate());

		// if existing record
		PogrnHeader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
		if (existPogrnHeader != null) {
			BeanUtils.copyProperties(pogrnHeader, existPogrnHeader, "xgrnnum", "xtypetrn","xtrn","xstatusgrn","xpronum","xcus","xstatusjv");
			long count = pogrnService.update(existPogrnHeader);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update GRN");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("GRN updated successfully");
			responseHelper.setRedirectUrl("/cost/pogrnheader/" + pogrnHeader.getXgrnnum());
			return responseHelper.getResponse();
		}

		// If new
		pogrnHeader.setXstatusjv("Open");
		long count = pogrnService.save(pogrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		responseHelper.setRedirectUrl("/cost/pogrnheader/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xgrnnum}")
	public  @ResponseBody Map<String, Object> archive(@PathVariable String xgrnnum) {
		PogrnHeader header = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(header == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN in the system");
			return responseHelper.getResponse();
		}

		// check grn has details
		List<PogrnDetail> grndetails = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		if(grndetails != null && !grndetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete GRN details first");
			return responseHelper.getResponse();
		}

		// 2nd now delete GRN
		long hcount = pogrnService.deletePogrnheader(xgrnnum);
		if(hcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete GRN");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("GRN deleted successfully");
		responseHelper.setRedirectUrl("/cost/pogrnheader/");
		return responseHelper.getResponse();
	}

	@GetMapping("{xgrnnum}/pogrndetail/{xrow}/show")
	public String openPogrnDetailModal(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {

		PogrnHeader grn = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(grn == null) return "redirect:/cost/pogrnheader/" + xgrnnum;

		model.addAttribute("xpornum", grn.getXpornum());
		if("new".equalsIgnoreCase(xrow)) {
			PogrnDetail detail = new PogrnDetail();
			detail.setXgrnnum(xgrnnum);
			detail.setPrevqty(BigDecimal.ZERO);
			model.addAttribute("pogrndetail", detail);
		} else {
			PogrnDetail detail = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
			detail.setPrevqty(detail.getXqtygrn() == null ? BigDecimal.ZERO : detail.getXqtygrn());
			model.addAttribute("pogrndetail", detail);
		}

		return "pages/cost/pogrn/pogrndetailmodal::pogrndetailmodal";
	}

	@PostMapping("/pogrndetail/save")
	public @ResponseBody Map<String, Object> savePogrndetail(PogrnDetail pogrnDetail) {
		if (pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(pogrnDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item selection required");
			return responseHelper.getResponse();
		}

		if(pogrnDetail.getXqtygrn() == null || BigDecimal.ZERO.equals(pogrnDetail.getXqtygrn()) || pogrnDetail.getXqtygrn().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("GRN quantity should be greater then 0");
			return responseHelper.getResponse();
		}

		// validate grn quantity
		PogrnHeader pgh = pogrnService.findPogrnHeaderByXgrnnum(pogrnDetail.getXgrnnum());
		if(pgh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN");
			return responseHelper.getResponse();
		}
		pogrnDetail.setXlineamt(pogrnDetail.getXqtygrn().multiply(pogrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		PogrnDetail existDetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pogrnDetail.getXgrnnum(), pogrnDetail.getXrow());
		
		if (existDetail != null) {
			BeanUtils.copyProperties(pogrnDetail, existDetail, "xgrnnum", "xrow");
			long count = pogrnService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			

			responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/cost/pogrnheader/pogrndetail/" + pogrnDetail.getXgrnnum());
			responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/cost/pogrnheader/pogrnheaderform/" + pogrnDetail.getXgrnnum());
			responseHelper.setSuccessStatusAndMessage("GRN details updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pogrnService.saveDetail(pogrnDetail);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save GRN details");
			return responseHelper.getResponse();
		}

		

		responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/cost/pogrnheader/pogrndetail/" + pogrnDetail.getXgrnnum());
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/cost/pogrnheader/pogrnheaderform/" + pogrnDetail.getXgrnnum());
		responseHelper.setSuccessStatusAndMessage("GRN details saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/pogrndetail/{xgrnnum}")
	public String reloadPogrnDetailTable(@PathVariable String xgrnnum, Model model) {
		List<PogrnDetail> details = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		model.addAttribute("pogrnDetailsList", details);
		model.addAttribute("pogrnheader", pogrnService.findPogrnHeaderByXgrnnum(xgrnnum));
		
		BigDecimal totalQuantityReceived = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(details != null && !details.isEmpty()) {
			for(PogrnDetail pd : details) {
				totalQuantityReceived = totalQuantityReceived.add(pd.getXqtygrn() == null ? BigDecimal.ZERO : pd.getXqtygrn());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantityReceived", totalQuantityReceived);
		model.addAttribute("totalLineAmount", totalLineAmount);
		return "pages/cost/pogrn/pogrn::pogrndetailtable";
	}

	@GetMapping("/pogrnheaderform/{xgrnnum}")
	public String loadPogrnheaderform(@PathVariable String xgrnnum, Model model) {
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		return "pages/cost/pogrn/pogrn::pogrnheaderform";
	}

	/*
	 * @GetMapping("/poordheadertable") public String reloadPogrnHeaderTable(Model
	 * model) { model.addAttribute("allPogrnHeader",
	 * pogrnService.getAllPogrnHeader()); return
	 * "pages/purchasing/pogrn/pogrn::pogrnheadertable"; }
	 */

	@PostMapping("{xgrnnum}/pogrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePogrnDetail(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {
		PogrnDetail pd = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// calculate and update poordetail xqtygrn 
		PogrnHeader pgh = pogrnService.findPogrnHeaderByXgrnnum(pd.getXgrnnum());
		if(pgh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN");
			return responseHelper.getResponse();
		}
		
		

		long count = pogrnService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete GRN detail");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/cost/pogrnheader/pogrndetail/" + xgrnnum);
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/cost/pogrnheader/pogrnheaderform/" + xgrnnum);
		return responseHelper.getResponse();
	}

	

}
