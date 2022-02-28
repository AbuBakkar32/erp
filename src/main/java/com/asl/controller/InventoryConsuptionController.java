package com.asl.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.Potoapi;
import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;
import com.asl.model.Acyearend;
import com.asl.model.Consumption;
import com.asl.service.AcyearendService;
import com.asl.service.ConsumptionService;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/inventory/consumption")
public class InventoryConsuptionController extends ASLAbstractController{

	@Autowired private ConsumptionService consumptionService;
	@Autowired private XcodesService xcodeService;
	@GetMapping
	public String loadAcyearendControllerPage(Model model) {
		Consumption consumption = new Consumption();
		model.addAttribute("consumption", consumption);
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		model.addAttribute("processType", xcodeService.findByXtype(CodeType.CONSUMPTION_PROCESS.getCode(), Boolean.TRUE));
		return "pages/inventory/consumption/consumption";
	}
	
	@PostMapping("/process")
	public @ResponseBody Map<String, Object> procrss(Consumption consumption) {
		
		String p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
		consumptionService.IM_2GL_Transfer(consumption.getXstartdate(),consumption.getXdatexenddate(), consumption.getXdate(),consumption.getXwh(),consumption.getXtrn(), p_seq);
		String em = getProcedureErrorMessages(p_seq);
		if (StringUtils.isNotBlank(em)) {
			responseHelper.setErrorStatusAndMessage(em);
			return responseHelper.getResponse();
		}

		

		responseHelper.setSuccessStatusAndMessage("Successfully completed");
		responseHelper.setRedirectUrl("/inventory/consumption");
		return responseHelper.getResponse();
	}
}