package com.asl.service.impl;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.asl.entity.Zbusiness;
import com.asl.model.LoggedInUserDetails;
import com.asl.model.MyUserDetails;
import com.asl.service.ASLSessionManager;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
@Data
public abstract class AbstractSessionManager implements ASLSessionManager {

	@Value("${default.business-id:900010}")
	private String defaultBusinessId;

	@Override
	public LoggedInUserDetails getLoggedInUserDetails() {
		LoggedInUserDetails user = new LoggedInUserDetails();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			user.setUsername(mud.getUsername());
			user.setEmailAddress(mud.getEmailAddress());
			user.setBusinessId(mud.getBusinessId());
			user.setFullName(mud.getStaffname() == null ? mud.getUsername() : mud.getStaffname());
			user.setXstaff(mud.getXstaff());
			user.setXcus(mud.getXcus());
			user.setXwh(mud.getXwh());
			user.setStaffname(mud.getStaffname());
			user.setStorename(mud.getStorename());
			user.setPartyname(mud.getPartyname());
			user.setBranch(mud.isBranch());
			Arrays.stream(mud.getRoles().split(",")).forEach(role -> user.getRoles().add(role));
		}

		Object details = auth.getDetails();
		if (details instanceof WebAuthenticationDetails) {
			String ip = ((WebAuthenticationDetails) details).getRemoteAddress();
			if(StringUtils.isNotBlank(ip)) user.setIpAddress(ip);
		}

		return user;
	}

	@Override
	public String getBusinessId() {
		String businessId = defaultBusinessId;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return businessId;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			businessId = mud.getBusinessId();
		}

		return businessId;
	}

	@Override
	public Zbusiness getZbusiness() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			return mud.getZbusiness();
		}

		return null;
	}

	@Override
	public boolean isCentral() {
		if(getLoggedInUserDetails().isBranch()) return false;
		return getZbusiness() != null && Boolean.TRUE.equals(getZbusiness().getCentral());
	}

	@Override
	public boolean isBranch() {
		if(getLoggedInUserDetails().isBranch()) return true;
		return getZbusiness() != null && Boolean.TRUE.equals(getZbusiness().getBranch());
	}

	

}
