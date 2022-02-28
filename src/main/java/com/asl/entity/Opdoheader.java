package com.asl.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@Table(name = "opdoheader")
@IdClass(OpdoheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xdornum" }, callSuper = false)
public class Opdoheader extends AbstractModel<String> {

	private static final long serialVersionUID = -3094969631197911920L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdornum")
	private String xdornum;

	// This column acts as chalan reference
	@Column(name = "xdocnum")
	private String xdocnum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xvatamt")
	private BigDecimal xvatamt;

	@Column(name = "xperson")
	private String xperson;

	@Column(name = "xpaymenttype")
	private String xpaymenttype;

	@Column(name = "xdiscamt")
	private BigDecimal xdiscamt;

	@Column(name = "xordernum")
	private String xordernum;

	@Column(name = "xstatusord")
	private String xstatusord;

	@Column(name = "xstatusjv")
	private String xstatusjv;

	@Column(name = "xpaid")
	private BigDecimal xpaid;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xchange")
	private BigDecimal xchange;

	@Column(name = "xpaystatus")
	private String xpaystatus;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xstatusar")
	private String xstatusar;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xhwh")
	private String xhwh;

	@Column(name = "xdatecom")
	@Temporal(TemporalType.DATE)
	private Date xdatecom;

	@Column(name = "xdatedue")
	@Temporal(TemporalType.DATE)
	private Date xdatedue;

	@Column(name = "xcur")
	private String xcur;

	@Column(name = "xexch")
	private BigDecimal xexch;

	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	// Added as for confirm Opdo procedure pre event
	@Column(name = "xgrandtot")
	private BigDecimal xgrandtot;

	@Column(name = "xvatait")
	private String xvatait;

	@Column(name = "xait")
	private BigDecimal xait; 

	@Column(name = "xchalancreated")
	private boolean xchalancreated;

	@Column(name = "requisitionnumber")
	private String requisitionnumber;

	@Column(name = "xland")
	private String xland;

	@Column(name = "xpurpose")
	private String xpurpose;

	@Column(name = "xreqnum")
	private String xreqnum;
	
	@Column(name = "xaitamt")
	private BigDecimal xaitamt;
	
	@Transient
	private String xtrnopdo;

	@Transient
	private String xorg;

	@Transient
	private String xphone;
	
	@Transient
	private BigDecimal prevqty;
	
	@Transient
	private BigDecimal prevrate;
	
	@Transient
	private boolean forupdate;
	
	@Transient
	private List<Opdodetail> salesDetails = new ArrayList<>();
	
	@Transient
	private String spellword;
	
	@Transient
	private String xprimeword;
	
	@Transient
	private String spellword2;
	
	@Transient
	private String xprimeword2;

	@Transient
	private String projectName;
}
