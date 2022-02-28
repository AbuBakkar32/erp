package com.asl.controller;

import java.util.List;
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

import com.asl.entity.LandOwner;
import com.asl.entity.Optoari;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.OptoariService;

@Controller
@RequestMapping("/system/optoari")
public class OptoariController extends ASLAbstractController{
	@Autowired private OptoariService optoariService;
	@GetMapping
	public String loadOptoariControllerPage(Model model) {
		Optoari optoari = new Optoari();
		optoari.setNewData(true);
		model.addAttribute("optoari", optoari);
		model.addAttribute("allOptoari", optoariService.getAllOptoari());
		model.addAttribute("cusGroup", xcodesService.findByXtype(CodeType.CUSTOMER_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("itemGroup", xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		return "pages/system/optoari";
	}
	
	@GetMapping("/{xtype}/{xgcus}/{xgitem}")
	public String loadOptoariPage(@PathVariable String xtype, @PathVariable String xgcus,@PathVariable String xgitem, Model model) {
		Optoari optoari = optoariService.findByXtypeXgcusAndXgitem(xtype, xgcus,xgitem);
		if (optoari == null)
			return "redirect:/system/optoari";

		model.addAttribute("optoari", optoari);
		model.addAttribute("allOptoari", optoariService.getAllOptoari());
		model.addAttribute("cusGroup", xcodesService.findByXtype(CodeType.CUSTOMER_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("itemGroup", xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		return "pages/system/optoari";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Optoari optoari, BindingResult bindingResult) {
		if (optoari == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		/*
		 * if(StringUtils.isBlank(optoari.getXaccdr())) {
		 * responseHelper.setErrorStatusAndMessage("Debit Account Required"); return
		 * responseHelper.getResponse(); }
		 */
		
		if(StringUtils.isBlank(optoari.getXacccr())) {
			responseHelper.setErrorStatusAndMessage("Credit Account Required");
			return responseHelper.getResponse();
		}
		

		Optoari exist = optoariService.findByXtypeXgcusAndXgitem(optoari.getXtype(), optoari.getXgcus(),optoari.getXgitem());

		// if new data
		if(optoari.isNewData()) {
			if(exist != null) {
				responseHelper.setErrorStatusAndMessage("Type '" + optoari.getXtype() + " ' with Custommer Group '" + optoari.getXgcus()+ " ' and Item Group '" + optoari.getXgitem() + "' data already exist in this system");
				return responseHelper.getResponse();
			}

			long count = optoariService.save(optoari);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save data");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Data saved successfully");
			responseHelper.setRedirectUrl("/system/optoari/" + optoari.getXtype()  + "/" + optoari.getXgcus() + "/" +optoari.getXgitem());
			return responseHelper.getResponse();
		}

		// if existing data
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find data in this system");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(optoari, exist, "xtype", "xgcus","xgitem");
		long count = optoariService.update(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update data");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Data updated successfully");
		responseHelper.setRedirectUrl("/system/optoari/" + optoari.getXtype()  + "/" + optoari.getXgcus() + "/" +optoari.getXgitem());
		return responseHelper.getResponse();
	}

	@PostMapping("/delete/{xtype}/{xgcus}/{xgitem}")
	public @ResponseBody Map<String, Object> deleteOptoariInfo(@PathVariable String xtype,@PathVariable String xgcus, @PathVariable String xgitem,   Model model) {
		Optoari optoari = optoariService.findByXtypeXgcusAndXgitem(xtype,xgcus,xgitem);
		if(optoari == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = optoariService.delete(optoari);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/system/optoari/" + xtype  + "/" + xgcus + "/" + xgitem);
		return responseHelper.getResponse();
}
}
