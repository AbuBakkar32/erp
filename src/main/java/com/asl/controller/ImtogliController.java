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

import com.asl.entity.Imtogli;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.ImtogliService;

@Controller
@RequestMapping("/system/imtogli")
public class ImtogliController extends ASLAbstractController{
	@Autowired private ImtogliService imtogliService;
	@GetMapping
	public String loadImtogliControllerPage(Model model) {
		Imtogli imtogli = new Imtogli();
		imtogli.setNewData(true);
		model.addAttribute("imtogli", imtogli);
		model.addAttribute("allImtogli", imtogliService.getAllImtogli(imtogli.getXtrn()));
		model.addAttribute("prefixes", imtogliService.getxtrn());
		model.addAttribute("itemGroup", xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		return "pages/system/imtogli";
	}
	
	@GetMapping("/{xtrn}/{xgitem}")
	public String loadImtogliPage(@PathVariable String xtrn,@PathVariable String xgitem, Model model) {
		Imtogli imtogli = imtogliService.findByXtrnAndXgitem(xtrn,xgitem);
		if (imtogli == null)
			return "redirect:/system/imtogli";

		model.addAttribute("imtogli", imtogli);
		model.addAttribute("allImtogli", imtogliService.getAllImtogli(imtogli.getXtrn()));
		model.addAttribute("prefixes", imtogliService.getxtrn());
		model.addAttribute("itemGroup", xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		return "pages/system/imtogli";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imtogli imtogli, BindingResult bindingResult) {
		if (imtogli == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(imtogli.getXaccdr())) {
			responseHelper.setErrorStatusAndMessage("Debit Account Required");
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(imtogli.getXacccr())) {
			responseHelper.setErrorStatusAndMessage("Credit Account Required");
			return responseHelper.getResponse();
		}


		Imtogli exist = imtogliService.findByXtrnAndXgitem(imtogli.getXtrn(),imtogli.getXgitem());

		// if new data
		if(imtogli.isNewData()) {
			if(exist != null) {
				responseHelper.setErrorStatusAndMessage("Transaction '" + imtogli.getXtrn() + "'Item Group '" + imtogli.getXgitem() + "' data already exist in this system");
				return responseHelper.getResponse();
			}

			long count = imtogliService.saveImtogli(imtogli);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save data");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Data saved successfully");
			responseHelper.setRedirectUrl("/system/imtogli/" + imtogli.getXtrn()  + "/" +imtogli.getXgitem());
			return responseHelper.getResponse();
		}

		// if existing data
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find data in this system");
			return responseHelper.getResponse();
		}

		
		BeanUtils.copyProperties(imtogli, exist, "xtrn","xgitem");
		long count = imtogliService.updateImtogli(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update data");
			return responseHelper.getResponse();
		}
		
		responseHelper.setSuccessStatusAndMessage("Data updated successfully");
		responseHelper.setRedirectUrl("/system/imtogli/" + imtogli.getXtrn()  + "/" +imtogli.getXgitem());
		return responseHelper.getResponse();
	}

	@PostMapping("/delete/{xtrn}/{xgitem}")
	public @ResponseBody Map<String, Object> deleteImtogliInfo(@PathVariable String xtrn,@PathVariable String xgitem,   Model model) {
		Imtogli imtogli = imtogliService.findByXtrnAndXgitem(xtrn,xgitem);
		if(imtogli == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imtogliService.deleteImtogli(imtogli);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/system/imtogli/" + xtrn  + "/" + xgitem);
		return responseHelper.getResponse();
}
	

}
