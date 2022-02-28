package com.asl.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
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

import com.asl.entity.Cacus;
import com.asl.entity.Cadag;
import com.asl.entity.Imstock;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.LandDagDetails;
import com.asl.entity.LandDocument;
import com.asl.entity.LandEvents;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.entity.LandPerson;
import com.asl.entity.LandSurvey;
import com.asl.entity.LandSurveyor;
import com.asl.entity.Zbusiness;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.LandInfoReport;
import com.asl.service.CacusService;
import com.asl.service.LandDagMasterService;
import com.asl.service.LandDocumentService;
import com.asl.service.LandInfoService;
import com.asl.service.LandPersonService;
import com.asl.service.LandSurveyService;
import com.asl.service.LandSurveyorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/landinfo")
public class LandInfoController extends ASLAbstractController {

	@Autowired private LandInfoService landInfoService;
	@Autowired private LandDocumentService landDocumentService;
	@Autowired private LandSurveyService landSurveyService;
	@Autowired private LandPersonService landPersonService;
	@Autowired private LandSurveyorService landSurveyorService;
	@Autowired private CacusService cacusService;
	@Autowired private LandDagMasterService landDagMasterService;

	@GetMapping
	public String loadLandInfoPage(Model model) {
		
		model.addAttribute("info", getDefaultLandinfo());
		model.addAttribute("allInfos", landInfoService.getAllLandInfo());
		//model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LAND_ID.getCode(), Boolean.TRUE));
		model.addAttribute("statusTypes", xcodesService.findByXtype(CodeType.STATUS_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/landinfo";
	}
	
	private LandInfo getDefaultLandinfo() {
		LandInfo lf  = new LandInfo();
		lf.setNewData(true);
		//lf.setXtypetrn(TransactionCodeType.LAND_ID.getCode());
		//lf.setXtrn(TransactionCodeType.LAND_ID.getdefaultCode());
		lf.setXtypetrn("Land ID");
		lf.setXlandqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlanddedother(0);
		lf.setXroadred(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXotherred(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlandnetqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlanddedroad(0);
		lf.setXriversideqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlandqtyd(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlandqtyk(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXtotdedprice(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXamtar(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXamtrv(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlandqtydsg(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlandqtydsn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		

		return lf;
	}
	
	@GetMapping("/{xland}")
	public String loadInfoPage(@PathVariable String xland, Model model) {
		LandInfo landInfo = landInfoService.findByLandInfo(xland);
		if (landInfo == null) return "redirect:/landinfo";

		 
		model.addAttribute("info", landInfo);
		model.addAttribute("allInfos", landInfoService.getAllLandInfo());
		model.addAttribute("lelist", landInfoService.findByLandEvents(xland));
		model.addAttribute("lldlist", landDocumentService.findByAllLandDocument(xland));
		model.addAttribute("lpelist", landInfoService.findByLandOwner(xland));
		model.addAttribute("lddlist", landInfoService.findByLandDagDetails(xland));
		model.addAttribute("llslist", landSurveyService.findByXlandSurvey(xland));
		//model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LAND_ID.getCode(), Boolean.TRUE));
		model.addAttribute("statusTypes", xcodesService.findByXtype(CodeType.STATUS_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));


		return "pages/land/landinfo";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandInfo landInfo, BindingResult bindingResult) {
		if (landInfo == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(landInfo.getXland())) {
			responseHelper.setErrorStatusAndMessage("Land Id required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(landInfo.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Member/Director required");
			return responseHelper.getResponse();
		}

		if (landInfo.isNewData()) {
			if (landInfoService.findByLandInfo(landInfo.getXland()) != null) {
				responseHelper.setErrorStatusAndMessage("Land Id " + landInfo.getXland() + " data already exist in this system");
				return responseHelper.getResponse();
			}
			
			long count = landInfoService.save(landInfo);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save land info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Land info saved successfully");
			responseHelper.setRedirectUrl("/landinfo/" + landInfo.getXland());
			return responseHelper.getResponse();
		}
		
		LandInfo xlp = landInfoService.findByLandInfo(landInfo.getXland());
		BeanUtils.copyProperties(landInfo, xlp, "xland");
		long count = landInfoService.update(xlp);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update land info");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Land info updated successfully");
		responseHelper.setRedirectUrl("/landinfo/" + xlp.getXland());
		return responseHelper.getResponse();

		
	}
	
	@PostMapping("/archive/{xland}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xland){
		return doArchiveOrRestore(xland, true);
	}

	@PostMapping("/restore/{xland}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xland){
		return doArchiveOrRestore(xland, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xland, boolean archive){
		LandInfo lp = landInfoService.findByLandInfo(xland);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lp.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landInfoService.update(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Land Information updated successfully");
		responseHelper.setRedirectUrl("/landinfo/" + lp.getXland());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xland}")
	public @ResponseBody Map<String, Object> deleteLandInfo(@PathVariable String xland,  Model model) {
		LandInfo li = landInfoService.findByLandInfo(xland);
		if(li == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landInfoService.delete(li);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/landinfo/" + xland );
		return responseHelper.getResponse();
}
	//start of landowner
	
	@GetMapping("/{xland}/owner/{xrow}/show")
	public String loadOwnerModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			LandOwner lpe = new LandOwner();
			lpe.setXnote("");
			lpe.setXperson("");
			lpe.setXqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			lpe.setXland(xland);
			lpe.setXtype("");
			lpe.setXunit("");
			model.addAttribute("lpe", lpe);
			model.addAttribute("ownerTypes", xcodesService.findByXtype(CodeType.OWNER_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		}
		else {
			LandOwner lpe = landInfoService.findLandOwnerByXlandAndXrow(xland, Integer.parseInt(xrow));
			if(lpe==null) {
				lpe = new LandOwner();
				
			}
			model.addAttribute("lpe", lpe);
			model.addAttribute("ownerTypes", xcodesService.findByXtype(CodeType.OWNER_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		}
		
		return "pages/land/ownermodal::ownermodal";
	}
	
	@PostMapping("/owner/save")
	public @ResponseBody Map<String, Object> saveLandOwner(LandOwner landOwner){
		if(landOwner == null || StringUtils.isBlank(landOwner.getXland())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		if( StringUtils.isBlank(landOwner.getXperson())) {
			responseHelper.setErrorStatusAndMessage("You must have to provide land owner.");
			return responseHelper.getResponse();
		}

		LandOwner exist = landInfoService.findLandOwnerByXlandAndXrow(landOwner.getXland(), landOwner.getXrow());
		LandOwner existing = landInfoService.findByXlandAndXperson(landOwner.getXland(), landOwner.getXperson());
		// if new data
		if(landOwner.getXrow() == 0 && existing != null) {
			responseHelper.setErrorStatusAndMessage("Land " + landOwner.getXland() + " with person " + landOwner.getXperson() + " data already exist in this system");
			return responseHelper.getResponse();
		}
		
		// if existing
		if(landOwner.getXrow() != 0 && exist != null) {
			
			BeanUtils.copyProperties(landOwner, exist);
			long count = landInfoService.update(exist);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			
			if( StringUtils.isBlank(landOwner.getXperson())) {
				responseHelper.setErrorStatusAndMessage("You must have to provide land owner.");
				return responseHelper.getResponse();
			}
			if(landOwner.getXrow() != 0 && existing != null) {
				responseHelper.setErrorStatusAndMessage("Land " + landOwner.getXland() + " with person " + landOwner.getXperson() + " data already exist in this system");
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("ownertable","/landinfo/owner/" + landOwner.getXland());
			responseHelper.setSuccessStatusAndMessage("Owner Detaails updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = landInfoService.save(landOwner);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("ownertable", "/landinfo/owner/" + landOwner.getXland());
		responseHelper.setSuccessStatusAndMessage("Owner Details saved successfully");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/owner/{xland}")
	public String reloadOwnerTable(@PathVariable String xland, Model model) {
		List<LandOwner> ownerList = landInfoService.findByLandOwner(xland);
		model.addAttribute("lpelist", ownerList);
		model.addAttribute("info", landInfoService.findByLandInfo(xland));
		return "pages/land/landinfo::ownertable";
	}

	//delete
	@PostMapping("{xland}/owner/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOwnerDetails(@PathVariable String xland, @PathVariable String xrow, Model model) {
		LandOwner lpe = landInfoService.findLandOwnerByXlandAndXrow(xland, Integer.parseInt(xrow));
		if(lpe == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landInfoService.deleteLandOwner(lpe);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("ownertable", "/landinfo/owner/" + xland);
		return responseHelper.getResponse();
	}
	////end of landowner
	
	
	//start of dag details
	
	@GetMapping("/{xland}/landdag/{xrow}/show")
	public String loadLandDagModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			LandDagDetails ldd = new LandDagDetails();
			ldd.setXdagnum(0);
			ldd.setXdagtype("");
			ldd.setXland(xland);
			ldd.setXqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			ldd.setXtype("");
			ldd.setXunit("");
			model.addAttribute("ldd", ldd);
			model.addAttribute("dagTypes", xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landTypes", xcodesService.findByXtype(CodeType.LAND_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
			model.addAttribute("finddagnum", Collections.emptyList());
		}
		else {
			LandDagDetails ldd = landInfoService.findlandDagDetailsByXlandAndXrow(xland, Integer.parseInt(xrow));
			if(ldd==null) {
				ldd = new LandDagDetails();
				
			}
			model.addAttribute("ldd", ldd);
			model.addAttribute("dagTypes", xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landTypes", xcodesService.findByXtype(CodeType.LAND_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
			List<Cadag> dag= landDagMasterService.getDagNo(ldd.getXdagtype());
			model.addAttribute("finddagnum", dag);
		}
		
		return "pages/land/landdagmodal::landdagmodal";
	}
	
	@PostMapping("/landdag/save")
	public @ResponseBody Map<String, Object> saveLandDagDetails(LandDagDetails landDagDetails) {
		if (landDagDetails == null || StringUtils.isBlank(landDagDetails.getXland())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		LandDagDetails exist = landInfoService.findlandDagDetailsByXlandAndXrow(landDagDetails.getXland(), landDagDetails.getXrow());
		if (exist != null) {
			BeanUtils.copyProperties(landDagDetails, exist,"xland");
			long count = landInfoService.update(exist);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("landdagtable","/landinfo/landdag/" + landDagDetails.getXland());
			responseHelper.setSuccessStatusAndMessage("Land Dag Details updated successfully");
			return responseHelper.getResponse();
		}

		
		// if new detail
		long count = landInfoService.save(landDagDetails);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("landdagtable","/landinfo/landdag/" + landDagDetails.getXland());
		responseHelper.setSuccessStatusAndMessage("Land Dag Details saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/landdag/{xland}")
	public String reloadLandDagTable(@PathVariable String xland, Model model) {
		List<LandDagDetails> dagList = landInfoService.findByLandDagDetails(xland);
		model.addAttribute("lddlist", dagList);
		model.addAttribute("info", landInfoService.findByLandInfo(xland));
		return "pages/land/landinfo::landdagtable";
	}
	
	//delete
		@PostMapping("{xland}/landdag/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteLandDagDetails(@PathVariable String xland, @PathVariable String xrow, Model model) {
			LandDagDetails ldd = landInfoService.findlandDagDetailsByXlandAndXrow(xland, Integer.parseInt(xrow));
			if(ldd == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = landInfoService.deleteLandDagDetails(ldd);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("landdagtable", "/landinfo/landdag/" + xland);
			return responseHelper.getResponse();
		}
	
	
	// For Land Land Document

	@GetMapping("/{xland}/land/{xrow}/show")
	public String loadLandDocModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
		if ("new".equalsIgnoreCase(xrow)) {
			LandDocument lld = new LandDocument();
			lld.setXname("");
			lld.setXperson("");
			lld.setXland(xland);
			lld.setXsurveyor("");
			model.addAttribute("lld", lld);
			lld.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
			lld.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes",xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		} else {
			LandDocument lld = landDocumentService.findLandLandDocumentByLandAndXrow(xland,Integer.parseInt(xrow));
			if (lld == null) {
				lld = new LandDocument();
				lld.setXperson("");
				lld.setXsurveyor("");
				lld.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
				lld.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
				
			}
			model.addAttribute("lld", lld);
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		}

		return "pages/land/landdocumentmodal::landdocumentmodal";
	}

	@PostMapping("/landdoc/save")
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
		LandDocument existLand = landDocumentService.findLandLandDocumentByLandAndXrow(landDocument.getXland(), landDocument.getXrow());
		if (existLand != null) {
			BeanUtils.copyProperties(landDocument, existLand, "xdoc");
			long count = landDocumentService.update(existLand);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("landdocumenttable", "/landinfo/land/" + existLand.getXland());
			responseHelper.setSuccessStatusAndMessage("Land Document updated successfully");
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
		responseHelper.setReloadSectionIdWithUrl("landdocumenttable","/landinfo/land/" + landDocument.getXland());
		responseHelper.setSuccessStatusAndMessage("land Document saved successfully");
		return responseHelper.getResponse();

	}

	@GetMapping("/land/{xland}")
	public String reloadLandDocTable(@PathVariable String xland, Model model) {
		List<LandDocument> LandDocList = landDocumentService.findByAllLandDocument(xland);
		model.addAttribute("lldlist", LandDocList);
		model.addAttribute("info", landInfoService.findByLandInfo(xland));
		return "pages/land/landinfo::landdocumenttable";
	}
	//Delete
	@PostMapping("{xland}/land/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteLandDocDetail(@PathVariable String xland, @PathVariable String xrow, Model model) {
		LandDocument lpe = landDocumentService.findLandLandDocumentByLandAndXrow(xland, Integer.parseInt(xrow));
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
		responseHelper.setReloadSectionIdWithUrl("landdocumenttable", "/landinfo/land/" + xland);
		return responseHelper.getResponse();
	}
	
	//start of Land Events
	
		@GetMapping("/{xland}/landevents/{xrow}/show")
		public String loadLandEventsModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
			if("new".equalsIgnoreCase(xrow)) {
				LandEvents le = new LandEvents();
				le.setXland(xland);
				le.setXnote("");
				le.setXperson("");
				le.setXplace("");
				model.addAttribute("le", le);
				model.addAttribute("priorityTypes", xcodesService.findByXtype(CodeType.PRIORITY_TYPE.getCode(), Boolean.TRUE));
				
			}
			else {
				LandEvents le = landInfoService.findLandEventsByXlandAndXrow(xland, Integer.parseInt(xrow));
				if(le==null) {
					le = new LandEvents();
					
				}
				model.addAttribute("le", le);
				model.addAttribute("priorityTypes", xcodesService.findByXtype(CodeType.PRIORITY_TYPE.getCode(), Boolean.TRUE));
			}
			
			return "pages/land/landeventsmodal::landeventsmodal";
		}


		@PostMapping("/landevents/save")
		public @ResponseBody Map<String, Object> saveLandEvents(LandEvents landEvents) {
			if (landEvents == null || StringUtils.isBlank(landEvents.getXland())) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			// if existing
			LandEvents exist = landInfoService.findLandEventsByXlandAndXrow(landEvents.getXland(), landEvents.getXrow());
			if (exist != null) {
				BeanUtils.copyProperties(landEvents, exist,"xland");
				long count = landInfoService.update(exist);
				if (count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
				responseHelper.setReloadSectionIdWithUrl("landeventstable","/landinfo/landevents/" + landEvents.getXland());
				responseHelper.setSuccessStatusAndMessage("Activity Details updated successfully");
				return responseHelper.getResponse();
			}

			
			// if new detail
			long count = landInfoService.save(landEvents);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("landeventstable","/landinfo/landevents/" + landEvents.getXland());
			responseHelper.setSuccessStatusAndMessage("Activity Details saved successfully");
			return responseHelper.getResponse();
		}

		@GetMapping("/landevents/{xland}")
		public String reloadLandEventsTable(@PathVariable String xland, Model model) {
			List<LandEvents> activityList = landInfoService.findByLandEvents(xland);
			model.addAttribute("lelist", activityList);
			model.addAttribute("info", landInfoService.findByLandInfo(xland));
			return "pages/land/landinfo::landeventstable";
		}
		
		//Delete
		@PostMapping("{xland}/landevents/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteLandEvents(@PathVariable String xland, @PathVariable String xrow, Model model) {
			LandEvents le = landInfoService.findLandEventsByXlandAndXrow(xland, Integer.parseInt(xrow));
			if (le == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = landInfoService.deleteLandEvents(le);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("landeventstable", "/landinfo/landevents/" + xland);
			return responseHelper.getResponse();
		}
		
	@GetMapping("/{xland}/survey/{xrow}/show")
	public String loadLandSurveyModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			LandSurvey lls = new LandSurvey();
			lls.setXland(xland);
			lls.setXsurveyor("");
			lls.setXnote("");
			lls.setXstatus("Open");
			lls.setXlandqtydsg(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			lls.setXlandqtyksg(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("lls", lls);
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		}
		else {
			LandSurvey lls = landSurveyService.findLandSurveydetailByXlandAndXrow(xland, Integer.parseInt(xrow));
			if(lls==null) {
				lls = new LandSurvey();
			}
			model.addAttribute("lls", lls);
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
			
		}
		
		return "pages/land/surveymodal::surveymodal";
	}
	
	@PostMapping("/survey/save")
	public @ResponseBody Map<String, Object> saveLandSurveyDetails(LandSurvey landSurvey) {
		if (landSurvey == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		landSurvey.setXstatus("Open");
		if(StringUtils.isEmpty(landSurvey.getXsurveyor())) {
			responseHelper.setErrorStatusAndMessage("Please Select your Surveyor ID");
			return responseHelper.getResponse();
		}

		// if existing
		LandSurvey exist = landSurveyService.findLandSurveydetailByXlandAndXrow(landSurvey.getXland(), landSurvey.getXrow());
		if (exist != null) {
			BeanUtils.copyProperties(landSurvey, exist);
			long count = landSurveyService.update(landSurvey);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("surveytable","/landinfo/survey/" + exist.getXland());
			responseHelper.setSuccessStatusAndMessage("Survey Details updated successfully");
			return responseHelper.getResponse();
		}

		
		// if new detail
		long count = landSurveyService.save(landSurvey);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("surveytable","/landinfo/survey/" + landSurvey.getXland());
		responseHelper.setSuccessStatusAndMessage("Survey Details saved successfully");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/survey/{xland}")
	public String reloadLandSurveyTable(@PathVariable String xland, Model model) {
		List<LandSurvey> LandSurveyList = landSurveyService.findByXlandSurvey(xland);
		model.addAttribute("llslist", LandSurveyList);
		model.addAttribute("info", landInfoService.findByLandInfo(xland));
		return "pages/land/landinfo::surveytable";
	}
	
	@PostMapping("{xland}/survey/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteLandSurveyDetail(@PathVariable String xland, @PathVariable String xrow, Model model) {
		LandSurvey lls = landSurveyService.findLandSurveydetailByXlandAndXrow(xland, Integer.parseInt(xrow));
		if (lls == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landSurveyService.deleteDetail(lls);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Survey Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("surveytable", "/landinfo/survey/" + xland);
		return responseHelper.getResponse();
	}
	
	@PostMapping("{xland}/survey/{xrow}/confirm")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xland,@PathVariable String xrow, Model model){
		LandSurvey lls = landSurveyService.findLandSurveydetailByXlandAndXrow(xland, Integer.parseInt(xrow));
		if(lls == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if("Confirmed".equalsIgnoreCase(lls.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Already confirmed");
			return responseHelper.getResponse();
		}
		
		//lls.setXstatus("Confirmed");
		long count= landSurveyService.updatesurveyinfo(lls.getXland(), Integer.parseInt(xrow));
		//long countt= landSurveyService.update(lls);
		long countc= landSurveyService.updatesurveystatusconfirmed(lls.getXland(), Integer.parseInt(xrow));
		long counto= landSurveyService.updatesurveystatusopen(lls.getXland(), Integer.parseInt(xrow));
		/*
		 * if (countt == 0) { responseHelper.setStatus(ResponseStatus.ERROR); return
		 * responseHelper.getResponse(); }
		 */
		 
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if (countc == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if (counto == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		

		responseHelper.setSuccessStatusAndMessage("Survey confirmed successfully");
		responseHelper.setReloadSectionIdWithUrl("surveytable", "/landinfo/survey/" + xland);
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{xland}")
	public ResponseEntity<byte[]> printChalan(@PathVariable String xland, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		//landS
		LandInfo landInfo = landInfoService.findByLandInfo(xland);
		if(landInfo == null) {
			message = "Land info not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Cacus cacus = cacusService.findByXcus(landInfo.getXcus());
		//LandSurveyor surName = landSurveyorService.findByLandSurveyor(landInfo.getXsurveyor());

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		Zbusiness zb = sessionManager.getZbusiness();

		LandInfoReport report = new LandInfoReport();
		report.setBusinessName(zb.getZorg());
		report.setBusinessAddress(zb.getXmadd());
		report.setReportName("Land Info Report : " + xland);
		report.setPrintDate(sdf.format(new Date()));
		report.setFromDate(sdf.format(new Date()));
		report.setXmemname(landInfo.getXmemname());
		report.setXcus(landInfo.getXcus());
		report.setReportLogo(sessionManager.getZbusiness().getXbimage());
		report.setXtotded(landInfo.getXtotded());
		report.setXtotrem(landInfo.getXtotrem());
		report.setXlandqtyd(landInfo.getXlandqtyd());
		report.setXlandqtyk(landInfo.getXlandqtyk());
		report.setXnote2(landInfo.getXnote2());
		report.setXnote(landInfo.getXnote());
		report.setXlandparent(landInfo.getXlandparent());
		report.setXtotdedprice(landInfo.getXtotdedprice());
		report.setXamtar(landInfo.getXamtar());
		report.setXamtrv(landInfo.getXamtrv());
		report.setXlandqtydsg(landInfo.getXlandqtydsg());
		report.setXlandqtydsn(landInfo.getXlandqtydsn());
		report.setXsurveyor(landInfo.getXsurveyor());
		report.setSurveyorName(landInfo.getXsername());
		//report.setXdatesrv(sdf.format(landInfo.getXdatesrv()));
		//report.setBalance(landInfo.getXamtar().subtract(landInfo.getXamtrv()));
		//report.setGrossDev(landInfo.getXlandqtyd().subtract(landInfo.getXlandqtydsg()));
		//report.setNetDev(landInfo.getXtotrem().subtract(landInfo.getXlandqtydsn()));
		if(landInfo.getXdatesrv()==null) {
			report.setXdatesrv("");
		}
		else if(landInfo.getXdatesrv()!=null) {
			report.setXdatesrv(sdf.format(landInfo.getXdatesrv()));
		}
		BeanUtils.copyProperties(landInfo, report);


		List<LandOwner> owners = landInfoService.findByLandOwner(xland);
		if(owners != null && !owners.isEmpty()) {
			for(LandOwner owner : owners) {
				if(StringUtils.isBlank(owner.getXperson())) continue;
				LandPerson lp = landPersonService.findByLandPerson(owner.getXperson());
				if(lp != null) {
					owner.setXperson(owner.getXperson() + " - " + lp.getXname());
				}
			}
			report.setOwners(owners);
		}

		List<LandDagDetails> dagList = landInfoService.findByLandDagDetails(xland);
		if(dagList != null && !dagList.isEmpty()) report.setDags(dagList);

		List<LandSurvey> surveyList = landSurveyService.findByXlandSurvey(xland);
		if(surveyList != null && !surveyList.isEmpty()) {
			for(LandSurvey ls : surveyList) {
				if(StringUtils.isBlank(ls.getXsurveyor())) continue;
				LandSurveyor lsy = landSurveyorService.findByLandSurveyor(ls.getXsurveyor());
				if(lsy != null) {
			 		ls.setXsurveyor(ls.getXsurveyor() + " - " + lsy.getXname());
			 		ls.setFormatedDate(sdf.format(ls.getXdate()));
				}
			}
			report.setSurveys(surveyList);
		}

		List<LandDocument> documents = landDocumentService.findByAllLandDocument(xland);
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
		List<LandEvents> activities = landInfoService.findByLandEvents(xland);
		if(activities != null && !activities.isEmpty()) {
			for(LandEvents activity : activities) {
				if(StringUtils.isBlank(activity.getXperson())) continue;
				LandPerson lp = landPersonService.findByLandPerson(activity.getXperson());
				
				if(lp != null) {
					activity.setXperson(activity.getXperson() + " - " + lp.getXname());
					activity.setFormatedDate(sdf.format(activity.getXdate()));
				}
			}
			report.setActivities(activities);
		}

		byte[] byt = getPDFByte(report, "landinforeport.xsl", request);
		if(byt == null) {
			message = "Can't generate pdf for Land : " + xland;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}
	
	@GetMapping("/cadag/{xdagtype}")
	public @ResponseBody List<Cadag> getLandInfo(@PathVariable String xdagtype, Model model) {
		List<Cadag> dag= landDagMasterService.getDagNo(xdagtype);
		model.addAttribute("finddagnum", dag);
		return dag;
	}
	
}

