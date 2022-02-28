package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2022
 */
@Data
public class ProjectStoreView implements Serializable {

	private static final long serialVersionUID = -1139846835671201050L;
	private String zid;
	private String xtype;
	private String xcode;
	private String xcodename;
	private String xproject;
	private String xprojectname;
	private String xmadd;
	private String xphone;
	private String xfax;
	private String xemail;
}
