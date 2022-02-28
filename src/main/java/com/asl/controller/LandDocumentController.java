package com.asl.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandDocumentService;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/landdocument")
public class LandDocumentController extends ASLAbstractController{
	
	@Autowired private LandDocumentService landDocumentService;
	
	@GetMapping
	public String loadLandDocumentPage(Model model){
		model.addAttribute("document", getDefaultLandDocument());
		model.addAttribute("allDocument", landDocumentService.getAllLandDocument());
		model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		return "pages/land/landdocument";
	}

	private LandDocument getDefaultLandDocument() {
		LandDocument ld  = new LandDocument();
		ld.setXname("Certificate");
		ld.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
		ld.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
		return ld;
	}
	
	@GetMapping("/{xdoc}")
	public String loadRolePage(@PathVariable String xdoc, Model model) {
		LandDocument landDocument = landDocumentService.findByLandDocument(xdoc);
		if (landDocument == null) return "redirect:/landdocument";

		model.addAttribute("document", getDefaultLandDocument());
		model.addAttribute("allDocument", landDocumentService.getAllLandDocument());
		model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		return "pages/land/landdocument";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandDocument obj, @RequestParam("files[]") MultipartFile[] files, BindingResult bindingResult) {
		if (obj == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		if(files != null && files.length > 0) {
//			System.out.println(files[0].getOriginalFilename());
			
			//Rename the file 
			String extension = null;
			int j = files[0].getOriginalFilename().lastIndexOf('.');
			if (j > 0) {
				extension = files[0].getOriginalFilename().substring(j + 1);
			}
			/* String OriginalFileName = files[0].getOriginalFilename(); */
//			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			String fileName = UUID.randomUUID()+ "." + extension;
			log.debug("File name is now: {}", fileName);

			try {
				//create a directory if not exist
				String uploadPath = "D://Bosila//Document";
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}
				//Upload file into server
				Files.copy(files[0].getInputStream(), Paths.get(uploadPath, fileName));
				obj.setXdocument(uploadPath+"/"+fileName);
		}catch (IOException e) {
			e.printStackTrace();
		}
			}
		
		// Validation
		if(StringUtils.isBlank(obj.getXname())) {
			responseHelper.setErrorStatusAndMessage("Please Enter Your Document Name");
			return responseHelper.getResponse();
		}

	
		// if existing
		if(StringUtils.isNotBlank(obj.getXdoc())) {
			LandDocument exist = landDocumentService.findByLandDocument(obj.getXdoc());
			
			BeanUtils.copyProperties(obj, exist, "xtypetrn","xtrn");
			long count = landDocumentService.update(exist);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Document info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Document info updated successfully");
			responseHelper.setRedirectUrl("/landdocument/" + exist.getXdoc());
			return responseHelper.getResponse();
		}
	
		String xtrn =  xtrnService.generateAndGetXtrnNumber(obj.getXtypetrn(), obj.getXtrn(), 6);
		obj.setXdoc(xtrn);
		
		long count = landDocumentService.save(obj);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save Document info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Document info saved successfully");
		responseHelper.setRedirectUrl("/landdocument/" + obj.getXdoc());
		return responseHelper.getResponse();
	}
	
}
