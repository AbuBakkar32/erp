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

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Imstock;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.Opordheader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.StoreTransferReport;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.ImtorService;
import com.asl.service.OpordService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/inventory/asset")
public class AssetTransferOrderController extends ASLAbstractController{
	@Autowired private ImtorService imtorService;
	@Autowired private XcodesService xcodeService;
	@Autowired private ImstockService imstockService;
	@Autowired private CaitemService caitemService;
	@Autowired private OpordService opordService;
	@Autowired private CacusService cacusService;

	@GetMapping
	public String loadTransferOrderdPage(Model model) {
		model.addAttribute("imtorheader", getDefaultImtorHeader());
		/*
		 * model.addAttribute("allImtorHeaders",
		 * imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.
		 * AGENT_TRANSFER_ORDER.getCode()));
		 */
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.ASSET_TRANSFER_ORDER.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.ASSET_TRANSFER_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXcode(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("transfers", xcodeService.findByXtype(CodeType.TRANSFER_TO.getCode(), Boolean.TRUE));
		
		return "pages/inventory/asset/imtor";
	}

	@GetMapping("/{xtornum}")
	public String loadPoordPage(@PathVariable String xtornum, Model model) {
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		if(data == null) return "rdirect:/inventory/asset";

		if(StringUtils.isNotBlank(data.getXcus())) {
			Cacus c = cacusService.findByXcus(data.getXcus());
			if(c != null) {
				data.setXorg(c.getXorg());
			}
		}

		model.addAttribute("imtorheader", data);
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.ASSET_TRANSFER_ORDER.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.ASSET_TRANSFER_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.ASSET_TRANSFER_ORDER.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.ASSET_TRANSFER_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("transfers", xcodeService.findByXtype(CodeType.TRANSFER_TO.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXtype(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		List<ImtorDetail> imtorDetails = imtorService.findImtorDetailByXtornum(xtornum); 
		BigDecimal totalQuantity = BigDecimal.ZERO; 
		if(imtorDetails != null && !imtorDetails.isEmpty()) {
		 for(ImtorDetail pd : imtorDetails) { 
			 totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord()); } } 
		model.addAttribute("totalQuantity", totalQuantity);
		 
		return "pages/inventory/asset/imtor";
	}


	private ImtorHeader getDefaultImtorHeader() {
		ImtorHeader imtorHeader = new ImtorHeader();
		imtorHeader.setXtypetrn(TransactionCodeType.ASSET_TRANSFER_ORDER.getCode());
		imtorHeader.setXtrn(TransactionCodeType.ASSET_TRANSFER_ORDER.getdefaultCode());
		imtorHeader.setXstatustor("Open");
		return imtorHeader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(ImtorHeader imtorHeader, BindingResult bindingResult){
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(imtorHeader.getXfwh().equalsIgnoreCase(imtorHeader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Stock can't be transfered to same warehouse !");
			return responseHelper.getResponse();
		}

		// chalan reference validation
		if(StringUtils.isNotBlank(imtorHeader.getXchalanref())) {
			Opordheader chalan = opordService.findOpordHeaderByXordernum(imtorHeader.getXchalanref());
			if(chalan == null) {
				responseHelper.setErrorStatusAndMessage("Invalid chalan reference");
				return responseHelper.getResponse();
			}
		}
		
		if(StringUtils.isBlank(imtorHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer Required");
			return responseHelper.getResponse();
		}
		

		// If existing
		ImtorHeader existImtorHeader = imtorService.findImtorHeaderByXtornum(imtorHeader.getXtornum());
		if(existImtorHeader != null) {
			BeanUtils.copyProperties(imtorHeader, existImtorHeader, "xtypetrn", "xtrn");
			long count = imtorService.update(existImtorHeader);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Transfer Order");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Transfer Order Updated Successfully");
			responseHelper.setRedirectUrl("/inventory/asset/" + existImtorHeader.getXtornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Transfer Order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Transfer Order Created Successfully");
		responseHelper.setRedirectUrl("/inventory/asset/" + imtorHeader.getXtornum());
		return responseHelper.getResponse();
	}

	@PostMapping("/delete/{xtypetrn}")
	public @ResponseBody Map<String, Object> deleteLandInfo(@PathVariable String xtypetrn,  Model model) {
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtypetrn);
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imtorService.delete(imtorHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/inventory/asset" );
		return responseHelper.getResponse();
}
	
	@PostMapping("/archive/{xtornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't archive transfer order");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> details = imtorService.findImtorDetailByXtornum(xtornum);
		if(details != null && !details.isEmpty()) {
			long dcount = imtorService.deleteImtorDetailByXtornum(xtornum);
			if(dcount == 0) {
				responseHelper.setErrorStatusAndMessage("Can't archive transfer order");
				return responseHelper.getResponse();
			}
		}

		imtorHeader.setZactive(Boolean.FALSE);
		long count = imtorService.update(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't archive transfer order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted Successfully");
		responseHelper.setRedirectUrl("/inventory/asset");
		return responseHelper.getResponse();
	}

	@GetMapping("{xtornum}/imtordetail/{xrow}/show")
	public String openImtorDetailModal(@PathVariable String xtornum, @PathVariable String xrow, Model model) {
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) return "redirect:/inventory/asset";

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
		return "pages/inventory/asset/imtordetailmodal::imtordetailmodal";
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
			// check the item is transfer eligible, that means it has enough stock available
			Imstock stock = imstockService.findByXitemAndXwh(imtorDetail.getXitem(), xfwh);
			if(stock == null) {
				responseHelper.setErrorStatusAndMessage("There is no stock information available in system for item : " + imtorDetail.getXitem());
				return responseHelper.getResponse();
			}
			if(stock.getXinhand().compareTo(imtorDetail.getXqtyord()) == -1) {
				responseHelper.setErrorStatusAndMessage("Stock not available");
				return responseHelper.getResponse();
			}

			BeanUtils.copyProperties(imtorDetail, existDetail, "xtornum", "xrow");
			long count = imtorService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/asset/imtordetail/" + imtorDetail.getXtornum());
			responseHelper.setSuccessStatusAndMessage("Transfer Order detail updated successfully");
			return responseHelper.getResponse();
		}

		Imstock stock = imstockService.findByXitemAndXwh(imtorDetail.getXitem(), xfwh);
		if(stock == null) {
			responseHelper.setErrorStatusAndMessage("There is no stock information available in system for item : " + imtorDetail.getXitem());
			return responseHelper.getResponse();
		}
		if(stock.getXavail().compareTo(imtorDetail.getXqtyord()) == -1) {
			responseHelper.setErrorStatusAndMessage("Stock not available");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imtorService.saveDetail(imtorDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/asset/imtordetail/" + imtorDetail.getXtornum());
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
			 totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord()); } } 
		model.addAttribute("totalQuantity", totalQuantity);
		return "pages/inventory/asset/imtor::imtordetailtable";
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
		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/asset/imtordetail/" + xtornum);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirmimtor/{xtornum}")
	public @ResponseBody Map<String, Object> confirmimtor(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(!"Open".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			responseHelper.setErrorStatusAndMessage("Transfer Order already confirmed");
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(imtorHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer not found");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> imtorDetailList = imtorService.findImtorDetailByXtornum(xtornum);
		if(imtorDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}

		// Stock validation check
		StringBuilder error = new StringBuilder();
		for(ImtorDetail id : imtorDetailList) {
			Imstock is = imstockService.findByXitemAndXwh(id.getXitem(), imtorHeader.getXfwh());
			if(is == null || is.getXinhand().compareTo(id.getXqtyord()) == -1) {
				error.append("Item " + is.getXitem() + " not available in " + imtorHeader.getXfwh() + " to do transfer\n");
			}
		}
		if(StringUtils.isNotBlank(error.toString())) {
			responseHelper.setErrorStatusAndMessage(error.toString());
			return responseHelper.getResponse();
		}

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

		responseHelper.setSuccessStatusAndMessage("Transfer Order Confirmed successfully");
		responseHelper.setRedirectUrl("/inventory/asset/" + xtornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}

	@GetMapping("/stock/{xitem}")
	public String findAvailableStockInfo(@PathVariable String xitem, Model model){
		model.addAttribute("availablestock", imstockService.findByXitem(xitem));
		return "pages/inventory/asset/imtordetailmodal::stocktable";
	}
	
	@GetMapping("/print/{xtornum}")
	public ResponseEntity<byte[]> printSalesReturnWithDetails(@PathVariable String xtornum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		StoreTransferReport report=new StoreTransferReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Asset Transfer");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
		report.setXtornum(data.getXtornum());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXchalanref(data.getXchalanref());
		report.setXfwh(data.getXfwh());
		report.setXtwh(data.getXtwh());
		report.setXfwhdesc(data.getXfwhdesc());
		report.setXtwhdesc(data.getXtwhdesc());
		report.setXstatustor(data.getXstatustor());
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
		
		byte[] byt = getPDFByte(report, "storetransreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for asset transfer : " + xtornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}

}
