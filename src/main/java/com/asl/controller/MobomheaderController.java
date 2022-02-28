package com.asl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Acdetail;
import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Mobomdetail;
import com.asl.entity.Mobomheader;
import com.asl.entity.Pdloantrn;
import com.asl.entity.Pdloanwriteoff;
import com.asl.entity.Pdmst;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.PdloantrnReport;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.MobomheaderService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/production/mobomheader")
public class MobomheaderController extends ASLAbstractController{

	@Autowired private MobomheaderService mobomheaderService;
	@Autowired private CaitemService caitemService;
	@Autowired private CacusService cacusService;

	@GetMapping
	public String loadAccountVoucherPage(Model model) {
		model.addAttribute("mobomheader", getDefaultMobomheader());
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.BOM_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("mobomheaders", mobomheaderService.getAllMobomheader());
		model.addAttribute("caitemList", caitemService.getAllCaitems());
		model.addAttribute("customers", cacusService.getAllCacus("Customer"));

		return"pages/production/mobomheader/mobomheader";
	}

	private Mobomheader getDefaultMobomheader() {
		Mobomheader mobomheader = new Mobomheader();
		mobomheader.setXtrn(TransactionCodeType.BOM_NUMBER.getdefaultCode());
		mobomheader.setXdate(new Date());
		return mobomheader;
	}
	
	@GetMapping("/{xbomkey}")
	public String loadAccountVoucherPage(@PathVariable String xbomkey, Model model) {
		Mobomheader mobomheader = mobomheaderService.findMobomheaderByXbomkey(xbomkey);
		if(mobomheader == null) return "rdirect:/production/mobomheader";

		model.addAttribute("mobomheader", mobomheader);
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.BOM_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("mobomheaders", mobomheaderService.getAllMobomheader());


		List<Mobomdetail> mobomdetails = mobomheaderService.findMobomdetailsByXbomkey(xbomkey);
		model.addAttribute("mobomdetails", mobomdetails);
		
		List<Caitem> caitemList = caitemService.getAllCaitems();
		model.addAttribute("caitemList", caitemList);
		
		List<Cacus> customers = cacusService.getAllCacus("Customer");
		model.addAttribute("customers", customers);
		
		return"pages/production/mobomheader/mobomheader";
	}
	
	@GetMapping("/preloadeddata")
	public @ResponseBody PreData getItemList(){
		PreData preData = new PreData();

		List<Caitem> caitemList = caitemService.getAllCaitems();
		if(caitemList != null && !caitemList.isEmpty()) {
			caitemList.sort(Comparator.comparing(Caitem::getXitem));
			preData.setCaitemList(caitemList);
		}
	
		List<Cacus> customers = cacusService.getAllCacus("Customer");
		if(customers != null && !customers.isEmpty()) {
			customers.sort(Comparator.comparing(Cacus::getXcus));
			preData.setCustomers(customers);
		}

		return preData;
	}

	@Data
	class PreData {
		List<Caitem> caitemList = new ArrayList<>();
		List<Cacus> customers = new ArrayList<>();
	}
	
	

	@PostMapping(value = "/savebom" , headers="Accept=application/json")
	public @ResponseBody Map<String, Object> save(@RequestBody String json){

		BOMMaster vm = new BOMMaster();

		ObjectMapper obm = new ObjectMapper();
		obm.setDateFormat(SDF2);
		try {
			vm = obm.readValue(json, BOMMaster.class);
			System.out.println(vm.toString());
		} catch (JsonProcessingException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		Mobomheader mobomheader = vm.getMobomheader();
		List<Mobomdetail> mobomdetails = vm.getMobomdetails();

		try {
			return mobomheaderService.saveWithDetail(mobomheader, mobomdetails, responseHelper);
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}
	}
	
	//fop report print method
	@GetMapping("/print/{xvoucher}")
	public ResponseEntity<byte[]> printLoanWithDetails(@PathVariable String xbomkey , HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Mobomheader mobomheader = mobomheaderService.findMobomheaderByXbomkey(xbomkey);
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		PdloantrnReport report=new PdloantrnReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("BOM Entry");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		//report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
		
//		List<Pdloanwriteoff>items = pdloantrnService.findByPdloanwriteoff(data.getXvoucher());
//		if(items != null && !items.isEmpty()) {
//			items.stream().forEach(it -> {
//				ItemDetails item = new ItemDetails();
//				item.setXdate(sdf.format(it.getXdate()));
//				item.setXloanamt(it.getXloanamt() != null ? it.getXloanamt():BigDecimal.ZERO);
//				item.setXstatus(it.getXstatus());
//				item.setXstatustag(it.getXstatustag());
//				item.setXnote(it.getXnote());
//				
//				report.getItems().add(item);
//		});
//		};
		byte[] byt = getPDFByte(report, "bomreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for Bom Entry: " + xbomkey;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}

}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class BOMMaster{
	private Mobomheader mobomheader;
	private List<Mobomdetail> mobomdetails;
}
