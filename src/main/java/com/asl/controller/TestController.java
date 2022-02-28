package com.asl.controller;

import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.Cadag;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.LandDagMasterService;

@Controller
@RequestMapping("/dag")
public class TestController extends ASLAbstractController{
	
	@Autowired private LandDagMasterService landDagMasterService;
	@GetMapping
	public String loadLandDagMasterPage(Model model) {
		Cadag dagMaster = new Cadag();
		model.addAttribute("landdag", dagMaster);
		model.addAttribute("allDags", landDagMasterService.getAllLandDagMaster());
		model.addAttribute("dagTypes", xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/test";
	}
	
	@GetMapping("/{xdagid}")
	public String loadDagMasterPage( @PathVariable int xdagid, Model model) {
		Cadag dagMaster = landDagMasterService.findbyXdagid(xdagid);
		if (dagMaster == null)
			return "redirect:/dag";

		model.addAttribute("landdag", dagMaster);
		model.addAttribute("allDags", landDagMasterService.getAllLandDagMaster());
		model.addAttribute("dagTypes", xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/test";
	}
	

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cadag cacus,BindingResult bindingResult){
		if(cacus == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		if(cacus.getXdagid() != 0){
			Cadag existingCacus = landDagMasterService.findbyXdagid(cacus.getXdagid());
			BeanUtils.copyProperties(cacus, existingCacus, "xdagtype","xdagnum","xdagid");
			long count = landDagMasterService.updateLandDagMaster(existingCacus);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update member info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Data updated successfully");
			responseHelper.setRedirectUrl("/dag/" + existingCacus.getXdagid());
			return responseHelper.getResponse();
		}

		long cus = landDagMasterService.saveLandDagMaster(cacus);
		if(cus ==0) {
			responseHelper.setErrorStatusAndMessage("Can't save info");
			return responseHelper.getResponse();
		}
		
		
		// If new
		long count = landDagMasterService.saveLandDagMaster(cacus);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setRedirectUrl("/dag/" + cacus.getXdagid());
		return responseHelper.getResponse();
	}
}
