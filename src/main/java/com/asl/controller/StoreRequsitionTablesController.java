package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.enums.TransactionCodeType;
import com.asl.service.ImtorService;
import com.asl.service.XcodesService;


@Controller
@RequestMapping("/inventory/storereqtables")
public class StoreRequsitionTablesController extends ASLAbstractController {

	@Autowired private ImtorService imtorService;
	@Autowired private XcodesService xcodeService;

	@GetMapping("/{histrytype}")
	public String loadHistoriesPage(@PathVariable String histrytype, Model model) {
		
		model.addAttribute("showtable", histrytype);		
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeaderbyPrefix(TransactionCodeType.INVENTORY_STORE_REQUISITION.getCode()));

		return "pages/inventory/storereq/storereqtables";
	}

}