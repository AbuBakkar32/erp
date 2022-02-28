package com.asl.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asl.controller.ASLAbstractController;
import com.asl.entity.Caitem;
import com.asl.service.CaitemService;

/**
 * @author Zubayer Ahamed
 * @since Oct 31, 2021
 */
@RestController
@RequestMapping("/v2/rest/caitem")
public class CaitemRestController extends ASLAbstractController {

	@Autowired private CaitemService caitemService;

	@GetMapping("/getall")
	public List<Caitem> getAllCaitem(){
		return caitemService.getAllCaitems();
	}
}
