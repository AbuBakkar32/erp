package com.asl.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Imtrn;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ImtrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/inventory/productissue")
public class ProductIssueController extends ASLAbstractController {
	
	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private ImtrnService imtrnService;
	
	@GetMapping
	public String loadProductIssuePage(Model model) {
		/*
		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		*/
		return "pages/purchasing/pogrn/pogrn";
	}
	
	@GetMapping("/{xgrnnum}")
	public String loadProductIssuePage(@PathVariable String xgrnnum, Model model) {
		
		/*
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum); 
		if(data == null) data = getDefaultPogrnHeader();

		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));
		*/
		
		return "pages/purchasing/pogrn/pogrn";
	}
	
	private Imtrn getDefaultImtrn() {
		Imtrn imtrn = new Imtrn();
		imtrn.setXtype(TransactionCodeType.INVENTORY_TRANSACTION.getCode());
		imtrn.setXqty(BigDecimal.ZERO);
		imtrn.setXsign(+1);
		imtrn.setXtype(TransactionCodeType.INVENTORY_NUMBER.getCode());
		imtrn.setXtrn(TransactionCodeType.INVENTORY_NUMBER.getdefaultCode());
		return imtrn;
	}

}
