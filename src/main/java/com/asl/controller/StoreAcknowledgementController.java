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

import com.asl.entity.Caitem;
import com.asl.entity.DataList;
import com.asl.entity.Imstock;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opordheader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesReturnReport;
import com.asl.model.report.StoreTransferReport;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.ImtorService;
import com.asl.service.OpordService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/inventory/storeacknow")
public class StoreAcknowledgementController extends ASLAbstractController {

	@Autowired private ImtorService imtorService;
	@Autowired private XcodesService xcodeService;
	@Autowired private ImstockService imstockService;
	@Autowired private CaitemService caitemService;
	@Autowired private OpordService opordService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadTransferOrderdPage(Model model) {
		model.addAttribute("imtorheader", getDefaultImtorHeader());
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXcode(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));


		model.addAttribute("hideextratable", false);
		
		List<DataList> dl = listService.getList("SYSTEM", "REQ_PROD_TO_SUPPLY_CHAIN");
		if(dl != null && !dl.isEmpty()) {
			DataList d = dl.get(0);
			if(StringUtils.isNotBlank(d.getListvalue2()) && "Y".equalsIgnoreCase(d.getListvalue2())) {
				model.addAttribute("hideextratable", true);
			}
		}
	
		return "pages/inventory/storeacknow/imtor";
	}

	@GetMapping("/{xtornum}")
	public String loadPoordPage(@PathVariable String xtornum, Model model) {
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		if(data == null) return "redirect:/inventory/storeacknow";

		model.addAttribute("imtorheader", data);
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXtype(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		model.addAttribute("sites", projectstoreviewService.getProjectStoresByXtype(data.getXfhwh()));
		
		//String storename = xcodesService.findByXtypesAndXcodes(CodeType.STORE.getCode(), data.getXtwh(), Boolean.TRUE).getXlong();
		//model.addAttribute("sessionstore", StringUtils.isBlank(storename) ? sessionManager.getLoggedInUserDetails().getStorename() : storename);

		model.addAttribute("hideextratable", false);
		
		List<DataList> dl = listService.getList("SYSTEM", "REQ_PROD_TO_SUPPLY_CHAIN");
		if(dl != null && !dl.isEmpty()) {
			DataList d = dl.get(0);
			if(StringUtils.isNotBlank(d.getListvalue2()) && "Y".equalsIgnoreCase(d.getListvalue2())) {
				model.addAttribute("hideextratable", true);
			}
		}

		List<ImtorDetail> imtorDetails = imtorService.findImtorDetailByXtornum(xtornum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		if(imtorDetails != null && !imtorDetails.isEmpty()) {
			for(ImtorDetail pd : imtorDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		
		return "pages/inventory/storeacknow/imtor";
	}

	private ImtorHeader getDefaultImtorHeader() {
		ImtorHeader imtorHeader = new ImtorHeader();
		imtorHeader.setXtypetrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode());
		imtorHeader.setXtrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getdefaultCode());
		imtorHeader.setXstatus("Confirmed");
		//imtorHeader.setXacknowledge(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());
		imtorHeader.setXdate(imtorHeader.getXreqdate());
		//SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		//imtorHeader.setXsignatorydate1(sdf.format(imtorHeader.getXreqdate()));
		return imtorHeader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(ImtorHeader imtorHeader, BindingResult bindingResult){
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		//imtorHeader.setXacknowledge(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());
		imtorHeader.setXstatustor("Approved");
		imtorHeader.setXthwh(imtorHeader.getXfhwh());
		//SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		//imtorHeader.setXsignatorydate1(sdf.format(imtorHeader.getXreqdate()));

		// Validate
		if(imtorHeader.getXfwh().equalsIgnoreCase(imtorHeader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Requisition can't be transfered to same store !");
			return responseHelper.getResponse();
		}
		
		if((imtorHeader.getXdaterec()).before(imtorHeader.getXreqdate())){
			responseHelper.setErrorStatusAndMessage("Receive Date & Time can't be before Requisition Date !");
			return responseHelper.getResponse(); 
		}
		
		if((imtorHeader.getXdaterec()).before(imtorHeader.getXsignatorydate1())){
			responseHelper.setErrorStatusAndMessage("Receive Date & Time can't be before Signatory Date !");
			return responseHelper.getResponse(); 
		}

		// If existing
		ImtorHeader existImtorHeader = imtorService.findImtorHeaderByXtornum(imtorHeader.getXtornum());
		if(existImtorHeader != null) {
			BeanUtils.copyProperties(imtorHeader, existImtorHeader, "xtypetrn", "xtrn","xreqdate","xsignatorydate1");
			long count = imtorService.update(existImtorHeader);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Transfer Order");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Requisition Order Updated Successfully");
			responseHelper.setRedirectUrl("/inventory/storeacknow/" + existImtorHeader.getXtornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Requisition Order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Requisition Order Created Successfully");
		responseHelper.setRedirectUrl("/inventory/storeacknow/" + imtorHeader.getXtornum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xtornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't archive Requisition order");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> details = imtorService.findImtorDetailByXtornum(xtornum);
		if(details != null && !details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete all Requisition order details first");
			return responseHelper.getResponse();
		}


		long count = imtorService.delete(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Requisition order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted Successfully");
		responseHelper.setRedirectUrl("/inventory/storeacknow");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm/{xtornum}")
	public @ResponseBody Map<String, Object> approve(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(!isKhanas() && StringUtils.isBlank(imtorHeader.getXacknowledgenote())) {
			responseHelper.setErrorStatusAndMessage("Acknowledge note required");
			return responseHelper.getResponse();
		}
		
		if("Confirmed".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			responseHelper.setErrorStatusAndMessage("Requisition Order already confirmed");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> imtorDetailList = imtorService.findImtorDetailByXtornum(xtornum);
		if(imtorDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}
		
		// Stock validation check

		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			imtorService.procConfirmTO(xtornum, "Issue and Receive", p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}
		imtorHeader.setXdate(imtorHeader.getXdaterec());

		responseHelper.setSuccessStatusAndMessage("Requisition Order confirmed successfully");
		responseHelper.setRedirectUrl("/inventory/storeacknow/" + xtornum);
		return responseHelper.getResponse();
	}
	
	@PostMapping("/reject/{xtornum}")
	public @ResponseBody Map<String, Object> reject(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(StringUtils.isBlank(imtorHeader.getXacknowledgenote())) {
			responseHelper.setErrorStatusAndMessage("Acknowledge note required");
			return responseHelper.getResponse();
		}
		
		
		if("Rejected".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			responseHelper.setErrorStatusAndMessage("Requisition Order already rejected");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> imtorDetailList = imtorService.findImtorDetailByXtornum(xtornum);
		if(imtorDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}

		imtorHeader.setXstatustor("Dismised");
		long count = imtorService.update(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't reject Requisition Order");
			return responseHelper.getResponse();
		}
		imtorHeader.setXdate(imtorHeader.getXdaterec());

		responseHelper.setSuccessStatusAndMessage("Requisition Order rejected successfully");
		responseHelper.setRedirectUrl("/inventory/storeacknow/" + xtornum);
		return responseHelper.getResponse();
	}
	
	@GetMapping("{xtornum}/imtordetail/{xrow}/show")
	public String openImtorDetailModal(@PathVariable String xtornum, @PathVariable String xrow, Model model) {
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) return "redirect:/inventory/storeacknow";

		model.addAttribute("fromWarehouse", imtorHeader.getXfwh());
		model.addAttribute("toWarehouse", imtorHeader.getXfwh());

		if("new".equalsIgnoreCase(xrow)) {
			ImtorDetail imtordetail = new ImtorDetail();
			imtordetail.setXtornum(xtornum);
			imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("imtordetail", imtordetail);
			model.addAttribute("availablestock", Collections.emptyList());
		} else {
			ImtorDetail imtordetail = imtorService.findImtorDetailByXtornumAndXrow(xtornum, Integer.parseInt(xrow));
			if(imtordetail == null) {
				imtordetail = new ImtorDetail();
				imtordetail.setXtornum(xtornum);
				imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("imtordetail", imtordetail);
			model.addAttribute("availablestock", imstockService.findByXitem(imtordetail.getXitem()));
		}
		return "pages/inventory/storeacknow/imtordetailmodal::imtordetailmodal";
	}

	@PostMapping("/imtordetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(ImtorDetail imtorDetail, String xfwh){
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum()) || StringUtils.isBlank(imtorDetail.getXitem())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(imtorDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item");
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if(imtorDetail.getXrow() == 0 && imtorService.findImtorDetailByXtornumAndXitem(imtorDetail.getXtornum(), imtorDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		if(imtorDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please enter valid quantity");
			return responseHelper.getResponse();
		}

		// if existing
		ImtorDetail existDetail = imtorService.findImtorDetailByXtornumAndXrow(imtorDetail.getXtornum(), imtorDetail.getXrow());
		if(existDetail != null) {
			
			BeanUtils.copyProperties(imtorDetail, existDetail, "xtornum", "xrow");
			long count = imtorService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/storeacknow/imtordetail/" + imtorDetail.getXtornum());
			responseHelper.setSuccessStatusAndMessage("Requisition Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imtorService.saveDetail(imtorDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/storeacknow/imtordetail/" + imtorDetail.getXtornum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/imtordetail/{xtornum}")
	public String reloadImtorDetailTabble(@PathVariable String xtornum, Model model) {
		List<ImtorDetail> imtorDetails = imtorService.findImtorDetailByXtornum(xtornum);
		model.addAttribute("imtordetailsList", imtorDetails);
		model.addAttribute("imtorheader", imtorService.findImtorHeaderByXtornum(xtornum));
		
		BigDecimal totalQuantity = BigDecimal.ZERO;
		if(imtorDetails != null && !imtorDetails.isEmpty()) {
			for(ImtorDetail pd : imtorDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		return "pages/inventory/storeacknow/imtor::imtordetailtable";
	}

	@PostMapping("{xtornum}/imtordetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteImtorDetail(@PathVariable String xtornum, @PathVariable String xrow, Model model) {
		ImtorDetail detail = imtorService.findImtorDetailByXtornumAndXrow(xtornum, Integer.parseInt(xrow));
		if(detail == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imtorService.deleteDetail(detail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/storeacknow/imtordetail/" + xtornum);
		return responseHelper.getResponse();
	}


}
