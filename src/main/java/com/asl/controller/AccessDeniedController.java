package com.asl.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asl.entity.UserAuditRecord;
import com.asl.model.LoggedInUserDetails;
import com.asl.service.UserAuditRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 *
 */
@Slf4j
@Controller
@RequestMapping("/accessdenied")
public class AccessDeniedController extends ASLAbstractController {

	@Autowired private UserAuditRecordService auditService;

	@GetMapping
	public String loadAccessDeniedController(Model model, @RequestParam(required = false) String message) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) return "pages/accessdenied/accessdenied";

		log.warn("{}, trying to unauthorized access", auth.getName());

		UserAuditRecord uar = new LoggedInUserDetails().getAuditRecord(sessionManager.getLoggedInUserDetails());
		Date date = new Date();
		uar.setUnAuthorizedAccessMessage(StringUtils.isBlank(message) ? "Unauthorized access attempt" : message);
		uar.setLoginDate(null);
		uar.setLogoutDate(null);
		uar.setRecordDate(date);
		auditService.save(uar);

		return "pages/accessdenied/accessdenied";
	}
}
