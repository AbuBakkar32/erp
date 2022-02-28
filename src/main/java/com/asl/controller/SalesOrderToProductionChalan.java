package com.asl.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.service.OpordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Sep 20, 2021
 */
@Slf4j
@Controller
@RequestMapping("/production/sotoprodchalan")
public class SalesOrderToProductionChalan extends ASLAbstractController {

	@Autowired private OpordService opordService;

	@GetMapping
	public String loadSalesOrderToProductionPage(Model model) {
		model.addAttribute("allOpordHeader", opordService.findAllOpenOpordHeaderByRawMaterials(TransactionCodeType.SALES_ORDER.getCode(), TransactionCodeType.SALES_ORDER.getdefaultCode()));
		return "pages/production/sotoprodchalan/sotoprodchalan";
	}

	@PostMapping("/create")
	public @ResponseBody Map<String, Object> saveOporddetail(@RequestParam(value="salesorders[]") String[] salesorders) {

		Map<String, Object> returnMap = new HashMap<>();
		try {
			returnMap = opordService.createSalesOrderToChalan(salesorders);
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		if(!(boolean) returnMap.get("status")) {
			responseHelper.setErrorStatusAndMessage("Can't create any production chalan");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/salesorderchalan/" + (String) returnMap.get("chalan"));
		responseHelper.setSuccessStatusAndMessage("Chalan " + (String) returnMap.get("chalan") + " created successfully");
		return responseHelper.getResponse();
	}
}
