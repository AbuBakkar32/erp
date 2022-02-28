package com.asl.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/posbusiness")
public class POSBusinessController extends ASLAbstractController {
	@Autowired private ZbusinessService zbusinessService;
	
	@GetMapping
	public String loadPOSBusinessPage(Model model) {
		
		Zbusiness zbusiness = new Zbusiness();
		zbusiness.setXdine(ProfileType.C);
		model.addAttribute("business", zbusiness);
		model.addAttribute("allbusiness", zbusinessService.getAllBranchBusiness());
		model.addAttribute("allBranchBusiness", zbusinessService.getAllBranchBusiness());

		return"pages/posbusiness";
	}
	
	@GetMapping("/{zid}")
	public String loadbusinessPage(@PathVariable String zid, Model model) {
		Zbusiness zb = zbusinessService.findBById(zid);
		if(zb == null) return "redirect:/posbusiness";
		model.addAttribute("business", zb);
		model.addAttribute("allbusiness", zbusinessService.getAllBranchBusiness());
		model.addAttribute("allBranchBusiness", zbusinessService.getAllBranchBusiness());
		
		return "pages/posbusiness";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Zbusiness zbusiness, @RequestParam("files[]") MultipartFile[] files) throws IOException{
		if(zbusiness == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		} 
		
		if(StringUtils.isBlank(zbusiness.getZid())) {
			responseHelper.setErrorStatusAndMessage("Business ID required");
			return responseHelper.getResponse();
			
		}
		
		if (files != null && files.length>0) {

			// Rename the file
			String extension = null;
			int j = files[0].getOriginalFilename().lastIndexOf('.');
			if (j > 0) {
				extension = files[0].getOriginalFilename().substring(j + 1);
			}
			
			// Upload file into server
			byte[] data;
			data = files[0].getBytes();
			zbusiness.setXimage(data);
			System.out.println("The file name is:" +data);
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
			responseHelper.setRedirectUrl("/posbusiness/" + zb.getZid());
			return responseHelper.getResponse();
		}
		 
		// If new
		long count = zbusinessService.save(zbusiness);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setRedirectUrl("/pages/posbusiness");
		return responseHelper.getResponse();
		
	}


}
