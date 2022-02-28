package com.asl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Imstock;
import com.asl.entity.Imtrn;
import com.asl.service.ImstockService;
import com.asl.service.ImtrnService;

/**
 * @author Zubayer Ahamed
 * @since Jun 8, 2021
 */
@Controller
@RequestMapping("/inventory/stocklist")
public class StockListController extends ASLAbstractController {

	@Autowired private ImstockService imstockService;

	@GetMapping
	public String loadStockListPage(Model model) {
		List<Imstock> list = imstockService.search(null, null);
		
		model.addAttribute("imstocks", list);
		return "pages/inventory/stocklist/stocklist";
	}
}
