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

import com.asl.entity.Imprjtogli;
import com.asl.entity.Imtogli;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.ImprjtogliService;

@Controller
@RequestMapping("/system/imprjtogli")
public class ImprjtogliController extends ASLAbstractController{
	
	@Autowired private ImprjtogliService imprjtogliService;
	
	@GetMapping
	public String loadImprjtogliControllerPage(Model model) {
		Imprjtogli imprjtogli=new Imprjtogli();
		imprjtogli.setNewData(true);
		model.addAttribute("imprjtogli", imprjtogli);
		model.addAttribute("allImprjtogli", imprjtogliService.getAllImprjtogli());
		model.addAttribute("itemGroup", xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		return "pages/system/imprjtogli";
	}
	
	@GetMapping("/{xgitem}")
	public String loadImprjtogliPage(@PathVariable String xgitem, Model model) {
		Imprjtogli imprjtogli = imprjtogliService.findByXgitem(xgitem);
		if (imprjtogli == null)
			return "redirect:/system/imprjtogli";

		model.addAttribute("imprjtogli", imprjtogli);
		model.addAttribute("allImprjtogli", imprjtogliService.getAllImprjtogli());
		model.addAttribute("itemGroup", xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		return "pages/system/imprjtogli";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imprjtogli imprjtogli, BindingResult bindingResult) {
		if (imprjtogli == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(imprjtogli.getXaccdr())) {
			responseHelper.setErrorStatusAndMessage("Debit Account Required");
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(imprjtogli.getXacccr())) {
			responseHelper.setErrorStatusAndMessage("Credit Account Required");
			return responseHelper.getResponse();
		}


		Imprjtogli exist = imprjtogliService.findByXgitem(imprjtogli.getXgitem());

		// if new data
		if(imprjtogli.isNewData()) {
			if(exist != null) {
				responseHelper.setErrorStatusAndMessage("Item Group '" + imprjtogli.getXgitem() + "' data already exist in this system");
				return responseHelper.getResponse();
			}

			long count = imprjtogliService.saveImprjtogli(imprjtogli);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save data");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Data saved successfully");
			responseHelper.setRedirectUrl("/system/imprjtogli/" +imprjtogli.getXgitem());
			return responseHelper.getResponse();
		}

		// if existing data
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find data in this system");
			return responseHelper.getResponse();
		}

		
		BeanUtils.copyProperties(imprjtogli, exist,"xgitem");
		long count = imprjtogliService.updateImprjtogli(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update data");
			return responseHelper.getResponse();
		}
		
		responseHelper.setSuccessStatusAndMessage("Data updated successfully");
		responseHelper.setRedirectUrl("/system/imprjtogli/" + imprjtogli.getXgitem());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xgitem}")
	public @ResponseBody Map<String, Object> deleteImprjtogliInfo(@PathVariable String xgitem, Model model) {
		Imprjtogli imprjtogli = imprjtogliService.findByXgitem(xgitem);
		if(imprjtogli == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imprjtogliService.deleteImprjtogli(imprjtogli);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/system/imprjtogli/"+ xgitem);
		return responseHelper.getResponse();
		}

}
