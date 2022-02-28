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

import com.asl.entity.Caitem;
import com.asl.entity.Imstock;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opordheader;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.EnterProjectTransferReport;
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
@RequestMapping("/inventory/projecttransfer")
public class EnterProjectTransferController extends ASLAbstractController {

	@Autowired private ImtorService imtorService;
	@Autowired private XcodesService xcodeService;
	@Autowired private ImstockService imstockService;
	@Autowired private CaitemService caitemService;
	@Autowired private OpordService opordService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadTransferOrderdPage(Model model) {
		model.addAttribute("imtorheader", getDefaultImtorHeader());
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.ENTER_PROJECT_TRANSFER.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.ENTER_PROJECT_TRANSFER.getCode(), Boolean.TRUE));
		//model.addAttribute("torstatusList", xcodeService.findByXcode(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());
		model.addAttribute("alltcodes", Collections.emptyList());

		return "pages/inventory/projecttransfer/imtor";
	}

	@GetMapping("/{xtornum}")
	public String loadPoordPage(@PathVariable String xtornum, Model model) {
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		if(data == null) data = getDefaultImtorHeader();

		model.addAttribute("imtorheader", data);
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.ENTER_PROJECT_TRANSFER.getCode()));
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.ENTER_PROJECT_TRANSFER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		//model.addAttribute("torstatusList", xcodeService.findByXtype(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXfhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		List<ProjectStoreView> tlist = projectstoreviewService.getProjectStoresByXtype(data.getXthwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("alltcodes", tlist);
		
		List<ImtorDetail> imtorDetails = imtorService.findImtorDetailByXtornum(xtornum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		if(imtorDetails != null && !imtorDetails.isEmpty()) {
			for(ImtorDetail pd : imtorDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);

		return "pages/inventory/projecttransfer/imtor";
	}

	private ImtorHeader getDefaultImtorHeader() {
		ImtorHeader imtorHeader = new ImtorHeader();
		imtorHeader.setXtypetrn(TransactionCodeType.ENTER_PROJECT_TRANSFER.getCode());
		imtorHeader.setXtrn(TransactionCodeType.ENTER_PROJECT_TRANSFER.getdefaultCode());
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
		
		if(imtorHeader.getXfhwh().isEmpty()) {
			responseHelper.setErrorStatusAndMessage("From Project/Busniness required.");
			return responseHelper.getResponse();
		}
		if(imtorHeader.getXthwh().isEmpty()) {
			responseHelper.setErrorStatusAndMessage("To Project/Busniness required.");
			return responseHelper.getResponse();
		}
		if(imtorHeader.getXfwh().isEmpty()) {
			responseHelper.setErrorStatusAndMessage("From Store/Site required.");
			return responseHelper.getResponse();
		}
		if(imtorHeader.getXtwh().isEmpty()) {
			responseHelper.setErrorStatusAndMessage("To Store/Site required.");
			return responseHelper.getResponse();
		}
		if(imtorHeader.getXfhwh().equalsIgnoreCase(imtorHeader.getXthwh())) {
			responseHelper.setErrorStatusAndMessage("Project/Business can't be transfered to same Project/Business !");
			return responseHelper.getResponse();
		}
		if(imtorHeader.getXfwh().equalsIgnoreCase(imtorHeader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Store/Site can't be transfered to same Store/Site !");
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
		//TODO: 

		// If existing
		ImtorHeader existImtorHeader = imtorService.findImtorHeaderByXtornum(imtorHeader.getXtornum());
		if(existImtorHeader != null) {
			BeanUtils.copyProperties(imtorHeader, existImtorHeader, "xtypetrn", "xtrn");
			long count = imtorService.update(existImtorHeader);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Enter Project Transfer Order");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Enter Project Transfer Order Updated Successfully");
			responseHelper.setRedirectUrl("/inventory/projecttransfer/" + existImtorHeader.getXtornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Enter Project Transfer Order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Enter Project Transfer Order Created Successfully");
		responseHelper.setRedirectUrl("/inventory/projecttransfer/" + imtorHeader.getXtornum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xtornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't archive Enter Project transfer order");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> details = imtorService.findImtorDetailByXtornum(xtornum);
		if(details != null && !details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete all Enter Project transfer order details first");
			return responseHelper.getResponse();
		}


		long count = imtorService.delete(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Enter Project transfer order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted Successfully");
		responseHelper.setRedirectUrl("/inventory/projecttransfer");
		return responseHelper.getResponse();
	}

	@GetMapping("{xtornum}/imtordetail/{xrow}/show")
	public String openImtorDetailModal(@PathVariable String xtornum, @PathVariable String xrow, Model model) {
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) return "redirect:/inventory/projecttransfer";

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
		return "pages/inventory/projecttransfer/imtordetailmodal::imtordetailmodal";
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
				responseHelper.setErrorStatusAndMessage("There is no enter project transfer information available in system for item : " + imtorDetail.getXitem());
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

			responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/projecttransfer/imtordetail/" + imtorDetail.getXtornum());
			responseHelper.setSuccessStatusAndMessage("Enter Project Transfer Order detail updated successfully");
			return responseHelper.getResponse();
		}

		Imstock stock = imstockService.findByXitemAndXwh(imtorDetail.getXitem(), xfwh);
		if(stock == null) {
			responseHelper.setErrorStatusAndMessage("There is no enter project transfer information available in system for item : " + imtorDetail.getXitem());
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

		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/projecttransfer/imtordetail/" + imtorDetail.getXtornum());
		responseHelper.setSuccessStatusAndMessage("Enter Project Transfer Order detail saved successfully");
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
		return "pages/inventory/projecttransfer/imtor::imtordetailtable";
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
		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/projecttransfer/imtordetail/" + xtornum);
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
		if("Confirmed".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			responseHelper.setErrorStatusAndMessage("Enter Project Transfer Order already confirmed");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> imtorDetailList = imtorService.findImtorDetailByXtornum(xtornum);
		if(imtorDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}

		// Stock validation check
		StringBuilder error = new StringBuilder();
		int i = 1;
		for(ImtorDetail id : imtorDetailList) {
			Imstock is = imstockService.findByXitemAndXwh(id.getXitem(), imtorHeader.getXfwh());
			if(is == null || is.getXinhand().compareTo(id.getXqtyord()) == -1) {
				error.append(i + ". Item " + is.getXitem() + " not available in " + imtorHeader.getXfwh() + " - " + imtorHeader.getXfwhdesc() + " to do transfer<br/>");
				i++;
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

		responseHelper.setSuccessStatusAndMessage("Enter Project Transfer Order Confirmed successfully");
		responseHelper.setRedirectUrl("/inventory/projecttransfer/" + xtornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}

	@GetMapping("/stock/{xitem}")
	public String findAvailableStockInfo(@PathVariable String xitem, Model model){
		model.addAttribute("availablestock", imstockService.findByXitem(xitem));
		return "pages/inventory/projecttransfer/imtordetailmodal::stocktable";
	}
	
	@GetMapping("/print/{xtornum}")
	public ResponseEntity<byte[]> printSalesReturnWithDetails(@PathVariable String xtornum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		EnterProjectTransferReport report=new EnterProjectTransferReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Enter Project Transfer");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
	
		  report.setXtornum(data.getXtornum());
		  report.setXfhwh(data.getXfhwh());
		  report.setFPojectName(data.getFPojectName());
		  report.setXfwh(data.getXfwh());
		  report.setXfwhdesc(data.getXfwhdesc());
		  report.setXstaff(data.getXstaff());
		  report.setXstaffName(data.getXstaffName());
		  report.setXlong(data.getXlong());
		  report.setXdate(sdf.format(data.getXdate()));
		  report.setXthwh(data.getXthwh());
		  report.setTPojectName(data.getTPojectName());
		  report.setXtwh(data.getXtwh());
		  report.setXtwhdesc(data.getXtwhdesc());
		  report.setXref(data.getXref());
		  report.setXstatustor(data.getXstatustor());

		 
		
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
				//report.setTotalQty(report.getTotalQty().add(BigDecimal.valueOf(Double.valueOf(item.getItemQty()))));
				
			});
		}
		
		byte[] byt = getPDFByte(report, "enterprojecttransreport.xsl", request);
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
	
	@GetMapping("/allToCodesbyproject/{xthwh}")
	public @ResponseBody List<ProjectStoreView> getToProjectstoreview(@PathVariable String xthwh){
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(xthwh);
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		return list;
	}
}
