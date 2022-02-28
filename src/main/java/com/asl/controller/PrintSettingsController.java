package com.asl.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
@RequestMapping("/printsettings")
public class PrintSettingsController extends ASLAbstractController{
	
	@GetMapping
	public String loadPrintSettingsPage(Model model) {
		List<String> list = Arrays.asList(new String[] {"Invoice Print", "Token Print","KOT Print","Signout Slip Print","CC Slip Print"});

		List<PrintSettingsHelper> pl = new ArrayList<>();
		list.stream().forEach(l -> {
			pl.add(new PrintSettingsHelper(l,0,false));
		});

		model.addAttribute("printSettingsList", pl);

		return "pages/printsettings";
	}


}

@Data
@AllArgsConstructor
@NoArgsConstructor
class PrintSettingsHelper{
	private String prompt;
	private Integer value;
	private boolean status;
}
