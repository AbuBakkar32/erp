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
import com.asl.entity.DataList;
import com.asl.entity.Imstock;
import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.ProjectStoreView;
import com.asl.entity.Vatait;
import com.asl.enums.CodeType;
import com.asl.enums.Modules;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.DirectSalesReport;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesOrder;
import com.asl.model.report.SalesOrderChalanReport;
import com.asl.service.AcService;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.OpcrnService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

import lombok.Data;

@Controller
@RequestMapping("/salesninvoice/salesandinvoice")
public class InvoiceController extends ASLAbstractController {

	@Autowired private OpdoService opdoService;
	@Autowired private OpcrnService opcrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private VataitService vataitService;
	@Autowired private CacusService cacusService;
	@Autowired private OpordService opordService;
	@Autowired private ImstockService imstockService;
	@Autowired private AcService acService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadInvoicePage(Model model) {

		model.addAttribute("opdoheader", getDefaultOpdoHeader());
		model.addAttribute("allOpdoHeader", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));

		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());

		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());

		List<DataList> dl = listService.getList("SYSTEM", "INVOICE_PAGE");
		if(dl != null && !dl.isEmpty()) {
			DataList d = dl.get(0);
			if(StringUtils.isNotBlank(d.getListvalue2())) {
				return "pages/salesninvoice/salesandinvoice/" + d.getListvalue2();
			}
		}

		return "pages/salesninvoice/salesandinvoice/opdo";
	}
	
	@GetMapping("/invoicelist")
	public String loadPoordListPage(Model model) {
		model.addAttribute("allOpdoHeader", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));

		return "pages/salesninvoice/salesandinvoice/invoicelist";
	}

	@GetMapping("/{xdornum}")
	public String loadInvoicePage(@PathVariable String xdornum, Model model) {
		Opdoheader data = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (data == null) return "redirect:/salesninvoice/salesandinvoice";

		model.addAttribute("opdoheader", data);
		model.addAttribute("allOpdoHeader", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));

		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());

		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		List<Opdodetail> mainitems = details.stream().filter(f -> !"Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList());
		List<Opdodetail> subitems = details.stream().filter(f -> "Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList());
		for(Opdodetail m : mainitems) {
			for(Opdodetail s : subitems) {
				if(m.getXrow() == s.getXparentrow()) {
					m.getSubitems().add(s);
				}
			}
		}
		
		model.addAttribute("opdoDetailsList", mainitems);

		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(details != null && !details.isEmpty()) {
			for(Opdodetail pd : details) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
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

		List<DataList> dl = listService.getList("SYSTEM", "INVOICE_PAGE");
		if(dl != null && !dl.isEmpty()) {
			DataList d = dl.get(0);
			if(StringUtils.isNotBlank(d.getListvalue2())) {
				return "pages/salesninvoice/salesandinvoice/" + d.getListvalue2();
			}
		}

		return "pages/salesninvoice/salesandinvoice/opdo";
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

		Vatait vatait = vataitService.findVataitByXvatait(opdoHeader.getXvatait());
		if(opdoHeader.getXtotamt() == null) opdoHeader.setXtotamt(BigDecimal.ZERO);
		if(StringUtils.isNotBlank(opdoHeader.getXvatait()) && !"No Vat".equalsIgnoreCase(opdoHeader.getXvatait()) && vatait != null) {
			if(opdoHeader.getXvatamt() == null) opdoHeader.setXvatamt((opdoHeader.getXtotamt().multiply(vatait.getXvat())).divide(BigDecimal.valueOf(100)));
			if(opdoHeader.getXait() == null) opdoHeader.setXait((opdoHeader.getXtotamt().multiply(vatait.getXait())).divide(BigDecimal.valueOf(100)));
		} else {
			if(opdoHeader.getXvatamt() == null) opdoHeader.setXvatamt(BigDecimal.ZERO);
			if(opdoHeader.getXait() == null) opdoHeader.setXait(BigDecimal.ZERO);
		}
		if(opdoHeader.getXdiscamt() == null) opdoHeader.setXdiscamt(BigDecimal.ZERO);
		if(opdoHeader.getXpaid() == null) opdoHeader.setXpaid(BigDecimal.ZERO);
		if(opdoHeader.getXchange() == null) opdoHeader.setXchange(BigDecimal.ZERO);
		BigDecimal grandTotal = (opdoHeader.getXtotamt().add(opdoHeader.getXvatamt()).add(opdoHeader.getXait())).subtract(opdoHeader.getXdiscamt());
		opdoHeader.setXgrandtot(grandTotal);

		if (opdoHeader.getXgrandtot().compareTo(opdoHeader.getXpaid()) == -1) {
			opdoHeader.setXchange(opdoHeader.getXpaid().subtract(opdoHeader.getXgrandtot()));
		} else {
			opdoHeader.setXchange(BigDecimal.ZERO);
		}

		// If new
		opdoHeader.setXstatusar("Open");
		opdoHeader.setXstatusjv("Open");

		// if existing record
		if (StringUtils.isNotBlank(opdoHeader.getXdornum())) {
			Opdoheader exist = opdoService.findOpdoHeaderByXdornum(opdoHeader.getXdornum());
			if(exist == null) {
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
			responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + opdoHeader.getXdornum());
			return responseHelper.getResponse();
		}

		long count = opdoService.save(opdoHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + opdoHeader.getXdornum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xdornum}")
	public  @ResponseBody Map<String, Object> archive(@PathVariable String xdornum) {
		Opdoheader header = opdoService.findOpdoHeaderByXdornum(xdornum);
		if(header == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Invoice in this system");
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		if(details != null && !details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete Invoice details first");
			return responseHelper.getResponse();
		}

		long hcount = opdoService.delete(header.getXdornum());
		if(hcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete GRN");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Invoice deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice");
		return responseHelper.getResponse();
	}

	@GetMapping("/{xdornum}/opdodetail/{xrow}/show")
	public String openOpdoDetailModal(@PathVariable String xdornum, @PathVariable String xrow, Model model) {

		Opdoheader opdoheader = opdoService.findOpdoHeaderByXdornum(xdornum);
		if(opdoheader == null) return "redirect:/salesninvoice/salesandinvoice/" + xdornum;

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
		
		return "pages/salesninvoice/salesandinvoice/opdodetailmodal::opdodetailmodal";
	}

	@PostMapping("/opdodetail/save")
	public @ResponseBody Map<String, Object> saveOpdodetail(Opdodetail opdoDetail) {
		if (opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(opdoDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item not selected! Please select an item");
			return responseHelper.getResponse();
		}
		if(opdoDetail.getXqtyord() == null || BigDecimal.ZERO.equals(opdoDetail.getXqtyord()) || opdoDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Quantity should be greater then 0");
			return responseHelper.getResponse();
		}

		// validate invoice quantity
		Opdoheader oph = opdoService.findOpdoHeaderByXdornum(opdoDetail.getXdornum());
		if(oph == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Invoice");
			return responseHelper.getResponse();
		}
		Oporddetail oporddetail = opordService.findOporddetailByXordernumAndXrow(oph.getXordernum(), opdoDetail.getXdorrow());
//		if(oporddetail == null) {
//			responseHelper.setErrorStatusAndMessage("Can't find Sales order detail for this item row");
//			return responseHelper.getResponse();
//		}
		// calculate and update oporddetail xqtygrn 
		

//		long count2 = opordService.updateOpordDetail(oporddetail);
//		if(count2 == 0) {
//			responseHelper.setErrorStatusAndMessage("Can't update sales qty to Sales detail");
//			return responseHelper.getResponse();
//		}

		// modify line amount
		opdoDetail.setXitem(opdoDetail.getXitem().split("\\|")[0]);
		opdoDetail.setXlineamt(opdoDetail.getXqtyord().multiply(opdoDetail.getXrate()).setScale(2, RoundingMode.DOWN));

		// if existing
		Opdodetail existDetail = opdoService.findOpdoDetailByXdornumAndXrow(opdoDetail.getXdornum(), opdoDetail.getXrow());
		
		// calculate and update oporddetail xqtydel 
			if(existDetail != null) {  // for existing item
				BigDecimal diff1 = opdoDetail.getPrevqty().subtract(opdoDetail.getXqtyord());
				BigDecimal diff2 = oporddetail.getXqtydel().subtract(diff1);
				if(diff2.compareTo(oporddetail.getXqtyord()) == 1) {
					responseHelper.setErrorStatusAndMessage("Invoice quantity can't be greater then sales order quantity");
					return responseHelper.getResponse();
				}
				oporddetail.setXqtydel(diff2);
				long count2 = opordService.updateOpordDetail(oporddetail);
				if(count2 == 0) {
					responseHelper.setErrorStatusAndMessage("Can't update sales qty to Sales detail");
					return responseHelper.getResponse();
				}
			} 
//			else {  // for new item
//				existDetail.setXqtydel(existDetail.getXqtydel().add(opdoDetail.getXqtyord()));
//				if(existDetail.getXqtydel().compareTo(existDetail.getXqtyord()) == 1) {
//					responseHelper.setErrorStatusAndMessage("Invoice quantity can't be greater then sales order quantity");
//					return responseHelper.getResponse();
//				}
//			}
			
			
		
		if (existDetail != null) {
			BeanUtils.copyProperties(opdoDetail, existDetail, "xdornum", "xrow");
			long count = opdoService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			// now update sales order header status
			if(!updateSalesOrderStatus(oph)) {
				responseHelper.setErrorStatusAndMessage("Can't update sales order status");
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("opdodetailtable", "/salesninvoice/salesandinvoice/opdodetail/" + opdoDetail.getXdornum());
			responseHelper.setSecondReloadSectionIdWithUrl("opdoheaderform", "/salesninvoice/salesandinvoice/opdoheaderform/" + opdoDetail.getXdornum());
			responseHelper.setThirdReloadSectionIdWithUrl("opdoheadertable", "/salesninvoice/salesandinvoice/opdoheadertable");
			responseHelper.setSuccessStatusAndMessage("Invoice detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opdoService.saveDetail(opdoDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// now update sales order header status
		if(!updateSalesOrderStatus(oph)) {
			responseHelper.setErrorStatusAndMessage("Can't update sales order status");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("opdodetailtable", "/salesninvoice/salesandinvoice/opdodetail/" + opdoDetail.getXdornum());
		responseHelper.setSecondReloadSectionIdWithUrl("opdoheaderform", "/salesninvoice/salesandinvoice/opdoheaderform/" + opdoDetail.getXdornum());
		responseHelper.setThirdReloadSectionIdWithUrl("opdoheadertable", "/salesninvoice/salesandinvoice/opdoheadertable");
		responseHelper.setSuccessStatusAndMessage("Invoice detail saved successfully");
		return responseHelper.getResponse();
	}

	private boolean updateSalesOrderStatus(Opdoheader oph) {
		String status = "Full Received";
		List<Oporddetail> detailsList = opordService.findOporddetailByXordernum(oph.getXordernum());
		if(detailsList != null && !detailsList.isEmpty()) {
			for(Oporddetail pd : detailsList) {
				if(!pd.getXqtydel().equals(pd.getXqtyord())) status = "DO Created";
			}
		}
		Opordheader ph = opordService.findOpordHeaderByXordernum(oph.getXordernum());
		ph.setXstatusord(status);
		long phcount = opordService.updateOpordHeader(ph);
		if(phcount == 0) {
			return false;
		}
		return true;
	}

	@GetMapping("/opdodetail/{xdornum}")
	public String reloadOpdoDetailTable(@PathVariable String xdornum, Model model) {
		List<Opdodetail> invoiceDetails = opdoService.findOpdoDetailByXdornum(xdornum);
		model.addAttribute("opdoDetailsList", invoiceDetails);
		model.addAttribute("opdoheader", opdoService.findOpdoHeaderByXdornum(xdornum));
		
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(invoiceDetails != null && !invoiceDetails.isEmpty()) {
			for(Opdodetail pd : invoiceDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		/* if(isBoshila()) return "pages/land/salesninvoice/opdo::opdodetailtable"; */
		return "pages/salesninvoice/salesandinvoice/opdo::opdodetailtable";
	}

	@GetMapping("/opdoheaderform/{xdornum}")
	public String reloadOpdoheaderform(@PathVariable String xdornum, Model model) {
		model.addAttribute("opdoheader", opdoService.findOpdoHeaderByXdornum(xdornum));
		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));
		
		Opdoheader data = opdoService.findOpdoHeaderByXdornum(xdornum);
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		return "pages/salesninvoice/salesandinvoice/opdo::opdoheaderform";
	}

	@GetMapping("/opdoheadertable")
	public String reloadPogrnHeaderTable(Model model) {
		model.addAttribute("allOpdoHeader", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));
		return "pages/salesninvoice/salesandinvoice/opdo::opdoheadertable";
	}

	@PostMapping("{xdornum}/opdodetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpdoDetail(@PathVariable String xdornum, @PathVariable String xrow, Model model) {
		Opdodetail pd = opdoService.findOpdoDetailByXdornumAndXrow(xdornum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setErrorStatusAndMessage("Detail item can't found to do delete");
			return responseHelper.getResponse();
		}

		Opdoheader oph = opdoService.findOpdoHeaderByXdornum(pd.getXdornum());
		if(oph == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Invoice");
			return responseHelper.getResponse();
		}
		Oporddetail oporddetail = opordService.findOporddetailByXordernumAndXrow(oph.getXordernum(), pd.getXdorrow());
		if(oporddetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Sales order detail for this item row");
			return responseHelper.getResponse();
		}
		oporddetail.setXqtydel(oporddetail.getXqtydel().subtract(pd.getXqtyord()));
		long count2 = opordService.updateOpordDetail(oporddetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update invoice qty to sales detail");
			return responseHelper.getResponse();
		}

		// update 
		long ophcount = opdoService.updateOpdoHeaderTotalAmtAndGrandTotalAmt(oph.getXdornum());
		if(ophcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update sales total amount");
			return responseHelper.getResponse();
		}

		// now update sales order header status
		if(!updateSalesOrderStatus(oph)) {
			responseHelper.setErrorStatusAndMessage("Can't update sales order status");
			return responseHelper.getResponse();
		}

		long count = opdoService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete invoice detail");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("opdodetailtable", "/salesninvoice/salesandinvoice/opdodetail/" + xdornum);
		responseHelper.setSecondReloadSectionIdWithUrl("opdoheaderform", "/salesninvoice/salesandinvoice/opdoheaderform/" + xdornum);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirminvoice/{xdornum}")
	public @ResponseBody Map<String, Object> confirmOpdo(@PathVariable String xdornum) {
		Opdoheader opdoHeader = opdoService.findOpdoHeaderByXdornum(xdornum);
		if(opdoHeader == null) {
			responseHelper.setErrorStatusAndMessage("Invoice not found in this system");
			return responseHelper.getResponse();
		}
		
		// validate
		if(StringUtils.isBlank(opdoHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}
		if (!"Open".equalsIgnoreCase(opdoHeader.getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Invoice already confirmed");
			return responseHelper.getResponse();
		}

		List<Opdodetail> opdoDetailList = opdoService.findOpdoDetailByXdornum(xdornum);
		if(opdoDetailList == null || opdoDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Invoice has no item details");
			return responseHelper.getResponse();
		}

		// stock validation
		Map<String, BigDecimal> itemMap = new HashMap<>();
		for(Opdodetail d : opdoDetailList) {
			if(itemMap.get(d.getXitem() + '|' + opdoHeader.getXwh()) != null) {
				itemMap.put(d.getXitem() + '|' + opdoHeader.getXwh(), itemMap.get(d.getXitem() + '|' + opdoHeader.getXwh()).add(d.getXqtyord()));
			} else {
				itemMap.put(d.getXitem() + '|' + opdoHeader.getXwh(), d.getXqtyord());
			}
		}

		boolean hasError = false;
		StringBuilder ems = new StringBuilder("Stock Not available.");
		for(Map.Entry<String, BigDecimal> m : itemMap.entrySet()) {
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
		if(!"Confirmed".equalsIgnoreCase(opdoHeader.getXstatusord())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procConfirmDO(opdoHeader.getXdornum(), p_seq);
			//Error check here for procConfrimDo
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}

			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procIssuePricing(opdoHeader.getXdornum(), opdoHeader.getXwh(),"opdodetail", p_seq);
			//Error check here for procIssuePricing
			em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		if(!"Confirmed".equalsIgnoreCase(opdoHeader.getXstatusar())){
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procTransferOPtoAR(opdoHeader.getXdornum(), "opdoheader", p_seq);
			//Error check here for procTransferOPtoAR
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
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
		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + xdornum);
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

	@GetMapping("/print/{pType}/{xdornum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String pType,
			@PathVariable String xdornum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);

		if (oh == null) {
			message = "Invoice not found to print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Cacus cacus = cacusService.findByXcus(oh.getXcus());

		SalesOrderChalanReport report = new SalesOrderChalanReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Delivery Chalan");
		report.setFromDate(sdf.format(oh.getXdate()));
		report.setPrintDate(sdf.format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());

		SalesOrder salesOrder = new SalesOrder();
		salesOrder.setOrderNumber(oh.getXdornum());
		salesOrder.setReqBranch(oh.getXcus());
		salesOrder.setCustomer(cacus.getXorg());
		salesOrder.setCustomerAddress(cacus.getXmadd());
		salesOrder.setDate(sdf.format(oh.getXdate()));
		salesOrder.setSpellword2(oh.getSpellword2());
		salesOrder.setXprimeword2(oh.getXprimeword2());
		
		if ("invoice".equalsIgnoreCase(pType)) {
			report.setReportName("Invoice Order");

			salesOrder
					.setTotalAmount(oh.getXtotamt() != null ? oh.getXtotamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setVatAmount(oh.getXvatamt() != null ? oh.getXvatamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setDiscountAmount(
					oh.getXdiscamt() != null ? oh.getXdiscamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setGrandTotalAmount(
					oh.getXgrandtot() != null ? oh.getXgrandtot().toString() : BigDecimal.ZERO.toString());
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
					item.setItemTotalAmount(
							it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());
				}

				salesOrder.getItems().add(item);
			});
		}

		report.getSalesorders().add(salesOrder);
		
//		System.out.println(salesOrder.getTotalAmount());
//		
//		NumberToWordExample3 nn = new NumberToWordExample3();
//		nn.processor = new DefaultProcessor();
//		System.out.println(nn.processor.getName(salesOrder.getTotalAmount()));
//		System.out.println(nn.processor.getName("12345.12"));
		
		

		byte[] byt;
		if ("invoice".equalsIgnoreCase(pType))
			byt = getPDFByte(report, "invoicereport.xsl", request);
		else
			byt = getPDFByte(report, "deliveryorderreport.xsl", request);
		if (byt == null) {
			message = "Can't print report for chalan : " + xdornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
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
		report.setXnote(oh.getXnote());
		report.setXorg(cacus.getXorg());
		report.setXmadd(cacus.getXmadd());
		report.setXphone(cacus.getXphone());
		report.setXdate(sdf.format(oh.getXdate()));
		report.setXref(oh.getXref());
		report.setXpaymenttype(oh.getXpaymenttype());
		report.setXtotamt(oh.getXtotamt());
		report.setXvatamt(oh.getXvatamt());
		report.setXaitamt(oh.getXaitamt());
		report.setXdiscamt(oh.getXdiscamt());
		report.setXgrandtot((report.getXtotamt().add(report.getXaitamt() !=null ? report.getXaitamt() : 
		BigDecimal.ZERO ).add(report.getXvatamt() != null ? report.getXvatamt() : BigDecimal.ZERO)).subtract(report.getXdiscamt() !=null ? report.getXdiscamt() : BigDecimal.ZERO));
		//report.setXgrandtot(oh.getXgrandtot());
		report.setTotalQty(BigDecimal.ZERO);
		report.setSpellword(oh.getSpellword());
		report.setXprimeword(oh.getXprimeword());
		
		
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
				report.setXgrandtot((oh.getXtotamt().add(oh.getXaitamt() !=null ? oh.getXaitamt() : 
					BigDecimal.ZERO ).add(oh.getXvatamt() != null ? oh.getXvatamt() : BigDecimal.ZERO)).subtract(oh.getXdiscamt() !=null ? oh.getXdiscamt() : BigDecimal.ZERO));
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

	@GetMapping("/invoicedetails/{xdornum}")
	public String loadInvoiceDetailspage(@PathVariable String xdornum, Model model) {
		Opdoheader data = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (data == null) return "redirect:/salesninvoice/salesandinvoice";
		
		model.addAttribute("opdoheader", data);
		
		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		List<Opdodetail> mainitems = details.stream().filter(f -> !"Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList());
		List<Opdodetail> subitems = details.stream().filter(f -> "Set Item".equalsIgnoreCase(f.getXtype())).collect(Collectors.toList());
		for(Opdodetail m : mainitems) {
			for(Opdodetail s : subitems) {
				if(m.getXrow() == s.getXparentrow()) {
					m.getSubitems().add(s);
				}
			}
		}
		
		model.addAttribute("opdoDetailsList", mainitems);

		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(details != null && !details.isEmpty()) {
			for(Opdodetail pd : details) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);

		return "pages/salesninvoice/salesandinvoice/invoicedetails";
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
//	public @ResponseBody OpdoheaderPaging loadPoordListpageNew(PaginationDTO paginationDTO) {
//		OpdoheaderPaging paging = new OpdoheaderPaging();
//			
//		List<Opdoheader> listhHeaders = new ArrayList<Opdoheader>();
//		listhHeaders.addAll(opdoService.findByOpdoheaderDetails(paginationDTO.getLimit(), paginationDTO.getPage(),
//		paginationDTO.getHint(), paginationDTO.getOrderBy(),paginationDTO.getSortType()));
//			
//		paging.setOpdoheader(listhHeaders);
//		long count = opdoService.countOfOpdoheaderDetails(paginationDTO.getLimit(), paginationDTO.getPage(),
//		paginationDTO.getHint(), paginationDTO.getOrderBy());
//			
//		paging.setCount(count);
//			
//		return paging;
//		}
//		

}

	@Data
	class OpdoheaderPaging{
	private List<Opdoheader> opdoheader;
	private long count;
}

