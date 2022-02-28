package com.asl.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;

import com.asl.entity.Caitem;
import com.asl.entity.Caproject;
import com.asl.entity.Cawh;
import com.asl.entity.Cawhbudget;
import com.asl.entity.Cawhcostest;
import com.asl.entity.Imissueheader;
import com.asl.entity.ImtorDetail;
import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.LandDocument;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;
import com.asl.entity.Poreqdetail;
import com.asl.entity.Poreqheader;
import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.CaprojectReport;
import com.asl.model.report.CawhReport;
import com.asl.service.CaitemService;
import com.asl.service.CaprojectService;
import com.asl.service.CawhService;
import com.asl.service.CawhbudgetService;
import com.asl.service.ImissueheaderService;
import com.asl.service.LandDocumentService;
import com.asl.service.XcodesService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/project/cawh")
public class CawhController extends ASLAbstractController{
	

	@Autowired private CawhService cawhService;
	@Autowired private XcodesService xcodeService;
	@Autowired private CaprojectService caprojectService;
	@Autowired private LandDocumentService landDocumentService;
	@Autowired private CawhbudgetService cawhbudgetService;
	@Autowired private ImissueheaderService imissueheaderService;
	@Autowired private CaitemService caitemService;
	

	@GetMapping
	public String loadSitePage(Model model) {
		model.addAttribute("cawh", getDefaultSite());
		model.addAttribute("allCawh", cawhService.getAllCawh());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		
		return "pages/project/cawh/cawh";
	}

