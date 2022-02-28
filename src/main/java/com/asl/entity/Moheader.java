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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "moheader")
@IdClass(MoheaderPK.class)
@XmlRootElement(name = "batch")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(of = { "zid", "xbatch" }, callSuper = false)
public class Moheader extends AbstractModel<String> {

	private static final long serialVersionUID = -8270354822047219816L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbatch")
	private String xbatch;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xhwh")
	private String xhwh;
	
	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xbomkey")
	private String xbomkey;

	@Column(name = "xqtyplan")
	private BigDecimal xqtyplan;
	
	@Column(name = "xqty")
	private BigDecimal xqty;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xrate")
	private BigDecimal xrate;
	
	@Column(name = "xlineamt")
	private BigDecimal xlineamt;
	
	@Column(name = "xordernum")
	private String xordernum;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xstatusjv")
	private String xstatusjv;
	
	@Column(name = "xvoucher")
	private String xvoucher;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xstatusbom")
	private String xstatusbom;

	//Previous fields
	
	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xqtyprd")
	private BigDecimal xqtyprd;

	@Column(name = "xqtycom")
	private BigDecimal xqtycom;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xsup")
	private String xsup;

	@Column(name = "xstatusmor")
	private String xstatusmor;

	@Column(name = "xcus")
	private String xcus;

	@Column(name ="xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xchalan")
	private String xchalan;

	@Column(name = "bomexploaded")
	private boolean bomexploaded;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xproduction")
	private BigDecimal xproduction;

	@Column(name = "xqtyreq")
	private BigDecimal xqtyreq;

	@Transient
	private String xitemdesc;

	@Transient
	private String xqtyprdunit;

	@Transient
	@XmlElementWrapper(name = "batchdetails")
	@XmlElement(name = "batchdetail")
	private List<Modetail> modetails = new ArrayList<>();

	@Transient
	private BigDecimal deviation;
}
