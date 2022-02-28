package com.asl.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.asl.config.AppConfig;
import com.asl.entity.ProfileLine;
import com.asl.enums.ReportMenu;
import com.asl.enums.UserRole;
import com.asl.model.LoggedInUserDetails;
import com.asl.model.ReportProfile;
import com.asl.service.ASLSessionManager;
import com.asl.service.ProfileService;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Component
public class ReportAccessAuthorizationInterceptor implements AsyncHandlerInterceptor{

	@Autowired private ProfileService profileService;
	@Autowired private ASLSessionManager sessionManager;
	@Autowired private AppConfig appConfig;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(!hasAccess(request.getServletPath())) {
			RequestDispatcher dispatcher = request.getSession().getServletContext().getRequestDispatcher("/accessdenied?message=Truing to access " + request.getServletPath());
			dispatcher.forward(request, response);
			return false;
		}

		return true;
	}

	private boolean hasAccess(String modulePath) {
		LoggedInUserDetails liud = sessionManager.getLoggedInUserDetails();
		if(liud != null && liud.getRoles() != null && liud.getRoles().contains(UserRole.SYSTEM_ADMIN.getCode()) && "Y".equalsIgnoreCase(appConfig.getAllowSystemAdmin())) return true;

		ReportProfile rp = profileService.getLoggedInUserReportProfile();
		if(rp == null) return false;

		boolean stat = true;
		for (ReportMenu menu : ReportMenu.values()) {
			if(modulePath.startsWith("/report/" + menu.name())) {
				ProfileLine pl = ReportProfile.invokeGetter(rp, menu.name());
				if(pl == null) {
					stat = false;
					continue;
				}
				stat = pl.isDisplay();
			}
		}

		return stat;
	}
}
