package com.asl.controller;

import java.util.Map;

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

import com.asl.entity.LandOwner;
import com.asl.entity.Cadag;
import com.asl.entity.LandInfo;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.LandDagMasterService;
import com.asl.service.LandOwnerService;
@Controller
@RequestMapping("/cadag")
public class LandDagMasterController  extends ASLAbstractController{

	@Autowired private LandDagMasterService landDagMasterService;
	@GetMapping
	public String loadLandDagMasterPage(Model model) {
		Cadag dagMaster = new Cadag();
		model.addAttribute("dag", dagMaster);
		model.addAttribute("allDags", landDagMasterService.getAllLandDagMaster());
		model.addAttribute("dagTypes", xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/cadag";
	}
	
	@GetMapping("/{xdagid}")
	public String loadDagMasterPage( @PathVariable int xdagid, Model model) {
		Cadag dagMaster = landDagMasterService.findbyXdagid(xdagid);
		if (dagMaster == null)
			return "redirect:/cadag";

		model.addAttribute("dag", dagMaster);
		model.addAttribute("allDags", landDagMasterService.getAllLandDagMaster());
		model.addAttribute("dagTypes", xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/cadag";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cadag cadag,BindingResult bindingResult){
		if(cadag == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		if(cadag.getXdagid() != 0){
			Cadag existingCacus = landDagMasterService.findbyXdagid(cadag.getXdagid());
			BeanUtils.copyProperties(cadag, existingCacus, "xdagtype","xdagnum","xdagid");
			long count = landDagMasterService.updateLandDagMaster(existingCacus);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update member info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Data updated successfully");
			responseHelper.setRedirectUrl("/cadag/" + existingCacus.getXdagid());
			return responseHelper.getResponse();
		}

		
		// If new
		if(landDagMasterService.findByXdagnumAndXdagtype(cadag.getXdagtype(), cadag.getXdagnum()) != null) {
			responseHelper.setErrorStatusAndMessage("Dag Type " + cadag.getXdagtype() + " with Dag Number " + cadag.getXdagnum() + " data already exist in this system");
			return responseHelper.getResponse();
		}
		long count = landDagMasterService.saveLandDagMaster(cadag);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setRedirectUrl("/cadag/" + cadag.getXdagid() );
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xdagid}")
	public @ResponseBody Map<String, Object> deleteLandInfo(@PathVariable int xdagid,  Model model) {
		Cadag li = landDagMasterService.findbyXdagid(xdagid);
		if(li == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landDagMasterService.deleteLandDagMaster(li);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/cadag/" + xdagid );
		return responseHelper.getResponse();
}
}