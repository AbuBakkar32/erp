package com.asl.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.asl.enums.MenuProfile;
import com.asl.enums.ProfileType;
import com.asl.enums.ReportMenu;
import com.asl.enums.TransactionCodeType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since 02-12-2020
 */
@Data
@Slf4j
@Entity
@NoArgsConstructor
@IdClass(ProfileLinePK.class)
@Table(name = "profileline")
@EqualsAndHashCode(of = { "zid", "profilelineid", "profilelinecode", "profilecode" }, callSuper = false)
public class ProfileLine extends AbstractModel<String> {

	private static final long serialVersionUID = -9011140154721641297L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "profilelineid")
	private String profilelineid;

	@Id
	@Basic(optional = false)
	@Column(name = "profilelinecode")
	@Size(max = 50, message = "Profile code maximum 50 character allowed")
	private String profilelinecode;

	@Id
	@Basic(optional = false)
	@Column(name = "profilecode")
	@Size(max = 50, message = "Profile code maximum 50 character allowed")
	private String profilecode;

	@Column(name = "profiletype")
	@Enumerated(EnumType.STRING)
	private ProfileType profiletype;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "display")
	private boolean display;

	@Column(name = "required")
	private boolean required;

	@Column(name = "screenprompt")
	private String screenprompt;

	@Column(name = "seqn")
	private int seqn;

	@Column(name= "xtypetrn")
	private String xtypetrn;

	@Column(name= "xtrn")
	private String xtrn;

	@Column(name = "pgroup")
	private String pgroup;

	@Column(name = "parent")
	private String parent;

	@Transient
	private String pgroupname;

	@Transient
	private String managerprompt;

	@Transient
	private int groupseqn;

	@Transient
	private String menutype;

	@Transient
	private boolean activeMenu;

	@Transient
	private String url;

	@Transient
	private String roles = "";

	@Transient
	private boolean centralAccess;

	@Transient
	private boolean branchAccess;

	@Transient
	private boolean roleHasAccess = true;

	@Transient
	private int profilelebel;

	public ProfileLine(DataList dl) {
		if(dl == null || StringUtils.isBlank(dl.getListvalue1())) return;

		try {
			this.profilelinecode = dl.getListvalue1();
			this.seqn = Integer.parseInt(dl.getListvalue2());
			this.profiletype = ProfileType.valueOf(dl.getListvalue3());  // M / R / U / B
			this.managerprompt = dl.getListvalue4();
			this.screenprompt = dl.getListvalue5();
			this.display = "Y".equalsIgnoreCase(dl.getListvalue6());
			this.pgroup = dl.getListvalue9();
			this.pgroupname = dl.getListvalue10();
			this.groupseqn = Integer.parseInt(dl.getListvalue11());
			this.menutype = dl.getListvalue12();
			this.url = dl.getListvalue13();
			this.parent = dl.getListvalue9();
			this.required = "Y".equalsIgnoreCase(dl.getListvalue7());
			this.enabled = "Y".equalsIgnoreCase(dl.getListvalue8());

			this.roles = "";
			if(StringUtils.isNotBlank(dl.getListvalue14())) {
				String[] rolesArr = dl.getListvalue14().split("\\|");
				for(String role : rolesArr) {
					this.roles += role + ",";
				}
				int lastComma = this.roles.lastIndexOf(',');
				String finalString = this.roles.substring(0, lastComma);
				this.roles = finalString;
			}
			this.centralAccess = "Y".equalsIgnoreCase(dl.getListvalue15());
			this.branchAccess = "Y".equalsIgnoreCase(dl.getListvalue16());

			this.xtypetrn = TransactionCodeType.PROFILE_LINE.getCode();
			this.xtrn = TransactionCodeType.PROFILE_LINE.getdefaultCode();
		} catch (Exception e) {
			log.error("Error is : {}, {}", e.getMessage(), e);
		}
	}

	public ProfileLine(ReportMenu rm) {
		this.profilelinecode = rm.name();
		this.seqn = rm.getSeqn();
		this.profiletype = ProfileType.R;
		this.managerprompt = rm.getDescription();
		this.screenprompt = rm.getDescription();
		this.display = "Y".equalsIgnoreCase(rm.getDefaultAccess());
		this.pgroup = rm.getGroup();
		this.pgroupname = rm.getGroupName();
		this.groupseqn = rm.getGroupseqn();
		this.menutype = "MENU";
		this.url = "report/" + rm.name();
		this.parent = rm.getGroup();
		this.required = false;
		this.enabled = rm.isFopEnabled();

		this.xtypetrn = TransactionCodeType.PROFILE_LINE.getCode();
		this.xtrn = TransactionCodeType.PROFILE_LINE.getdefaultCode();
	}

	public ProfileLine(MenuProfile rm) {
		this.profilelinecode = rm.name();
		this.seqn = rm.getSeqn();
		this.profiletype = ProfileType.M;
		this.managerprompt = rm.getDescription();
		this.screenprompt = rm.getDescription();
		this.display = "Y".equalsIgnoreCase(rm.getDefaultAccess());
		this.pgroup = rm.getGroupName();
		this.pgroupname = rm.getGroupName();
		this.groupseqn = rm.getGroupseqn();
		this.menutype = rm.getMenuType();
		this.url = rm.getMenuPath();
		this.parent = rm.getParent();
		this.required = false;
		this.enabled = rm.isFopEnabled();
		this.roles = rm.getRoles();
		this.centralAccess = rm.isCentralAccess();
		this.branchAccess = rm.isBranchAccess();

		this.xtypetrn = TransactionCodeType.PROFILE_LINE.getCode();
		this.xtrn = TransactionCodeType.PROFILE_LINE.getdefaultCode();
	}

	@Transient
	private List<ProfileLine> submenus = new ArrayList<>();
}
