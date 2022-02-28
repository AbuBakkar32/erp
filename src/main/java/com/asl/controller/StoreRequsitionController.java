package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.Opordheader;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ProductionSuggestion;
import com.asl.model.ServiceException;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.StoreRequisitionReport;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.ImtorService;
import com.asl.service.ProductionSuggestionService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/inventory/storereq")
public class StoreRequsitionController extends ASLAbstractController {

	@Autowired private ImtorService imtorService;
	@Autowired private XcodesService xcodeService;
	@Autowired private ImstockService imstockService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProductionSuggestionService productionSuggestionService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadTransferOrderdPage(Model model) {
		model.addAttribute("imtorheader", getDefaultImtorHeader());
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXcode(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("sessionstore", sessionManager.getLoggedInUserDetails().getStorename());
		model.addAttribute("hideextratable", false);
		model.addAttribute("allcodes", Collections.emptyList());
		
		List<DataList> dl = listService.getList("SYSTEM", "REQ_PROD_TO_SUPPLY_CHAIN");
		if(dl != null && !dl.isEmpty()) {
			DataList d = dl.get(0);
			if(StringUtils.isNotBlank(d.getListvalue2()) && "Y".equalsIgnoreCase(d.getListvalue2())) {
				model.addAttribute("hideextratable", true);
			}
		}
		
		return "pages/inventory/storereq/imtor";
	}

	@GetMapping("/{xtornum}")
	public String loadPoordPage(@PathVariable String xtornum, Model model) {
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		if(data == null) data = getDefaultImtorHeader();

		model.addAttribute("imtorheader", data);
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXtype(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));

		//String storename = xcodesService.findByXtypesAndXcodes(CodeType.STORE.getCode(), data.getXtwh(), Boolean.TRUE).getXlong();
		//model.addAttribute("sessionstore", StringUtils.isBlank(storename) ? sessionManager.getLoggedInUserDetails().getStorename() : storename);
		
		model.addAttribute("hideextratable", false);
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXfhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
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

		return "pages/inventory/storereq/imtor";
	}

