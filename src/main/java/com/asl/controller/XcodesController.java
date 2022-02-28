package com.asl.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.DataList;
import com.asl.entity.Xcodes;
import com.asl.enums.ResponseStatus;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Controller
@RequestMapping("/mastersetup/xcodes")
public class XcodesController extends ASLAbstractController {

	@Autowired private XcodesService xcodesService;

	@GetMapping
	public String loadXtrnPage(@RequestParam(required = false) String xtype, @RequestParam(required = false) String xcode, Model model) {
		Xcodes xcodes = new Xcodes();
		if(StringUtils.isNotBlank(xtype) && StringUtils.isNotBlank(xcode)) {
			xcodes = xcodesService.findByXtypesAndXcodes(xtype, xcode);
		}
		model.addAttribute("xcodes", xcodes);
		model.addAttribute("xcodesList", xcodesService.getAllXcodes());
		List<DataList> lsit = listService.findDataListByListcode("CODES");
		if(lsit != null) lsit.sort(Comparator.comparing(DataList::getListvalue1));
		model.addAttribute("codetypes", lsit == null ? Collections.emptyList() : lsit);
		model.addAttribute("parentTypes", xcodesService.getAllParents());
		
		return "pages/mastersetup/xcodes/xcodes";
	}

//	@GetMapping("/{xtype}/{xcode}")
//	public String loadXtrnPage(@PathVariable String xtype, @PathVariable String xcode, Model model) {
//		Xcodes x = xcodesService.findByXtypesAndXcodes(xtype, xcode);
//		if(x == null) return "redirect:/mastersetup/xcodes";
//
//		model.addAttribute("xcodes", x);
//		model.addAttribute("xcodesList", xcodesService.getAllXcodes());
//		List<DataList> lsit = listService.findDataListByListcode("CODES");
//		model.addAttribute("codetypes", lsit == null ? Collections.emptyList() : lsit);
//		return "pages/mastersetup/xcodes/xcodes";
//	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Xcodes xcodes, BindingResult bindingResult){
		if(xcodes == null || StringUtils.isBlank(xcodes.getXtype()) || StringUtils.isBlank(xcodes.getXcode())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Modify xtrn code
		xcodes.setXcode(xcodes.getXcode().trim());
		xcodes.setXlong(xcodes.getXlong());

		// Validate Xtrn

		// if existing record
		Xcodes existXcodes = xcodesService.findByXtypesAndXcodes(xcodes.getXtype(), xcodes.getXcode());
		if(existXcodes != null) {
			BeanUtils.copyProperties(xcodes, existXcodes, "xtype", "xcode");
			long count = xcodesService.update(existXcodes);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Item code updated successfully");
			responseHelper.setRedirectUrl("/mastersetup/xcodes?xtype=" + xcodes.getXtype() + "&xcode=" + xcodes.getXcode());
			return responseHelper.getResponse();
		}

		// If new xtrn
		long count = xcodesService.save(xcodes);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Item code saved successfully");
		responseHelper.setRedirectUrl("/mastersetup/xcodes?xtype=" + xcodes.getXtype() + "&xcode=" + xcodes.getXcode());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive")
	public @ResponseBody Map<String, Object> archive(@RequestParam String xtype, @RequestParam String xcode){
		return doArchiveOrRestore(xtype, xcode, true);
	}

	public Map<String, Object> doArchiveOrRestore(String xtype, String xcode, boolean archive){

		Xcodes xcodes = xcodesService.findByXtypesAndXcodes(xtype, xcode);
		if(xcodes == null) {
			responseHelper.setErrorStatusAndMessage("Code not found in this system");
			return responseHelper.getResponse();
		}

		// delete
		long count = xcodesService.deleteXcodes(xcode, xtype);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete this code");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Item code deleted successfully");
		responseHelper.setRedirectUrl("/mastersetup/xcodes/");
		return responseHelper.getResponse();
	}
}
