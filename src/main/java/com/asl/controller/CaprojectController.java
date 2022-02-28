package com.asl.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
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

import com.asl.entity.Acdetail;
import com.asl.entity.Caproject;
import com.asl.entity.Caprojectplan;
import com.asl.entity.Cawh;
import com.asl.entity.Cawhcostest;
import com.asl.entity.Cawhfact;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.LandDocument;
import com.asl.entity.LandExperience;
import com.asl.entity.LandPerson;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.ProjectStoreView;
import com.asl.entity.Termstrn;
import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.CaprojectReport;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.StoreRequisitionReport;
import com.asl.service.CaprojectService;
import com.asl.service.CawhService;
import com.asl.service.CawhfactService;
import com.asl.service.LandDocumentService;
import com.asl.service.ProjectStoreViewService;
import com.asl.service.XcodesService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/project/caproject")
public class CaprojectController extends ASLAbstractController{
	
	@Autowired private CaprojectService caprojectService;
	@Autowired private LandDocumentService landDocumentService;
	@Autowired private CawhService cawhService;
	@Autowired private XcodesService xcodeService;
	@Autowired private ProjectStoreViewService projectstoreviewService;
	@Autowired private CawhfactService cawhfactService;
	

	@GetMapping
	public String loadCaprojectPage(Model model) {
		model.addAttribute("caproject", getDefaultCaproject());
		model.addAttribute("allCaproject", caprojectService.getAllCaproject());
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_NUMBER.getCode(), Boolean.TRUE));
		return "pages/project/caproject/caproject";
	}

	private Caproject getDefaultCaproject() {
		Caproject cp = new Caproject();
		cp.setNewData(true);
		cp.setXtypetrn(TransactionCodeType.PROJECT_NUMBER.getCode());
		cp.setXtrn(TransactionCodeType.PROJECT_NUMBER.getdefaultCode());
		cp.setXstatus("Open");
		cp.setXamountcost(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		cp.setXamountbudgt(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		return cp;
	}
	

	@GetMapping("/{xproject}")
	public String loadCaprojectPage(@PathVariable String xproject, Model model) {
		Caproject data = caprojectService.findByCaproject(xproject);
		if (data == null) data = getDefaultCaproject();
		model.addAttribute("caproject", data);
		model.addAttribute("allCaproject", caprojectService.getAllCaproject());
		model.addAttribute("documentlist", landDocumentService.findByAllProjectDocument(xproject));
		model.addAttribute("sitedocumentlist", landDocumentService.findByAllProjectsSiteDocument(xproject));
		model.addAttribute("caprojectplanlist", caprojectService.findByCaprojectplan(xproject));
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_NUMBER.getCode(), Boolean.TRUE));
		
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXproject(data.getXproject());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);
		
		List<Cawh> sitedetails = cawhService.getCawhBYProject(data.getXproject());
		model.addAttribute("sitedetails", sitedetails);
		return "pages/project/caproject/caproject";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Caproject cp, BindingResult bindingResult){
		if(cp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(cp.getXproject())) {
			responseHelper.setErrorStatusAndMessage("Project required");
			return responseHelper.getResponse();
		}
		Xcodes xcodes = xcodeService.findByXcodes(cp.getXproject());
		cp.setXdesc(xcodes.getXlong());

		// Validate
		if(StringUtils.isBlank(cp.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}

		if (cp.isNewData()) {
			if (caprojectService.findByCaproject(cp.getXproject()) != null) {
				responseHelper.setErrorStatusAndMessage("Project Id " + cp.getXproject() + " already exist in this system");
				return responseHelper.getResponse();
			}
		}
		// If existing
		Caproject data = caprojectService.findByCaproject(cp.getXproject());
		if(data != null) {
			BeanUtils.copyProperties(cp, data, "xtypetrn", "xtrn");
			long count = caprojectService.updateCaproject(data);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Project Master.");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Project Master Updated Successfully");
			responseHelper.setRedirectUrl("/project/caproject/" + data.getXproject());
			return responseHelper.getResponse();
		}

		// If new

		long count = caprojectService.saveCaproject(cp);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Save Project Master");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Project Master Saved Successfully");
		responseHelper.setRedirectUrl("/project/caproject/" + cp.getXproject());
		return responseHelper.getResponse();
	}
	@PostMapping("/delete/{xproject}")
	public @ResponseBody Map<String, Object> deleteProject(@PathVariable String xproject,  Model model) {
		Caproject cp = caprojectService.findByCaproject(xproject);
		if(cp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = caprojectService.deleteCaproject(cp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/project/caproject/" + xproject );
		return responseHelper.getResponse();
	}
	

	@PostMapping("/confirm/{xproject}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xproject){
		Caproject cp = caprojectService.findByCaproject(xproject);
		if(cp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if("Confirmed".equalsIgnoreCase(cp.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("This project is already confirmed");
			return responseHelper.getResponse();
		}
		cp.setXstatus("Confirmed");
		long count = caprojectService.updateCaproject(cp);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirm this project.");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("This project is Confirmed successfully");
		responseHelper.setRedirectUrl("/project/caproject/" + xproject);
		return responseHelper.getResponse();
	}
	
	// For Document

	@GetMapping("/{xproject}/document/{xrow}/show")
	public String loadDocModal(@PathVariable String xproject, @PathVariable String xrow, Model model) {
		if ("new".equalsIgnoreCase(xrow)) {
			LandDocument document = new LandDocument();
			document.setXname("");
			document.setXproject(xproject);
			model.addAttribute("document", document);
			document.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
			document.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes",xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		} else {
			LandDocument document = landDocumentService.findDocumentByXprojectAndXrow(xproject,Integer.parseInt(xrow));
			if (document == null) {
				document = new LandDocument();
				document.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
				document.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
				
			}
			model.addAttribute("document", document);
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		}

		return "pages/project/caproject/documentmodal::documentmodal";
	}
	@PostMapping("/document/save")
	public @ResponseBody Map<String, Object> saveLandDoc(LandDocument landDocument, @RequestParam("files[]") MultipartFile[] files) {
		if (landDocument == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
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
		LandDocument existLand = landDocumentService.findDocumentByXprojectAndXrow(landDocument.getXproject(), landDocument.getXrow());
		if (existLand != null) {
			BeanUtils.copyProperties(landDocument, existLand, "xdoc");
			long count = landDocumentService.update(existLand);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("documenttable", "/project/caproject/document/" + existLand.getXproject());
			responseHelper.setSuccessStatusAndMessage("Document updated successfully");
			return responseHelper.getResponse();
		}
		
		
		String xtrn =  xtrnService.generateAndGetXtrnNumber(landDocument.getXtypetrn(), landDocument.getXtrn(), 6);
		landDocument.setXdoc(xtrn);
		//System.out.println("The Value Of Xtrn Is Now: "+xtrn);

		// if new detail
		long count = landDocumentService.save(landDocument);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("documenttable", "/project/caproject/document/" + landDocument.getXproject());
		responseHelper.setSuccessStatusAndMessage("Document saved successfully");
		return responseHelper.getResponse();
	}
	

	@GetMapping("/document/{xproject}")
	public String reloadDocTable(@PathVariable String xproject, Model model) {
		List<LandDocument> DocList = landDocumentService.findByAllProjectDocument(xproject);
		model.addAttribute("documentlist", DocList);
		model.addAttribute("caproject", caprojectService.findByCaproject(xproject));
		return "pages/project/caproject/caproject::documenttable";
	}
	
	//Delete
	@PostMapping("{xproject}/document/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteDocDetail(@PathVariable String xproject, @PathVariable String xrow, Model model) {
		LandDocument lpe = landDocumentService.findDocumentByXprojectAndXrow(xproject, Integer.parseInt(xrow));
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
		responseHelper.setReloadSectionIdWithUrl("documenttable", "/project/caproject/document/" + xproject);
		return responseHelper.getResponse();
	}
	

	@GetMapping("/print/{xproject}")
	public ResponseEntity<byte[]> printCaprojectWithDetails(@PathVariable String xproject , HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		
		Caproject data = caprojectService.findByCaproject(xproject); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		
		CaprojectReport report=new CaprojectReport();
		
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Project Master");
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setFromDate(sdf.format(data.getXdate()));
		report.setPrintDate(new SimpleDateFormat("E, dd-MMM-yyyy").format(new Date()));
		report.setPhone(sessionManager.getZbusiness().getXphone());
		report.setFax(sessionManager.getZbusiness().getXfax());
		
		report.setXproject(data.getXproject());
		report.setXcus(data.getXcus());
		if(data.getXdate()==null) {
			report.setXdate("");
		}
		if(data.getXdate()!=null)
		{
			report.setXdate(sdf.format(data.getXdate()));
		}
		
		if(data.getXestartdate()==null) {
			report.setXestartdate("");
		}
		if(data.getXestartdate()!=null)
		{
			report.setXestartdate(sdf.format(data.getXestartdate()));
		}
		
		if(data.getXeenddate()==null) {
			report.setXeenddate("");
		}
		if(data.getXeenddate()!=null)
		{
			report.setXeenddate(sdf.format(data.getXeenddate()));
		}
		
		if(data.getXastartdate()==null) {
			report.setXastartdate("");
		}
		if(data.getXastartdate()!=null)
		{
			report.setXastartdate(sdf.format(data.getXastartdate()));
		}
		
		if(data.getXaenddate()==null) {
			report.setXaenddate("");
		}
		if(data.getXaenddate()!=null)
		{
			report.setXaenddate(sdf.format(data.getXaenddate()));
		}
		report.setXstatus(data.getXstatus());
		report.setXref(data.getXref());
		report.setXnote(data.getXnote());
		report.setXamountbudgt(data.getXamountbudgt());
		report.setXamountcost(data.getXamountcost());
		report.setXcontact(data.getXcontact());
		report.setXplannedby(data.getXplannedby());
		report.setCusName(data.getXorg());
		report.setXcontactName(data.getXcontactName());
		report.setXpreparerName(data.getXplannedbyName());
		report.setXdesc(data.getXdesc());
		
		if("Open".equalsIgnoreCase(data.getXstatus())) {
			report.setXstatus("Open");
		}
		else if("Confirmed".equalsIgnoreCase(data.getXstatus())) {
			report.setXstatus("Confirmed");
		}
		
		List<LandDocument> documents = landDocumentService.findByAllProjectDocument(xproject);
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
		List<Caprojectplan> plans = caprojectService.findByCaprojectplan(xproject);
		if(plans != null && !plans.isEmpty()) report.setPlans(plans);
		
		List<Cawh> exp = cawhService.getCawhBYProject(xproject);
		if(exp != null && !exp.isEmpty()) report.setSites(exp);
		
		List<Cawhfact> tasks = cawhfactService.getAllTasksByProject(xproject);
		if(tasks != null && !tasks.isEmpty()) report.setTasks(tasks);
		
		byte[] byt = getPDFByte(report, "caprojectreport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for project master: " + xproject;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
		
	}
	
	//start of Project Planning Details
	
	@GetMapping("/{xproject}/caprojectplan/{xrow}/show")
	public String loadEstCostModal(@PathVariable String xproject, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			Caprojectplan ec = new Caprojectplan();
			ec.setXproject(xproject);
			model.addAttribute("caprojectplan", ec);
			model.addAttribute("sites", projectstoreviewService.getProjectStoresByXproject(xproject));
			model.addAttribute("allcodes", Collections.emptyList());
		}
		else {
			Caprojectplan ec = caprojectService.findCaprojectplanByXprojectAndXrow(xproject, Integer.parseInt(xrow));
			if(ec==null) {
				ec = new Caprojectplan();
			}
			model.addAttribute("caprojectplan", ec);
			model.addAttribute("sites", projectstoreviewService.getProjectStoresByXproject(xproject));
			//model.addAttribute("sites", xcodeService.findByXtype(CodeType.SITE.getCode(), Boolean.TRUE));
			model.addAttribute("allcodes", Collections.emptyList());
		}
		
		return "pages/project/caproject/caprojectplanmodal::caprojectplanmodal";
	}

	@PostMapping("/caprojectplan/save")
	public @ResponseBody Map<String, Object> saveCaprojectplan(Caprojectplan caprojectplan){
		if(caprojectplan == null || StringUtils.isBlank(caprojectplan.getXproject())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(caprojectplan.getXproject())) {
			responseHelper.setErrorStatusAndMessage("Project required.");
			return responseHelper.getResponse();
		}

		Caprojectplan exist = caprojectService.findCaprojectplanByXprojectAndXrow(caprojectplan.getXproject(), caprojectplan.getXrow());

		// if existing
		if(exist != null) {
			BeanUtils.copyProperties(caprojectplan, exist, "xproject", "xrow");
			long count = caprojectService.updateCaprojectplan(exist);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("caprojectplantable","/project/caproject/caprojectplan/" + caprojectplan.getXproject());
			responseHelper.setSecondReloadSectionIdWithUrl("caprojectheaderform", "/project/caproject/caprojectheaderform/" + caprojectplan.getXproject());
			responseHelper.setThirdReloadSectionIdWithUrl("caprojectheadertable", "/project/caproject/caprojectheadertable");
			responseHelper.setSuccessStatusAndMessage("Project Planning details updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = caprojectService.saveCaprojectplan(caprojectplan);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("caprojectplantable","/project/caproject/caprojectplan/" + caprojectplan.getXproject());
		responseHelper.setSecondReloadSectionIdWithUrl("caprojectheaderform", "/project/caproject/caprojectheaderform/" + caprojectplan.getXproject());
		responseHelper.setThirdReloadSectionIdWithUrl("caprojectheadertable", "/project/caproject/caprojectheadertable");
		responseHelper.setSuccessStatusAndMessage("Project Planning details saved successfully");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/caprojectplan/{xproject}")
	public String reloadCaprojectplanTable(@PathVariable String xproject, Model model) {
		List<Caprojectplan> caprojectplanList = caprojectService.findByCaprojectplan(xproject);
		model.addAttribute("caprojectplanlist", caprojectplanList);
		model.addAttribute("caproject", caprojectService.findByCaproject(xproject));
		
		Caproject data = caprojectService.findByCaproject(xproject);
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXproject(data.getXproject());
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		model.addAttribute("allcodes", list);

		return "pages/project/caproject/caproject::caprojectplantable";
	}
	
	//delete
	@PostMapping("{xproject}/caprojectplan/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteEstCost(@PathVariable String xproject, @PathVariable String xrow, Model model) {
		Caprojectplan ec = caprojectService.findCaprojectplanByXprojectAndXrow(xproject, Integer.parseInt(xrow));
		if(ec == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = caprojectService.deleteCaprojectplan(ec);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("caprojectplantable","/project/caproject/caprojectplan/" + xproject);
		responseHelper.setSecondReloadSectionIdWithUrl("caprojectheaderform", "/project/caproject/caprojectheaderform/" + xproject);
		responseHelper.setThirdReloadSectionIdWithUrl("caprojectheadertable", "/project/caproject/caprojectheadertable");return responseHelper.getResponse();
		
	}

	@GetMapping("/caprojectheaderform/{xproject}")
	public String loadCaprojectheaderformFormPage(@PathVariable String xproject, Model model) {

		Caproject data = caprojectService.findByCaproject(xproject);
		if (data == null) data = getDefaultCaproject();
		model.addAttribute("caproject", data);
		model.addAttribute("allCaproject", caprojectService.getAllCaproject());
		model.addAttribute("documentlist", landDocumentService.findByAllProjectDocument(xproject));
		model.addAttribute("sitedocumentlist", landDocumentService.findByAllProjectsSiteDocument(xproject));
		model.addAttribute("prefix",xtrnService.findByXtypetrn(TransactionCodeType.PROJECT_NUMBER.getCode(), Boolean.TRUE));
		
		List<Cawh> sitedetails = cawhService.getCawhBYProject(data.getXproject());
		model.addAttribute("sitedetails", sitedetails);
		
		return "pages/project/caproject/caproject:: caprojectheaderform";
	}

	@GetMapping("/caprojectheadertable")
	public String reloadCawhheaderTable(Model model) {
		model.addAttribute("allCaproject", caprojectService.getAllCaproject());
		return "pages/project/caproject/caproject::caprojectheadertable";
	}
	
	@GetMapping("/allcodesbyproject/{xproject}")
	public @ResponseBody List<ProjectStoreView> getProjectstoreview(@PathVariable String xproject){
		List<ProjectStoreView> list = projectstoreviewService.getProjectStoresByXproject(xproject);
		list.sort(Comparator.comparing(ProjectStoreView::getXcode));
		return list;
	}
	////end of Project Planning Details



}
