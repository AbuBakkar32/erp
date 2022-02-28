package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Acdetail;
import com.asl.entity.Cacus;
import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.CodeType;
import com.asl.enums.Modules;
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
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/procurements/pogrn")
public class GRNController extends ASLAbstractController {

	@Autowired private PogrnService pogrnService;
	@Autowired private PocrnService pocrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CacusService cacusService;
	@Autowired private PoordService poordService;
	@Autowired private AcService acService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadGRNPage(Model model) {

		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeader());
		//model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());

		return "pages/purchasing/pogrn/pogrn";
	}

	@GetMapping("/pogrnlist")
	public String loadGRNListPage(Model model) {

		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeader());
		return "pages/purchasing/pogrn/pogrnlist";
	}

	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (data == null) data = getDefaultPogrnHeader();
		if (data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO);

		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeader());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));

		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

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
		
		return "pages/purchasing/pogrn/pogrn";
	}

	private PogrnHeader getDefaultPogrnHeader() {
		PogrnHeader pogrn = new PogrnHeader();
		pogrn.setXtypetrn(TransactionCodeType.GRN_NUMBER.getCode());
		pogrn.setXdate(new Date());
		pogrn.setXstatusgrn("Open");
		pogrn.setXtotamt(BigDecimal.ZERO);
		pogrn.setXstatusap("Open");
		pogrn.setXstatusjv("Open");
		pogrn.setXtype("Other");
		return pogrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PogrnHeader pogrnHeader, BindingResult bindingResult) {

		// Validate
		if (StringUtils.isBlank(pogrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier selection required");
			return responseHelper.getResponse();
		}
		if (StringUtils.isBlank(pogrnHeader.getXhwh())) {
			responseHelper.setErrorStatusAndMessage("Project/Business required");
			return responseHelper.getResponse();
		}

		if (StringUtils.isBlank(pogrnHeader.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Site/Store required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXinvnum())) {
			responseHelper.setErrorStatusAndMessage("Supplier Bill No. required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXpornum())) {
			responseHelper.setErrorStatusAndMessage("Purchase Order Number not found");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Purchase Type Required");
			return responseHelper.getResponse();
		}
		// check date
		if(pogrnHeader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("GRN date Required");
			return responseHelper.getResponse();
		}
		PoordHeader po = poordService.findPoordHeaderByXpornum(pogrnHeader.getXpornum());

		if (po == null) {
			responseHelper.setErrorStatusAndMessage("Can't find purchae order for this GRN");
			return responseHelper.getResponse();
		}

		if (pogrnHeader.getXdate().before(po.getXdate())) {
			responseHelper.setErrorStatusAndMessage("GRN date can't be before purchase order date");
			return responseHelper.getResponse();
		}
		
		// Hidden data
		pogrnHeader.setXdategl(pogrnHeader.getXdate());

		// if existing record
		PogrnHeader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
		if (existPogrnHeader != null) {
			BeanUtils.copyProperties(pogrnHeader, existPogrnHeader, "xgrnnum", "xtypetrn","xtrn","xstatusgrn","xpornum","xcus","xstatusjv","xstatusprjtrn","xaitamt","xvatamt","xamtother","xdisc");
			long count = pogrnService.update(existPogrnHeader);
			if (count == 0) {
				
				responseHelper.setErrorStatusAndMessage("Can't update GRN");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("GRN updated successfully");
			responseHelper.setRedirectUrl("/procurements/pogrn/" + pogrnHeader.getXgrnnum());
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
		responseHelper.setRedirectUrl("/procurements/pogrn/" + pogrnHeader.getXgrnnum());
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
		responseHelper.setRedirectUrl("/procurements/pogrn/");
		return responseHelper.getResponse();
	}

	@GetMapping("{xgrnnum}/pogrndetail/{xrow}/show")
	public String openPogrnDetailModal(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {

		PogrnHeader grn = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(grn == null) return "redirect:/procurements/pogrn/" + xgrnnum;

		//model.addAttribute("xpornum", grn.getXpornum());
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

		return "pages/purchasing/pogrn/pogrndetailmodal::pogrndetailmodal";
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
		PoordDetail podetail = poordService.findPoorddetailByXpornumAndXrow(pgh.getXpornum(), pogrnDetail.getXdocrow());
		if(podetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Puchase order detail for this item row");
			return responseHelper.getResponse();
		}
		// calculate and update poordetail xqtygrn 
		if(pogrnDetail.getXrow() != 0) {  // for existing item
			BigDecimal diff1 = pogrnDetail.getPrevqty().subtract(pogrnDetail.getXqtygrn());
			BigDecimal diff2 = podetail.getXqtygrn().subtract(diff1);
			if(diff2.compareTo(podetail.getXqtypur()) == 1) {
				responseHelper.setErrorStatusAndMessage("GRN quantity can't be greater then purchase order quantity");
				return responseHelper.getResponse();
			}
			podetail.setXqtygrn(diff2);
		} else {  // for new item
			podetail.setXqtygrn(podetail.getXqtygrn().add(pogrnDetail.getXqtygrn()));
			if(podetail.getXqtygrn().compareTo(podetail.getXqtypur()) == 1) {
				responseHelper.setErrorStatusAndMessage("GRN quantity can't be greater then purchase order quantity");
				return responseHelper.getResponse();
			}
		}

		long count2 = poordService.updateDetail(podetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update GRN qty to purchase detail");
			return responseHelper.getResponse();
		}

		// modify line amount
		// first get item vat rate
		// split item from other data
		pogrnDetail.setXitem(pogrnDetail.getXitem().split("\\|")[0]);
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

			// now finally update purchase order header status
			if(!updatePurchaseOrderStatus(pgh)) {
				responseHelper.setErrorStatusAndMessage("Can't update purchase order status");
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/procurements/pogrn/pogrndetail/" + pogrnDetail.getXgrnnum());
			responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrn/pogrnheaderform/" + pogrnDetail.getXgrnnum());
			responseHelper.setThirdReloadSectionIdWithUrl("pogrnheadertable", "/procurements/pogrn/poordheadertable");
			responseHelper.setSuccessStatusAndMessage("GRN details updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pogrnService.saveDetail(pogrnDetail);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save GRN details");
			return responseHelper.getResponse();
		}

		// now finally update purchase order header status
		if(!updatePurchaseOrderStatus(pgh)) {
			responseHelper.setErrorStatusAndMessage("Can't update purchase order status");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/procurements/pogrn/pogrndetail/" + pogrnDetail.getXgrnnum());
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrn/pogrnheaderform/" + pogrnDetail.getXgrnnum());
		responseHelper.setThirdReloadSectionIdWithUrl("pogrnheadertable", "/procurements/pogrn/poordheadertable");
		responseHelper.setSuccessStatusAndMessage("GRN details saved successfully");
		return responseHelper.getResponse();
	}

	private boolean updatePurchaseOrderStatus(PogrnHeader pgh) {
		String status = "Full Received";
		List<PoordDetail> detailsList = poordService.findPoorddetailByXpornum(pgh.getXpornum());
		if(detailsList != null && !detailsList.isEmpty()) {
			for(PoordDetail pd : detailsList) {
				if(!pd.getXqtygrn().equals(pd.getXqtypur())) status = "GRN Created";
			}
		}
		PoordHeader ph = poordService.findPoordHeaderByXpornum(pgh.getXpornum());
		ph.setXstatuspor(status);
		long phcount = poordService.update(ph);
		if(phcount == 0) {
			return false;
		}
		return true;
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
		return "pages/purchasing/pogrn/pogrn::pogrndetailtable";
	}

	@GetMapping("/pogrnheaderform/{xgrnnum}")
	public String loadPogrnheaderform(@PathVariable String xgrnnum, Model model) {
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		return "pages/purchasing/pogrn/pogrn::pogrnheaderform";
	}

	@GetMapping("/poordheadertable")
	public String reloadPogrnHeaderTable(Model model) {
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeader());
		return "pages/purchasing/pogrn/pogrn::pogrnheadertable";
	}

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
		PoordDetail podetail = poordService.findPoorddetailByXpornumAndXrow(pgh.getXpornum(), pd.getXdocrow());
		if(podetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Puchase order detail for this item row");
			return responseHelper.getResponse();
		}
		podetail.setXqtygrn(podetail.getXqtygrn().subtract(pd.getXqtygrn()));
		long count2 = poordService.updateDetail(podetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update GRN qty to purchase detail");
			return responseHelper.getResponse();
		}

		// Update purchase order status
		if(!updatePurchaseOrderStatus(pgh)) {
			responseHelper.setErrorStatusAndMessage("Can't update purchase order status");
			return responseHelper.getResponse();
		}

		long count = pogrnService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete GRN detail");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/procurements/pogrn/pogrndetail/" + xgrnnum);
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrn/pogrnheaderform/" + xgrnnum);
		responseHelper.setThirdReloadSectionIdWithUrl("pogrnheadertable", "/procurements/pogrn/poordheadertable");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirmgrn/{xgrnnum}")
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
			responseHelper.setErrorStatusAndMessage("Warehouse required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(pogrnHeader.getXinvnum())) {
			responseHelper.setErrorStatusAndMessage("Supplier Bill No. required");
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

		if (!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusap())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.procTransferPOtoAP(pogrnHeader.getXgrnnum(), p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		if(isModuleActive(Modules.ACCOUNTING)) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.poTransferPOtoGL(xgrnnum, "pogrnheaderac", p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("GRN Confirmed successfully");
		responseHelper.setRedirectUrl("/procurements/pogrn/" + xgrnnum);
		return responseHelper.getResponse();
	}

	@PostMapping("/returngrn/{xgrnnum}")
	public @ResponseBody Map<String, Object> returngrn(@PathVariable String xgrnnum) {
		// Get PoordHeader record by Xpornum
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(pogrnHeader == null) {
			responseHelper.setErrorStatusAndMessage("GRN not found in the system to do return");
			return responseHelper.getResponse();
		}

		// validation
		if("GRN Returned".equalsIgnoreCase(pogrnHeader.getXstatusgrn())){
			responseHelper.setErrorStatusAndMessage("This GRN already returned. Return number is : " + pogrnHeader.getXcrnnum());
			return responseHelper.getResponse();
		}

		Pocrnheader pocrnHeader = new Pocrnheader();
		BeanUtils.copyProperties(pogrnHeader, pocrnHeader, "xdate", "xtype", "xtrngrn", "xnote");
//		pocrnHeader.setXtype(TransactionCodeType.PURCHASE_RETURN.getCode());
//		pocrnHeader.setXtrncrn(TransactionCodeType.PURCHASE_RETURN.getdefaultCode());
		pocrnHeader.setXgrnnum(xgrnnum);
		pocrnHeader.setXstatuscrn("Open");
		pocrnHeader.setXdate(new Date());
//		pocrnHeader.setXsup(pogrnHeader.getXcus());
		// pocrnHeader.setXtypetrn("??");

		long count = pocrnService.save(pocrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN Return");
			return responseHelper.getResponse();
		}

		pocrnHeader = pocrnService.findPocrnHeaderByXgrnnum(xgrnnum);

		// Get GRN items to copy them in CRN.
		List<PogrnDetail> pogrnDetailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		List<Pocrndetail> detailsList = new ArrayList<>();
		if(pogrnDetailList != null && !pogrnDetailList.isEmpty()) {
			for (int i = 0; i < pogrnDetailList.size(); i++) {
				Pocrndetail pocrnDetail = new Pocrndetail();
				// Copying PO items to GRN items.
				BeanUtils.copyProperties(pogrnDetailList.get(i), pocrnDetail, "xrow", "xnote");
				pocrnDetail.setXcrnnum(pocrnHeader.getXcrnnum());
				pocrnDetail.setXqtygrn(pogrnDetailList.get(i).getXqtygrn());
				pocrnDetail.setXunit(pogrnDetailList.get(i).getXunitpur());
				detailsList.add(pocrnDetail);
			}
		}

		if(!detailsList.isEmpty()) {
			try {
				long dcount = pocrnService.saveDetails(detailsList);
				if(dcount == 0) {
					responseHelper.setErrorStatusAndMessage("GRN Return detail not saved");
					return responseHelper.getResponse();
				}
			} catch (ServiceException e) {
				log.error(ERROR, e.getMessage(), e);
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}
		}

		// Update PoGRNHeader Status with pocrn reference
		if(isModuleActive(Modules.ACCOUNTING)) {
			pogrnHeader.setXcrnnum(pocrnHeader.getXcrnnum());
			pogrnHeader.setXstatusgrn("GRN Returned"); // Is it final?
			long pCount = pogrnService.update(pogrnHeader);
			if (pCount == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("GRN Returned successfully");
		responseHelper.setRedirectUrl("/procurements/pogrn/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/createprn")
	public @ResponseBody Map<String, Object> saveCrndetail(@RequestParam(value = "xgrnnum") String xgrnnum,
			@RequestParam(value = "rows[]") int[] rows) throws ServiceException {
		
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(pogrnHeader == null) {
			responseHelper.setErrorStatusAndMessage("GRN not found in the system to do return");
			return responseHelper.getResponse();
		}

		// validation
		if("GRN Returned".equalsIgnoreCase(pogrnHeader.getXstatusgrn())){
			responseHelper.setErrorStatusAndMessage("This GRN already returned. Return number is : " + pogrnHeader.getXcrnnum());
			return responseHelper.getResponse();
		}

		Pocrnheader pocrnHeader = new Pocrnheader();
		pocrnHeader.setXtypetrn(TransactionCodeType.PURCHASE_RETURN.getCode());
		pocrnHeader.setXtype(TransactionCodeType.PURCHASE_RETURN.getdefaultCode());
		pocrnHeader.setXstatuscrn("Open");
		pocrnHeader.setXdate(new Date());
		pocrnHeader.setXgrnnum(xgrnnum);
		pocrnHeader.setXcus(pogrnHeader.getXcus());
		pocrnHeader.setXwh(pogrnHeader.getXwh());
		pocrnHeader.setZid(sessionManager.getBusinessId());
		pocrnHeader.setZauserid(sessionManager.getLoggedInUserDetails().getUsername());
		pocrnHeader.setZuuserid(sessionManager.getLoggedInUserDetails().getUsername());
		pocrnHeader.setXpaymenttype("Others");
		pocrnHeader.setXstatusjv("Open");
		pocrnHeader.setXstatusap("Open");
		
		long count = pocrnService.save(pocrnHeader);
		if (count == 0 && count > 1) {
			responseHelper.setErrorStatusAndMessage("Can't create CRN Return");
			return responseHelper.getResponse();
		}
		
			// Get GRN items to copy them in CRN.
			for (int rowdata : rows) {
				PogrnDetail pogrndetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, rowdata);
				if (pogrndetail.getXqtyprn() == null) pogrndetail.setXqtyprn(BigDecimal.ZERO);
				if (pogrndetail.getXqtygrn() == null) pogrndetail.setXqtygrn(BigDecimal.ZERO);
				
				Pocrndetail pocrnDetail = new Pocrndetail();
				pocrnDetail.setXdocrow(pogrndetail.getXrow());
				pocrnDetail.setXcrnnum(pocrnHeader.getXcrnnum());
				pocrnDetail.setXitem(pogrndetail.getXitem());
				pocrnDetail.setXrate(pogrndetail.getXrate());
				pocrnDetail.setXqtyord(pogrndetail.getXqtygrn());
				pocrnDetail.setXunit(pogrndetail.getXunitpur());
				pocrnDetail.setXlineamt(pogrndetail.getXqtygrn().multiply(pogrndetail.getXrate()));

				// Validation
				if (BigDecimal.ZERO.equals(pocrnDetail.getXqtygrn())) continue;
				
				long dcount = pocrnService.saveDetail(pocrnDetail);
				if (dcount == 0) {
					responseHelper.setErrorStatusAndMessage("Can't Save Details");
					return responseHelper.getResponse();
				}
				
				pogrndetail.setXqtyprn(pogrndetail.getXqtygrn());
				
				long update = pogrnService.updateDetail(pogrndetail);
				if(update == 0) throw new ServiceException("Can't update purchase detail");

			}
		

		// Update PoGRNHeader Status with pocrn reference
		if(isModuleActive(Modules.ACCOUNTING)) {
			pogrnHeader.setXcrnnum(pocrnHeader.getXcrnnum());
			pogrnHeader.setXstatusgrn("GRN Returned"); // Is it final?
			long pCount = pogrnService.update(pogrnHeader);
			if (pCount == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
		}
		
		responseHelper.setRedirectUrl("/procurements/pogrn/"+xgrnnum);
		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		return responseHelper.getResponse();
	}

	
	@GetMapping("/print/{xgrnnum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String xgrnnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		PogrnHeader oh = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (oh == null) {
			message = "Good Receipt Note not found to print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Cacus cacus = cacusService.findByXcus(oh.getXcus());

		// SalesOrderChalanReport orderReport = new SalesOrderChalanReport();

		GrnReport report = new GrnReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Goods Receipt Note");
		report.setFromDate(sdf.format(oh.getXdate()));
		report.setToDate(sdf.format(oh.getXdate()));
		report.setPrintDate(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());

		GRNOrder grn = new GRNOrder();
		grn.setOrderNumber(oh.getXgrnnum());
		grn.setPoNumber(oh.getXpornum());
		grn.setReturnNumber(oh.getXcrnnum());
		grn.setSupplier(cacus.getXcus());
		grn.setSupplierName(cacus.getXorg());
		grn.setSupplierAddress(cacus.getXmadd());
		grn.setXhwh(oh.getXhwh());
		grn.setProjectName(oh.getProjectName());
		grn.setWarehouse(oh.getXwh());
		grn.setStorename(oh.getStorename());
		grn.setXpreparer(oh.getXpreparer());
		grn.setXpreparername(oh.getXpreparername());
		grn.setXinvnum(oh.getXinvnum());
		grn.setXvoucher(oh.getXvoucher());
		grn.setXtype("Other");
		grn.setXstatusgrn(oh.getXstatusgrn());
		grn.setXnote(oh.getXnote());
		grn.setDate(sdf.format(oh.getXdate()));
		grn.setVatAit(oh.getXvatait());
		grn.setTotalAmount(oh.getXtotamt() != null ? oh.getXtotamt().toString() : BigDecimal.ZERO.toString());
		grn.setVatAmount(oh.getXvatamt() != null ? oh.getXvatamt().toString() : BigDecimal.ZERO.toString());
		grn.setTaxAmount(oh.getXaitamt() != null ? oh.getXaitamt().toString() : BigDecimal.ZERO.toString());
		grn.setDiscountAmount(oh.getXdiscprime() != null ? oh.getXdiscprime().toString() : BigDecimal.ZERO.toString());
		grn.setGrandTotalAmount(oh.getXgrandtot() != null ? oh.getXgrandtot().toString() : BigDecimal.ZERO.toString());
		grn.setTotalQty(BigDecimal.ZERO);
		grn.setSpellword(oh.getSpellword());
		grn.setXprimeword(oh.getXprimeword());

		List<PogrnDetail> items = pogrnService.findPogrnDetailByXgrnnum(oh.getXgrnnum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXitemdesc());
				item.setItemQty(it.getXqtygrn() != null ? it.getXqtygrn().toString() : BigDecimal.ZERO.toString());
				item.setItemRate(it.getXrate() != null ? it.getXrate().toString() : BigDecimal.ZERO.toString());
				item.setItemTotalAmount(it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());
				item.setItemUnit(it.getXunitpur());
				item.setItemCategory(it.getXcatitem());
				item.setItemGroup(it.getXgitem());
				item.setXcfpur(it.getXcfpur());
				item.setXnote(it.getXnote());
				
				grn.getItems().add(item);
				
				grn.setTotalQty(grn.getTotalQty().add(BigDecimal.valueOf(Double.valueOf(item.getItemQty()))));
			});
		}

		report.getGrnorders().add(grn);

		byte[] byt = getPDFByte(report, "grnreport.xsl", request);
		if (byt == null) {
			message = "Can't print report for GRN : " + xgrnnum;
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
