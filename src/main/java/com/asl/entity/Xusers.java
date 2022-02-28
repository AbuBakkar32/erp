package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.asl.enums.UserRole;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Data
@Entity
@Table(name = "xusers")
@IdClass(XusersPK.class)
@EqualsAndHashCode(of = { "zid","zemail" }, callSuper = false)
public class Xusers extends AbstractModel<String> {

	private static final long serialVersionUID = -6408600048875043467L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail")
	@Size(max = 20, message = "Username maximum 20 character allowed")
	private String zemail;

	@Column(name = "xpassword")
	private String xpassword;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xrole")
	private String xrole;

	@Column(name = "xaccess")
	private String xaccess;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xsp")
	private String xsp;

	@Column(name = "xdformat")
	private String xdformat;

	@Column(name = "xdsep")
	private String xdsep;

	@Column(name = "xlastlogdate")
	private String xlastlogdate;

	@Column(name = "xname")
	private String xname;

	@Column(name = "xoldpass")
	private String xoldpass;

	@Column(name = "xposition")
	private String xposition;

	@Column(name = "xpriority")
	private String xpriority;

	@Column(name = "xcontact")
	private String xcontact;

	@Column(name = "xcrlimit")
	private String xcrlimit;

	@Column(name = "xgcus")
	private String xgcus;

	@Column(name = "xgsup")
	private String xgsup;

	@Column(name = "xmadd")
	private String xmadd;

	@Column(name = "xorg")
	private String xorg;

	@Column(name = "xphone")
	private String xphone;

	@Column(name = "xstatuscus")
	private String xstatuscus;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xvatregno")
	private String xvatregno;

	@Column(name = "systemadmin")
	private boolean systemadmin;

	@Column(name = "admin")
	private boolean admin;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xnote")
	private String xnote;

	@Transient
	private String roles;

	public String getRoles() {
		this.roles = "";
		if(Boolean.TRUE.equals(systemadmin)) roles += UserRole.SYSTEM_ADMIN.getCode() + ',';
		if(Boolean.TRUE.equals(admin)) roles += UserRole.ADMIN.getCode() + ',';

		if(StringUtils.isBlank(roles)) return roles;

		int lastComma = roles.lastIndexOf(',');
		String finalString = roles.substring(0, lastComma);
		roles = finalString;
		return roles;
	}

	@Transient
	private String newflag;

	@Transient
	private String staffname;

	@Transient
	private String partyname;

	@Transient
	private String storename;

	@Transient
	private String customergroup;
}
