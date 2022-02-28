package com.asl.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.service.CaitemService;
import com.asl.service.OpordService;

@Controller
@RequestMapping("/roommanagement")
public class RoomManagementController extends ASLAbstractController{
	
	@Autowired
	OpordService opordService;
	@Autowired
	CaitemService caitemService;
	
	@GetMapping
	public String loadConventionManagementMenuPage(Model model) {
		model.addAttribute("availableRooms", opordService.findAvailableRoomsByDate(new Date()));
		model.addAttribute("bookedRooms", opordService.findBookedRoomsByDate(new Date()));
		//model.addAttribute("allRooms", caitemService.searchCaitem("ICRM-"));
		return "pages/roommanagement/roommanagement";
	}


}
