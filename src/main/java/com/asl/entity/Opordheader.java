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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Data
@Entity
@Table(name = "opordheader")
@IdClass(OpordheaderPK.class)
@XmlRootElement(name = "opordheader")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(of = { "zid", "xordernum" }, callSuper = false)
public class Opordheader extends AbstractModel<String> {

	private static final long serialVersionUID = 4164878691134692756L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xordernum")
	private String xordernum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xstatusord")
	private String xstatusord;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xpornum")
	private String xpornum;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xchalanref")
	private String xchalanref;

	@Column(name = "batchcreated")
	private boolean batchcreated;

	@Column(name = "productioncompleted")
	private boolean productioncompleted;

	@Column(name = "invoicecreated")
	private boolean invoicecreated;
	
	@Column(name = "xreqnum")
	private String xreqnum;

	// Following variables are added for Convention Management

	@Column(name = "xfunction")
	private String xfunction;

	@Column(name = "xtotguest")
	private Integer xtotguest;

	@Column(name = "xbookdate")
	@Temporal(TemporalType.DATE)
	private Date xbookdate;

	@Column(name = "xfuncdate")
	@Temporal(TemporalType.DATE)
	private Date xfuncdate;

	@Column(name = "xfacamt")
	private BigDecimal xfacamt;

	@Column(name = "xfoodamt")
	private BigDecimal xfoodamt;

	@Column(name = "xdiscamt")
	private BigDecimal xdiscamt;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xgrandtot")
	private BigDecimal xgrandtot;

	@Column(name = "xadvamt")
	private BigDecimal xadvamt;
	

	// Following variables are added for Convention Management (Room Booking)

	@Column(name = "xcheckindate")
	@Temporal(TemporalType.DATE)
	private Date xcheckindate;

	@Column(name = "xcheckoutdate")
	@Temporal(TemporalType.DATE)
	private Date xcheckoutdate;

	@Column(name = "xroomamt")
	private BigDecimal xroomamt;

	@Column(name = "xdornum")
	private String xdornum;

	@Column(name = "xtornum")
	private String xtornum;

	@Column(name= "xstartdate")
	@Temporal(TemporalType.DATE)
	private Date xstartdate;

	@Column(name= "xenddate")
	@Temporal(TemporalType.DATE)
	private Date xenddate;

	@Column(name = "xstarttime")
	private String xstarttime;

	@Column(name = "xendtime")
	private String xendtime;

	@Column(name = "xhallamt")
	private BigDecimal xhallamt;

	@Column(name = "xfunctionamt")
	private BigDecimal xfunctionamt;

	@Column(name = "xvatait")
	private String xvatait;

	@Column(name = "xvatamt")
	private BigDecimal xvatamt;

	@Column(name = "xaitamt")
	private BigDecimal xaitamt;

	@Column(name = "xpaymenttype")
	private String xpaymenttype;

	@Column(name = "xpaystatus")
	private String xpaystatus;

	@Column(name = "xpaid")
	private BigDecimal xpaid;

	@Column(name = "suggestioncreated")
	private boolean suggestionCreated;

	@Column(name = "xdue")
	private BigDecimal xdue;

	// FOR production calan transfer roder
	@Column(name = "rawxtornum")
	private String rawxtornum;

	@Column(name = "finishedxtornum")
	private String finishedxtornum;

	@Column(name = "rawtocomp")
	private boolean rawtocomp;

	@Column(name = "finishedtocomp")
	private boolean finishedtocomp;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xhwh")
	private String xhwh;

	@Column(name = "xreqitemtype")
	private String xreqitemtype;


	@Transient
	private boolean isbooked;

	@Transient
	private String branchname;

	@Transient
	private String xorg;
	
	@Transient
	private String customeraddress;
	
	@Transient
	private String projectName;
	
	@Transient
	private String storeName;
	
	@Transient
	private String spellword;

	@Transient
	private String xprimeword;
}
