package com.asl.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pdmst")
@IdClass(PdmstPK.class)
@EqualsAndHashCode(of = { "zid", "xstaff" }, callSuper = false)
public class Pdmst extends AbstractModel<String> {

	private static final long serialVersionUID = -1426693265670268657L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xfname")
	private String xfname;
	
	@Column(name = "xmname")
	private String xmname;
	
	@Column(name = "xmadd")
	private String xmadd;  // current address

	@Column(name = "xpadd")
	private String xpadd;  // permanent address
	
	@Column(name = "xbirthdate")
	@Temporal(TemporalType.DATE)
	private Date xbirthdate;
	
	@Column(name = "xsex")
	private String xsex;
	
	@Column(name = "xmstat")
	private String xmstat;
	
	@Column(name = "xdeptname")
	private String xdeptname;
	
	@Column(name = "xsec")
	private String xsec;

	@Column(name = "xdesignation")
	private String xdesignation;
	
	@Column(name = "xdatejoin")
	@Temporal(TemporalType.DATE)
	private Date xdatejoin;
	
	@Column(name = "xgrade")
	private String xgrade;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "xenddate")
	@Temporal(TemporalType.DATE)
	private Date xenddate;
	
	@Column(name = "xretdate")
	@Temporal(TemporalType.DATE)
	private Date xretdate;
	
	@Column(name = "xempstatus")
	private String xempstatus;
	
	@Column(name = "xdistrict")
	private String xdistrict;
	
	@Column(name = "xdatecom")
	@Temporal(TemporalType.DATE)
	private Date xdatecom;
	
	@Column(name = "xbank")
	private String xbank;
	
	@Column(name = "xacc")
	private String xacc;
	
	@Column(name = "xposition")
	private String xposition;
	
	@Column(name = "xtin")
	private String xtin;
	
	@Column(name = "xempcategory")
	private String xempcategory;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xsid")
	private String xsid;
	
	@Column(name = "xbloodgroup")
	private String xbloodgroup;

	@Column(name = "xphone")
	private String xphone;

	@Column(name = "xmobile")
	private String xmobile;

	@Column(name = "xplan")
	private String xplan;

	@Column(name = "xcar")
	private String xcar;

	@Column(name = "xgross")
	private String xgross;

	@Column(name = "xemname")
	private String xemname;

	@Column(name = "xrelation")
	private String xrelation;

	@Column(name = "xemcmobile")
	private String xemcmobile;
	
	@Column(name = "xreligion")
	private String xreligion;
	
	@Column(name = "xemptype")
	private String xemptype;
	
	@Column(name = "xsignature")
	private String xsignature;

	@Column(name = "xgrossold")
	private String xgrossold;
	
	@Column(name = "xshortname")
	private String xshortname;
	
	@Column(name = "xbnkpay")
	private String xbnkpay;
	
	@Column(name = "xweekday")
	private String xweekday;
	
	@Column(name = "xhdayapp")
	private String xhdayapp;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "allowlogin")
	private boolean allowlogin;

	@Column(name = "username")
	private String username;

	@Transient
	private String password;
	
	@Transient
	private String xbname;
}
