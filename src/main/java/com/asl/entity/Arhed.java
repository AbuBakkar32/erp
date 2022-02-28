package com.asl.entity;

import java.math.BigDecimal;
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
@Table(name = "arhed")
@IdClass(ArhedPK.class)
@EqualsAndHashCode(of = { "zid", "xvoucher" }, callSuper = false)
public class Arhed extends AbstractModel<String> {

	private static final long serialVersionUID = 2473303101908084113L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xbank")
	private String xbank;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xprime")
	private BigDecimal xprime;

	@Column(name = "xbalprime")
	private BigDecimal xbalprime;

	@Column(name = "xpaymenttype")
	private String xpaymenttype;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xdateref")
	@Temporal(TemporalType.DATE)
	private Date xdateref;

	@Column(name = "xvatait")
	private String xvatait;

	@Column(name = "xvatamt")
	private BigDecimal xvatamt;

	@Column(name = "xaitamt")
	private BigDecimal xaitamt;

	@Column(name = "xstatuschq")
	private String xstatuschq;

	@Column(name = "xdiscprime")
	private BigDecimal xdiscprime;

	@Column(name = "xstatusbnk")
	private String xstatusbnk;

	@Column(name = "xstatusjv")
	private String xstatusjv;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xdocnum")
	private String xdocnum;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xbase")
	private BigDecimal xbase;

	@Column(name = "xsign")
	private Integer xsign;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xcusledbal")
	private BigDecimal xcusledbal;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xpaymentterm")
	private String xpaymentterm;

	@Column(name = "xtypeobj")
	private String xtypeobj;

	@Column(name = "xsub")
	private String xsub;

	@Column(name = "xtypeadj")
	private String xtypeadj;

	@Column(name = "xtyperec")
	private String xtyperec;

	@Column(name = "xinvnum")
	private String xinvnum;

	@Column(name = "xcur")
	private String xcur;
	
	@Column(name = "xacc")
	private String xacc;

	@Column(name = "xland")
	private String xland;
	
	@Column(name = "xpurpose")
	private String xpurpose;

	@Transient
	private String xtrntype;

	@Transient
	private String xorg;

	@Transient
	private String customeraddress;

	@Transient
	private String xname;

	@Transient
	private String xename;
	
	@Transient
	private String spellword;
	
	@Transient
	private String xprimeword;
	
	@Transient
	private String bankHeader;
	
	@Transient
	private String accountname;
	
	@Transient
	private String landname;
	
	@Transient
	private String siteName;
	
	@Transient
	private String projectName;
	
	@Transient
	private String xstaffName;

}
