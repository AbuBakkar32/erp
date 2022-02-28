package com.asl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.asl.entity.Menu;
import com.asl.enums.MenuType;

/**
 * @author Zubayer Ahamed
 * @since Sep 19, 2021
 */
public class MenuTest {

	@Test
	void testMenu() {
		List<Menu> list = new ArrayList<Menu>();
		list.add(new Menu("MOD0101", null, "Administration", "/system", 1, true, MenuType.MOD, "Y", null, Collections.emptyList()));
		list.add(new Menu("SUB0101", "MOD0101", "Users", "/system", 1, true, MenuType.SUB, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0101", "SUB0101", "Manage Users", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0102", "SUB0101", "Manage Access Profiles", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0103", "SUB0101", "Profile Allocation", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("SUB0102", "MOD0101", "System Administration", "/system", 1, true, MenuType.SUB, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0104", "SUB0102", "Code & Parameters", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0105", "SUB0102", "Transaction Code", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0106", "SUB0102", "Related transaction codes", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0107", "MOD0101", "List Maintenance", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0108", "MOD0101", "List ImportExport", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("MOD0102", null, "Import/Export", "/system", 1, true, MenuType.MOD, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0109", "MOD0102", "Code ImportExport", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));
		list.add(new Menu("MEN0110", "MOD0102", "Transaction Code ImportExport", "/system", 1, true, MenuType.MEN, "Y", null, Collections.emptyList()));

		Map<String, Menu> menumap = new HashMap<String, Menu>();

		for(Menu m : list) {
			// Module
			if(MenuType.MOD.equals(m.getType())) {
				if(menumap.get(m.getMenuid()) != null) {
					
				} else {
					menumap.put(m.getMenuid(), m);
				}
			}
		}

		

		for(Map.Entry<String, Menu> m : menumap.entrySet()) {
			System.out.println(m.getKey() + " - " + m.getValue());
		}
	}
}
