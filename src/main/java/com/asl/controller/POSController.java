package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Caitem;
import com.asl.enums.CodeType;
import com.asl.service.CaitemService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

/**
 * @author Zubayer Ahamed
 * @since May 29, 2021
 */
@Controller
@RequestMapping("/pos")
public class POSController extends ASLAbstractController {
	
	@Autowired private XcodesService xcodeService;
	@Autowired private CaitemService caitemService;
	@Autowired private XtrnService xtrnService;
	
	@GetMapping
	public String loadPOSPage(Model model) {
		
		model.addAttribute("xitemCategories", xcodeService.findByXtype(CodeType.ITEM_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("xsubCategories", xcodeService.findByXtype(CodeType.ITEM_SUB_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("caitem", caitemService.getAllCaitems( ));
		//model.addAttribute("categories", xcodeService.getAllCategoryWiseItem("Beef"));
		return "pages/pos/pos"; 
	}
	
	@GetMapping("/caitem/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem) {
		Caitem centralCaitem = caitemService.findByXitem(xitem);
		return centralCaitem;
	}
}
