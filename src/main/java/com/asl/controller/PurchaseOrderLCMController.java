package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.Poterms;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CaitemService;
import com.asl.service.PogrnService;
import com.asl.service.PoordService;

@Controller
@RequestMapping("/cost/poordheader")
public class PurchaseOrderLCMController extends ASLAbstractController {

	@Autowired private PoordService poordService;
	@Autowired private CaitemService caitemService;
	@Autowired private PogrnService pogrnService;

	@GetMapping
	public String loadPoordPage(Model model) {
		model.addAttribute("poordheader", getDefaultPoordHeader());
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode()));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode(), Boolean.TRUE));
		model.addAttribute("branchs", xcodesService.findByXtype(CodeType.BRANCH.getCode(), Boolean.TRUE));
		model.addAttribute("deliveryLocations", xcodesService.findByXtype(CodeType.DELIVERY_LOCATION.getCode(), Boolean.TRUE));
		model.addAttribute("currency", xcodesService.findByXtype(CodeType.CURRENCY.getCode(), Boolean.TRUE));
		return "pages/cost/poord/poord";
	}

	@GetMapping("/{xpornum}")
	public String loadPoordPage(@PathVariable String xpornum, Model model) {
		PoordHeader data = poordService.findPoordHeaderByXpornum(xpornum);
		if(data == null) data = getDefaultPoordHeader();
		if(data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO);

		model.addAttribute("poordheader", data);
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode()));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode(), Boolean.TRUE));
		model.addAttribute("branchs", xcodesService.findByXtype(CodeType.BRANCH.getCode(), Boolean.TRUE));
		model.addAttribute("deliveryLocations", xcodesService.findByXtype(CodeType.DELIVERY_LOCATION.getCode(), Boolean.TRUE));
		model.addAttribute("currency", xcodesService.findByXtype(CodeType.CURRENCY.getCode(), Boolean.TRUE));
		
		model.addAttribute("poorddetailsList", poordService.findPoorddetailByXpornum(xpornum));
		model.addAttribute("grnlist", pogrnService.findPogrnHeaderByXpornum(xpornum));
		//model.addAttribute("lddlist", poordService.findPotermsByXpornum(xpornum));
		
		List<PoordDetail> poordDetails = poordService.findPoorddetailByXpornum(xpornum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalNetAmount = BigDecimal.ZERO;
		if(poordDetails != null && !poordDetails.isEmpty()) {
			for(PoordDetail pd : poordDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtypur() == null ? BigDecimal.ZERO : pd.getXqtypur());
				totalNetAmount = totalNetAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalNetAmount", totalNetAmount);
		
		return "pages/cost/poord/poord";
	}

	@GetMapping("/clear")
	public String clearPoordForm(Model model) {
		model.addAttribute("poordheader", getDefaultPoordHeader());
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode()));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode()));
		model.addAttribute("branchs", xcodesService.findByXtype(CodeType.BRANCH.getCode(), Boolean.TRUE));
		model.addAttribute("deliveryLocations", xcodesService.findByXtype(CodeType.DELIVERY_LOCATION.getCode(), Boolean.TRUE));
		model.addAttribute("currency", xcodesService.findByXtype(CodeType.CURRENCY.getCode(), Boolean.TRUE));
		
		return "pages/cost/poord/poord::poordheaderform";
	}

	private PoordHeader getDefaultPoordHeader() {
		PoordHeader poord = new PoordHeader();
		poord.setXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode());
		poord.setXstatuspor("Open");
		poord.setXstatus("Open");
		poord.setXtotamt(BigDecimal.ZERO);
		poord.setXlcno("");
		poord.setXtype("LC");
		poord.setXexch(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
		poord.setXpreparer(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());
		
		
		return poord;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PoordHeader poordHeader, BindingResult bindingResult){
		// Validate
		if(StringUtils.isBlank(poordHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}
		poordHeader.setXlcno("");
		//poordHeader.setXpreparer(sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());
		poordHeader.setXpreparer(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());
		
		// if existing record
		if(StringUtils.isNotBlank(poordHeader.getXpornum())) {
			PoordHeader exist = poordService.findPoordHeaderByXpornum(poordHeader.getXpornum());

			BeanUtils.copyProperties(poordHeader, exist, "xpornum", "xtypetrn", "xtrn", "xtotamt");
			long count = poordService.update(exist);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Purchase Order updated successfully");
			responseHelper.setRedirectUrl("/cost/poordheader/" + poordHeader.getXpornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = poordService.save(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase Order created successfully");
		responseHelper.setRedirectUrl("/cost/poordheader/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xpornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xpornum){
		return doArchiveOrRestore(xpornum, true);
	}

	private Map<String, Object> doArchiveOrRestore(String xpornum, boolean archive){
		// todo: need to delete actually from db
		
		PoordHeader poordHeader = poordService.findPoordHeaderByXpornum(xpornum);
		if(poordHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find purchase order " + xpornum);
			return responseHelper.getResponse();
		}
		if(!"Open".equalsIgnoreCase(poordHeader.getXstatuspor())) {
			responseHelper.setErrorStatusAndMessage("Can't delete purhcase order, because order is not Open");
			return responseHelper.getResponse();
		}

		// check order has detail
		List<PoordDetail> details = poordService.findPoorddetailByXpornum(xpornum);
		if(details != null && !details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete all order details first");
			return responseHelper.getResponse();
		}

		long count = poordService.deletePoordheaderByXpornum(xpornum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Delete Purchase Order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase order deleted successfully");
		responseHelper.setRedirectUrl("/cost/poordheader");
		return responseHelper.getResponse();
	}

	@GetMapping("{xpornum}/poorddetail/{xrow}/show")
	public String openPoordDetailModal(@PathVariable String xpornum, @PathVariable String xrow, Model model) {

		model.addAttribute("purchaseUnit", xcodesService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			PoordDetail poorddetail = new PoordDetail();
			poorddetail.setXpornum(xpornum);
			poorddetail.setXqtypur(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			poorddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			poorddetail.setXstatus("Open");
			poorddetail.setXlineamt(poorddetail.getXqtypur().multiply(poorddetail.getXrate()));
			model.addAttribute("poorddetail", poorddetail);
		} else {
			PoordDetail poorddetail = poordService.findPoorddetailByXpornumAndXrow(xpornum, Integer.parseInt(xrow));
			if(poorddetail == null) {
				poorddetail = new PoordDetail();
				poorddetail.setXpornum(xpornum);
				poorddetail.setXqtypur(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				poorddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				poorddetail.setXstatus("Open");
				poorddetail.setXlineamt(poorddetail.getXqtypur().multiply(poorddetail.getXrate()));
			}
			model.addAttribute("poorddetail", poorddetail);
		}

		return "pages/cost/poord/poorddetailmodal::poorddetailmodal";
	}

	@PostMapping("/poorddetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(PoordDetail poordDetail){
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		poordDetail.setXstatus("Open");

		// validation
		if(StringUtils.isBlank(poordDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item");
			return responseHelper.getResponse();
		}
		if(poordDetail.getXqtypur() == null || BigDecimal.ZERO.equals(poordDetail.getXqtypur()) || poordDetail.getXqtypur().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Purchase quantity should be greater then 0");
			return responseHelper.getResponse();
		}

		// modify line amount
		// first get item vat rate
		Caitem caitem = caitemService.findByXitem(poordDetail.getXitem());
		if(caitem == null) {
			responseHelper.setErrorStatusAndMessage("Item not found");
			return responseHelper.getResponse();
		}
		if(caitem.getXvatrate() == null) caitem.setXvatrate(BigDecimal.ZERO);

		poordDetail.setXlineamt(poordDetail.getXqtypur().multiply(poordDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		PoordDetail existDetail = poordService.findPoorddetailByXpornumAndXrow(poordDetail.getXpornum(), poordDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(poordDetail, existDetail, "xpornum", "xrow");
			long count = poordService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update order detail");
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("poorddetailtable", "/cost/poordheader/poorddetail/" + poordDetail.getXpornum());
			responseHelper.setSecondReloadSectionIdWithUrl("poordheaderform", "/cost/poordheader/poordheaderform/" + poordDetail.getXpornum());
			responseHelper.setThirdReloadSectionIdWithUrl("poordheadertable", "/cost/poordheader/poordheadertable");
			responseHelper.setSuccessStatusAndMessage("Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = poordService.saveDetail(poordDetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save order detail");
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("poorddetailtable", "/cost/poordheader/poorddetail/" + poordDetail.getXpornum());
		responseHelper.setSecondReloadSectionIdWithUrl("poordheaderform", "/cost/poordheader/poordheaderform/" + poordDetail.getXpornum());
		responseHelper.setThirdReloadSectionIdWithUrl("poordheadertable", "/cost/poordheader/poordheadertable");
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/poorddetail/{xpornum}")
	public String reloadPoordDetailTabble(@PathVariable String xpornum, Model model) {
		List<PoordDetail> poordDetails = poordService.findPoorddetailByXpornum(xpornum);
		model.addAttribute("poorddetailsList", poordDetails);
		model.addAttribute("poordheader", poordService.findPoordHeaderByXpornum(xpornum));
		
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalNetAmount = BigDecimal.ZERO;
		if(poordDetails != null && !poordDetails.isEmpty()) {
			for(PoordDetail pd : poordDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtypur() == null ? BigDecimal.ZERO : pd.getXqtypur());
				totalNetAmount = totalNetAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalNetAmount", totalNetAmount);
		return "pages/cost/poord/poord::poorddetailtable";
	}

	@GetMapping("/poordheaderform/{xpornum}")
	public String reloadPoordheaderForm(@PathVariable String xpornum, Model model) {
		model.addAttribute("poprefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode(), Boolean.TRUE));
		model.addAttribute("poordheader", poordService.findPoordHeaderByXpornum(xpornum));
		
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode()));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode(), Boolean.TRUE));
		model.addAttribute("branchs", xcodesService.findByXtype(CodeType.BRANCH.getCode(), Boolean.TRUE));
		model.addAttribute("deliveryLocations", xcodesService.findByXtype(CodeType.DELIVERY_LOCATION.getCode(), Boolean.TRUE));
		model.addAttribute("currency", xcodesService.findByXtype(CodeType.CURRENCY.getCode(), Boolean.TRUE));
		return "pages/cost/poord/poord::poordheaderform";
	}

	@GetMapping("/poordheadertable")
	public String reloadPoordHeaderTable(Model model) {
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER_LCM.getCode()));
		return "pages/cost/poord/poord::poordheadertable";
	}

	@PostMapping("{xpornum}/poorddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePoordDetail(@PathVariable String xpornum, @PathVariable String xrow, Model model) {
		PoordDetail pd = poordService.findPoorddetailByXpornumAndXrow(xpornum, Integer.parseInt(xrow));
		if(pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = poordService.deleteDetail(pd);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("poorddetailtable", "/cost/poordheader/poorddetail/" + xpornum);
		responseHelper.setSecondReloadSectionIdWithUrl("poordheaderform", "/cost/poordheader/poordheaderform/" + xpornum);
		responseHelper.setThirdReloadSectionIdWithUrl("poordheadertable", "/cost/poordheader/poordheadertable");
		return responseHelper.getResponse();
	}
	
	//start of Terms & Conditions
	/*
	 * @GetMapping("/{xpornum}/potermsdetail/{xrow}/show") public String
	 * loadPoTermsModal(@PathVariable String xpornum, @PathVariable String xrow,
	 * Model model) { if("new".equalsIgnoreCase(xrow)) { Poterms ldd = new
	 * Poterms(); ldd.setXcode(""); ldd.setXpornum(xpornum); ldd.setXnote("");
	 * model.addAttribute("ldd", ldd); model.addAttribute("termsAndConditions",
	 * xcodesService.findByXtype(CodeType.TERMS_AND_CONDITION.getCode(),
	 * Boolean.TRUE)); } else { Poterms ldd =
	 * poordService.findPotermsByXpornumAndXrow(xpornum, Integer.parseInt(xrow));
	 * if(ldd==null) { ldd = new Poterms();
	 * 
	 * } model.addAttribute("ldd", ldd); model.addAttribute("termsAndConditions",
	 * xcodesService.findByXtype(CodeType.TERMS_AND_CONDITION.getCode(),
	 * Boolean.TRUE)); }
	 * 
	 * return "pages/cost/poord/potermsmodal::potermsmodal"; }
	 * 
	 * @PostMapping("/potermsdetail/save") public @ResponseBody Map<String, Object>
	 * saveLandDagDetails(Poterms po) { if (po == null ||
	 * StringUtils.isBlank(po.getXpornum())) {
	 * responseHelper.setStatus(ResponseStatus.ERROR); return
	 * responseHelper.getResponse(); }
	 * 
	 * // if existing Poterms exist =
	 * poordService.findPotermsByXpornumAndXrow(po.getXpornum(), po.getXrow()); if
	 * (exist != null) { BeanUtils.copyProperties(po, exist,"xpornum"); long count =
	 * poordService.updatePoterms(exist); if (count == 0) {
	 * responseHelper.setStatus(ResponseStatus.ERROR); return
	 * responseHelper.getResponse(); }
	 * responseHelper.setReloadSectionIdWithUrl("potermstable",
	 * "/cost/poordheader/potermsdetail/" + po.getXpornum()); responseHelper.
	 * setSuccessStatusAndMessage("Terms & Conditions updated successfully"); return
	 * responseHelper.getResponse(); }
	 * 
	 * 
	 * // if new detail long count = poordService.savePoterms(po); if (count == 0) {
	 * responseHelper.setStatus(ResponseStatus.ERROR); return
	 * responseHelper.getResponse(); }
	 * responseHelper.setReloadSectionIdWithUrl("potermstable",
	 * "/cost/poordheader/potermsdetail/" + po.getXpornum()); responseHelper.
	 * setSuccessStatusAndMessage("Terms & Conditions updated successfully"); return
	 * responseHelper.getResponse(); }
	 * 
	 * @GetMapping("/potermsdetail/{xpornum}") public String
	 * reloadPoTermsTable(@PathVariable String xpornum, Model model) { List<Poterms>
	 * dagList = poordService.findPotermsByXpornum(xpornum);
	 * model.addAttribute("lddlist", dagList); model.addAttribute("poordheader",
	 * poordService.findPoordHeaderByXpornum(xpornum)); return
	 * "pages/cost/poord/poord::potermstable"; }
	 * 
	 * //delete
	 * 
	 * @PostMapping("{xpornum}/potermsdetail/{xrow}/delete") public @ResponseBody
	 * Map<String, Object> deleteLandDagDetails(@PathVariable String
	 * xpornum, @PathVariable String xrow, Model model) { Poterms ldd =
	 * poordService.findPotermsByXpornumAndXrow(xpornum, Integer.parseInt(xrow));
	 * if(ldd == null) { responseHelper.setStatus(ResponseStatus.ERROR); return
	 * responseHelper.getResponse(); }
	 * 
	 * long count = poordService.deletePoterms(ldd); if(count == 0) {
	 * responseHelper.setStatus(ResponseStatus.ERROR); return
	 * responseHelper.getResponse(); }
	 * 
	 * responseHelper.setSuccessStatusAndMessage("Deleted successfully");
	 * responseHelper.setReloadSectionIdWithUrl("potermstable",
	 * "/cost/poordheader/potermsdetail/" + xpornum); return
	 * responseHelper.getResponse(); }
	 */
}
