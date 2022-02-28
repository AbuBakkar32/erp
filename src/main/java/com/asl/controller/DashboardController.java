package com.asl.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.DataList;
import com.asl.entity.ProfileLine;
import com.asl.model.MenuProfile;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController extends ASLAbstractController {

	@GetMapping
	public String loadDashboard(Model model) {

//		List<ProfileLine> master = new ArrayList<>();
//		List<ProfileLine> sub = new ArrayList<>();
//		List<ProfileLine> menus = new ArrayList<>();

//		MenuProfile mp = (MenuProfile) sessionManager.getFromMap("menuProfile");
//		if(mp != null) {
//			for (com.asl.enums.MenuProfile menu : com.asl.enums.MenuProfile.values()) {
//				if("MASTER".equalsIgnoreCase(menu.getMenuType()) && MenuProfile.invokeGetter(mp, menu.name()) != null && MenuProfile.invokeGetter(mp, menu.name()).isDisplay()) master.add(MenuProfile.invokeGetter(mp, menu.name()));
//				if("SUB-MASTER".equalsIgnoreCase(menu.getMenuType()) && MenuProfile.invokeGetter(mp, menu.name()) != null && MenuProfile.invokeGetter(mp, menu.name()).isDisplay()) sub.add(MenuProfile.invokeGetter(mp, menu.name()));
//				if(!"MASTER".equalsIgnoreCase(menu.getMenuType()) && !"SUB-MASTER".equalsIgnoreCase(menu.getMenuType()) && MenuProfile.invokeGetter(mp, menu.name()) != null && MenuProfile.invokeGetter(mp, menu.name()).isDisplay()) {
//					menus.add(MenuProfile.invokeGetter(mp, menu.name()));
//				}
//			}
//		}

		// if menu version 2 is active
		List<DataList> dl = listService.getList("SYSTEM", "HOME_PAGE");
		if(dl != null && !dl.isEmpty()) {
			DataList ls = dl.get(0);
			if(StringUtils.isNotBlank(ls.getListvalue2())) {
//				master.sort(Comparator.comparing(ProfileLine::getGroupseqn));
//				model.addAttribute("modules", master);
//				model.addAttribute("selectedModule", null);
				return "pages/dashboard/" + ls.getListvalue2();
			}
		}

		if(isKhanas() || isPg()) {
			return "pages/dashboard/dashboard-khanas";
		} else if (isTCC()) {
			return "pages/dashboard/dashboard-tcc";
		} else if (isBoshila()) {
			return "pages/dashboard/dashboard-boshila";
		} else if (isGarments()) {
			return "pages/dashboard/dashboard-garments";
		} else if (isIdeal()) {
			return "pages/dashboard/dashboard-ideal";
		} else if (isASL()) {
			return "pages/dashboard/dashboard-asl";
		}

		return "pages/dashboard/dashboard";
	}
}
