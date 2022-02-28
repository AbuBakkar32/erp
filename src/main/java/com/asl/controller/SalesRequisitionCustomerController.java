package com.asl.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.entity.DataList;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opreqheader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesRequsitionReport;
import com.asl.service.CaitemService;
import com.asl.service.OpreqService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/salesninvoice/opreqcus")
public class SalesRequisitionCustomerController extends ASLAbstractController {

	@Autowired private OpreqService opreqService;
	@Autowired private CaitemService caitemService;


	@GetMapping
	public String loadSalesOrdercusPage(@RequestParam(required = false) String reqtype, Model model) {

		Opreqheader opreqheader = new Opreqheader();
		opreqheader.setXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode());
		opreqheader.setXtrn(TransactionCodeType.SALES_REQUESTION_ORDER.getdefaultCode());
		opreqheader.setXstatus("Open");
		opreqheader.setXamount(BigDecimal.ZERO);
		opreqheader.setXcus(sessionManager.getLoggedInUserDetails().getXcus());
		opreqheader.setXorg(sessionManager.getLoggedInUserDetails().getPartyname());

		model.addAttribute("opreqheader", opreqheader);
		model.addAttribute("cusID", sessionManager.getLoggedInUserDetails().getXcus());
		model.addAttribute("allOpreqHeader", opreqService.getAllOpreqCusheader());
		model.addAttribute("allOpenOpreqHeader", opreqService.getAllStatusOpenOpreqCusheader());
		model.addAttribute("opreqprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode(), Boolean.TRUE));

		model.addAttribute("reqtype", reqtype);
		List<Caitem> items = caitemService.getAllRequisitionItems();
		items.stream().forEach(i -> {
			if(i.getXdealerp() == null) i.setXdealerp(BigDecimal.ZERO);
		});
		items = items.stream().filter(f -> !"Assets".equalsIgnoreCase(f.getXcatitem())).collect(Collectors.toList());
		if("Packaging Item".equalsIgnoreCase(reqtype)) {
			model.addAttribute("items",items.stream().filter(i -> "Packaging Item".equalsIgnoreCase(i.getXgitem()) && !"Accessories".equalsIgnoreCase(i.getXcatitem())).collect(Collectors.toList()));
		} else if("Accessories".equalsIgnoreCase(reqtype)){
			model.addAttribute("items",items.stream().filter(i -> "Accessories".equalsIgnoreCase(i.getXcatitem()) && !"Packaging Item".equalsIgnoreCase(i.getXgitem())).collect(Collectors.toList()));
		} else if("Raw Materials".equalsIgnoreCase(reqtype)) {
			model.addAttribute("items",items.stream().filter(i -> !"Accessories".equalsIgnoreCase(i.getXcatitem()) && !"Packaging Item".equalsIgnoreCase(i.getXgitem())).collect(Collectors.toList()));
		} else {
			model.addAttribute("items", items);
		}

		List<DataList> dl = listService.getList("SYSTEM", "REQUISITION_CUS_PAGE");
		if(dl != null && !dl.isEmpty()) {
			DataList d = dl.get(0);
			if(StringUtils.isNotBlank(d.getListvalue2())) {
				return "pages/salesninvoice/reqorder/" + d.getListvalue2();
			}
		}

		return "pages/salesninvoice/reqorder/requisition";
	}

