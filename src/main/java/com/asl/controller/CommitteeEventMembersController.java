package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landcommitteeeventmembers")
public class CommitteeEventMembersController extends ASLAbstractController{


	@GetMapping
	public String loadCommitteeEventMembersPage() {
		
		
		return "pages/land/landcommitteeeventmembers";
	}
}
