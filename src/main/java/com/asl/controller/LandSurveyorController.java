package com.asl.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.asl.entity.LandDocument;
import com.asl.entity.LandSurveyor;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandDocumentService;
import com.asl.service.LandSurveyorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/landsurveyor")
public class LandSurveyorController extends ASLAbstractController{

	@Autowired 
	private LandSurveyorService landSurveyorService;
	@Autowired
	private LandDocumentService landDocumentService;
	
	@GetMapping
	public String loadLandSurveyorPage(Model model) {
		
		model.addAttribute("surveyor", getDefaultLandsurveyor());
		model.addAttribute("allSurveyors", landSurveyorService.getAllLandSurveyor());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.SURVEYOR_ID.getCode(), Boolean.TRUE));
		return "pages/land/landsurveyor";
	}
	
	private LandSurveyor getDefaultLandsurveyor() {
		LandSurveyor ls  = new LandSurveyor();
		ls.setXtypetrn(TransactionCodeType.SURVEYOR_ID.getCode());
		ls.setXtrn(TransactionCodeType.SURVEYOR_ID.getdefaultCode());
		return ls;
	}
	
	@GetMapping("/{xsurveyor}")
	public String loadSurveyorPage(@PathVariable String xsurveyor, Model model) {
		LandSurveyor landSurveyor = landSurveyorService.findByLandSurveyor(xsurveyor);
		if (landSurveyor == null) return "redirect:/landsurveyor";
		
		model.addAttribute("surveyor", landSurveyor);
		model.addAttribute("allSurveyors", landSurveyorService.getAllLandSurveyor());
		model.addAttribute("lsdlist", landDocumentService.findByLandSurveyorDocument(xsurveyor));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.SURVEYOR_ID.getCode(), Boolean.TRUE));
		return "pages/land/landsurveyor";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandSurveyor landSurveyor, BindingResult bindingResult) {
		if (landSurveyor == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
		// if existing
		if(StringUtils.isNotBlank(landSurveyor.getXsurveyor())) {
			LandSurveyor xlp = landSurveyorService.findByLandSurveyor(landSurveyor.getXsurveyor());
			BeanUtils.copyProperties(landSurveyor, xlp,"xtypetrn","xtrn");
			long count = landSurveyorService.update(xlp);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update surveyor info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Surveyor info updated successfully");
			responseHelper.setRedirectUrl("/landsurveyor/" + xlp.getXsurveyor());
			return responseHelper.getResponse();
		}
		// if new
		long count = landSurveyorService.save(landSurveyor);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save surveyor info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Surveyor info saved successfully");
		responseHelper.setRedirectUrl("/landsurveyor/" + landSurveyor.getXsurveyor());
			return responseHelper.getResponse();
		}
	
	@PostMapping("/archive/{xsurveyor}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xsurveyor){
		return doArchiveOrRestore(xsurveyor, true);
	}

	@PostMapping("/restore/{xsurveyor}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xsurveyor){
		return doArchiveOrRestore(xsurveyor, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xsurveyor, boolean archive){
		LandSurveyor lp = landSurveyorService.findByLandSurveyor(xsurveyor);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lp.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landSurveyorService.update(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Surveyor Information updated successfully");
		responseHelper.setRedirectUrl("/landsurveyor/" + lp.getXsurveyor());
		return responseHelper.getResponse();
	}

	
	@PostMapping("/delete/{xsurveyor}")
	public @ResponseBody Map<String, Object> deleteSurveyor(@PathVariable String xsurveyor,  Model model) {
		LandSurveyor ls = landSurveyorService.findByLandSurveyor(xsurveyor);
		if(ls == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landSurveyorService.delete(ls);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/landsurveyor/" + xsurveyor );
		return responseHelper.getResponse();
	}
	// For Surveyor Land Document

		@GetMapping("/{xsurveyor}/surveyor/{xrow}/show")
		public String loadSurveyorDocModal(@PathVariable String xsurveyor, @PathVariable String xrow, Model model) {
			if ("new".equalsIgnoreCase(xrow)) {
				LandDocument lsd = new LandDocument();
				lsd.setXname("");
				lsd.setXperson("");
				lsd.setXland("");
				lsd.setXsurveyor(xsurveyor);
				model.addAttribute("lsd", lsd);
				lsd.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
				lsd.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
				model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
				model.addAttribute("prefixes",xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
			} else {
				LandDocument lsd = landDocumentService.findLandSurveyorDocumentBySurveyorAndXrow(xsurveyor,Integer.parseInt(xrow));
				if (lsd == null) {
					lsd = new LandDocument();
					lsd.setXperson("");
					lsd.setXland("");
					lsd.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
					lsd.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
				}
				model.addAttribute("lsd", lsd);
				model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
				model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
			}

			return "pages/land/Surveyordocumentmodal::Surveyordocumentmodal";
		}

		@PostMapping("/surveyordoc/save")
		public @ResponseBody Map<String, Object> saveSurveyorDoc(LandDocument landDocument, @RequestParam("files[]") MultipartFile[] files) {
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
				// System.out.println("The File Name Is: "+part1);
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
			LandDocument existSurveyor = landDocumentService.findLandSurveyorDocumentBySurveyorAndXrow(landDocument.getXsurveyor(), landDocument.getXrow());
			if (existSurveyor != null) {
				BeanUtils.copyProperties(landDocument, existSurveyor, "xdoc");
				long count = landDocumentService.update(existSurveyor);
				if (count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
				responseHelper.setReloadSectionIdWithUrl("surveyordocumenttable", "/landsurveyor/surveyor/" + existSurveyor.getXsurveyor());
				responseHelper.setSuccessStatusAndMessage("Surveyor Document updated successfully");
				return responseHelper.getResponse();
			}
			
			
			String xtrn =  xtrnService.generateAndGetXtrnNumber(landDocument.getXtypetrn(), landDocument.getXtrn(), 6);
			landDocument.setXdoc(xtrn);
			System.out.println("The Value Of Xtrn Is Now: "+xtrn);

			// if new detail
			long count = landDocumentService.save(landDocument);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("surveyordocumenttable","/landsurveyor/surveyor/" + landDocument.getXsurveyor());
			responseHelper.setSuccessStatusAndMessage("Surveyor Document saved successfully");
			return responseHelper.getResponse();

		}

		@GetMapping("/surveyor/{xsurveyor}")
		public String reloadSurveyorDocTable(@PathVariable String xsurveyor, Model model) {
			List<LandDocument> LandSurveyorDocList = landDocumentService.findByLandSurveyorDocument(xsurveyor);
			model.addAttribute("lsdlist", LandSurveyorDocList);
			model.addAttribute("surveyor", landSurveyorService.findByLandSurveyor(xsurveyor));
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes",xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
			return "pages/land/landsurveyor::surveyordocumenttable";
		}
		
		@PostMapping("{xsurveyor}/surveyor/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteSurveyorDocDetail(@PathVariable String xsurveyor, @PathVariable String xrow, Model model) {
			LandDocument lpe = landDocumentService.findLandSurveyorDocumentBySurveyorAndXrow(xsurveyor, Integer.parseInt(xrow));
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
			responseHelper.setReloadSectionIdWithUrl("surveyordocumenttable", "/landsurveyor/surveyor/" + xsurveyor);
			return responseHelper.getResponse();
		}
	
}
