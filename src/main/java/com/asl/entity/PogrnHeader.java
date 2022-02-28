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

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pogrnheader")
@IdClass(PogrnHeaderPK.class)
@EqualsAndHashCode(of = { "zid", "xgrnnum" }, callSuper = false)
public class PogrnHeader extends AbstractModel<String> {

	private static final long serialVersionUID = 3246354454094927618L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xgrnnum")
	private String xgrnnum;

	@Temporal(TemporalType.DATE)
	@Column(name = "xdate")
	private Date xdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "xdategl")
	private Date xdategl;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xhwh")
	private String xhwh;

	@Column(name = "xproject")
	private String xproject;

	@Column(name = "xstatusgrn")
	private String xstatusgrn;

	@Column(name = "xinvnum")
	private String xinvnum;

	@Column(name = "xpcnum")
	private String xpcnum;

	@Column(name = "xstatusap")
	private String xstatusap;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xpornum")
	private String xpornum;

	@Column(name = "xstatusjv")
	private String xstatusjv;

	@Column(name = "xstatuscrn")
	private String xstatuscrn;

	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xvatait")
	private String xvatait;

	@Column(name = "xvatamt")
	private BigDecimal xvatamt;

	@Column(name = "xaitamt")
	private BigDecimal xaitamt;

	@Column(name = "xdiscprime")
	private BigDecimal xdiscprime;

	@Column(name = "xgrandtot")
	private BigDecimal xgrandtot;

	@Column(name = "xdocnum")
	private String xdocnum;

	@Column(name = "xcrnnum")
	private String xcrnnum;

	@Column(name = "xtrn")
	private String xtrn;

	/*
	 * @Column(name = "xlcno") private String xlcno;
	 * 
	 */
	@Column(name = "xcur")
	private String xcur;

	@Column(name = "xexch")
	private BigDecimal xexch;

	@Column(name = "xfreightcost")
	private BigDecimal xfreightcost;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xstatusimgl")
	private String xstatusimgl;

	
	@Column(name = "xtypeobj")
	private String xtypeobj;

	@Column(name = "xstatusapgl")
	private String xstatusapgl;

	@Column(name = "xstatusjvgl")
	private String xstatusjvgl;
	
	@Column(name = "xstatusinsp")
	private String xstatusinsp;
	
	@Column(name = "xstatusdoc")
	private String xstatusdoc;
	
	@Column(name = "xstatusclaim")
	private String xstatusclaim;
	
	@Column(name = "xpreparer")
	private String xpreparer;
	
	@Column(name = "xporeqnum")
	private String xporeqnum;
	
	@Column(name = "xquotnum")
	private String xquotnum;
	 
	@Column(name = "xdisc")
	private BigDecimal xdisc;
	
	@Column(name = "xamtother")
	private BigDecimal xamtother;
	
	@CreationTimestamp
	@Column(name = "xprepdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xprepdate = new Date();

	@Column(name = "xdirectsup")
	private String xdirectsup;
	
	@Column(name = "xstatusprjtrn")
	private String xstatusprjtrn;

	@Transient
	private String xorg;

	@Transient 
	private String storename;

	@Transient 
	private String xpreparername;

	@Transient
	private String spellword;

	@Transient
	private String xprimeword;
	
	@Transient
	private String projectName;
}
