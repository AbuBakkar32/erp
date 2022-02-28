package com.asl.security;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.asl.model.LoggedInUserDetails;
import com.asl.model.MyUserDetails;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Nov 27, 2020
 */
@Data
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		String username = "SYSTEM_ADMIN";
		LoggedInUserDetails user = getLoggedInUserDetails();
		if(user != null && StringUtils.isNotBlank(user.getUsername())) username = user.getUsername();
		return Optional.ofNullable(username);
	}

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
			Arrays.stream(mud.getRoles().split(",")).forEach(role -> user.getRoles().add(role));
		}

		Object details = auth.getDetails();
		if (details instanceof WebAuthenticationDetails) {
			String ip = ((WebAuthenticationDetails) details).getRemoteAddress();
			if(StringUtils.isNotBlank(ip)) user.setIpAddress(ip);
		}

		return user;
	}
}
