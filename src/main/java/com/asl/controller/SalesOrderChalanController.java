package com.asl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.entity.Immofgdetail;
import com.asl.entity.Imstock;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.Moheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.Zbusiness;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ProductionSuggestion;
import com.asl.model.ServiceException;
import com.asl.model.report.AllSalesOrderChalanReport;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesOrderChalan;
import com.asl.service.CaitemService;
import com.asl.service.ImmofgdetailService;
import com.asl.service.ImstockService;
import com.asl.service.ImtorService;
import com.asl.service.MoService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.ProductionSuggestionService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Mar 9, 2021
 */
@Slf4j
@Controller
@RequestMapping("/salesninvoice/salesorderchalan")
public class SalesOrderChalanController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private ImmofgdetailService immofgdetailService;
	@Autowired private MoService moService;
	@Autowired private OpdoService opdoService;
	@Autowired private CaitemService caitemService;
	@Autowired private ImtorService imtorService;
	@Autowired private ProductionSuggestionService productionSuggestionService;
	@Autowired private ImstockService imstockService;

	@GetMapping
	public String loadSalesOrderChalanPage(Model model) {
		model.addAttribute("productioncompleted", false);
		model.addAttribute("salesorderchalan", getDefaultOpordheader());
		model.addAttribute("salesorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("salesorderchalanList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));
		return "pages/salesninvoice/salesorderchalan/salesorderchalan";
	}

	@GetMapping("/{xordernum}")
	public String loadSalesOrderChalanPage(@PathVariable String xordernum, Model model) {
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) return "redirect:/salesninvoice/salesorderchalan";

		model.addAttribute("productioncompleted", moService.isProductionProcessCompleted(oh.getXordernum()));
		model.addAttribute("salesorderchalan", oh);
		model.addAttribute("salesorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("salesorderchalanList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));

		List<Opordheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		if("Open".equalsIgnoreCase(oh.getXstatus())) allOpenAndConfirmesSalesOrders.addAll(opordService.findAllSalesOrder(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), "Open", new Date()));
		allOpenAndConfirmesSalesOrders.addAll(opordService.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum));
		model.addAttribute("opensalesorders", allOpenAndConfirmesSalesOrders);
		model.addAttribute("chalandetails", opordService.findOporddetailByXordernum(xordernum));
		return "pages/salesninvoice/salesorderchalan/salesorderchalan";
	}

	private Opordheader getDefaultOpordheader() {
		Opordheader oh = new Opordheader();
		oh.setXtypetrn(TransactionCodeType.CHALAN_NUMBER.getCode());
		oh.setXdate(new Date());
		oh.setXstatus("Open");
		return oh;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opordheader opordheader, BindingResult bindingResult, Model model){
		if(opordheader == null || StringUtils.isBlank(opordheader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		Opordheader existOh = opordService.findOpordHeaderByXordernum(opordheader.getXordernum());
		if(existOh != null) {
			BeanUtils.copyProperties(opordheader, existOh, "xdate", "xstatus");
			long count = opordService.updateOpordHeader(existOh);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Chalan not updated");
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + existOh.getXordernum());
			responseHelper.setSuccessStatusAndMessage("Chalan updated successfully");
			return responseHelper.getResponse();
		}

		// if new
		opordheader.setXstatus("Open");
		opordheader.setXdate(new Date());
		long count = opordService.saveOpordHeader(opordheader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Chalan not created");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + opordheader.getXordernum());
		responseHelper.setSuccessStatusAndMessage("Chalan created successfully");
		return responseHelper.getResponse();
	}
	

	@PostMapping("/delete/{xordernum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xordernum){
		Opordheader opordheader = opordService.findOpordHeaderByXordernum(xordernum);
		if(opordheader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Chalan Number : " + xordernum);
			return responseHelper.getResponse();
		}
		
		List<Opordheader> ohh = opordService.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum);
		
		for(Opordheader data : ohh) {
			if(data.getXchalanref() !=null) {
				responseHelper.setErrorStatusAndMessage("Chalan Number : " + xordernum + " Have a Child Record");
				return responseHelper.getResponse();
			}
		}
		
		long count = opordService.deleteOpordHeader(xordernum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Delete Chalan Number : " + xordernum);
			return responseHelper.getResponse();
		}
		
		responseHelper.setSuccessStatusAndMessage("Production Chalan deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/");
		return responseHelper.getResponse();
	}


	@GetMapping("/opensalesorder/query")
	public String reloadTableWithData(@RequestParam String xordernum, @RequestParam String date, Model model) throws ParseException {
		model.addAttribute("salesorderchalan", opordService.findOpordHeaderByXordernum(xordernum));
		List<Opordheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		allOpenAndConfirmesSalesOrders.addAll(opordService.findAllSalesOrder(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), "Open", SDF.parse(date)));
		allOpenAndConfirmesSalesOrders.addAll(opordService.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum));
		model.addAttribute("opensalesorders", allOpenAndConfirmesSalesOrders);
		return "pages/salesninvoice/salesorderchalan/salesorderchalan::opensalesorderstable";
	}

	@PostMapping("/opensalesorder/query")
	public @ResponseBody Map<String, Object> queryForrequistionDetails(String xordernum, Date xdate, Model model){
		responseHelper.setReloadSectionIdWithUrl("opensalesorderstable", "/salesninvoice/salesorderchalan/opensalesorder/query?xordernum="+ xordernum +"&date=" + SDF.format(xdate));
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		responseHelper.setDisplayMessage(false);
		return responseHelper.getResponse();
	}

	@GetMapping("/ordreqdetails/{xordernum}/show")
	public String displayItemDetailsOfOrderRequistion(@PathVariable String xordernum, Model model) {
		model.addAttribute("oporddetailsList", opordService.findOporddetailByXordernum(xordernum));
		return "pages/salesninvoice/salesorderchalan/salesorderdetailmodal::salesorderdetailmodal";
	}

	@PostMapping("/salesorderconfirm/{chalan}/{xordernum}")
	public @ResponseBody Map<String, Object> confirmSalesOrderAndCreateChalanDetail(@PathVariable String chalan, @PathVariable String xordernum, Model model){
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isNotBlank(oh.getXchalanref())) {
			responseHelper.setErrorStatusAndMessage("Sales order already added to chalan : " + oh.getXchalanref() + " . Please reload this page again");
			return responseHelper.getResponse();
		}

		List<Oporddetail> details = opordService.findOporddetailByXordernum(xordernum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This " + xordernum + " Sales Order has no item to add this chalan");
			return responseHelper.getResponse();
		}

		// create or update chalan detail first
		for(Oporddetail pd : details) {
			// if not production item, then don't add it to chalan
			Caitem caitem = caitemService.findByXitem(pd.getXitem());
			if(caitem == null || !caitem.isXproditem()) continue;

			// check chalan detail already exist using item
			Oporddetail existChalanDetail = opordService.findOporddetailByXordernumAndXitem(chalan, pd.getXitem());
			if(existChalanDetail != null) {  // update existing with qty
				existChalanDetail.setXqtyord(existChalanDetail.getXqtyord().add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord()));
				long countChalanDetail = opordService.updateOpordDetail(existChalanDetail);
				if(countChalanDetail == 0) {
					responseHelper.setErrorStatusAndMessage("Can't update chalan detail");
					return responseHelper.getResponse();
				}
			} else {  // create new detail
				Oporddetail chalanDetail = new Oporddetail();
				chalanDetail.setXordernum(chalan);
				chalanDetail.setXitem(pd.getXitem());
				chalanDetail.setXunit(pd.getXunit());
				chalanDetail.setXqtyord(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				chalanDetail.setXrate(pd.getXrate() == null ? BigDecimal.ZERO : pd.getXrate());
				chalanDetail.setXlineamt(chalanDetail.getXrate().multiply(chalanDetail.getXqtyord()));
				chalanDetail.setXdesc(pd.getXdesc());
				chalanDetail.setXcatitem(pd.getXcatitem());
				chalanDetail.setXgitem(pd.getXgitem());
				long countChalanDetail = opordService.saveOpordDetail(chalanDetail);
				if(countChalanDetail == 0) {
					responseHelper.setErrorStatusAndMessage("Can't create chalan detail");
					return responseHelper.getResponse();
				}
			}
		}

		// now update sales order with chalan reference
		oh.setXchalanref(chalan);
		oh.setXstatusord("Confirmed");
		long count = opordService.updateOpordHeader(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Update Sales Order");
			return responseHelper.getResponse();
		}

		// now create suggestion
		Opordheader pchalan = opordService.findOpordHeaderByXordernum(chalan);
		if(pchalan == null) {
			responseHelper.setErrorStatusAndMessage("Chalan " + chalan + " not found in this system");
			return responseHelper.getResponse();
		}
		// delete suggestion table where xordernum
		productionSuggestionService.deleteSuggestion(chalan, pchalan.getXdate());
		// create suggestion
		productionSuggestionService.createSuggestion(chalan);
		// update chalan status
		pchalan.setSuggestionCreated(true);
		long chalancount = opordService.updateOpordHeader(pchalan);
		if(chalancount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update chalan suggestion status");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + chalan);
		responseHelper.setSuccessStatusAndMessage("Sales order " + oh.getXordernum() + " assigned to chalan " + chalan + " successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/salesorderrevoke/{chalan}/{xordernum}")
	public @ResponseBody Map<String, Object> revokeSalesOrder(@PathVariable String chalan, @PathVariable String xordernum, Model model){
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			responseHelper.setErrorStatusAndMessage("Sales order not selected");
			return responseHelper.getResponse();
		}

		List<Oporddetail> details = opordService.findOporddetailByXordernum(xordernum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This " + xordernum + " Sales Order has no item to remove from chalan");
			return responseHelper.getResponse();
		}

		// create or update chalan detail first
		for(Oporddetail pd : details) {
			// check chalan detail already exist using item
			Oporddetail existChalanDetail = opordService.findOporddetailByXordernumAndXitem(chalan, pd.getXitem());
			if(existChalanDetail == null) continue;

			// update existing with qty
			existChalanDetail.setXqtyord(existChalanDetail.getXqtyord().subtract(pd.getXqtyord()));
			long countChalanDetail = 0;
			if(existChalanDetail.getXqtyord().compareTo(BigDecimal.ONE) == -1) {
				countChalanDetail = opordService.deleteOpordDetail(existChalanDetail);
			} else {
				countChalanDetail = opordService.updateOpordDetail(existChalanDetail);
			}
			if(countChalanDetail == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update chalan detail");
				return responseHelper.getResponse();
			}
		}

		// now update sales order with chalan reference
		oh.setXchalanref(null);
		oh.setXstatusord("Open");
		long count = opordService.updateOpordHeader(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Update Sales Order");
			return responseHelper.getResponse();
		}

		// now create suggestion
		Opordheader pchalan = opordService.findOpordHeaderByXordernum(chalan);
		if(pchalan == null) {
			responseHelper.setErrorStatusAndMessage("Chalan " + chalan + " not found in this system");
			return responseHelper.getResponse();
		}
		// delete suggestion table where xordernum
		productionSuggestionService.deleteSuggestion(chalan, pchalan.getXdate());
		// create suggestion
		productionSuggestionService.createSuggestion(chalan);
		// update chalan status
		List<Oporddetail> chalandetails = opordService.findOporddetailByXordernum(chalan);
		if(chalandetails == null || chalandetails.isEmpty()) {
			pchalan.setSuggestionCreated(false);
			long chalancount = opordService.updateOpordHeader(pchalan);
			if(chalancount == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update chalan suggestion status");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + chalan);
		responseHelper.setSuccessStatusAndMessage("Sales order " + oh.getXordernum() + " unassigned from chalan " + chalan + " successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/reloadchalanform/{xchalan}")
	public String reloadChalanForm(@PathVariable String xchalan, Model model) {
		Opordheader oh = opordService.findOpordHeaderByXordernum(xchalan);
		if(oh == null) return "redirect:/salesninvoice/salesorderchalan";

		model.addAttribute("productioncompleted", moService.isProductionProcessCompleted(oh.getXordernum()));
		model.addAttribute("salesorderchalan", oh);
		model.addAttribute("salesorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));

		model.addAttribute("salesorderchalanList", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));

		List<Opordheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		if("Open".equalsIgnoreCase(oh.getXstatus())) allOpenAndConfirmesSalesOrders.addAll(opordService.findAllSalesOrder(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), "Open", new Date()));
		allOpenAndConfirmesSalesOrders.addAll(opordService.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xchalan));
		model.addAttribute("opensalesorders", allOpenAndConfirmesSalesOrders);
		model.addAttribute("chalandetails", opordService.findOporddetailByXordernum(xchalan));

		return "pages/salesninvoice/salesorderchalan/salesorderchalan::chalanform";
	}

	@GetMapping("/chalandetail/{xordernum}")
	public String reloadChalanDetailSection(@PathVariable String xordernum, Model model) {
		model.addAttribute("salesorderchalan", opordService.findOpordHeaderByXordernum(xordernum));
		model.addAttribute("chalandetails", opordService.findOporddetailByXordernum(xordernum));
		return "pages/salesninvoice/salesorderchalan/salesorderchalan::salesorderchalandetailtable";
	}

	@PostMapping("/lockchalan/{xordernum}")
	public @ResponseBody Map<String, Object> lockChalan(@PathVariable String xordernum, Model model){
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			responseHelper.setErrorStatusAndMessage("Sales order not found in the system");
			return responseHelper.getResponse();
		}

		// check chalan has any sales order assigned
		List<Opordheader> allAssigendSalesOrder = opordService.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum);
		if(allAssigendSalesOrder == null || allAssigendSalesOrder.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This chalan has no sales order assigned");
			return responseHelper.getResponse();
		}

		// transfer all chalan deails to immofgdetail
		List<Oporddetail> chalandetails = opordService.findOporddetailByXordernum(oh.getXordernum());
		for(Oporddetail c : chalandetails) {
			Immofgdetail id = new Immofgdetail();
			id.setXtornum(c.getXordernum());
			id.setXrow(c.getXrow());
			id.setXunit(c.getXunit());
			id.setXitem(c.getXitem());
			id.setXqtyord(c.getXqtyord());
			id.setXnote(c.getXlong());
			long count = immofgdetailService.save(id);
			if(count == 0) {
				log.error("ERROR is : {}", "Can't insert chaland details to Immofgdetail table for chalan " + c.getXordernum());
			}
		}

		// now lock chalan
		oh.setXstatus("Confirmed");
		long count = opordService.updateOpordHeader(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't lock Chalan");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Chalan locked successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + xordernum);
		return responseHelper.getResponse();
	}

	@PostMapping("/createinvoice/{xordernum}")
	public @ResponseBody Map<String, Object> createInvoice(@PathVariable String xordernum, Model model){
		try {
			long count = opdoService.createSalesFromChalan(xordernum);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + xordernum);
		responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{xordernum}")
	public ResponseEntity<byte[]> printChalan(@PathVariable String xordernum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			message = "Chalan not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Oporddetail> details = opordService.findOporddetailByXordernum(xordernum);
		if(details == null || details.isEmpty()) {
			message = "Chalan Details is empty";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		Zbusiness zb = sessionManager.getZbusiness();

		AllSalesOrderChalanReport report = new AllSalesOrderChalanReport();
		report.setBusinessName(zb.getZorg());
		report.setBusinessAddress(zb.getXmadd());
		report.setReportName("Item Details of Production Chalan : " + oh.getXordernum());
		report.setFromDate(sdf.format(oh.getXdate()));
		report.setToDate(sdf.format(oh.getXdate()));
		report.setPrintDate(sdf.format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());

		List<SalesOrderChalan> chalans = new ArrayList<>();
		SalesOrderChalan chalan = new SalesOrderChalan();
		chalan.setChalanName(oh.getXordernum());
		chalan.setChalanDate(sdf.format(oh.getXdate()));
		chalan.setStatus(oh.getXstatus());
		chalans.add(chalan);

		List<ItemDetails> items = new ArrayList<>();
		for(Oporddetail d : details) {
			Caitem c = caitemService.findByXitem(d.getXitem());
			if(c == null) continue;
			
			ItemDetails id = new ItemDetails();
			id.setItemCode(d.getXitem());
			id.setItemName(c.getXdesc());
			id.setItemCategory(d.getXdesc());
			id.setItemQty(d.getXqtyord().toString());
			id.setItemUnit(d.getXunit());
			id.setItemCategory(d.getXcatitem());
			id.setItemGroup(d.getXgitem());
			items.add(id);
		}
		chalan.getItems().addAll(items);
		report.getChalans().addAll(chalans);

		byte[] byt = getPDFByte(report, "productionchalanitemdetailreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for chalan : " + xordernum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	@GetMapping("/printsalesorders/{xordernum}")
	public ResponseEntity<byte[]> printChalanWithSalesOrderDetails(@PathVariable String xordernum) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			message = "Chalan not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Opordheader> salesOrders = opordService.findAllSalesOrderByChalan(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode(), xordernum);
		if(salesOrders == null || salesOrders.isEmpty()) {
			message = "Sales orders are not assigned with this chalan : " + xordernum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
		
		
		
		
		return null;
	}

	@PostMapping("/torawforchalan/{xordernum}")
	public @ResponseBody Map<String, Object> transferRawProductBeforProd(@PathVariable String xordernum, Model model) {

		ImtorHeader imtorHeader = new ImtorHeader();
		imtorHeader.setXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode());
		imtorHeader.setXtrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getdefaultCode());
		imtorHeader.setXstatustor("Open");
		imtorHeader.setXchalanref(xordernum);
		imtorHeader.setXfwh("01");
		imtorHeader.setXtwh("02");
		imtorHeader.setXdate(new Date());

		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create transfer order");
			return responseHelper.getResponse();
		}

		// insert raw materials for transfer from suggestion into imtordetail
		List<ProductionSuggestion> suggestions = productionSuggestionService.getProductionSuggestionByChalan(xordernum);
		List<ImtorDetail> details = new ArrayList<>();
		Map<String, RawTO> rm = new HashMap<>();
		suggestions.stream().forEach(s -> {
			if(rm.get(s.getXrawitem()) != null) {
				RawTO r = rm.get(s.getXrawitem());
				r.setXqty(r.getXqty().add(s.getXrawqty()));
			} else {
				RawTO r = new RawTO();
				r.setXitem(s.getXrawitem());
				r.setXqty(s.getXrawqty() != null ? s.getXrawqty() : BigDecimal.ZERO);
				r.setXunit(s.getXrawunit());
				r.setXdesc(s.getXrawdes());
				rm.put(s.getXrawitem(), r);
			}
		});

		for(Map.Entry<String, RawTO> m : rm.entrySet()) {
			ImtorDetail id = new ImtorDetail();
			id.setXtornum(imtorHeader.getXtornum());
			id.setXchalanref(imtorHeader.getXchalanref());
			id.setXitem(m.getKey());
			id.setXqtyord(m.getValue().getXqty());
			id.setXunit(m.getValue().getXunit());
			id.setXitemdesc(m.getValue().getXdesc());
			id.setXrate(BigDecimal.ZERO);
			details.add(id);
		}

		long dcount = 0;
		try {
			dcount = imtorService.saveDetail(details);
		} catch (ServiceException e) {
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}
		if(dcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create transfer order details");
			return responseHelper.getResponse();
		}

		// now update chalan reference
		Opordheader chalan = opordService.findOpordHeaderByXordernum(xordernum);
		if(chalan != null) {
			chalan.setRawxtornum(imtorHeader.getXtornum());
			long ccount = opordService.updateOpordHeader(chalan);
			if(ccount == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update chalan with transfer order reference");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + xordernum);
		responseHelper.setSuccessStatusAndMessage("Transfer order created successfull");
		return responseHelper.getResponse();
	}

	@PostMapping("/tofinishedfromchalan/{xordernum}")
	public @ResponseBody Map<String, Object> transferFrinsiedGoodAfterProd(@PathVariable String xordernum, Model model) {
		ImtorHeader imtorHeader = new ImtorHeader();
		imtorHeader.setXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode());
		imtorHeader.setXtrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getdefaultCode());
		imtorHeader.setXstatustor("Open");
		imtorHeader.setXchalanref(xordernum);
		imtorHeader.setXfwh("02");
		imtorHeader.setXtwh("01");
		imtorHeader.setXdate(new Date());

		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create transfer order");
			return responseHelper.getResponse();
		}

		// get finished goodes from batch and create transfer order details
		List<Moheader> batches = moService.findMoheaderByXchalan(imtorHeader.getXchalanref());
		if(batches == null || batches.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Batch not found in this system for chalan : " + imtorHeader.getXchalanref());
			return responseHelper.getResponse();
		}

		// now crate transfer details from batch
		List<ImtorDetail> toDetails = new ArrayList<>();
		for(Moheader b : batches) {
			ImtorDetail id = new ImtorDetail();
			id.setXtornum(imtorHeader.getXtornum());
			id.setXchalanref(imtorHeader.getXchalanref());
			id.setXitem(b.getXitem());
			id.setXqtyord(b.getXqtycom() != null ? b.getXqtycom() : BigDecimal.ZERO);
			id.setXunit(b.getXqtyprdunit());
			id.setXrate(BigDecimal.ZERO);
			toDetails.add(id);
		}

		// save all details
		try {
			long c = imtorService.saveDetail(toDetails);
			if(c == 0) {
				responseHelper.setErrorStatusAndMessage("Can't add transfer details");
				return responseHelper.getResponse();
			}
		} catch (ServiceException e) {
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// now update chalan reference
		Opordheader chalan = opordService.findOpordHeaderByXordernum(xordernum);
		if(chalan != null) {
			chalan.setFinishedxtornum(imtorHeader.getXtornum());
			long ccount = opordService.updateOpordHeader(chalan);
			if(ccount == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update chalan with transfer order reference");
				return responseHelper.getResponse();
			}
		}
		
		ImtorHeader tor = imtorService.findImtorHeaderByXtornum(imtorHeader.getXtornum());
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if("Confirmed".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			responseHelper.setErrorStatusAndMessage("Transfer Order already confirmed");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> imtorDetailList = imtorService.findImtorDetailByXtornum(imtorHeader.getXtornum());
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
			imtorService.procConfirmTO(imtorHeader.getXtornum(), "Issue and Receive", p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Transfer order created successfull");
		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + xordernum);
		return responseHelper.getResponse();
	}

}

@Data
class RawTO{
	private String xitem;
	private BigDecimal xqty;
	private String xunit;
	private String xdesc;
	private BigDecimal xrate;
}

