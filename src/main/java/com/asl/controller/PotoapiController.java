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

import com.asl.entity.Potoapi;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.PotoapiService;

@Controller
@RequestMapping("/system/potoapi")
public class PotoapiController extends ASLAbstractController{
	@Autowired private PotoapiService potoapiService;
	@GetMapping
	public String loadPotoapiControllerPage(Model model) {
		Potoapi potoapi = new Potoapi();
		potoapi.setNewData(true);
		model.addAttribute("potoapi", potoapi);
		model.addAttribute("allPotoapi", potoapiService.getAllPotoapi());
		model.addAttribute("supGroup", xcodesService.findByXtype(CodeType.SUPPLIER_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("itemGroup", xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		return "pages/system/potoapi";
	}
	
	@GetMapping("/{xtype}/{xgsup}/{xgitem}")
	public String loadPotoapiPage(@PathVariable String xtype, @PathVariable String xgsup,@PathVariable String xgitem, Model model) {
		Potoapi potoapi = potoapiService.findByXtypeXgsupAndXgitem(xtype, xgsup,xgitem);
		if (potoapi == null)
			return "redirect:/system/potoapi";

		model.addAttribute("potoapi", potoapi);
		model.addAttribute("allPotoapi", potoapiService.getAllPotoapi());
		model.addAttribute("supGroup", xcodesService.findByXtype(CodeType.SUPPLIER_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("itemGroup", xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		return "pages/system/potoapi";
	}
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Potoapi potoapi, BindingResult bindingResult) {
		if (potoapi == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(potoapi.getXaccdr())) {
			responseHelper.setErrorStatusAndMessage("Debit Account Required");
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(potoapi.getXacccr())) {
			responseHelper.setErrorStatusAndMessage("Credit Account Required");
			return responseHelper.getResponse();
		}
		
		Potoapi exist = potoapiService.findByXtypeXgsupAndXgitem(potoapi.getXtype(), potoapi.getXgsup(),potoapi.getXgitem());

		// if new data
		if(potoapi.isNewData()) {
			if(exist != null) {
				responseHelper.setErrorStatusAndMessage("Type '" + potoapi.getXtype() + " ' with Supplier Group '" + potoapi.getXgsup()+ " ' and Item Group '" + potoapi.getXgitem() + "' data already exist in this system");
				return responseHelper.getResponse();
			}

			long count = potoapiService.savePotoapi(potoapi);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save data");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Data saved successfully");
			responseHelper.setRedirectUrl("/system/potoapi/" + potoapi.getXtype()  + "/" + potoapi.getXgsup() + "/" +potoapi.getXgitem());
			return responseHelper.getResponse();
		}

		// if existing data
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find data in this system");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(potoapi, exist, "xtype", "xgsup","xgitem");
		long count = potoapiService.updatePotoapi(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update data");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Data updated successfully");
		responseHelper.setRedirectUrl("/system/potoapi/" + potoapi.getXtype()  + "/" + potoapi.getXgsup() + "/" +potoapi.getXgitem());
		return responseHelper.getResponse();
	}

	@PostMapping("/delete/{xtype}/{xgsup}/{xgitem}")
	public @ResponseBody Map<String, Object> deletePotoapiInfo(@PathVariable String xtype,@PathVariable String xgsup, @PathVariable String xgitem,   Model model) {
		Potoapi potoapi = potoapiService.findByXtypeXgsupAndXgitem(xtype,xgsup,xgitem);
		if(potoapi == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = potoapiService.deletePotoapi(potoapi);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/system/potoapi/" + xtype  + "/" + xgsup + "/" + xgitem);
		return responseHelper.getResponse();
}

}