	private ImtorHeader getDefaultImtorHeader() {
		ImtorHeader imtorHeader = new ImtorHeader();
		imtorHeader.setXtypetrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode());
		imtorHeader.setXtrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getdefaultCode());
		imtorHeader.setXstatus("Open");
		//imtorHeader.setXtwh(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXwh()) ? "" :sessionManager.getLoggedInUserDetails().getXwh());
		//imtorHeader.setXpreparer(StringUtils.isBlank(sessionManager.getLoggedInUserDetails().getXstaff()) ? "" :sessionManager.getLoggedInUserDetails().getXstaff()+'-'+ sessionManager.getLoggedInUserDetails().getStaffname());
		imtorHeader.setXdate(imtorHeader.getXreqdate());
		
		return imtorHeader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(ImtorHeader imtorHeader, BindingResult bindingResult){
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		imtorHeader.setXstatustor("Open");
		imtorHeader.setXsignatorydate1(null);
		imtorHeader.setXdaterec(null);
		imtorHeader.setXthwh(imtorHeader.getXfhwh());

		// Validate
		if(imtorHeader.getXfwh().equalsIgnoreCase(imtorHeader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Requisition can't be transfered to same store !");
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(imtorHeader.getXfhwh())) {
			responseHelper.setErrorStatusAndMessage("Project/Business requied.");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(imtorHeader.getXfwh())) {
			responseHelper.setErrorStatusAndMessage("Site/Store required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(imtorHeader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Site/Store required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(imtorHeader.getXpreparer())) {
			responseHelper.setErrorStatusAndMessage("Preparer required");
			return responseHelper.getResponse();
		}
		if(!isKhanas() && StringUtils.isBlank(imtorHeader.getXlong())) {
			responseHelper.setErrorStatusAndMessage("Preparer's note required");
			return responseHelper.getResponse();
		}

		// If existing
		ImtorHeader existImtorHeader = imtorService.findImtorHeaderByXtornum(imtorHeader.getXtornum());
		if(existImtorHeader != null) {
			BeanUtils.copyProperties(imtorHeader, existImtorHeader, "xtypetrn", "xtrn");
			long count = imtorService.update(existImtorHeader);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Requisition Order");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Requisition Order Updated Successfully");
			responseHelper.setRedirectUrl("/inventory/storereq/" + existImtorHeader.getXtornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Requisition Order");
			return responseHelper.getResponse();
		}


		// if has production chalan reference then create detail items from suggestion
		if(StringUtils.isNotBlank(imtorHeader.getXchalanref())) {
			List<ProductionSuggestion> list = productionSuggestionService.getProductionSuggestion(imtorHeader.getXchalanref(), null);
			if(list != null && !list.isEmpty()) {

				// Map of final products with their desired qty
				Map<String, BigDecimal> m = new HashMap<>();
				for(ProductionSuggestion p : list) {
					if(m.get(p.getXrawitem()) != null) {
						BigDecimal qty = p.getXrawqty() != null ? p.getXrawqty() : BigDecimal.ZERO;
						m.put(p.getXrawitem(), m.get(p.getXrawitem()).add(qty));
					} else {
						m.put(p.getXrawitem() , p.getXrawqty() != null ? p.getXrawqty() : BigDecimal.ZERO);
					}
				}

				// now create detail items from this map
				List<ImtorDetail> detailsList = new ArrayList<>();
				for(Map.Entry<String, BigDecimal> item : m.entrySet()) {
					Caitem c = caitemService.findByXitem(item.getKey());
					if(c == null) continue;

					ImtorDetail imtordetail = new ImtorDetail();
					imtordetail.setXtornum(imtorHeader.getXtornum());
					imtordetail.setXitem(c.getXitem());
					imtordetail.setXqtyord(item.getValue() == null ? BigDecimal.ZERO.setScale(2, RoundingMode.DOWN) : item.getValue().setScale(2, RoundingMode.DOWN));
					imtordetail.setXrate(c.getXrate() == null ? BigDecimal.ZERO.setScale(2, RoundingMode.DOWN) : c.getXrate().setScale(2, RoundingMode.DOWN));
					imtordetail.setXunit(c.getXunitpur());

					detailsList.add(imtordetail);
				}
				long countlist = 0;
				try {
					countlist = imtorService.saveDetail(detailsList);
				} catch (ServiceException e) {
					log.error(ERROR, e.getMessage(), e);
				}
				if(countlist == 0) {
					responseHelper.setErrorStatusAndMessage("Can't Create Requisition Details from suggestions");
					return responseHelper.getResponse();
				}

			}
		}

		responseHelper.setSuccessStatusAndMessage("Requisition Order Created Successfully");
		responseHelper.setRedirectUrl("/inventory/storereq/" + imtorHeader.getXtornum());
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
		responseHelper.setRedirectUrl("/inventory/storereq");
		return responseHelper.getResponse();
	}

	@GetMapping("{xtornum}/imtordetail/{xrow}/show")
	public String openImtorDetailModal(@PathVariable String xtornum, @PathVariable String xrow, Model model) {
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) return "redirect:/inventory/storereq";

		model.addAttribute("fromWarehouse", imtorHeader.getXfwh());
		model.addAttribute("toWarehouse", imtorHeader.getXfwh());

		if("new".equalsIgnoreCase(xrow)) {
			ImtorDetail imtordetail = new ImtorDetail();
			imtordetail.setXtornum(xtornum);
			imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			imtordetail.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("imtordetail", imtordetail);
			model.addAttribute("availablestock", Collections.emptyList());
		} else {
			ImtorDetail imtordetail = imtorService.findImtorDetailByXtornumAndXrow(xtornum, Integer.parseInt(xrow));
			if(imtordetail == null) {
				imtordetail = new ImtorDetail();
				imtordetail.setXtornum(xtornum);
				imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				imtordetail.setXlineamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("imtordetail", imtordetail);
			model.addAttribute("availablestock", imstockService.findByXitem(imtordetail.getXitem()));
		}
		return "pages/inventory/storereq/imtordetailmodal::imtordetailmodal";
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

			responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/storereq/imtordetail/" + imtorDetail.getXtornum());
			responseHelper.setSuccessStatusAndMessage("Requisition Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imtorService.saveDetail(imtorDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/storereq/imtordetail/" + imtorDetail.getXtornum());
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
		return "pages/inventory/storereq/imtor::imtordetailtable";
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
		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/storereq/imtordetail/" + xtornum);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirmimtor/{xtornum}")
	public @ResponseBody Map<String, Object> confirmimtor(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		imtorHeader.setXsignatorydate1(null);
		imtorHeader.setXdaterec(null);

		// Validate
		if("Confirmed".equalsIgnoreCase(imtorHeader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Requisition Order already confirmed");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> imtorDetailList = imtorService.findImtorDetailByXtornum(xtornum);
		if(imtorDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}

		imtorHeader.setXstatus("Confirmed");
		long count = imtorService.update(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm Requisition Orde");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Requisition Order Confirmed successfully");
		responseHelper.setRedirectUrl("/inventory/storereq/" + xtornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
	
	@GetMapping("/main/{xtornum}")
	public String reloadMainform(@PathVariable String xtornum, Model model) {
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		if(data == null) data = getDefaultImtorHeader();

		model.addAttribute("imtorheader", data);
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXtype(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));

		return "pages/inventory/storereq/imtor::main";
	}

	@GetMapping("/print/{xtornum}")
	public ResponseEntity<byte[]> printSalesReturnWithDetails(@PathVariable String xtornum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy, HH:mm:ss");
		
		StoreRequisitionReport report=new StoreRequisitionReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Store Requisition");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
		report.setXtornum(data.getXtornum());
		report.setXreqdate(sdf.format(data.getXreqdate()));
		report.setXfhwh(data.getXfhwh());
		report.setFPojectName(data.getFPojectName());
		report.setXfwh(data.getXfwh());
		report.setXtwh(data.getXtwh());
		report.setXtwhdesc(data.getXtwhdesc());
		report.setXpreparerName(data.getXpreparerName());
		report.setXsignatory1Name(data.getXsignatory1Name());
		report.setXacknowledgeName(data.getXacknowledgeName());
		//report.setStoreName(xcodesService.findByXtypesAndXcodes(CodeType.STORE.getCode(), data.getXtwh(), Boolean.TRUE).getXlong());
		
		report.setXfwhdesc(data.getXfwhdesc());
		report.setXstatus(data.getXstatus());
		
		if("Open".equalsIgnoreCase(data.getXstatus())) {
			report.setXstatus("Open");
		}
		else if("Confirmed".equalsIgnoreCase(data.getXstatus())) {
			report.setXstatus("Applied");
		}
		
		if(("Approved".equalsIgnoreCase(data.getXstatustor())) || ("Confirmed".equalsIgnoreCase(data.getXstatustor())) || ("Dismised".equalsIgnoreCase(data.getXstatustor())) )
		{
			report.setApprovalStatus("Approved");
		}
		else if(("Rejected".equalsIgnoreCase(data.getXstatustor())))
		{
			report.setApprovalStatus("Rejected");
		}
		

		if((("Confirmed".equalsIgnoreCase(data.getXstatustor()))))
		{
			report.setAckStatus("Received");
		}
		else if("Dismised".equalsIgnoreCase(data.getXstatustor()))
		{
			report.setAckStatus("Dismised");
		}
		else if("Open".equalsIgnoreCase(data.getXstatustor()))
		{
			report.setXstatustor("");
		}
		
		report.setXpreparer(data.getXpreparer());
		report.setXlong(data.getXlong());
		report.setXsignatory1(data.getXsignatory1());
		report.setXsignatorynote1(data.getXsignatorynote1());
		report.setXacknowledge(data.getXacknowledge());
		report.setXacknowledgenote(data.getXacknowledgenote());
		
		if(data.getXsignatorydate1()==null) {
			report.setXsignatorydate1("");
		}
		if(data.getXsignatorydate1()!=null)
		{
			report.setXsignatorydate1(sdf.format(data.getXsignatorydate1()));
		}
		if(data.getXdaterec()==null) {
			report.setXdaterec("");
		}
		else if(data.getXdaterec()!=null) {
			report.setXdaterec(sdf.format(data.getXdaterec()));
		}
		//report.setXstatustor(data.getXstatustor());
		report.setXref(data.getXref());
		report.setTotalQty(BigDecimal.ZERO);
		
		List<ImtorDetail> items = imtorService.findImtorDetailByXtornum(data.getXtornum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXitemdesc());
				item.setItemQty(it.getXqtyord().toString());
				item.setRate(it.getXrate());
				item.setItemUnit(it.getXunit());
				item.setItemQty(it.getXqtyord() != null ? it.getXqtyord().toString() : BigDecimal.ZERO.toString());
				
				report.getItems().add(item);
				report.setTotalQty(report.getTotalQty().add(BigDecimal.valueOf(Double.valueOf(item.getItemQty()))));
				
			});
		}
		
		byte[] byt = getPDFByte(report, "storerequisitionreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for store transfer: " + xtornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}
	
	@GetMapping("/allcodesbyproject/{xfhwh}")
	public @ResponseBody List<ProjectStoreView> getProjectstoreview(@PathVariable String xfhwh){
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(xfhwh);
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		return list;
	}
	
}
