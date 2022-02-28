package com.asl.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.asl.config.AppConfig;
import com.asl.entity.ProfileLine;
import com.asl.enums.UserRole;
import com.asl.model.LoggedInUserDetails;
import com.asl.model.MenuProfile;
import com.asl.service.ASLSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Component
public class MenuAccessAuthorizationInterceptor implements AsyncHandlerInterceptor {

	@Autowired private ASLSessionManager sessionManager;
	@Autowired private AppConfig appConfig;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(!hasAccess(request.getServletPath())) {
			RequestDispatcher dispatcher = request.getSession().getServletContext().getRequestDispatcher("/accessdenied?message=Trying to access " + request.getServletPath());
			dispatcher.forward(request, response);
			return false;
		}

		return true;
	}

	private boolean hasAccess(String modulePath) {
		LoggedInUserDetails liud = sessionManager.getLoggedInUserDetails();
		if(liud != null 
				&& liud.getRoles() != null 
				&& liud.getRoles().contains(UserRole.SYSTEM_ADMIN.getCode()) 
				&& "Y".equalsIgnoreCase(appConfig.getAllowSystemAdmin())) return true;

		//MenuProfile mp = profileService.getLoggedInUserMenuProfile();
		MenuProfile mp = (MenuProfile) sessionManager.getFromMap("menuProfile");
		if(mp == null) return false;

		boolean stat = true;

		for (com.asl.enums.MenuProfile menu : com.asl.enums.MenuProfile.values()) {
			if("MASTER".equalsIgnoreCase(menu.getParent()) || "SUB-MASTER".equalsIgnoreCase(menu.getParent())) continue;
			if(modulePath.startsWith("/" + menu.getMenuPath())) {
				ProfileLine pl = MenuProfile.invokeGetter(mp, menu.name());
				if(pl == null) {
					stat = true;
					continue;
				}
				stat = pl.isDisplay();
			}
		}

		return stat;
	}
}
