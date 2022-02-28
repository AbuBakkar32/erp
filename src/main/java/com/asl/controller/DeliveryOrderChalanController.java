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

import com.asl.entity.Cacus;
import com.asl.entity.Imstock;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesOrder;
import com.asl.model.report.SalesOrderChalanReport;
import com.asl.service.CacusService;
import com.asl.service.ImstockService;
import com.asl.service.OpdoService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/salesninvoice/deliveryorderchalan")
public class DeliveryOrderChalanController extends ASLAbstractController {

	@Autowired private OpdoService opdoService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CacusService cacusService;
	@Autowired private ImstockService imstockService;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@GetMapping
	public String loadDeliveryOrderChalanPage(Model model) {
		model.addAttribute("deliveryorderchalan", getDefaultOpdoheader());
		model.addAttribute("deliveryorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("deliveryorderchalanList", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan";
	}

	@GetMapping("/{xdornum}")
	public String loadSalesOrderChalanPage(@PathVariable String xdornum, Model model) {
		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null)
			return "redirect:/salesninvoice/deliveryorderchalan";

		model.addAttribute("deliveryorderchalan", oh);
		model.addAttribute("deliveryorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("deliveryorderchalanList", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));

		List<Opdoheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		if ("Open".equalsIgnoreCase(oh.getXstatusord()))
			allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrder(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), "Open", new Date()));

		allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), xdornum));

		model.addAttribute("openinvoiceorders", allOpenAndConfirmesSalesOrders);

		model.addAttribute("chalandetails", opdoService.findAssignedOpdoDetailByChalan(xdornum));
		
		List<Opdodetail> poordDetails = opdoService.findAssignedOpdoDetailByChalan(xdornum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		if(poordDetails != null && !poordDetails.isEmpty()) {
			for(Opdodetail pd : poordDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan";
	}

	private Opdoheader getDefaultOpdoheader() {
		Opdoheader oh = new Opdoheader();
		oh.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		oh.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		oh.setXtypetrn(TransactionCodeType.CHALAN_NUMBER.getCode());
		oh.setXdate(new Date());
		oh.setXstatusord("Open");
		return oh;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opdoheader opdoheader, BindingResult bindingResult, Model model) {
		if (opdoheader == null || StringUtils.isBlank(opdoheader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		Opdoheader existOh = opdoService.findOpdoHeaderByXdornum(opdoheader.getXdornum());
		if (existOh != null) {
			BeanUtils.copyProperties(opdoheader, existOh, "xdate", "xstatusord");
			long count = opdoService.update(existOh);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Chalan not updated");
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/salesninvoice/deliveryorderchalan/" + existOh.getXdornum());
			responseHelper.setSuccessStatusAndMessage("Chalan updated successfully");
			return responseHelper.getResponse();
		}

		// if new
		opdoheader.setXstatusord("Open");
		opdoheader.setXdate(new Date());
		long count = opdoService.save(opdoheader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Chalan not created");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/deliveryorderchalan/" + opdoheader.getXdornum());
		responseHelper.setSuccessStatusAndMessage("Chalan created successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/opendeliveryorder/query")
	public String reloadTableWithData(@RequestParam String xdornum, @RequestParam String date, Model model) throws ParseException {
		model.addAttribute("deliveryorderchalan", opdoService.findOpdoHeaderByXdornum(xdornum));
		List<Opdoheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrder(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), "Open", new Date()));
		allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), xdornum));
		model.addAttribute("openinvoiceorders", allOpenAndConfirmesSalesOrders);
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan::opendeliveryorderstable";
	}

	@PostMapping("/opendeliveryorder/query")
	public @ResponseBody Map<String, Object> queryForrequistionDetails(String xdornum, Date xdate, Model model) {
		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable","/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum=" + xdornum + "&date=" + sdf.format(xdate));
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}

	@GetMapping("/ordreqdetails/{xdornum}/show")
	public String displayItemDetailsOfOrderRequistion(@PathVariable String xdornum, Model model) {
		model.addAttribute("opdodetailsList", opdoService.findOpdoDetailByXdornum(xdornum));
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderdetailmodal::deliveryorderdetailmodal";
	}

	@PostMapping("/deliveryorderconfirm/{chalan}/{xdornum}")
	public @ResponseBody Map<String, Object> confirmSalesOrderAndCreateChalanDetail(@PathVariable String chalan, @PathVariable String xdornum, Model model) {

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if (StringUtils.isNotBlank(oh.getXdocnum())) {
			responseHelper.setErrorStatusAndMessage("Sales order already added to chalan : " + oh.getXdocnum() + " . Please reload this page again");
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		if (details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This " + xdornum + " Sales Order has no item to add this chalan");
			return responseHelper.getResponse();
		}

		// now update sales order with chalan reference
		oh.setXdocnum(chalan);
		oh.setXchalancreated(true);
		long count = opdoService.update(oh);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Update Sales Order");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable", "/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum=" + chalan + "&date=" + sdf.format(oh.getXdate()));
		responseHelper.setSecondReloadSectionIdWithUrl("deliveryorderchalandetailtable", "/salesninvoice/deliveryorderchalan/chalandetail/" + chalan);
		responseHelper.setSuccessStatusAndMessage("Sales order assigned to this chalan successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/deliveryorderrevoke/{chalan}/{xdornum}")
	public @ResponseBody Map<String, Object> revokeSalesOrder(@PathVariable String chalan, @PathVariable String xdornum,
			Model model) {
		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		if (details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This " + xdornum + " Sales Order has no item to remove from chalan");
			return responseHelper.getResponse();
		}


		// now update sales order with chalan reference
		oh.setXdocnum(null);
		oh.setXchalancreated(false);
		oh.setXstatusord("Open");
		long count = opdoService.update(oh);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Update Sales Order");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable", "/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum=" + chalan + "&date=" + sdf.format(oh.getXdate()));
		responseHelper.setSecondReloadSectionIdWithUrl("deliveryorderchalandetailtable", "/salesninvoice/deliveryorderchalan/chalandetail/" + chalan);
		responseHelper.setSuccessStatusAndMessage("Sales order unassigned from this chalan successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/chalandetail/{xdornum}")
	public String reloadChalanDetailSection(@PathVariable String xdornum, Model model) {
		model.addAttribute("deliveryorderchalan", opdoService.findOpdoHeaderByXdornum(xdornum));
		model.addAttribute("chalandetails", opdoService.findAssignedOpdoDetailByChalan(xdornum));
		
		List<Opdodetail> poordDetails = opdoService.findAssignedOpdoDetailByChalan(xdornum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		if(poordDetails != null && !poordDetails.isEmpty()) {
			for(Opdodetail pd : poordDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan::deliveryorderchalandetailtable";
	}

	@PostMapping("/lockchalan/{chalan}")
	public @ResponseBody Map<String, Object> lockChalan(@PathVariable String chalan, Model model) {
		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(chalan);
		if (oh == null) {
			responseHelper.setErrorStatusAndMessage("Delivery chalan " + chalan + " not exist in this system");
			return responseHelper.getResponse();
		}

		List<Opdoheader> invoiceList = opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), chalan);
		if(invoiceList == null || invoiceList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No Sales invoice assigned in this delivery chalan : " + chalan);
			return responseHelper.getResponse();
		}

		//TODO: stock validation
		Map<String, BigDecimal> itemMap = new HashMap<>();
		for(Opdoheader inv : invoiceList) {
			List<Opdodetail> dlist = opdoService.findOpdoDetailByXdornum(inv.getXdornum());
			if(dlist != null && !dlist.isEmpty()) {
				for(Opdodetail d : dlist) {
					if(itemMap.get(d.getXitem() + '|' + inv.getXwh()) != null) {
						itemMap.put(d.getXitem() + '|' + inv.getXwh(), itemMap.get(d.getXitem() + '|' + inv.getXwh()).add(d.getXqtyord()));
					} else {
						itemMap.put(d.getXitem() + '|' + inv.getXwh(), d.getXqtyord());
					}
				}
			}
		}

		boolean hasError = false;
		StringBuilder ems = new StringBuilder("Stock Not available.");
		for(Map.Entry<String, BigDecimal> m : itemMap.entrySet()) {
			String[] key = m.getKey().split("\\|");
			String xitem = key[0];
			String xwh = key[1];
			BigDecimal qty = m.getValue();

			Imstock stock = imstockService.findByXitemAndXwh(xitem, xwh);
			if(stock == null) continue;

			if(stock.getXavail().compareTo(qty) == -1) {
				hasError = true;
				ems.append("<br/>Item [" + xitem + "] - " + xwh + ", Available : " + stock.getXavail() + ", Required : " + qty);
			}
		}
		if(hasError) {
			responseHelper.setErrorStatusAndMessage(ems.toString());
			return responseHelper.getResponse();
		}


		String p_seq;
		for (Opdoheader order : invoiceList) {
			if(!"Confirmed".equalsIgnoreCase(order.getXstatusord())) {
				p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
				opdoService.procConfirmDO(order.getXdornum(), p_seq);
				//Error check here for procConfrimDo
				String em = getProcedureErrorMessages(p_seq);
				if(StringUtils.isNotBlank(em)) {
					responseHelper.setErrorStatusAndMessage(em);
					return responseHelper.getResponse();
				}

				p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
				opdoService.procIssuePricing(order.getXdocnum(), order.getXwh(),"opdodetail", p_seq);
				//Error check here for procIssuePricing
				em = getProcedureErrorMessages(p_seq);
				if(StringUtils.isNotBlank(em)) {
					responseHelper.setErrorStatusAndMessage(em);
					return responseHelper.getResponse();
				}
			}

			if(!"Confirmed".equalsIgnoreCase(order.getXstatusar())){
				p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
				opdoService.procTransferOPtoAR(order.getXdornum(), "opdoheader", p_seq);
				//Error check here for procTransferOPtoAR
				String em = getProcedureErrorMessages(p_seq);
				if(StringUtils.isNotBlank(em)) {
					responseHelper.setErrorStatusAndMessage(em);
					return responseHelper.getResponse();
				}
				
			}
		}
		
		// now lock chalan
		oh.setXstatusord("Confirmed");
		long count = opdoService.update(oh);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't lock Chalan");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Chalan locked successfully");
		responseHelper.setRedirectUrl("/salesninvoice/deliveryorderchalan/" + chalan);
		return responseHelper.getResponse();
	}

	@GetMapping("/print/chalan/{pType}/{xdornum}")
	public ResponseEntity<byte[]> printChalanWithSalesOrderDetails(@PathVariable String pType,
			@PathVariable String xdornum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null) {
			message = "Chalan not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Opdoheader> salesOrders = opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),
				TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), oh.getXdornum());
		if (salesOrders == null || salesOrders.isEmpty()) {
			message = "No sales order found in this chalan : " + xdornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<SalesOrderChalanReport> allReports = new ArrayList<>();
		Cacus cacus = new Cacus();

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		for (Opdoheader so : salesOrders) {

			SalesOrderChalanReport report = new SalesOrderChalanReport();
			report.setBusinessName(sessionManager.getZbusiness().getZorg());
			report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
			report.setReportName("Delivery Chalan" );
			report.setFromDate(sdf.format(oh.getXdate()));
			report.setToDate(sdf.format(oh.getXdate()));
			report.setPrintDate(sdf.format(new Date()));
			report.setReportLogo(sessionManager.getZbusiness().getXbimage());

			report.setChalanNumber(oh.getXdornum());
			report.setChalanDate(sdf.format(oh.getXdate()));
			report.setChalanStatus(oh.getXstatusar());

			SalesOrder salesOrder = new SalesOrder();
			cacus = cacusService.findByXcus(so.getXcus());
			salesOrder.setOrderNumber(so.getXdornum());
			salesOrder.setReqBranch(so.getXcus());
			salesOrder.setCustomer(cacus.getXorg());
			salesOrder.setCustomerAddress(cacus.getXmadd());
			salesOrder.setDate(sdf.format(so.getXdate()));
			salesOrder.setSpellword2(so.getSpellword2());
			salesOrder.setXprimeword2(so.getXprimeword2());
			if ("invoices".equalsIgnoreCase(pType)) {
				report.setReportName("Invoice");

				salesOrder.setTotalAmount(so.getXtotamt() != null ? so.getXtotamt().toString() : "");
				salesOrder.setVatAmount(so.getXvatamt() != null ? so.getXvatamt().toString() : "");
				salesOrder.setDiscountAmount(so.getXdiscamt() != null ? so.getXdiscamt().toString() : "");
				salesOrder.setGrandTotalAmount(so.getXgrandtot() != null ? so.getXgrandtot().toString() : "");
			}

			List<Opdodetail> items = opdoService.findOpdoDetailByXdornum(so.getXdornum());
			if (items != null && !items.isEmpty()) {
				items.stream().forEach(it -> {
					ItemDetails item = new ItemDetails();
					item.setItemCode(it.getXitem());
					item.setItemName(it.getXdesc());
					item.setItemQty(it.getXqtyord() != null ? it.getXqtyord().toString() : "");
					item.setItemUnit(it.getXunitsel());
					item.setItemCategory(it.getXcatitem());
					item.setItemGroup(it.getXgitem());

					if ("invoices".equalsIgnoreCase(pType)) {
						item.setItemRate(it.getXrate() != null ? it.getXrate().toString() : "");
						item.setItemTotalAmount(it.getXlineamt() != null ? it.getXlineamt().toString() : "");
					}

					salesOrder.getItems().add(item);
				});
			}

			report.getSalesorders().add(salesOrder);

			allReports.add(report);

		}

		byte[] byt;
		if (!"invoices".equalsIgnoreCase(pType))
			byt = getBatchPDFByte(allReports, "deliverychalanreport.xsl", request);
		else
			byt = getBatchPDFByte(allReports, "deliverychalaninvoicesreport.xsl", request);
		if (byt == null) {
			message = "Can't print report for chalan : " + xdornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	@GetMapping("/print/order/{pType}/{xdornum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String pType,
			@PathVariable String xdornum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		
		if (oh == null) {
			message = "Invoice not found to print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Cacus cacus = cacusService.findByXcus(oh.getXcus());
		Opdoheader chalan = opdoService.findOpdoHeaderByXdornum(oh.getXdocnum());
		if (chalan == null) {
			message = "Invoice is not assigned to a chalan";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		/*
		 * List<Opdoheader> salesOrders =
		 * opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.
		 * SALES_AND_INVOICE_NUMBER.getCode(),
		 * TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(),
		 * oh.getXdornum()); if(salesOrders == null || salesOrders.isEmpty()) { message
		 * = "No sales order found in this chalan : " + xdornum; return new
		 * ResponseEntity<>(message.getBytes(), headers,
		 * HttpStatus.INTERNAL_SERVER_ERROR); }
		 */

		// List<SalesOrderChalanReport> allReports = new ArrayList<>();
		//SalesOrderChalanReport orderReport = new SalesOrderChalanReport();

		// for(Opdoheader so : salesOrders) {

		SalesOrderChalanReport report = new SalesOrderChalanReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Delivery Chalan : " + oh.getXdocnum());
		report.setFromDate(SDF.format(chalan.getXdate()));
		report.setToDate(SDF.format(chalan.getXdate()));
		report.setPrintDate(SDF.format(new Date()));
		report.setReportLogo(appConfig.getReportLogo());

		report.setChalanNumber(chalan.getXdornum());
		report.setChalanDate(SDF.format(chalan.getXdate()));
		report.setChalanStatus(chalan.getXstatusar());

		SalesOrder salesOrder = new SalesOrder();
		salesOrder.setOrderNumber(oh.getXdornum());
		salesOrder.setReqBranch(oh.getXcus());
		salesOrder.setCustomer(cacus.getXorg());
		salesOrder.setCustomerAddress(cacus.getXmadd());
		salesOrder.setDate(SDF.format(oh.getXdate()));
		salesOrder.setSpellword2(oh.getSpellword2());
		salesOrder.setXprimeword2(oh.getXprimeword2());
		if ("invoice".equalsIgnoreCase(pType)) {
			report.setReportName("Sales Invoice  : " + oh.getXdocnum());

			salesOrder.setTotalAmount(oh.getXtotamt() != null ? oh.getXtotamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setVatAmount(oh.getXvatamt() != null ? oh.getXvatamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setDiscountAmount(oh.getXdiscamt() != null ? oh.getXdiscamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setGrandTotalAmount(oh.getXgrandtot() != null ? oh.getXgrandtot().toString() : BigDecimal.ZERO.toString());
		}

		List<Opdodetail> items = opdoService.findOpdoDetailByXdornum(oh.getXdornum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXdesc());
				item.setItemQty(it.getXqtyord().toString());
				item.setItemUnit(it.getXunitsel());
				item.setItemCategory(it.getXcatitem());
				item.setItemGroup(it.getXgitem());

				if ("invoice".equalsIgnoreCase(pType)) {
					item.setItemRate(it.getXrate() != null ? it.getXrate().toString() : BigDecimal.ZERO.toString());
					item.setItemTotalAmount(it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());
				}

				salesOrder.getItems().add(item);
			});
		}

		report.getSalesorders().add(salesOrder);

		byte[] byt;
		if ("invoice".equalsIgnoreCase(pType))
			byt = getPDFByte(report, "deliverychalaninvoicesreport.xsl", request);
		else
			byt = getPDFByte(report, "deliverychalanreport.xsl", request);
		if (byt == null) {
			message = "Can't print report for chalan : " + xdornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

}

