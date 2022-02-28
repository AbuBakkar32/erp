package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.service.ASLProcErrorService;

/**
 * @author Zubayer Ahamed
 * @since Apr 26, 2021
 */
@Controller
@RequestMapping("/system/aslprocerror")
public class ASLProcErrorController extends ASLAbstractController {

	@Autowired private ASLProcErrorService errorService;

	@GetMapping
	public String getPROCErrorPage(Model model) {
		model.addAttribute("errorsList", errorService.getAllProcErrors());
		return "pages/system/aslprocerror/aslprocerror";
	}

}
