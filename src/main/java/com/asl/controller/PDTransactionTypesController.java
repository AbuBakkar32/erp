package com.asl.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;import javax.sound.sampled.DataLine;

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

import com.asl.entity.Arhed;
import com.asl.entity.DataList;
import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Controller
@RequestMapping("/system/pdtranstype")
public class PDTransactionTypesController extends ASLAbstractController {

	@Autowired private XcodesService xcodesService;
	@Autowired private XcodesService xcodeService;	

	@GetMapping
	public String loadXtrnPage(Model model) {
		model.addAttribute("xcodes", getDefaultCodes());
		model.addAttribute("xcodesList", xcodesService.getAllPDTransactionTypes());
		model.addAttribute("types", xcodeService.findByXtype(CodeType.SALARY_TYPE.getCode(),Boolean.TRUE));
		
		List<DataList> lsit = listService.findDataListByListcode("CODES");
		model.addAttribute("codetypes", lsit == null ? Collections.emptyList() : lsit);
		
		return "pages/mastersetup/pdtranstype/pdtranstype";
	}

	@GetMapping("/{xtype}/{xcode}")
	public String loadXtrnPage(@PathVariable String xtype, @PathVariable String xcode, Model model) {
		Xcodes x = xcodesService.findByXtypesAndXcodes(xtype, xcode);
		if(x == null) return "redirect:/system/pdtranstype";

		model.addAttribute("xcodes", x);
		model.addAttribute("xcodesList", xcodesService.getAllPDTransactionTypes());
		model.addAttribute("types", xcodeService.findByXtype(CodeType.SALARY_TYPE.getCode(),Boolean.TRUE));
		List<DataList> lsit = listService.findDataListByListcode("CODES");
		model.addAttribute("codetypes", lsit == null ? Collections.emptyList() : lsit);
		x.setXtype("PD Transaction Type");
		return "pages/mastersetup/pdtranstype/pdtranstype";
	}

	private Xcodes getDefaultCodes() {
		Xcodes codes = new Xcodes();

		codes.setXtype("PD Transaction Type");
		

		return codes;
	}
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Xcodes xcodes, BindingResult bindingResult){
		if(StringUtils.isBlank(xcodes.getXcode())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Modify xtrn code
		xcodes.setXcode(xcodes.getXcode().trim());
		xcodes.setXtype("PD Transaction Type");

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
			responseHelper.setRedirectUrl("/system/pdtranstype/" + xcodes.getXtype() + "/" + xcodes.getXcode());
			return responseHelper.getResponse();
		}

		// If new xtrn
		long count = xcodesService.save(xcodes);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Item code saved successfully");
		responseHelper.setRedirectUrl("/system/pdtranstype/" + xcodes.getXtype() + "/" + xcodes.getXcode());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xtype}/{xcode}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xtype, @PathVariable String xcode){
		return doArchiveOrRestore(xtype, xcode, true);
	}

	public Map<String, Object> doArchiveOrRestore(String xtype, String xcode, boolean archive){

		Xcodes xcodes = xcodesService.findByXtypesAndXcodes(xtype, xcode);
		if(xcodes == null) {
			responseHelper.setErrorStatusAndMessage("Code not found in this system");
			return responseHelper.getResponse();
		}

		// delete
		long count = xcodesService.deletePDCodes(xcode);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete this code");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Item code deleted successfully");
		responseHelper.setRedirectUrl("/system/pdtranstype/");
		return responseHelper.getResponse();
	}
}