	@GetMapping("/{xdoreqnum}")
	public String loadSalesOrdercusPage(@RequestParam(required = false) String reqtype, @PathVariable String xdoreqnum, Model model) {
		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if (data == null) return "redirect:/salesninvoice/opreqcus?reqtype=" + reqtype;

		if(!data.getXcus().equalsIgnoreCase(sessionManager.getLoggedInUserDetails().getXcus())) {
			return "redirect:/salesninvoice/opreqcus?reqtype=" + reqtype;
		}

		model.addAttribute("reqtype", reqtype);
		List<Caitem> caitems = caitemService.getAllRequisitionItems();
		caitems.stream().forEach(i -> {
			if(i.getXdealerp() == null) i.setXdealerp(BigDecimal.ZERO);
			i.setLineamt(BigDecimal.ZERO);
		});
		caitems = caitems.stream().filter(f -> !"Assets".equalsIgnoreCase(f.getXcatitem())).collect(Collectors.toList());
		List<Opreqdetail> details = opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		if(details != null && !details.isEmpty()) {
			for(Caitem c : caitems) {
				for(Opreqdetail d : details) {
					if(d.getXitem().equalsIgnoreCase(c.getXitem())) {
						c.setXqtyord(d.getXqtyord());
						c.setLineamt(d.getXlineamt() == null ? BigDecimal.ZERO : d.getXlineamt());
					}
				}
			}
		}
		if("Packaging Item".equalsIgnoreCase(reqtype)) {
			model.addAttribute("items",caitems.stream().filter(i -> "Packaging Item".equalsIgnoreCase(i.getXgitem()) && !"Accessories".equalsIgnoreCase(i.getXcatitem())).collect(Collectors.toList()));
		} else if("Accessories".equalsIgnoreCase(reqtype)){
			model.addAttribute("items",caitems.stream().filter(i -> "Accessories".equalsIgnoreCase(i.getXcatitem()) && !"Packaging Item".equalsIgnoreCase(i.getXgitem())).collect(Collectors.toList()));
		} else if("Raw Materials".equalsIgnoreCase(reqtype)) {
			model.addAttribute("items",caitems.stream().filter(i -> !"Accessories".equalsIgnoreCase(i.getXcatitem()) && !"Packaging Item".equalsIgnoreCase(i.getXgitem())).collect(Collectors.toList()));
		} else {
			model.addAttribute("items", caitems);
		}

		model.addAttribute("opreqheader", data);
		model.addAttribute("allOpreqHeader", opreqService.getAllOpreqCusheader());
		model.addAttribute("allOpenOpreqHeader", opreqService.getAllStatusOpenOpreqCusheader());
		model.addAttribute("opreqDetailsList", opreqService.findOpreqDetailByXdoreqnum(xdoreqnum));

		List<DataList> dl = listService.getList("SYSTEM", "REQUISITION_CUS_PAGE");
		if(dl != null && !dl.isEmpty()) {
			DataList d = dl.get(0);
			if(StringUtils.isNotBlank(d.getListvalue2())) {
				return "pages/salesninvoice/reqorder/" + d.getListvalue2();
			}
		}

		return "pages/salesninvoice/reqorder/requisition";
	}

	@GetMapping("/view/{xdoreqnum}")
	public String loadSalesOrdercusViewPage(@RequestParam(required = false) String reqtype, @PathVariable String xdoreqnum, Model model) {
		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if (data == null) return "redirect:/salesninvoice/opreqcus?reqtype=" + reqtype;

		if(!data.getXcus().equalsIgnoreCase(sessionManager.getLoggedInUserDetails().getXcus())) {
			return "redirect:/salesninvoice/opreqcus?reqtype=" + reqtype;
		}

		model.addAttribute("reqtype", reqtype);
		List<Caitem> caitems = caitemService.getAllRequisitionItems();
		caitems.stream().forEach(i -> {
			if(i.getXdealerp() == null) i.setXdealerp(BigDecimal.ZERO);
			i.setLineamt(BigDecimal.ZERO);
		});
		caitems = caitems.stream().filter(f -> !"Assets".equalsIgnoreCase(f.getXcatitem())).collect(Collectors.toList());
		List<Opreqdetail> details = opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		if(details != null && !details.isEmpty()) {
			for(Caitem c : caitems) {
				for(Opreqdetail d : details) {
					if(d.getXitem().equalsIgnoreCase(c.getXitem())) {
						c.setXqtyord(d.getXqtyord());
						c.setLineamt(d.getXlineamt() == null ? BigDecimal.ZERO : d.getXlineamt());
						c.setRequested(true);
					}
				}
			}
		}
		
		caitems = caitems.stream().filter(f -> f.isRequested()).collect(Collectors.toList());
		
		if("Packaging Item".equalsIgnoreCase(reqtype)) {
			model.addAttribute("items",caitems.stream().filter(i -> "Packaging Item".equalsIgnoreCase(i.getXgitem()) && !"Accessories".equalsIgnoreCase(i.getXcatitem())).collect(Collectors.toList()));
		} else if("Accessories".equalsIgnoreCase(reqtype)){
			model.addAttribute("items",caitems.stream().filter(i -> "Accessories".equalsIgnoreCase(i.getXcatitem()) && !"Packaging Item".equalsIgnoreCase(i.getXgitem())).collect(Collectors.toList()));
		} else if("Raw Materials".equalsIgnoreCase(reqtype)) {
			model.addAttribute("items",caitems.stream().filter(i -> !"Accessories".equalsIgnoreCase(i.getXcatitem()) && !"Packaging Item".equalsIgnoreCase(i.getXgitem())).collect(Collectors.toList()));
		} else {
			model.addAttribute("items", caitems);
		}

		model.addAttribute("opreqheader", data);
		model.addAttribute("allOpreqHeader", opreqService.getAllOpreqCusheader());
		model.addAttribute("allOpenOpreqHeader", opreqService.getAllStatusOpenOpreqCusheader());
		model.addAttribute("opreqDetailsList", opreqService.findOpreqDetailByXdoreqnum(xdoreqnum));

		return "pages/salesninvoice/reqorder/requisitionview";
	}

