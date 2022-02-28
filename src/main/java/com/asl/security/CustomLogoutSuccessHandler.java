package com.asl.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.asl.entity.UserAuditRecord;
import com.asl.model.LoggedInUserDetails;
import com.asl.model.MyUserDetails;
import com.asl.service.UserAuditRecordService;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2021
 */
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired private UserAuditRecordService auditService;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		LoggedInUserDetails user = new LoggedInUserDetails();

		Object principal = authentication.getPrincipal();
		if(principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			user.setUsername(mud.getUsername());
			user.setEmailAddress(mud.getEmailAddress());
			user.setBusinessId(mud.getBusinessId());
			Arrays.stream(mud.getRoles().split(",")).forEach(role -> user.getRoles().add(role));
		}

		Object details = authentication.getDetails();
		if (details instanceof WebAuthenticationDetails) {
			String ip = ((WebAuthenticationDetails) details).getRemoteAddress();
			if(StringUtils.isNotBlank(ip)) user.setIpAddress(ip);
		}

		Date date = new Date();
		UserAuditRecord uar = user.getAuditRecord(user);
		uar.setLoginDate(null);
		uar.setLogoutDate(date);
		uar.setRecordDate(date);
		auditService.save(uar);

		super.onLogoutSuccess(request, response, authentication);
	}
}
