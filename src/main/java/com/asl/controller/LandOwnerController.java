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

import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.LandOwnerService;


@Controller
@RequestMapping("/landowner")
public class LandOwnerController extends ASLAbstractController {

	@Autowired
	private LandOwnerService landOwnerService;

	@GetMapping
	public String loadLandOwnerPage(Model model) {
		LandOwner landOwner = new LandOwner();
		landOwner.setNewData(true);
		model.addAttribute("owner", landOwner);
		model.addAttribute("allOwners", landOwnerService.getAllLandOwner());
		model.addAttribute("ownerTypes", xcodesService.findByXtype(CodeType.OWNER_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/landowner";
	}

	@GetMapping("/{xland}/{xperson}")
	public String loadInfoPage(@PathVariable String xland, @PathVariable String xperson, Model model) {
		LandOwner landOwner = landOwnerService.findByXlandAndXperson(xland, xperson);
		if (landOwner == null)
			return "redirect:/landowner";

		model.addAttribute("owner", landOwner);
		model.addAttribute("allOwners", landOwnerService.getAllLandOwner());
		model.addAttribute("ownerTypes", xcodesService.findByXtype(CodeType.OWNER_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/landowner";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandOwner landOwner, BindingResult bindingResult) {
		if (landOwner == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		LandOwner exist = landOwnerService.findByXlandAndXperson(landOwner.getXland(), landOwner.getXperson());

		// if new data
		if(landOwner.isNewData()) {
			if(exist != null) {
				responseHelper.setErrorStatusAndMessage("Land " + landOwner.getXland() + " with person " + landOwner.getXperson() + " data already exist in this system");
				return responseHelper.getResponse();
			}

			long count = landOwnerService.save(landOwner);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save data");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Owner Data saved successfully");
			responseHelper.setRedirectUrl("/landowner/" + landOwner.getXland()  + "/" + landOwner.getXperson());
			return responseHelper.getResponse();
		}

		// if existing data
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find data in this system");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(landOwner, exist);
		long count = landOwnerService.update(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update owner data");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Owner Data updated successfully");
		responseHelper.setRedirectUrl("/landowner/" + landOwner.getXland()  + "/" + landOwner.getXperson());
		return responseHelper.getResponse();
	}
	

	@PostMapping("/archive/{xland}/{xperson}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xland, @PathVariable String xperson){
		return doArchiveOrRestore(xland,xperson, true);
	}

	@PostMapping("/restore/{xland}/{xperson}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xland, @PathVariable String xperson){
		return doArchiveOrRestore(xland,xperson, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xland,String xperson, boolean archive){
		LandOwner lp = landOwnerService.findByXlandAndXperson(xland,xperson);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lp.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landOwnerService.update(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Land Owner Information updated successfully");
		responseHelper.setRedirectUrl("/landowner/" + lp.getXland() +'/'+ lp.getXperson());
		return responseHelper.getResponse();
	}

}
