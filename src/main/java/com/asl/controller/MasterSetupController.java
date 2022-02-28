package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Controller
@RequestMapping("/mastersetup")
public class MasterSetupController extends ASLAbstractController {

	@GetMapping("/itemmaster")
	public String loadItemMasterMenuPage(Model model) {
		return "pages/mastersetup/itemmaster";
	}

	@GetMapping("/partymaster")
	public String loadPartyMasterMenuPage(Model model) {
		return "pages/mastersetup/partymaster";
	}

	@GetMapping("/cap")
	public String loadCodeAndParametersPage(Model model) {
		if(isBoshila()) {
			return "pages/land/xcodes/cap";
		}
		return "pages/mastersetup/cap";
	}
	
	@GetMapping("/vataitmaster")
	public String loadVatAndTaxPage(Model model) {		
		return "pages/mastersetup/vataitmaster";
	}
}
