package com.asl.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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

import com.asl.entity.Zbusiness;
import com.asl.enums.ProfileType;
import com.asl.enums.ResponseStatus;
import com.asl.service.ZbusinessService;


import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/system/businessprof")
public class BusinessProfileController extends ASLAbstractController{

	private static final String BUSINESS_LOGO_DIR = "resources/bussinesslogo/";

	@Autowired private ZbusinessService zbusinessService;

	@GetMapping
	public String loadBusinessPage(Model model) {

		Zbusiness zbusiness = new Zbusiness();
		zbusiness.setXdine(ProfileType.C);
		model.addAttribute("business", zbusinessService.findfromZid());
		model.addAttribute("allBranchBusiness", zbusinessService.getAllBranchBusiness());

		return"pages/businessProfile/business";
	}

	@GetMapping("/{zid}")
	public String loadbusinessPrifilePage(@PathVariable String zid, Model model) {
		Zbusiness zb = zbusinessService.findBById(zid);
		if(zb == null) return "redirect:/system/businessprof";
		model.addAttribute("business", zb);
		model.addAttribute("allbusiness", zbusinessService.findfromZid());
		model.addAttribute("allBranchBusiness", zbusinessService.getAllBranchBusiness());

		return "pages/businessProfile/business";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Zbusiness zbusiness, BindingResult bindingResult, @RequestParam(value= "files[]", required = false) MultipartFile[] files, HttpServletRequest request){
		/*
		 * if(files.length > 0) { try { zbusiness.setXimage(files[0].getBytes()); }
		 * catch (IOException e) { log.error(ERROR, e.getMessage(), e);
		 * responseHelper.setStatus(ResponseStatus.ERROR); return
		 * responseHelper.getResponse(); } }
		 */
		
		//Image part
		if(files != null && files.length > 0) {
			//Rename the file 
			String extension = null;
			int j = files[0].getOriginalFilename().lastIndexOf('.');
			if (j > 0) {
				extension = files[0].getOriginalFilename().substring(j + 1);
			}
			//String recoursePath = request.getServletContext().getRealPath(BUSINESS_LOGO_DIR);
			String businessid = zbusiness.getZid();
			String fileName = businessid + "_logo." + extension;
			log.debug("File name is now: {}", fileName);

			try {
				//create a directory if not exist
				String uploadPath = request.getServletContext().getRealPath(BUSINESS_LOGO_DIR);
				File dir = new File(uploadPath);
				if(!dir.exists()) {
					dir.mkdirs();
				}

				File img = new File(uploadPath +  zbusiness.getXbimage());
				if(img != null) {
				 if(img.exists()) {
					if(!img.delete()) {
						img.delete();
					}
				}
				}
				
				File image = new File(uploadPath +  fileName);
				if(image.exists()) {
					if(!image.delete()) {
						image.delete();
					}
				}

				//Upload file into server
				Files.copy(files[0].getInputStream(), Paths.get(uploadPath, fileName));

				//set photo name into database
				zbusiness.setXbimage(fileName);
				zbusiness.setXimage(files[0].getBytes());
				zbusiness.setXresource(BUSINESS_LOGO_DIR);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(StringUtils.isBlank(zbusiness.getZid())) {
			responseHelper.setErrorStatusAndMessage("Business ID required");
			return responseHelper.getResponse();
		}
		
		

		// if existing
		if(StringUtils.isNotBlank(zbusiness.getZid())) {
			Zbusiness zb = zbusinessService.findBById(zbusiness.getZid());
			BeanUtils.copyProperties(zbusiness, zb,"zid");
			long count = zbusinessService.update(zb);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Business info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Business info updated successfully");
			responseHelper.setRedirectUrl("/system/businessprof/" + zb.getZid());
			return responseHelper.getResponse();
		}

		// If new
		long count = zbusinessService.save(zbusiness);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Business info saved successfully");
		responseHelper.setRedirectUrl("/system/businessprof");
		return responseHelper.getResponse();
	}


}
