package com.asl.entity;

import java.util.ArrayList;
import java.util.List;

import com.asl.enums.MenuType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Sep 19, 2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

	private String menuid;
	private String parentid;
	private String name;
	private String url;
	private int seqn;
	private boolean active;
	private MenuType type;
	private String defaccess;
	private String img;

	private List<Menu> menus = new ArrayList<Menu>();
}
