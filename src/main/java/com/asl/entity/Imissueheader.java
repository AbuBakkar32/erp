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
@Table(name = "imissueheader")
@IdClass(ImissueheaderPK.class)
@EqualsAndHashCode(of = { "zid","xissuenum" }, callSuper = false)
public class Imissueheader extends AbstractModel<String>{

	private static final long serialVersionUID = 1212820269421173777L;


	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xissuenum")
	private String xissuenum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xwh")
	private String xwh;
	
	@Column(name = "xhwh")
	private String xhwh;

	@Column(name = "xstatus ")
	private String xstatus;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xperparer")
	private String xperparer;

	@Column(name = "xprepdate")
	@Temporal(TemporalType.DATE)
	private Date xprepdate;
	
	@Column(name = "xstatusjv")
	private String xstatusjv;
	
	@Column(name = "xvoucher")
	private String xvoucher;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xtrn")
	private String xtrn;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Transient
	private String storeName;

	@Transient
	private String xpreparername;
	
	@Transient
	private String siteName;
	
	@Transient
	private String projectName;
}
