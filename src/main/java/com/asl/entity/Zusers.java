package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "zusers")
public class Zusers{
	
	@Id
	@Basic(optional = false)
	@Column(name = "userid")
	private String userid;
	
	@Column(name = "zemail") 
	private String zemail;
	
	@Column(name = "xpassword")
	private String xpassword;
	
	@Column(name = "xsalute")
	private String xsalute;
	
	@Column(name = "xfirst")
	private String xfirst;
	
	@Column(name = "xmiddle")
	private String xmiddle;
	
	@Column(name = "xlast")
	private String xlast;
	
	@Column(name = "xtitle")
	private String xtitle;
	
	@Column(name = "xorg")
	private String xorg;
	
	@Column(name = "xadd1")
	private String xadd1;
	
	@Column(name = "xadd2")
	private String xadd2;
	
	@Column(name = "xcity")
	private String xcity;
	
	@Column(name = "xstate")
	private String xstate;
	
	@Column(name = "xzip")
	private String xzip;
	
	@Column(name = "xcountry")
	private String xcountry;
	
	@Column(name = "xphone")
	private String xphone;
	
	@Column(name = "xfax")
	private String xfax;
	
	@Column(name = "xurl")
	private String xurl;
	
	@Column(name = "zref")
	private String zref;
	
	@Column(name = "xemailc")
	private String xemailc;

}
