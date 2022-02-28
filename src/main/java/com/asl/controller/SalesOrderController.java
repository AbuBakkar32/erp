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
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.ProjectStoreView;
import com.asl.entity.Vatait;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesOrderReport;
import com.asl.service.CaitemService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.VataitService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/salesninvoice/opord")
public class SalesOrderController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private CaitemService caitemService;
	@Autowired private VataitService vataitService;
	@Autowired private OpdoService opdoService;
	@Autowired private ProjectStoreViewService projectstoreviewService;

	@GetMapping
	public String loadSalesOrderPage(Model model) {

		model.addAttribute("opordheader", getDefaultOpordHeader());
		model.addAttribute("opordprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_ORDER.getCode(), Boolean.TRUE));
		//model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("allcodes", Collections.emptyList());

//	List<DataList> dl = listService.getList("SYSTEM", "SALES_ORDER_PAGE");
//	if(dl != null && !dl.isEmpty()) {
//		DataList d = dl.get(0);
//		if(StringUtils.isNotBlank(d.getListvalue2())) {
//			return "pages/salesninvoice/salesorder/" + d.getListvalue2();
//		}
//	}

		return "pages/salesninvoice/salesorder/salesorder";
	}
	
	@GetMapping("/salesorderlist")
	public String loadPoordListPage(Model model) {
		model.addAttribute("allOpordHeader", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode()));
		return "pages/salesninvoice/salesorder/salesorderlist";
	}

	@GetMapping("/{xordernum}")
	public String loadSalesOrderPage(@PathVariable String xordernum, Model model) {
		Opordheader data = opordService.findOpordHeaderByXordernum(xordernum);
		if (data == null) return "redirect:/salesninvoice/opord";

		model.addAttribute("opordheader", data);
		//model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("invoicelist", opdoService.findOpdoheaderByXordernum(xordernum));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

		List<Oporddetail> opordDetails = opordService.findOporddetailByXordernum(xordernum);
		model.addAttribute("opordDetailsList", opordDetails);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(opordDetails != null && !opordDetails.isEmpty()) {
			for(Oporddetail pd : opordDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);

//		List<DataList> dl = listService.getList("SYSTEM", "SALES_ORDER_PAGE");
//		if(dl != null && !dl.isEmpty()) {
//			DataList d = dl.get(0);
//			if(StringUtils.isNotBlank(d.getListvalue2())) {
//				return "pages/salesninvoice/salesorder/" + d.getListvalue2();
//			}
//		}
		
		return "pages/salesninvoice/salesorder/salesorder";
	}

	private Opordheader getDefaultOpordHeader() {
		Opordheader opordheader = new Opordheader();
		opordheader.setXtypetrn(TransactionCodeType.SALES_ORDER.getCode());
		opordheader.setXtrn(TransactionCodeType.SALES_ORDER.getdefaultCode());
		opordheader.setXstatus("Open");
		opordheader.setXstatusord("Open");
		opordheader.setXtype("SO");
		opordheader.setXtotamt(BigDecimal.ZERO);
		opordheader.setXgrandtot(BigDecimal.ZERO);
		opordheader.setXvatait("No Vat");
		opordheader.setXvatamt(BigDecimal.ZERO);
		opordheader.setXaitamt(BigDecimal.ZERO);
		opordheader.setXdiscamt(BigDecimal.ZERO);

		return opordheader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opordheader opordHeader, BindingResult bindingResult) {
		if (opordHeader == null || StringUtils.isBlank(opordHeader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if (StringUtils.isBlank(opordHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please select a customer.");
			return responseHelper.getResponse();
		}
		
		if (StringUtils.isBlank(opordHeader.getXhwh())) {
			responseHelper.setErrorStatusAndMessage("Project/Business required.");
			return responseHelper.getResponse();
		}
		
		if (StringUtils.isBlank(opordHeader.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Store/Site required.");
			return responseHelper.getResponse();
		}

		Vatait vatait = vataitService.findVataitByXvatait(opordHeader.getXvatait());
		if(opordHeader.getXtotamt() == null) opordHeader.setXtotamt(BigDecimal.ZERO);
		if(StringUtils.isNotBlank(opordHeader.getXvatait()) && !"No Vat".equalsIgnoreCase(opordHeader.getXvatait()) && vatait != null) {
			if(opordHeader.getXvatamt() == null) opordHeader.setXvatamt((opordHeader.getXtotamt().multiply(vatait.getXvat())).divide(BigDecimal.valueOf(100)));
			if(opordHeader.getXaitamt() == null) opordHeader.setXaitamt((opordHeader.getXtotamt().multiply(vatait.getXait())).divide(BigDecimal.valueOf(100)));
		} else {
			if(opordHeader.getXvatamt() == null) opordHeader.setXvatamt(BigDecimal.ZERO);
			if(opordHeader.getXaitamt() == null) opordHeader.setXaitamt(BigDecimal.ZERO);
		}
		if(opordHeader.getXdiscamt() == null) opordHeader.setXdiscamt(BigDecimal.ZERO);
		BigDecimal grandTotal = (opordHeader.getXtotamt().add(opordHeader.getXvatamt()).add(opordHeader.getXaitamt())).subtract(opordHeader.getXdiscamt());
		opordHeader.setXgrandtot(grandTotal);

		// if existing record
		Opordheader existOpordHeader = opordService.findOpordHeaderByXordernum(opordHeader.getXordernum());
		if (existOpordHeader != null) {
			BeanUtils.copyProperties(opordHeader, existOpordHeader, "xordernum", "xtypetrn", "xtrn","xpornum","xstatus");
			long count = opordService.updateOpordHeader(existOpordHeader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Sales Order updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/opord/" + opordHeader.getXordernum());
			return responseHelper.getResponse();
		}

		// If new
		long count = opordService.saveOpordHeader(opordHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Sales Order created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opord/" + opordHeader.getXordernum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xordernum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xordernum){
		return doArchiveOrRestore(xordernum, true);
	}

	public Map<String, Object> doArchiveOrRestore(String xordernum, boolean archive){
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(xordernum);
		if(opordHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// archive all details first
		List<Oporddetail> details = opordService.findOporddetailByXordernum(xordernum);
		if(details != null && !details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Archive order details first");
			return responseHelper.getResponse();
		}

		// update opordheader zactive
		long count = opordService.deleteOpordHeader(xordernum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete Sales Order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Salees order deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opord");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm/{xordernum}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xordernum){

		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			responseHelper.setErrorStatusAndMessage("Sales order " + xordernum + " not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(oh.getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Sales order " + xordernum + " is not Open");
			return responseHelper.getResponse();
		}

		// check sales order has details
		List<Oporddetail> details = opordService.findOporddetailByXordernum(xordernum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Sales order details is empty");
			return responseHelper.getResponse();
		}

		oh.setXstatusord("Confirmed");
		long count = opordService.updateOpordHeader(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Confirmed sales order " + xordernum);
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/opord/" + xordernum);
		responseHelper.setSuccessStatusAndMessage("Order Confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/{xordernum}/oporddetail/{xrow}/show")
	public String openOpordDetailModal(@PathVariable String xordernum, @PathVariable String xrow, Model model) {

		if ("new".equalsIgnoreCase(xrow)) {
			Oporddetail oporddetail = new Oporddetail();
			oporddetail.setXordernum(xordernum);
			oporddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			oporddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			oporddetail.setXqtydel(BigDecimal.ZERO);
			model.addAttribute("oporddetail", oporddetail);
		} else {
			Oporddetail oporddetail = opordService.findOporddetailByXordernumAndXrow(xordernum, Integer.parseInt(xrow));
			if (oporddetail == null) {
				oporddetail = new Oporddetail();
				oporddetail.setXordernum(xordernum);
				oporddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				oporddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				oporddetail.setXqtydel(BigDecimal.ZERO);
			}
			model.addAttribute("oporddetail", oporddetail);
		}
		return "pages/salesninvoice/salesorder/oporddetailmodal::oporddetailmodal";
	}

	@PostMapping("/oporddetail/save")
	public @ResponseBody Map<String, Object> saveOporddetail(Oporddetail opordDetail) {
		if (opordDetail == null || StringUtils.isBlank(opordDetail.getXordernum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(opordDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item not selected! Please select an item");
			return responseHelper.getResponse();
		}

		Caitem caitem = caitemService.findByXitem(opordDetail.getXitem());
		if(caitem == null) {
			responseHelper.setErrorStatusAndMessage("Item not found in the system");
			return responseHelper.getResponse();
		}

		if(opordDetail.getXqtyord() == null || opordDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Item quantity can't be less then zero");
			return responseHelper.getResponse();
		}

		// modify line amount
		opordDetail.setXdesc(caitem.getXdesc());
		opordDetail.setXcatitem(caitem.getXcatitem());
		opordDetail.setXgitem(caitem.getXgitem());
		opordDetail.setXlineamt(opordDetail.getXqtyord().multiply(opordDetail.getXrate()).setScale(2, RoundingMode.DOWN));
		
		//opordDetail.setXlineamt(opordDetail.getXlineamt().add(opordDetail.getXlineamt().multiply(caitem.getXvatrate() == null ? BigDecimal.ZERO : caitem.getXvatrate()).divide(BigDecimal.valueOf(100))));

		// if existing
		Oporddetail existDetail = opordService.findOporddetailByXordernumAndXrow(opordDetail.getXordernum(), opordDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(opordDetail, existDetail, "xordernum", "xrow");
			long count = opordService.updateOpordDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/salesninvoice/opord/oporddetail/" + opordDetail.getXordernum());
			responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/salesninvoice/opord/opordheaderform/" + opordDetail.getXordernum());
			responseHelper.setThirdReloadSectionIdWithUrl("opordheadertable", "/salesninvoice/salesandinvoice/opordheadertable");
			responseHelper.setSuccessStatusAndMessage("Sales Order item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opordService.saveOpordDetail(opordDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/salesninvoice/opord/oporddetail/" + opordDetail.getXordernum());
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/salesninvoice/opord/opordheaderform/" + opordDetail.getXordernum());
		responseHelper.setThirdReloadSectionIdWithUrl("opordheadertable", "/salesninvoice/salesandinvoice/opordheadertable");
		responseHelper.setSuccessStatusAndMessage("Sales Order item saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/oporddetail/{xordernum}")
	public String reloadOpordDetailTable(@PathVariable String xordernum, Model model) {
		List<Oporddetail> opordDetails = opordService.findOporddetailByXordernum(xordernum);
		model.addAttribute("opordDetailsList", opordDetails);
		model.addAttribute("opordheader", opordService.findOpordHeaderByXordernum(xordernum));
		
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(opordDetails != null && !opordDetails.isEmpty()) {
			for(Oporddetail pd : opordDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		return "pages/salesninvoice/salesorder/salesorder::oporddetailtable";
	}

	@GetMapping("/opordheaderform/{xordernum}")
	public String reloadOpdoheaderform(@PathVariable String xordernum, Model model) {
		Opordheader data = opordService.findOpordHeaderByXordernum(xordernum);
		if (data == null) return "redirect:/salesninvoice/opord";

		model.addAttribute("opordheader", data);
		model.addAttribute("opordprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		//model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(data.getXhwh());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

		return "pages/salesninvoice/salesorder/salesorder::opordheaderform";
	}
	
	@GetMapping("/opordheadertable")
	public String reloadOpordHeaderTable(Model model) {
		model.addAttribute("allOpordHeader", opordService.findAllOpordHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode()));
		return "pages/salesninvoice/salesandinvoice/salesorder::opordheadertable";
	}

	@PostMapping("{xordernum}/oporddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpdoDetail(@PathVariable String xordernum, @PathVariable String xrow, Model model) {
		Oporddetail pd = opordService.findOporddetailByXordernumAndXrow(xordernum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setErrorStatusAndMessage("Detail item can't found to do delete");
			return responseHelper.getResponse();
		}

		long count = opordService.deleteOpordDetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("oporddetailtable", "/salesninvoice/opord/oporddetail/" + xordernum);
		responseHelper.setSecondReloadSectionIdWithUrl("opordheaderform", "/salesninvoice/opord/opordheaderform/" + xordernum);
		responseHelper.setThirdReloadSectionIdWithUrl("opordheadertable", "/salesninvoice/salesandinvoice/opordheadertable");
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
	
	@PostMapping("/confirmorder/{xordernum}")
	public @ResponseBody Map<String, Object> confirmorder(@PathVariable String xordernum){
		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			responseHelper.setErrorStatusAndMessage("Sales order " + xordernum + " not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(oh.getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Sales order " + xordernum + " is not Open");
			return responseHelper.getResponse();
		}

		// check sales order has details
		List<Oporddetail> details = opordService.findOporddetailByXordernum(xordernum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Sales order details is empty");
			return responseHelper.getResponse();
		}

		oh.setXstatusord("Confirmed");
		long count = opordService.updateOpordHeader(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Confirmed sales order " + xordernum);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Sales Order Confirmed successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opord/" + xordernum);
		return responseHelper.getResponse();
	}

	@PostMapping("/createinvoice/{xordernum}")
	public @ResponseBody Map<String, Object> createInvoice(@PathVariable String xordernum){

		try {
			return opordService.createSalesOrderToInvoice(xordernum, responseHelper);
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

	}

	@GetMapping("/print/{xordernum}")
	public ResponseEntity<byte[]> printSalseOrderWithDetails(@PathVariable String xordernum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Opordheader data = opordService.findOpordHeaderByXordernum(xordernum);
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
	
		SalesOrderReport report = new SalesOrderReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Sales Order");
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());

		report.setXordernum(data.getXordernum());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXcus(data.getXcus());
		report.setXorg(data.getXorg());
		report.setXhwh(data.getXhwh());
		report.setProjectName(data.getProjectName());
		report.setXwh(data.getXwh());
		report.setStoreName(data.getStoreName());
		report.setXref(data.getXref());
		report.setXstatusord(data.getXstatusord());
		report.setXtotamt(data.getXtotamt());
		report.setXnote(data.getXnote());
		report.setSpellword(data.getSpellword());
		report.setXprimeword(data.getXprimeword());
		
		List<Oporddetail> items = opordService.findOporddetailByXordernum(data.getXordernum());	
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getItemname());
				item.setItemQty(it.getXqtyord().toString());
				item.setRate(it.getXrate());
				item.setItemUnit(it.getXunit());
				item.setXunitsel(it.getXunitsel());
				item.setXcfsel(it.getXcfsel());
				item.setXlong(it.getXlong());
				item.setXqtydel(it.getXqtydel());
				item.setLineamt(it.getXlineamt());

				report.getItems().add(item);
			});
		}
		
		
		byte[] byt = getPDFByte(report, "salesorder.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for voucher : " + xordernum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	@GetMapping("/salesorderdetails/{xordernum}")
	public String loadSalesOrderDetails(@PathVariable String xordernum, Model model) {
		Opordheader data = opordService.findOpordHeaderByXordernum(xordernum);
		if (data == null) return "redirect:/salesninvoice/opord";

		model.addAttribute("opordheader", data);

		List<Oporddetail> opordDetails = opordService.findOporddetailByXordernum(xordernum);
		model.addAttribute("opordDetailsList", opordDetails);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(opordDetails != null && !opordDetails.isEmpty()) {
			for(Oporddetail pd : opordDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);

		return "pages/salesninvoice/salesorder/salesorderdetails";
	}
	
	@GetMapping("/allcodesbyproject/{xhwh}")
	public @ResponseBody List<ProjectStoreView> getProjectstoreview(@PathVariable String xhwh){
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXtype(xhwh);
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		return list;
	}
	
//	//server side pagenation
//	@PostMapping("/data")
//	public @ResponseBody OpordheaderPaging loadOpordListpageNew(PaginationDTO paginationDTO) {
//		OpordheaderPaging paging = new OpordheaderPaging();
//		
//		List<Opordheader> listhHeaders = new ArrayList<Opordheader>();
//		listhHeaders.addAll(opordService.findByOpordheaderNewMethod(paginationDTO.getLimit(), paginationDTO.getPage(),
//				paginationDTO.getHint(), paginationDTO.getOrderBy(),paginationDTO.getSortType()));
//		
//		paging.setOpordheader(listhHeaders);
//		long count = opordService.countOfOpordheaderDetails(paginationDTO.getLimit(), paginationDTO.getPage(),
//				paginationDTO.getHint(), paginationDTO.getOrderBy());
//		
//		paging.setCount(count);
//		
//		return paging;
//	}
//	

}

@Data
class OpordheaderPaging{
	private List<Opordheader> opordheader;
	private long count;
}
