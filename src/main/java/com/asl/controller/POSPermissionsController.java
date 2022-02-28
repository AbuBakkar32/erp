package com.asl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.POSPermissions;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.POSPermissionsService;


@Controller
@RequestMapping("/permissions")
public class POSPermissionsController extends ASLAbstractController{

	@Autowired private POSPermissionsService posPermissionsService;

	@GetMapping
	public String loadPermissionsPage(Model model) {
		model.addAttribute("selectedRole", "");
		model.addAttribute("roleTypes", xcodesService.findByXtype(CodeType.ROLE_TYPE.getCode(), Boolean.TRUE));
		return"pages/permissions";
	}
	
	@GetMapping("/reloadform/{xrole}")
	public String reloadForm(@PathVariable String xrole, Model model) { 
		formDefaultValues(xrole, model);
		model.addAttribute("selectedRole", xrole);
		model.addAttribute("roleTypes", xcodesService.findByXtype(CodeType.ROLE_TYPE.getCode(), Boolean.TRUE));
		return "pages/permissions::permissionsform";
	}

	@PostMapping("/loadformbyxrole/{xrole}")
	public @ResponseBody Map<String, Object> reloadForm2(@PathVariable String xrole, Model model) { 
		responseHelper.setReloadSectionIdWithUrl("permissionsform", "/permissions/reloadform/" + xrole);
		responseHelper.setSuccessStatusAndMessage("Load successfully");
		return responseHelper.getResponse();
	}
	
	private void formDefaultValues(String xrole, Model model) {
		POSPermissions posSign = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Reopen POS Sign In", xrole);
		model.addAttribute("posSign", posSign != null ? posSign.getZactive() : false);
		
		POSPermissions saleOnPOS = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Sale on POS", xrole);
		model.addAttribute("saleOnPOS", saleOnPOS != null ? saleOnPOS.getZactive() : false);
		
		POSPermissions giftComplimentary = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Gift on Complimentary", xrole);
		model.addAttribute("giftComplimentary", giftComplimentary != null ? giftComplimentary.getZactive() : false);
		
		POSPermissions discount = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Discount", xrole);
		model.addAttribute("discount", discount != null ? discount.getZactive() : false);
		
		POSPermissions billsView = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Bills view", xrole);
		model.addAttribute("billsView", billsView != null ? billsView.getZactive() : false);
		
		POSPermissions billCancellation = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Bill cancellation", xrole);
		model.addAttribute("billCancellation", billCancellation != null ? billCancellation.getZactive() : false);
		
		POSPermissions printBill = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Print bill", xrole);
		model.addAttribute("printBill", printBill != null ? printBill.getZactive() : false);
		
		POSPermissions reportsView = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Reports View", xrole);
		model.addAttribute("reportsView", reportsView != null ? reportsView.getZactive() : false);
		
		POSPermissions settings = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Settings", xrole);
		model.addAttribute("settings", settings != null ? settings.getZactive() : false);
		
		POSPermissions checkOnItem = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Check On Item", xrole);
		model.addAttribute("checkOnItem", checkOnItem != null ? checkOnItem.getZactive() : false);
		
		POSPermissions permissionsModify = posPermissionsService.findByPOSPermissionsByXaccAndXrole("Permissions modify", xrole);
		model.addAttribute("permissionsModify", permissionsModify != null ? permissionsModify.getZactive() : false);
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(String name, boolean status, String xrole){
		
		System.out.println(name + " - " + status + " - " + xrole);
		
		POSPermissions ps = new POSPermissions();
		ps.setXacc(name);
		ps.setZactive(status);
		ps.setXrole(xrole);
		
		POSPermissions exps = posPermissionsService.findByPOSPermissionsByXaccAndXrole(name, xrole);
		if(exps == null) {
			long count = posPermissionsService.save(ps);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
		} else {
			long count = posPermissionsService.update(ps);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setReloadSectionIdWithUrl("permissionsform", "/permissions/reloadform/" + xrole);
		return responseHelper.getResponse();
				
	}
}
