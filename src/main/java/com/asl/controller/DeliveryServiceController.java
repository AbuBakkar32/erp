package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deliveryservice")
public class DeliveryServiceController extends ASLAbstractController{
	
	@GetMapping
	public String loadDeliveryServicePage() {
		
		return"pages/deliveryservice";
	}

}
