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

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Imtag;
import com.asl.entity.Imtdet;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.ProjectStoreView;
import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.EnterProjectTransferReport;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.StockAdjustmentReport;
import com.asl.service.CaitemService;
import com.asl.service.ImtagService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/inventory/stocktake")
public class StockTakeController extends ASLAbstractController {

	@Autowired private ImtagService imtagService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadStockTakePage(Model model) {
		model.addAttribute("imtag", getDefaultImtag());
		model.addAttribute("imtagprefix", xtrnService.findByXtypetrn(TransactionCodeType.STOCK_TAKE.getCode(), Boolean.TRUE));
		model.addAttribute("allimtags", imtagService.getAllImTag());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("imtagstatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());
		
		return "pages/inventory/stocktake/imtag";
	}

	@GetMapping("/{xtagnum}")
	public String loadStockTakePage(@PathVariable String xtagnum, Model model) {
		Imtag data = imtagService.findImtagByXtagnum(xtagnum);
		if(data == null) data = getDefaultImtag();

		model.addAttribute("imtag", data);
		model.addAttribute("imtagprefix", xtrnService.findByXtypetrn(TransactionCodeType.STOCK_TAKE.getCode(), Boolean.TRUE));
		model.addAttribute("allimtags", imtagService.getAllImTag());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("imtagstatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("imtagDetailsList", imtagService.findImtdetByXtagnum(xtagnum));

		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		List<Imtdet> imtagDetails = imtagService.findImtdetByXtagnum(xtagnum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		if(imtagDetails != null && !imtagDetails.isEmpty()) {
			for(Imtdet id : imtagDetails) {
				totalQuantity = totalQuantity.add(id.getXqty() == null ? BigDecimal.ZERO : id.getXqty());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		return "pages/inventory/stocktake/imtag";
	}

	private Imtag getDefaultImtag() {
		Imtag imtag= new Imtag();
		imtag.setXtype(TransactionCodeType.STOCK_TAKE.getCode());
		imtag.setXtypetrn(TransactionCodeType.STOCK_TAKE.getCode());
		imtag.setXtrn(TransactionCodeType.STOCK_TAKE.getdefaultCode());
		imtag.setXtrnimtag(TransactionCodeType.STOCK_TAKE.getdefaultCode()); // Removal queue
		imtag.setXstatustag("Open");
		return imtag;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imtag imtag, BindingResult bindingResult){
		if((imtag == null || StringUtils.isBlank(imtag.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		if(imtag.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		// if existing record
		Imtag existImtag = imtagService.findImtagByXtagnum(imtag.getXtagnum());
		if(existImtag != null) {
			BeanUtils.copyProperties(imtag, existImtag, "xtagnum", "xtype", "xdate","xtypetrn","xtrn","xtrnimtag");
			long count = imtagService.updateImtag(existImtag);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update stock take entry");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Stock Take Entry updated successfully");
			responseHelper.setRedirectUrl("/inventory/stocktake/" + imtag.getXtagnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtagService.saveImtag(imtag);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create stock take entry");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Stock Take Entry created successfully");
		responseHelper.setRedirectUrl("/inventory/stocktake/" + imtag.getXtagnum());
		return responseHelper.getResponse();
	}

	
	@GetMapping("/imtagdetail/{xtagnum}")
	public String reloadImtagDetailTable(@PathVariable String xtagnum, Model model) {
		List<Imtdet> detailList = imtagService.findImtdetByXtagnum(xtagnum);
		model.addAttribute("imtagDetailsList", detailList);
		Imtag header = new Imtag();
		header.setXtagnum(xtagnum);
		model.addAttribute("imtag", header);
		return "pages/inventory/stocktake/imtag::imtagdetailtable";
	}
	
	@GetMapping("{xtagnum}/imtagdetail/{xrow}/show")
	public String openImtagDetailModal(@PathVariable String xtagnum, @PathVariable String xrow, Model model) {		

		if("new".equalsIgnoreCase(xrow)) {
			Imtdet imtdet = new Imtdet();
			imtdet.setXtagnum(xtagnum);
			imtdet.setXqty(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));			
			model.addAttribute("imtagdetail", imtdet);
		} else {
			Imtdet imtdet = imtagService.findImtdetByXtagnumAndXrow(xtagnum, Integer.parseInt(xrow));
			if(imtdet == null) {
				imtdet = new Imtdet();
				imtdet.setXtagnum(xtagnum);
				imtdet.setXqty(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("imtagdetail", imtdet);
		}

		return "pages/inventory/stocktake/imtagdetailmodal::imtagdetailmodal";
	}
	
	@PostMapping("/imtagdetail/save")
	public @ResponseBody Map<String, Object> saveImtagDetail(Imtdet imtdet){
		
		if(imtdet == null || StringUtils.isBlank(imtdet.getXtagnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		// if existing
		Imtdet existDetail = imtagService.findImtdetByXtagnumAndXrow(imtdet.getXtagnum(), imtdet.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(imtdet, existDetail, "xtagnum", "xrow");
			long count = imtagService.updateImtdet(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/inventory/stocktake/" +  imtdet.getXtagnum());
			responseHelper.setSuccessStatusAndMessage("Stock-Take entry detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imtagService.saveImtdet(imtdet);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/inventory/stocktake/" +  imtdet.getXtagnum());
		responseHelper.setSuccessStatusAndMessage("Stock-Take entry detail saved successfully");
		
		return responseHelper.getResponse();
	}
	
	
	@PostMapping("{xtagnum}/imtagdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteImtagDetail(@PathVariable String xtagnum, @PathVariable String xrow, Model model) {
		Imtdet imtdet = imtagService.findImtdetByXtagnumAndXrow(xtagnum, Integer.parseInt(xrow));
		if(imtdet == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imtagService.deleteImtdet(imtdet);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/inventory/stocktake/" +  xtagnum);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirmstocktake/{xtagnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xtagnum){
		if(StringUtils.isBlank(xtagnum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		//Get PogrnHeader record by Xgrnnum		
		Imtag imtag = imtagService.findImtagByXtagnum(xtagnum);
		// Validate		
		if("Confirmed".equalsIgnoreCase(imtag.getXstatustag())) {
			responseHelper.setErrorStatusAndMessage("TAG already confirmed");
			return responseHelper.getResponse();
		}		
		Xcodes xcode = xcodeService.findByXtypesAndXcodes(CodeType.STORE.getCode(), imtag.getXwh());
		if(xcode == null) {
			responseHelper.setErrorStatusAndMessage("A valid warehouse must be selected.");
			return responseHelper.getResponse();
		}
		List<Imtdet> imtdetList = imtagService.findImtdetByXtagnum(xtagnum);
		if(imtdetList.size() == 0) {
			responseHelper.setErrorStatusAndMessage("Please add items to confirm stock-take!");
			return responseHelper.getResponse();
		}
		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(imtag.getXstatustag())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			imtagService.procStockTake(imtag.getXdate(), imtag.getXtagnum(), p_seq);

			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}		
		responseHelper.setSuccessStatusAndMessage("Imtag Confirmed successfully");
		responseHelper.setRedirectUrl("/inventory/stocktake/" + imtag.getXtagnum());
		return responseHelper.getResponse();
	}
	
	
	@PostMapping("/delete/{xtagnum}")
	public @ResponseBody Map<String, Object> deleteOtherEvent(@PathVariable String xtagnum,  Model model) {
		Imtag data = imtagService.findImtagByXtagnum(xtagnum);
		if(data == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imtagService.deleteImtag(data);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/inventory/stocktake/" + xtagnum );
		return responseHelper.getResponse();
	}
	
	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
	
	@GetMapping("/allcodesbyproject/{xhwh}")
	public @ResponseBody List<ProjectStoreView> getProjectstoreview(@PathVariable String xhwh){
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(xhwh);
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		return list;
	}
	
	@GetMapping("/print/{xtagnum}")
	public ResponseEntity<byte[]> printStockAdjustmentWithDetails(@PathVariable String xtagnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Imtag data = imtagService.findImtagByXtagnum(xtagnum);
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		StockAdjustmentReport report=new StockAdjustmentReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Stock Adjustment");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
		report.setXtagnum(data.getXtagnum());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXhwh(data.getXhwh());
		report.setProjectName(data.getProjectName());
		report.setXwh(data.getXhwh());
		report.setStoreName(data.getStoreName());
		report.setXstatustag(data.getXstatustag());
		report.setXref(data.getXref());
		
		
		List<Imtdet> imtagDetails = imtagService.findImtdetByXtagnum(data.getXtagnum());
		if (imtagDetails != null && !imtagDetails.isEmpty()) {
			imtagDetails.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getItemname());
				item.setItemUnit(it.getXunit());
				item.setItemQty(it.getXqty() != null ? it.getXqty().toString() : BigDecimal.ZERO.toString());
				
				report.getItems().add(item);
				//report.setTotalQty(report.getTotalQty().add(BigDecimal.valueOf(Double.valueOf(item.getItemQty()))));
				
			});
		}
		
		byte[] byt = getPDFByte(report, "stockadjustmentreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for store transfer: " + xtagnum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}

}
