package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.asl.entity.Caitem;
import com.asl.entity.Imstock;
import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.ProjectStoreView;
import com.asl.enums.CodeType;
import com.asl.enums.Modules;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.DirectSalesReport;
import com.asl.model.report.ItemDetails;
import com.asl.service.AcService;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.OpcrnService;
import com.asl.service.OpdoService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/salesninvoice/directsales")
public class DirectSalesController extends ASLAbstractController {

	@Autowired private OpdoService opdoService;
	@Autowired private OpcrnService opcrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private VataitService vataitService;
	@Autowired private CacusService cacusService;
	@Autowired private ImstockService imstockService;
	@Autowired private CaitemService caitemService;
	@Autowired private AcService acService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadInvoicePage(Model model) {

		model.addAttribute("opdoheader", getDefaultOpdoHeader());
		  model.addAttribute("allOpdoHeader",
		  opdoService.findAllDirectOpdoHeaderByXtypetrnAndXtrn(
		  TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),
		  TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));
		 
		model.addAttribute("opdoprefix",
				xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());

		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList",xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("purpose", xcodeService.findByXtype(CodeType.PURPOSE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList",xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());
		
		return "pages/salesninvoice/directsales/opdo";
	}

	@GetMapping("/{xdornum}")
	public String loadInvoicePage(@PathVariable String xdornum, Model model) {
		Opdoheader data = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (data == null)
			return "redirect:/salesninvoice/directsales";

		if (data.getXtotamt() == null)
			data.setXtotamt(BigDecimal.ZERO);
		if (data.getXgrandtot() == null)
			data.setXgrandtot(BigDecimal.ZERO);
		if (data.getXvatamt() == null)
			data.setXvatamt(BigDecimal.ZERO);
		if (data.getXpaid() == null)
			data.setXpaid(BigDecimal.ZERO);
		if (data.getXchange() == null)
			data.setXchange(BigDecimal.ZERO);
		if (data.getXdiscamt() == null)
			data.setXdiscamt(BigDecimal.ZERO);
		if (data.getXaitamt() == null)
			data.setXdiscamt(BigDecimal.ZERO);

		model.addAttribute("opdoheader", data);
		model.addAttribute("allOpdoHeader",opdoService.findAllDirectOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));

		model.addAttribute("opdoprefix",xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());

		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList",xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("purpose", xcodeService.findByXtype(CodeType.PURPOSE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList",xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));

		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		List<Opdodetail> mainitems = details.stream().filter(f -> !"Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList());
		List<Opdodetail> subitems = details.stream().filter(f -> "Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList());
		for (Opdodetail m : mainitems) {
			for (Opdodetail s : subitems) {
				if (m.getXrow() == s.getXparentrow()) {
					m.getSubitems().add(s);
				}
			}
		}

		model.addAttribute("opdoDetailsList", mainitems);

		List<Opdodetail> invoiceDetails = opdoService.findOpdoDetailByXdornum(xdornum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		BigDecimal totalVat = BigDecimal.ZERO;

		if (invoiceDetails != null && !invoiceDetails.isEmpty()) {
			for (Opdodetail pd : invoiceDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
				totalVat = totalVat.add(pd.getXvatamt() == null ? BigDecimal.ZERO : pd.getXvatamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		model.addAttribute("totalVat", totalVat);


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

		return "pages/salesninvoice/directsales/opdo";
	}

	private Opdoheader getDefaultOpdoHeader() {
		Opdoheader opdoheader = new Opdoheader();
		opdoheader.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		opdoheader.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		opdoheader.setXdate(new Date());
		opdoheader.setXstatusord("Open");
		opdoheader.setXtotamt(BigDecimal.ZERO);
		opdoheader.setXwh("01");
		opdoheader.setXpaymenttype("Other");

		return opdoheader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opdoheader opdoHeader, BindingResult bindingResult) {
		// Validate
		if (StringUtils.isBlank(opdoHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please select a customer.");
			return responseHelper.getResponse();
		}
		
		if (StringUtils.isBlank(opdoHeader.getXhwh())) {
			responseHelper.setErrorStatusAndMessage("Project/Business required.");
			return responseHelper.getResponse();
		}
		
		if (StringUtils.isBlank(opdoHeader.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Store/Site required.");
			return responseHelper.getResponse();
		}
		
		opdoHeader.setXtype("Direct");

		// If new
		opdoHeader.setXstatusar("Open");
		opdoHeader.setXstatusjv("Open");
		opdoHeader.setXpaymenttype("Other");

		// if existing
		if (StringUtils.isNotBlank(opdoHeader.getXdornum())) {
			Opdoheader exist = opdoService.findOpdoHeaderByXdornum(opdoHeader.getXdornum());
			if (exist == null) {
				responseHelper.setErrorStatusAndMessage("Invoice not found to do update");
				return responseHelper.getResponse();
			}
			BeanUtils.copyProperties(opdoHeader, exist, "xdornum", "xtypetrn", "xtrn");
			long count = opdoService.update(exist);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update invoice");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Invoice updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/directsales/" + opdoHeader.getXdornum());
			return responseHelper.getResponse();
		}

		long count = opdoService.save(opdoHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/directsales/" + opdoHeader.getXdornum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xdornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xdornum) {
		Opdoheader header = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (header == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Invoice in this system");
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		if (details != null && !details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete Invoice details first");
			return responseHelper.getResponse();
		}

		long hcount = opdoService.delete(header.getXdornum());
		if (hcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete GRN");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Invoice deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/directsales");
		return responseHelper.getResponse();
	}

	// Detail Page
	@GetMapping("/{xdornum}/opdodetail/{xrow}/show")
	public String openOpdoDetailModal(@PathVariable String xdornum, @PathVariable String xrow, Model model) {

		Opdoheader opdoheader = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (opdoheader == null)
			return "redirect:/salesninvoice/directsales/" + xdornum;

		model.addAttribute("xordernum", opdoheader.getXordernum());

		if ("new".equalsIgnoreCase(xrow)) {
			Opdodetail opdodetail = new Opdodetail();
			Caitem caitem = new Caitem();
			caitem.setPrevrate(BigDecimal.ZERO);
			opdodetail.setXdornum(xdornum);
			opdodetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			opdodetail.setXqtyord(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			opdodetail.setPrevqty(BigDecimal.ZERO);
			opdodetail.setPrevrate(BigDecimal.ZERO);
			opdodetail.setXaitamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("opdodetail", opdodetail);
		} else {
			Opdodetail opdodetail = opdoService.findOpdoDetailByXdornumAndXrow(xdornum, Integer.parseInt(xrow));
			Caitem caitem = caitemService.findByXitem(opdodetail.getXitem());
			caitem.setPrevrate(caitem.getXrate() == null ? BigDecimal.ZERO : caitem.getXrate());
			opdodetail.setPrevqty(opdodetail.getXqtyord() == null ? BigDecimal.ZERO : opdodetail.getXqtyord());
			opdodetail.setPrevrate(opdodetail.getXrate() == null ? BigDecimal.ZERO : opdodetail.getXrate());
			opdodetail.setForupdate(true);
			model.addAttribute("opdodetail", opdodetail);
		}
		return "pages/salesninvoice/directsales/opdodetailmodal::opdodetailmodal";
	}

	@PostMapping("/opdodetail/save")
	public @ResponseBody Map<String, Object> saveOpdodetail(Opdodetail opdoDetail) {
		boolean savedStatus = false;
		try {
			savedStatus = opdoService.saveInvoiceDetail(opdoDetail);
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		if (savedStatus) {
			responseHelper.setReloadSectionIdWithUrl("opdodetailtable", "/salesninvoice/directsales/opdodetail/" + opdoDetail.getXdornum());
			responseHelper.setSecondReloadSectionIdWithUrl("opdoheaderform", "/salesninvoice/directsales/opdoheaderform/" + opdoDetail.getXdornum());
			responseHelper.setSuccessStatusAndMessage("Invoice detail updated successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Can't save invoice detail");
		return responseHelper.getResponse();
	}

	@GetMapping("/opdodetail/{xdornum}")
	public String reloadOpdoDetailTable(@PathVariable String xdornum, Model model) {
		List<Opdodetail> invoiceDetails = opdoService.findOpdoDetailByXdornum(xdornum);
		model.addAttribute("opdoDetailsList", invoiceDetails);
		model.addAttribute("opdoheader", opdoService.findOpdoHeaderByXdornum(xdornum));

		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		BigDecimal totalVat = BigDecimal.ZERO;

		if (invoiceDetails != null && !invoiceDetails.isEmpty()) {
			for (Opdodetail pd : invoiceDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
				totalVat = totalVat.add(pd.getXvatamt() == null ? BigDecimal.ZERO : pd.getXvatamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		model.addAttribute("totalVat", totalVat);

		return "pages/salesninvoice/directsales/opdo::opdodetailtable";
	}

	@GetMapping("/opdoheaderform/{xdornum}")
	public String reloadOpdoheaderform(@PathVariable String xdornum, Model model) {
		Opdoheader data = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (data.getXtotamt() == null)
			data.setXtotamt(BigDecimal.ZERO);
		if (data.getXgrandtot() == null)
			data.setXgrandtot(BigDecimal.ZERO);
		if (data.getXvatamt() == null)
			data.setXvatamt(BigDecimal.ZERO);
		if (data.getXpaid() == null)
			data.setXpaid(BigDecimal.ZERO);
		if (data.getXchange() == null)
			data.setXchange(BigDecimal.ZERO);
		if (data.getXdiscamt() == null)
			data.setXdiscamt(BigDecimal.ZERO);

		model.addAttribute("opdoheader", data);
		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("purpose", xcodeService.findByXtype(CodeType.PURPOSE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		return "pages/salesninvoice/directsales/opdo::opdoheaderform";
	}

	@PostMapping("{xdornum}/opdodetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpdoDetail(@PathVariable String xdornum, @PathVariable String xrow, Model model) {
		boolean deletestatus = false;
		try {
			deletestatus = opdoService.deleteInvoiceDetail(xdornum, Integer.parseInt(xrow));
		} catch (NumberFormatException | ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}
		
		if(!deletestatus) {
			responseHelper.setErrorStatusAndMessage("Can't delete invoice detail");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("opdodetailtable", "/salesninvoice/directsales/opdodetail/" + xdornum);
		responseHelper.setSecondReloadSectionIdWithUrl("opdoheaderform", "/salesninvoice/directsales/opdoheaderform/" + xdornum);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirminvoice/{xdornum}")
	public @ResponseBody Map<String, Object> confirmOpdo(@PathVariable String xdornum) {
		Opdoheader opdoHeader = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (opdoHeader == null) {
			responseHelper.setErrorStatusAndMessage("Invoice not found in this system");
			return responseHelper.getResponse();
		}

		// validate
		if (StringUtils.isBlank(opdoHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}
		if (!"Open".equalsIgnoreCase(opdoHeader.getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Invoice already confirmed");
			return responseHelper.getResponse();
		}

		List<Opdodetail> opdoDetailList = opdoService.findOpdoDetailByXdornum(xdornum);
		if (opdoDetailList == null || opdoDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Invoice has no item details");
			return responseHelper.getResponse();
		}

		// stock validation
		Map<String, BigDecimal> itemMap = new HashMap<>();
		for (Opdodetail d : opdoDetailList) {
			if (itemMap.get(d.getXitem() + '|' + opdoHeader.getXwh()) != null) {
				itemMap.put(d.getXitem() + '|' + opdoHeader.getXwh(),
						itemMap.get(d.getXitem() + '|' + opdoHeader.getXwh()).add(d.getXqtyord()));
			} else {
				itemMap.put(d.getXitem() + '|' + opdoHeader.getXwh(), d.getXqtyord());
			}
		}

		boolean hasError = false;
		StringBuilder ems = new StringBuilder("Stock Not available.");
		for (Map.Entry<String, BigDecimal> m : itemMap.entrySet()) {
			String[] key = m.getKey().split("\\|");
			String xitem = key[0];
			String xwh = key[1];
			BigDecimal qty = m.getValue();

			// check this item is an service item or not
			// 'Service','Cost','Servicing','Service Charge','Services'
			Caitem caitem = caitemService.findByXitem(xitem);
			if(caitem == null 
					|| "Service".equalsIgnoreCase(caitem.getXgitem())
					|| "Cost".equalsIgnoreCase(caitem.getXgitem())
					|| "Servicing".equalsIgnoreCase(caitem.getXgitem())
					|| "Service Charge".equalsIgnoreCase(caitem.getXgitem())
					|| "Services".equalsIgnoreCase(caitem.getXgitem())) continue;

			Imstock stock = imstockService.findByXitemAndXwh(xitem, xwh);
			if (stock == null)
				continue;

			if (stock.getXavail().compareTo(qty) == -1) {
				hasError = true;
				ems.append("<br/>Item [" + xitem + "] - " + xwh + ", Available : " + stock.getXavail() + ", Required : "
						+ qty);
			}
		}
		if (hasError) {
			responseHelper.setErrorStatusAndMessage(ems.toString());
			return responseHelper.getResponse();
		}

		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(opdoHeader.getXstatusord())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procConfirmDO(opdoHeader.getXdornum(), p_seq);
			// Error check here for procConfrimDo
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}

			p_seq =xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procIssuePricing(opdoHeader.getXdornum(), opdoHeader.getXwh(),"opdodetail",p_seq); 
			// Error check here for procIssuePricing 
			em = getProcedureErrorMessages(p_seq); if (StringUtils.isNotBlank(em)) {
			responseHelper.setErrorStatusAndMessage(em); 
			return responseHelper.getResponse(); 
			}
			
		}
		
		if (!"Confirmed".equalsIgnoreCase(opdoHeader.getXstatusar())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procTransferOPtoAR(opdoHeader.getXdornum(), "opdoheader", p_seq);
			// Error check here for procTransferOPtoAR
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		if(isModuleActive(Modules.ACCOUNTING)) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procTransferOPtoGL(opdoHeader.getXdornum(), "opdoheader", p_seq);
			// Error check here for procTransferOPtoAR 
			String em2 = getProcedureErrorMessages(p_seq); 
			if (StringUtils.isNotBlank(em2)) {
				responseHelper.setErrorStatusAndMessage(em2); 
				return responseHelper.getResponse(); 
			}
		}

		responseHelper.setSuccessStatusAndMessage("Invoice Confirmed successfully");
		responseHelper.setRedirectUrl("/salesninvoice/directsales/" + xdornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/returnsales/{xdornum}")
	public @ResponseBody Map<String, Object> retrunsales(@PathVariable String xdornum) {
		if (StringUtils.isBlank(xdornum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// Get OpdoHeader record by xdornum
		Opdoheader opdoheader = opdoService.findOpdoHeaderByXdornum(xdornum);

		if (opdoheader != null) {
			Opcrnheader opcrnheader = new Opcrnheader();
			BeanUtils.copyProperties(opdoheader, opcrnheader, "xdate", "xtype", "xtypetrn", "xtrn");
			opcrnheader.setXdornum(opdoheader.getXdornum());
			opcrnheader.setXstatuscrn("Open");
			opcrnheader.setXdate(new Date());
			opcrnheader.setXcus(opdoheader.getXcus());
			opcrnheader.setXtype(TransactionCodeType.SALES_RETURN.getCode());
			opcrnheader.setXtypetrn("CRN Number");
//			opcrnheader.setXtrncrn(TransactionCodeType.SALES_RETURN.getCode());
//			opcrnheader.setXtrn(TransactionCodeType.SALES_RETURN.getdefaultCode());

			long count = opcrnService.save(opcrnheader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			opcrnheader = opcrnService.findOpcrnHeaderByXdornum(xdornum);

			// Get Sales Order details to Copy them in CRN
			List<Opdodetail> opdoDetailList = opdoService.findOpdoDetailByXdornum(xdornum);
			Opcrndetail opcrnDetail;

			for (int i = 0; i < opdoDetailList.size(); i++) {
				opcrnDetail = new Opcrndetail();
				BeanUtils.copyProperties(opdoDetailList.get(i), opcrnDetail, "xrow", "xnote");
				opcrnDetail.setXcrnnum(opcrnheader.getXcrnnum());
				opcrnDetail.setXunit(opdoDetailList.get(i).getXunitsel());
				// opcrnDetail.setXqtyord(opdoDetailList.get(i).getXqtyord());

				long nCount = opcrnService.saveDetail(opcrnDetail);

				// Update Inventory
				if (nCount == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
			}

			// Update PoordHeader Status
			/*
			 * opdoheader.setXstatusord("Returned"); long pCount =
			 * opdoService.update(opdoheader); if(pCount == 0) {
			 * responseHelper.setStatus(ResponseStatus.ERROR); return
			 * responseHelper.getResponse(); }
			 */

			responseHelper.setSuccessStatusAndMessage("Sales Returned successfully");
			responseHelper.setRedirectUrl("/salesninvoice/salesreturn/" + opcrnheader.getXcrnnum());
			return responseHelper.getResponse();
		}
		responseHelper.setStatus(ResponseStatus.ERROR);
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{xdornum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String xdornum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);

		Cacus cacus = cacusService.findByXcus(oh.getXcus());
		
		DirectSalesReport report = new DirectSalesReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Invoice/Bill");
		report.setFromDate(sdf.format(oh.getXdate()));
		report.setPrintDate(sdf.format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setEmail(sessionManager.getZbusiness().getXemail());
		

		report.setXdornum(oh.getXdornum());
		report.setXcus(oh.getXcus());
		report.setXpaymenttype(oh.getXpaymenttype());
		report.setXnote(oh.getXnote());
		report.setXorg(cacus.getXorg());
		report.setXmadd(cacus.getXmadd());
		report.setXphone(cacus.getXphone());
		report.setXdate(sdf.format(oh.getXdate()));
		report.setXtotamt(oh.getXtotamt());
		report.setXvatamt(oh.getXvatamt());
		report.setXdiscamt(oh.getXdiscamt());
		report.setXgrandtot(oh.getXgrandtot());
		report.setTotalQty(BigDecimal.ZERO);
		report.setSpellword(oh.getSpellword());
		report.setXprimeword(oh.getXprimeword());
		report.setXpurpose(oh.getXpurpose());
		report.setXland(oh.getXland());
		//report.setXgrandtot((oh.getXtotamt().add(oh.getXaitamt()).add(oh.getXvatamt())).subtract(oh.getXdiscamt()));
		report.setXgrandtot((oh.getXtotamt().add(oh.getXaitamt() !=null ? oh.getXaitamt() : 
			BigDecimal.ZERO ).add(oh.getXvatamt() != null ? oh.getXvatamt() : BigDecimal.ZERO)).subtract(oh.getXdiscamt() !=null ? oh.getXdiscamt() : BigDecimal.ZERO));
			
		
		List<Opdodetail> items = opdoService.findOpdoDetailByXdornum(oh.getXdornum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXdesc());
				item.setItemQty(it.getXqtyord().toString());
				item.setRate(it.getXrate());
				item.setItemUnit(it.getXunitsel());
				item.setLineamt(it.getXlineamt());
				item.setItemQty(it.getXqtyord() != null ? it.getXqtyord().toString() : BigDecimal.ZERO.toString());
				
				report.getItems().add(item);
				report.setTotalQty(report.getTotalQty().add(BigDecimal.valueOf(Double.valueOf(item.getItemQty()))));

				
			});
		}

		byte[] byt = getPDFByte(report, "directsalesinvoicereport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for direct sales : " + xdornum;
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
	
	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
	//server side pagenation
//	@PostMapping("/data")
//	public @ResponseBody SalesListPaging loadPoordListpageNew(PaginationDTO paginationDTO) {
//		SalesListPaging paging = new SalesListPaging();
//				
//		List<Opdoheader> listhHeaders = new ArrayList<Opdoheader>();
//		listhHeaders.addAll(opdoService.directSalesDetails(paginationDTO.getLimit(), paginationDTO.getPage(),
//		paginationDTO.getHint(), paginationDTO.getOrderBy(),paginationDTO.getSortType()));
//				
//		paging.setOpdoheader(listhHeaders);
//		long count = opdoService.countOfdirectSalesDetails(paginationDTO.getLimit(), paginationDTO.getPage(),
//		paginationDTO.getHint(), paginationDTO.getOrderBy());
//				
//		paging.setCount(count);
//				
//		return paging;
//			}
}

@Data
class SalesListPaging{
	private List<Opdoheader> opdoheader;
	private long count;
}
