package com.asl.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.DataList;
import com.asl.entity.ProfileLine;
import com.asl.model.MenuProfile;

/**
 * @author Zubayer Ahamed
 * @since Oct 10, 2021
 */
@Controller
@RequestMapping("/module")
public class ModuleController extends ASLAbstractController {

	@GetMapping("/{moduleid}")
	public String loadModulePage(@PathVariable String moduleid, Model model) {

		MenuProfile mp = (MenuProfile) sessionManager.getFromMap("menuProfile");
		ProfileLine selectedModule = new ProfileLine();
		List<ProfileLine> master = new ArrayList<>();
		if(mp != null) {
			for (com.asl.enums.MenuProfile menu : com.asl.enums.MenuProfile.values()) {
				ProfileLine pl = MenuProfile.invokeGetter(mp, menu.name());
				if(pl == null || !"MASTER".equalsIgnoreCase(pl.getMenutype())) continue;
				if(StringUtils.isNotBlank(pl.getRoles())) {
					String[] roles = pl.getRoles().split(",");
					boolean rolePermissionOk = false;
					for(String role : Arrays.asList(roles)) {
						if(sessionManager.getLoggedInUserDetails().getRoles().contains(role)) {
							rolePermissionOk = true;
						}
					}
					pl.setRoleHasAccess(rolePermissionOk);
				}

				pl.setSubmenus(new ArrayList<>());
				pl.setActiveMenu(menu.name().equalsIgnoreCase(moduleid));
				if(pl.isActiveMenu()) selectedModule = pl;
				master.add(pl);
			}
			
			//prepareMasterMenus(mp, master, selectedModule, moduleid);
		}

		// L1
		// now find all sub menus and normal menus according to master
		for (com.asl.enums.MenuProfile menu : com.asl.enums.MenuProfile.values()) {
			ProfileLine pl = MenuProfile.invokeGetter(mp, menu.name());
			if(pl == null || !pl.getParent().equalsIgnoreCase(selectedModule.getProfilelinecode())) {
				continue;
			}

			if(StringUtils.isNotBlank(pl.getRoles())) {
				String[] roles = pl.getRoles().split(",");
				boolean rolePermissionOk = false;
				for(String role : Arrays.asList(roles)) {
					if(sessionManager.getLoggedInUserDetails().getRoles().contains(role)) {
						rolePermissionOk = true;
					}
				}
				pl.setRoleHasAccess(rolePermissionOk);
			}

			selectedModule.getSubmenus().add(pl);
			pl.setSubmenus(new ArrayList<>());

			// get sub menus menu
			// L2
			createSubMenus(mp, pl);
		}


		List<DataList> dl = listService.getList("SYSTEM", "HOME_PAGE");
		if(dl != null && !dl.isEmpty()) {
			DataList ls = dl.get(0);
			if(StringUtils.isNotBlank(ls.getListvalue2())) {
				master.sort(Comparator.comparing(ProfileLine::getGroupseqn));
				master.stream().forEach(m -> {
					m.getSubmenus().sort(Comparator.comparing(ProfileLine::getSeqn));
					sortSubMenus(m);
				});
				model.addAttribute("modules", master);
				model.addAttribute("selectedModule", selectedModule);
				
				sessionManager.addToMap("PERMITTED_MODULES", master);
				sessionManager.addToMap("SELECTED_MODULES", selectedModule);
				
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
		}
		else if (isIdeal()) {
			return "pages/dashboard/dashboard-ideal";
		}
		

		return "pages/dashboard/dashboard";
	}

}