	@PostMapping(value = "/save" , headers="Accept=application/json")
	public @ResponseBody Map<String, Object> save(@RequestBody String json) throws UnsupportedEncodingException{

		Opreqheader opreqHeader = new Opreqheader();
		Collection<Opreqdetail> itemdetails = null;
		ObjectMapper obm = new ObjectMapper();
		obm.setDateFormat(SDF2);
		try {
			opreqHeader = obm.readValue(json, Opreqheader.class);
			JsonNode rootNode = obm.readTree(json);
			JsonNode itemsNode = rootNode.get("items");
			TypeFactory typeFactory = obm.getTypeFactory();
			CollectionType cType = typeFactory.constructCollectionType(List.class, Opreqdetail.class);
			itemdetails = obm.readValue(itemsNode.toString(), cType);
		} catch (JsonProcessingException e) {
			log.error(ERROR, e.getMessage(), e); 
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		if (opreqHeader == null || StringUtils.isBlank(opreqHeader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		opreqHeader.setXwh("01");
		opreqHeader.setXcus(sessionManager.getLoggedInUserDetails().getXcus());
		opreqHeader.setXorg(sessionManager.getLoggedInUserDetails().getPartyname());

		if (StringUtils.isBlank(opreqHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please Add a customer to create Requisition Order");
			return responseHelper.getResponse();
		}

		// Check if existing record have then update data
		List<Opreqdetail> details = itemdetails.stream().filter(i -> i.getXqtyord() != null && !i.getXqtyord().equals(BigDecimal.ZERO)).collect(Collectors.toList());
		Opreqheader existOpreqHeader = opreqService.findOpreqHeaderByXdoreqnum(opreqHeader.getXdoreqnum());

		if (existOpreqHeader != null) {
			BeanUtils.copyProperties(opreqHeader, existOpreqHeader, "xtypetrn", "xtrn");
			long count = opreqService.updateOpreqheader(existOpreqHeader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			if(!opreqService.findOpreqDetailByXdoreqnum(opreqHeader.getXdoreqnum()).isEmpty()) {
				long countData = opreqService.deleteOpreqdetailData(opreqHeader.getXdoreqnum());
				if(countData == 0) {
					responseHelper.setErrorStatusAndMessage("Can't update requisition details");
					return responseHelper.getResponse();
				}
			}

			int j = 1;
			for(Opreqdetail d : details) {
				Caitem c = caitemService.findByXitem(d.getXitem());
				if(c == null) continue;
				d.setXrow(j);
				d.setXdoreqnum(opreqHeader.getXdoreqnum());
				if(d.getXrate() == null) d.setXrate(BigDecimal.ZERO);
				d.setXlineamt((d.getXqtyord().multiply(d.getXrate())).setScale(BigDecimal.ROUND_DOWN));
				j++;
			}

			long countdata = 0;
			try {
				countdata = opreqService.saveDetail(details);
			} catch (ServiceException e) {
				log.error(ERROR, e.getMessage(), e);
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}
			if(countdata == 0) {
				responseHelper.setErrorStatusAndMessage("Can't Add item details");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Requisition Order updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/opreqcus/view/" + opreqHeader.getXdoreqnum() + "?reqtype=" + opreqHeader.getXreqitemtype());
			return responseHelper.getResponse();
		}

		// This Section Only for Details Table
		List<Opreqdetail> detailData = itemdetails.stream().filter(i -> i.getXqtyord() != null && !i.getXqtyord().equals(BigDecimal.ZERO)).collect(Collectors.toList());
		if(detailData == null || detailData.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add quantity atleast one product");
			return responseHelper.getResponse();
		}else {
			long count = opreqService.saveOpreqheader(opreqHeader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			int i = 1;
			for(Opreqdetail d : detailData) {
				Caitem c = caitemService.findByXitem(d.getXitem());
				if(c == null) continue;
				d.setXrow(i);
				d.setXdoreqnum(opreqHeader.getXdoreqnum());
				if(d.getXrate() == null) d.setXrate(BigDecimal.ZERO);
				d.setXlineamt((d.getXqtyord().multiply(d.getXrate())).setScale(BigDecimal.ROUND_DOWN));
				i++;
			}

			long countd = 0;
			try {
				countd = opreqService.saveDetail(detailData);
			} catch (ServiceException e) {
				log.error(ERROR, e.getMessage(), e);
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}
			if(countd == 0) {
				responseHelper.setErrorStatusAndMessage("Can't Add item details");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Requisition Order Created Successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opreqcus/view/" + opreqHeader.getXdoreqnum() + "?reqtype=" + opreqHeader.getXreqitemtype());
		return responseHelper.getResponse();
	}
	

	@PostMapping("/archive/{xdoreqnum}")
	public @ResponseBody Map<String, Object> archive(@RequestParam(required = false) String reqtype, @PathVariable String xdoreqnum){
		Opreqheader opreqheader = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if(opreqheader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find requisition : " + xdoreqnum);
			return responseHelper.getResponse();
		}

		long count = opreqService.deleteOpreqheader(xdoreqnum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't find requisition : " + xdoreqnum);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Requisition Deleted Successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opreqcus?reqtype=" + reqtype);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm/{xdoreqnum}")
	public @ResponseBody Map<String, Object> confirm(@RequestParam(required = false) String reqtype, @PathVariable String xdoreqnum){

		Opreqheader oh = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if(oh == null) {
			responseHelper.setErrorStatusAndMessage("Requisition Order " + xdoreqnum + " not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(oh.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Requisition Order " + xdoreqnum + " is not Open");
			return responseHelper.getResponse();
		}

		// check sales order has details
		List<Opreqdetail> details = opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Requisition Order details is empty");
			return responseHelper.getResponse();
		}

		oh.setXstatus("Confirmed");
		oh.setXstatusreq("Open");
		long count = opreqService.updateOpreqheader(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Confirmed Requisition Order " + xdoreqnum);
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/opreqcus/view/" + xdoreqnum + "?reqtype=" + reqtype);
		responseHelper.setSuccessStatusAndMessage("Requisition Order Confirmed Successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{xdoreqnum}")
	public ResponseEntity<byte[]> printSalesReqWithDetails(@PathVariable String xdoreqnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		SalesRequsitionReport report=new SalesRequsitionReport();

		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Requisition");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());

		report.setXdoreqnum(data.getXdoreqnum());
		report.setXdate(sdf.format(data.getXdate()));
		report.setXcus(data.getXcus());
		report.setXmadd(data.getCustomeraddress());
		report.setXorg(data.getXorg());
		report.setXstatus(data.getXstatus());
		report.setXamount(data.getXamount().setScale(2, RoundingMode.DOWN));
		report.setSpellword(data.getSpellword());
		report.setXprimeword(data.getXprimeword());

		List<Opreqdetail> items = opreqService.findOpreqDetailByXdoreqnum(data.getXdoreqnum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXdesc());
				item.setItemQty(it.getXqtyord().setScale(2, RoundingMode.DOWN).toString());
				item.setRate(it.getXrate().setScale(2, RoundingMode.DOWN));
				item.setItemUnit(it.getXunit());
				item.setLineamt(it.getXlineamt().setScale(2, RoundingMode.DOWN));

				report.getItems().add(item);
			});
		}

		byte[] byt = getPDFByte(report, "salesreqreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for sales requisition (user) : " + xdoreqnum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}


	@GetMapping("/requisitionhistory")
	public String loadAllRequisitions(Model model) {
		model.addAttribute("allOpreqHeader", opreqService.getAllOpreqCusheader());
		return "pages/salesninvoice/reqorder/allrequisitions";
	}
	
}
