package com.asl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Imtrn;
import com.asl.enums.CodeType;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ImtrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/inventory/imtrn")
public class ImtrnController extends ASLAbstractController{
	
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private XcodesService xcodeService;
	
	@Autowired
	private ImtrnService imtrnService;
	
	@GetMapping
	public String loadImtrnPage(Model model) {
		model.addAttribute("imtrn", getDefaultImtrn());
		
		model.addAttribute("imtrn", getDefaultImtrn());
		model.addAttribute("allImtrn", imtrnService.getAllImtrn());
		model.addAttribute("imtrnprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_NUMBER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode()));
		
		return "pages/inventory/imtrn/imtrn";
	}

	@GetMapping("/{ximtrnnum}")
	public String loadImtrnPage(@PathVariable String ximtrnnum, Model model) {
		
		Imtrn imtrn = imtrnService.findImtrnByXimtrnnum(ximtrnnum); 
		if(imtrn == null) imtrn = getDefaultImtrn();
		
		model.addAttribute("imtrn", imtrn);
		model.addAttribute("allImtrn", imtrnService.getAllImtrn());
		model.addAttribute("imtrnprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_NUMBER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode()));
			
		return "pages/inventory/imtrn/imtrn";
	}

	private Imtrn getDefaultImtrn() {
		Imtrn imtrn = new Imtrn();
		
		return imtrn;
	}
	
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imtrn imtrn, BindingResult bindingResult){
		
		//responseHelper.setStatus(ResponseStatus.ERROR);
		return null;
	}
	
	@PostMapping("/archive/{ximtrnnum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String ximtrnnum){
		return doArchiveOrRestore(ximtrnnum, true);
	}

	@PostMapping("/restore/{ximtrnnum}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String ximtrnnum){
		return doArchiveOrRestore(ximtrnnum, false);
	}

	public Map<String, Object> doArchiveOrRestore(String ximtrnnum, boolean archive){
		
		return null;
	}

}