	private Cawh getDefaultSite() {
		Cawh cawh = new Cawh();
		cawh.setXtrn("Site");
		cawh.setXstatus("Open");
		cawh.setXamountcost(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		cawh.setXamountbudgt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		cawh.setXtotamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		cawh.setNewData(true);
		return cawh;
	}
	
	@GetMapping("/{xwh}")
	public String loadSitePage(@PathVariable String xwh, Model model) {
		Cawh data = cawhService.findByCawh(xwh);
		if (data == null) data = getDefaultSite();
		model.addAttribute("cawh", data);
		model.addAttribute("allCawh", cawhService.getAllCawh());
		model.addAttribute("documentlist", landDocumentService.findByAllSiteDocument(xwh));
		model.addAttribute("estcostlist", cawhService.findByCawhcostest(xwh));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		
		List<Cawhbudget> allCawhbudget = cawhbudgetService.getAllCawhbudgetByXwh(data.getXwh());
		model.addAttribute("allCawhbudget", allCawhbudget);
		
		List<Imissueheader> allImissueheader = imissueheaderService.getALllImissueheaderByXwh(data.getXwh());
		model.addAttribute("allImissueheader", allImissueheader);

		List<Cawhcostest> details = cawhService.findByCawhcostest(xwh);
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if (details != null && !details.isEmpty()) {
			for (Cawhcostest pd : details) {
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalLineAmount", totalLineAmount);

		return "pages/project/cawh/cawh";
	}
	

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cawh cawh, BindingResult bindingResult) {
		if (cawh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(cawh.getXwh())) {
			responseHelper.setErrorStatusAndMessage("Site required");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(cawh.getXproject())) {
			responseHelper.setErrorStatusAndMessage("Project required");
			return responseHelper.getResponse();
		}

		Xcodes xcodes = xcodeService.findByXcodes(cawh.getXwh());
		cawh.setXdesc(xcodes.getXlong());

		if (cawh.isNewData()) {
			if (cawhService.findByCawh(cawh.getXwh()) != null) {
				responseHelper.setErrorStatusAndMessage("Side Code" + cawh.getXwh() + " already exist in this system");
				return responseHelper.getResponse();
			}
			
			
			long count = cawhService.saveCawh(cawh);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save this site.");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Site info saved successfully");
			responseHelper.setRedirectUrl("/project/cawh/" + cawh.getXwh());
			return responseHelper.getResponse();
		}
		
		
		Cawh xlp = cawhService.findByCawh(cawh.getXwh());
		BeanUtils.copyProperties(cawh, xlp, "xwh");
		long count = cawhService.updateCawh(xlp);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update site info");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Site info updated successfully");
		responseHelper.setRedirectUrl("/project/cawh/" + xlp.getXwh());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xwh}")
	public @ResponseBody Map<String, Object> deleteProject(@PathVariable String xwh,  Model model) {
		Cawh cp = cawhService.findByCawh(xwh);
		if(cp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		List<LandDocument> DocList = landDocumentService.findByAllSiteDocument(xwh);
		if(DocList != null && !DocList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete all Document Details first");
			return responseHelper.getResponse();
		}
		
		List<Cawhcostest> estcostList = cawhService.findByCawhcostest(xwh);
		if(estcostList != null && !estcostList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete all Estimated Cost Details first");
			return responseHelper.getResponse();
		}
		
		long count = cawhService.deleteCawh(cp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/project/cawh/" + xwh );
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm/{xwh}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xwh){
		Cawh cp = cawhService.findByCawh(xwh);
		if(cp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if("Confirmed".equalsIgnoreCase(cp.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("This site is already confirmed");
			return responseHelper.getResponse();
		}
		List<Cawhcostest> estcostList = cawhService.findByCawhcostest(xwh);
		if(estcostList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}
		cp.setXstatus("Confirmed");
		long count = cawhService.updateCawh(cp);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm this site.");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("This site is Confirmed successfully");
		responseHelper.setRedirectUrl("/project/cawh/" + xwh);
		return responseHelper.getResponse();
	}	
	@GetMapping("/projectcustomer/{xproject}")
	public @ResponseBody Caproject getCustomerDetail(@PathVariable String xproject){
		return caprojectService.findByCaproject(xproject);
	}

	/*
	 * @GetMapping("/sitename/{xcode}") public @ResponseBody Cawh
	 * getSiteName(@PathVariable String xcode){ return
	 * cawhService.findByCawh(xcode); }
	 */
	@GetMapping("/sitename/{xcode}")
	public @ResponseBody Xcodes getSiteName(@PathVariable String xcode){
		return xcodeService.findByXcodes(xcode);
	}
	// For Document

	@GetMapping("/{xwh}/document/{xrow}/show")
	public String loadDocModal(@PathVariable String xwh, @PathVariable String xrow, Model model) {
		if ("new".equalsIgnoreCase(xrow)) {
			LandDocument document = new LandDocument();
			document.setXname("");
			document.setXwh(xwh);
			model.addAttribute("document", document);
			document.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
			document.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes",xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		} else {
			LandDocument document = landDocumentService.findSiteDocumentByXwhAndXrow(xwh,Integer.parseInt(xrow));
			if (document == null) {
				document = new LandDocument();
				document.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
				document.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
				
			}
			model.addAttribute("document", document);
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		}

		return "pages/project/cawh/documentmodal::documentmodal";
	}
	@PostMapping("/document/save")
	public @ResponseBody Map<String, Object> saveLandDoc(LandDocument landDocument, @RequestParam("files[]") MultipartFile[] files) {
		if (landDocument == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(landDocument.getXdoctype())) {
			responseHelper.setErrorStatusAndMessage("Type required");
			return responseHelper.getResponse();
		}

		if (files != null && files.length > 0) {

			// Rename the file
			String extension = null;
			int j = files[0].getOriginalFilename().lastIndexOf('.');
			if (j > 0) {
				extension = files[0].getOriginalFilename().substring(j + 1);
			}
			
			//Split Text
			 String[] a = files[0].getOriginalFilename().split("\\.");
			 String part1 = a[0];
			 //System.out.println("The File Name Is: "+part1);
			 //End split
			
			String fileName = UUID.randomUUID() + "_" + part1 + "." + extension;
			log.debug("File name is now: {}", fileName);

			try {
				// create a directory if not exist
				String uploadPath = appConfig.getDocumentPath();
				File dir = new File(uploadPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// Upload file into server
				Files.copy(files[0].getInputStream(), Paths.get(uploadPath, fileName));
				landDocument.setXdocument(fileName);
				landDocument.setXnameold(files[0].getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// if existing
		LandDocument existLand = landDocumentService.findSiteDocumentByXwhAndXrow(landDocument.getXwh(), landDocument.getXrow());
		if (existLand != null) {
			BeanUtils.copyProperties(landDocument, existLand, "xdoc");
			long count = landDocumentService.update(existLand);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("documenttable", "/project/cawh/document/" + existLand.getXwh());
			responseHelper.setSuccessStatusAndMessage("Document updated successfully");
			return responseHelper.getResponse();
		}
		
		
		String xtrn =  xtrnService.generateAndGetXtrnNumber(landDocument.getXtypetrn(), landDocument.getXtrn(), 6);
		landDocument.setXdoc(xtrn);
		Cawh data = cawhService.findByCawh(landDocument.getXwh());
		landDocument.setXproject(data.getXproject());
		//System.out.println("The Value Of Xtrn Is Now: "+xtrn);

		// if new detail
		long count = landDocumentService.save(landDocument);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("documenttable", "/project/cawh/document/" + landDocument.getXwh());
		responseHelper.setSuccessStatusAndMessage("Document saved successfully");
		return responseHelper.getResponse();
	}
	

	@GetMapping("/document/{xwh}")
	public String reloadDocTable(@PathVariable String xwh, Model model) {
		List<LandDocument> DocList = landDocumentService.findByAllSiteDocument(xwh);
		model.addAttribute("documentlist", DocList);
		model.addAttribute("cawh", cawhService.findByCawh(xwh));
		return "pages/project/cawh/cawh::documenttable";
	}
	
	//Delete
	@PostMapping("{xwh}/document/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteDocDetail(@PathVariable String xwh, @PathVariable String xrow, Model model) {
		LandDocument lpe = landDocumentService.findSiteDocumentByXwhAndXrow(xwh, Integer.parseInt(xrow));
		if (lpe == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landDocumentService.deleteDetail(lpe);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Document Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("documenttable", "/project/cawh/document/" + xwh);
		return responseHelper.getResponse();
	}
	

	@GetMapping("/print/{xwh}")
	public ResponseEntity<byte[]> printCawhWithDetails(@PathVariable String xwh , HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Cawh data = cawhService.findByCawh(xwh); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		CawhReport report=new CawhReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Site Master");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
		report.setXwh(data.getXwh());
		report.setXname(data.getXname());
		report.setXproject(data.getXproject());
		report.setXcus(data.getXcus());
		report.setXsupevisor(data.getXsupervisor());
		report.setXlocation(data.getXlocation());
		if(data.getXdate()==null) {
			report.setXdate("");
		}
		if(data.getXdate()!=null)
		{
			report.setXdate(sdf.format(data.getXdate()));
		}
		
		if(data.getXstartdate()==null) {
			report.setXstartdate("");
		}
		if(data.getXstartdate()!=null)
		{
			report.setXstartdate(sdf.format(data.getXstartdate()));
		}
		
		if(data.getXenddate()==null) {
			report.setXenddate("");
		}
		if(data.getXenddate()!=null)
		{
			report.setXenddate(sdf.format(data.getXenddate()));
		}
		
		report.setXamountbudgt(data.getXamountbudgt());
		report.setXamountcost(data.getXamountcost());
		report.setXref(data.getXref());
		report.setXnote(data.getXnote());
		report.setXstatus(data.getXstatus());
		report.setCusName(data.getXorg());
		report.setXsupervisorName(data.getXsupervisorName());
		report.setXdesc(data.getXdesc());
		report.setProjectName(data.getProjectName());
		
		List<LandDocument> documents = landDocumentService.findByAllSiteDocument(xwh);
		if(documents != null && !documents.isEmpty()) {
			for(LandDocument documet : documents) {
				if(StringUtils.isBlank(documet.getXdoc())) continue;
				LandDocument lp = landDocumentService.findByLandDocument(documet.getXdoc());
				if(lp != null) {
					documet.setXdoc(documet.getXdoc());
				}
			}
			report.setDocuments(documents);
		}
		
		List<Cawhcostest> costs = cawhService.findByCawhcostest(xwh);
		if(costs != null && !costs.isEmpty()) {
			for(Cawhcostest cost : costs) {
				if(StringUtils.isBlank(cost.getXwh())) continue;
				Cawh lp = cawhService.findByCawh(cost.getXwh());
				if(lp != null) {
					report.setXfdate(sdf.format(cost.getXfdate()));
				}
			}
			report.setCostests(costs);
		}

		//List<Cawhcostest> cost = cawhService.findByCawhcostest(xwh);
		//if(cost != null && !cost.isEmpty()) report.setCostests(cost);
		//cost.setFromDate(sdf.format(cost.getXfdate()));


		byte[] byt = getPDFByte(report, "cawhreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for site master: " + xwh;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}
	

	//start of Estimated Cost Details
	
		@GetMapping("/{xwh}/estcost/{xrow}/show")
		public String loadEstCostModal(@PathVariable String xwh, @PathVariable String xrow, Model model) {
			if("new".equalsIgnoreCase(xrow)) {
				Cawhcostest ec = new Cawhcostest();
				ec.setXwh(xwh);
				ec.setXqty(BigDecimal.ZERO);
				ec.setXrate(BigDecimal.ONE);
				ec.setXlineamt(BigDecimal.ZERO);
				model.addAttribute("estcost", ec);
				model.addAttribute("purpose", xcodesService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
			}
			else {
				Cawhcostest ec = cawhService.findCawhcostestByXwhAndXrow(xwh, Integer.parseInt(xrow));
				if(ec==null) {
					ec = new Cawhcostest();
					ec.setXqty(BigDecimal.ZERO);
					ec.setXrate(BigDecimal.ONE);
					ec.setXlineamt(BigDecimal.ZERO);
				}
				model.addAttribute("estcost", ec);
				model.addAttribute("purpose", xcodesService.findByXtype(CodeType.EXPENSE_PURPOSE.getCode(), Boolean.TRUE));
			}
			
			return "pages/project/cawh/estcostmodal::estcostmodal";
		}

		@PostMapping("/estcost/save")
		public @ResponseBody Map<String, Object> saveEstCost(Cawhcostest cawhcostest){
			if(cawhcostest == null || StringUtils.isBlank(cawhcostest.getXwh())) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			
			if(StringUtils.isBlank(cawhcostest.getXitem())) {
				responseHelper.setErrorStatusAndMessage("Please select an item");
				return responseHelper.getResponse();
			}

			if(cawhcostest.getXrate().compareTo(BigDecimal.ZERO) == 0.00 ) {
				responseHelper.setErrorStatusAndMessage("Unit Price should not <0.01");
				return responseHelper.getResponse();
			}

			if(cawhcostest.getXqty().compareTo(BigDecimal.ZERO) == 0.00){
				responseHelper.setErrorStatusAndMessage("Quantity should not <0.01");
				return responseHelper.getResponse();
			}
			
			if(StringUtils.isBlank(cawhcostest.getXpurpose())) {
				responseHelper.setErrorStatusAndMessage("Please select any purpose.");
				return responseHelper.getResponse();
			}

			
//			if((cawhcostest.getXgitem().equalsIgnoreCase("Services") || cawhcostest.getXgitem().equalsIgnoreCase("Service") || cawhcostest.getXgitem().equalsIgnoreCase("Cost")
//			|| cawhcostest.getXgitem().equalsIgnoreCase("Non-Inventory") || cawhcostest.getXgitem().equalsIgnoreCase("Servicing")) && StringUtils.isBlank(cawhcostest.getXpurpose())) {
//				responseHelper.setErrorStatusAndMessage("Select any purpose.");
//				return responseHelper.getResponse();
//			}

			// Check item already exist in detail list
			/*
			 * if(cawhcostest.getXrow() == 0 &&
			 * cawhService.findCawhcostestByXwhAndXitem(cawhcostest.getXwh(),
			 * cawhcostest.getXitem()) != null) { responseHelper.
			 * setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing"
			 * ); return responseHelper.getResponse(); }
			 */

			Cawhcostest exist = cawhService.findCawhcostestByXwhAndXrow(cawhcostest.getXwh(), cawhcostest.getXrow());

			// if existing
			if(exist != null) {
				BeanUtils.copyProperties(cawhcostest, exist, "xwh", "xrow");
				long count = cawhService.updateCawhcostest(exist);
				if(count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
				responseHelper.setReloadSectionIdWithUrl("estcosttable","/project/cawh/estcost/" + cawhcostest.getXwh());
				responseHelper.setSecondReloadSectionIdWithUrl("cawhheaderform", "/project/cawh/cawhheaderform/" + cawhcostest.getXwh());
				responseHelper.setThirdReloadSectionIdWithUrl("cawhheadertable", "/project/cawh/cawhheadertable");
				responseHelper.setSuccessStatusAndMessage("Estimated cost details updated successfully");
				return responseHelper.getResponse();
			}

			// if new detail
			long count = cawhService.saveCawhcostest(cawhcostest);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("estcosttable","/project/cawh/estcost/" + cawhcostest.getXwh());
			responseHelper.setSecondReloadSectionIdWithUrl("cawhheaderform", "/project/cawh/cawhheaderform/" + cawhcostest.getXwh());
			responseHelper.setThirdReloadSectionIdWithUrl("cawhheadertable", "/project/cawh/cawhheadertable");
			responseHelper.setSuccessStatusAndMessage("Estimated cost details updated successfully");
			return responseHelper.getResponse();
		}
		
		@GetMapping("/estcost/{xwh}")
		public String reloadEstCostTable(@PathVariable String xwh, Model model) {
			List<Cawhcostest> estcostList = cawhService.findByCawhcostest(xwh);
			model.addAttribute("estcostlist", estcostList);
			model.addAttribute("cawh", cawhService.findByCawh(xwh));

			List<Cawhcostest> details = cawhService.findByCawhcostest(xwh);
			BigDecimal totalLineAmount = BigDecimal.ZERO;
			if (details != null && !details.isEmpty()) {
				for (Cawhcostest pd : details) {
					totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
				}
			}
			model.addAttribute("totalLineAmount", totalLineAmount);

			return "pages/project/cawh/cawh::estcosttable";
		}
		
		//delete
		@PostMapping("{xwh}/estcost/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteEstCost(@PathVariable String xwh, @PathVariable String xrow, Model model) {
			Cawhcostest ec = cawhService.findCawhcostestByXwhAndXrow(xwh, Integer.parseInt(xrow));
			if(ec == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = cawhService.deleteCawhcostest(ec);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("estcosttable", "/project/cawh/estcost/" + xwh);
			responseHelper.setSecondReloadSectionIdWithUrl("cawhheaderform", "/project/cawh/cawhheaderform/" + xwh);
			responseHelper.setThirdReloadSectionIdWithUrl("cawhheadertable", "/project/cawh/cawhheadertable");
			return responseHelper.getResponse();
			
		}
		
		@GetMapping("/itemdetail/{xitem}")
		public @ResponseBody Caitem getItemDetail(@PathVariable String xitem){
			return caitemService.findByXitem(xitem);
		}
		
		@GetMapping("/cawhheaderform/{xwh}")
		public String loadCawhheaderFormPage(@PathVariable String xwh, Model model) {

			Cawh data = cawhService.findByCawh(xwh);
			if (data == null) data = getDefaultSite();
			//if (data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("cawh", data);
			model.addAttribute("allCawh", cawhService.getAllCawh());
			model.addAttribute("documentlist", landDocumentService.findByAllSiteDocument(xwh));
			model.addAttribute("estcostlist", cawhService.findByCawhcostest(xwh));
			model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
			
			return "pages/project/cawh/cawh:: cawhheaderform";
		}

		@GetMapping("/cawhheadertable")
		public String reloadCawhheaderTable(Model model) {
			model.addAttribute("allCawh",cawhService.getAllCawh());
			return "pages/project/cawh/cawh::cawhheadertable";
		}
		////end of Estimated Cost Details


}
