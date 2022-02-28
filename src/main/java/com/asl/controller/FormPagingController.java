package com.asl.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Zubayer Ahamed
 * @since Feb 20, 2021
 */
@Controller
@RequestMapping("/formpaging")
public class FormPagingController extends ASLAbstractController {

	@GetMapping("/step/{direction}/{id}")
	public String formPaging(@PathVariable("direction") String direction, @PathVariable("id") String id, @RequestParam String table, @RequestParam String column, @RequestParam(required = false) String redirectUrl) {
		if(StringUtils.isBlank(redirectUrl)) return "redirect:/";
		Long expectedId = formPagingService.getExpectedRecord(table, column, id, direction);
		return "redirect:" + redirectUrl + "/" + expectedId;
	}

	@GetMapping("/jump/{direction}")
	public String formPaging(@PathVariable("direction") String direction, @RequestParam String table, @RequestParam String column, @RequestParam(required = false) String redirectUrl) {
		if(StringUtils.isBlank(redirectUrl)) return "redirect:/";
		Long expectedId = formPagingService.getSeilingRecord(table, column, direction);
		return "redirect:" + redirectUrl + "/" + expectedId;
	}
}
