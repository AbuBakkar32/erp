package com.asl.controller;

import java.util.Collections;
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

import com.asl.entity.Caitem;
import com.asl.entity.DataList;
import com.asl.entity.Imtrn;
import com.asl.entity.Xtrnp;
import com.asl.enums.ResponseStatus;
import com.asl.model.ServiceException;
import com.asl.service.XtrnpService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Slf4j
@Controller
@RequestMapping("/mastersetup/xtrnp")
public class XtrnpController extends ASLAbstractController {

	@Autowired private XtrnpService xtrnpService;

	@GetMapping
	public String loadXtrnPage(Model model) {
		model.addAttribute("xtrnp", getDefaultXtrnp());
		model.addAttribute("xtrnpList", xtrnpService.getAllXtrnp());
		List<DataList> lsit = listService.findDataListByListcode("TRNCODES");
		model.addAttribute("codetypes", lsit == null ? Collections.emptyList() : lsit);
		return "pages/mastersetup/xtrnp/xtrnp";
	}

	@GetMapping("/{xtypetrn}/{xtrn}/{xtyperel}")
	public String loadXtrnPage(@PathVariable String xtypetrn, @PathVariable String xtrn, @PathVariable String xtyperel, Model model) {
		Xtrnp x = xtrnpService.findXtrnpByXvoucher(xtypetrn, xtrn, xtyperel);
		if(x == null) {
			return "redirect:/mastersetup/xtrnp";
		}
		
		model.addAttribute("xtrnp", x);
		model.addAttribute("xtrnpList", xtrnpService.getAllXtrnp());
		List<DataList> lsit = listService.findDataListByListcode("TRNCODES");
		model.addAttribute("codetypes", lsit == null ? Collections.emptyList() : lsit);
		return "pages/mastersetup/xtrnp/xtrnp"; 
	}

	private Xtrnp getDefaultXtrnp() {
		Xtrnp xtrnp = new Xtrnp();
		return xtrnp;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Xtrnp xtrnp, BindingResult bindingResult){
		if(xtrnp == null || StringUtils.isBlank(xtrnp.getXtypetrn()) || StringUtils.isBlank(xtrnp.getXtrn()) || StringUtils.isBlank(xtrnp.getXtyperel())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}


		// if existing record
		Xtrnp existXtrn = xtrnpService.findXtrnpByXvoucher(xtrnp.getXtypetrn(), xtrnp.getXtrn(), xtrnp.getXtyperel());
		if(existXtrn != null) {
			BeanUtils.copyProperties(xtrnp, existXtrn);
			long count = xtrnpService.update(existXtrn);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Related Transaction code updated successfully");
			responseHelper.setRedirectUrl("/mastersetup/xtrnp/" + existXtrn.getXtypetrn() + "/" + existXtrn.getXtrn()+ "/" + existXtrn.getXtyperel());
			return responseHelper.getResponse();
		}

		long count = xtrnpService.save(xtrnp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Related Transaction code saved successfully");
		responseHelper.setRedirectUrl("/mastersetup/xtrnp/" + xtrnp.getXtypetrn() + "/" + xtrnp.getXtrn()+ "/" + xtrnp.getXtyperel());
		return responseHelper.getResponse();
	}

	@PostMapping("/{xtypetrn}/{xtrn}/{xtyperel}/delete")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xtypetrn, @PathVariable String xtrn, @PathVariable String xtyperel, Model model){
		Xtrnp delete = xtrnpService.findXtrnpByXvoucher(xtypetrn,xtrn,xtyperel);
		if(delete == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = xtrnpService.delete(delete);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Related Transaction deleted successfully");
		responseHelper.setRedirectUrl("/mastersetup/xtrnp/");
		return responseHelper.getResponse();
	}

}
