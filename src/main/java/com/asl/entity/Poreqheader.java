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
@Table(name = "poreqheader")
@IdClass(PoreqheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xporeqnum" }, callSuper = false)
public class Poreqheader extends AbstractModel<String> {

	private static final long serialVersionUID = -1684969477091438547L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xporeqnum")
	private String xporeqnum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xhwh")
	private String xhwh;

	@Column(name = "xstatusreq")
	private String xstatusreq;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xquotnum")
	private String xquotnum;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xpreparer")
	private String xpreparer;

	@CreationTimestamp
	@Column(name = "xprepdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xprepdate = new Date();

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xsignatory1")
	private String xsignatory1;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xsignatorynote1")
	private String xsignatorynote1;

	@CreationTimestamp
	@Column(name = "xsignatorydate1")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xsignatorydate1 = new Date();

	@Column(name = "xpornum")
	private String xpornum;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Transient
	private String xorg;

	@Transient
	private String xwhdesc;

	@Transient
	private String xpreparername;
	
	@Transient
	private String xsignatoryname;
	
	@Transient
	private String test;
	
	@Transient
	private String storeName;
	
	@Transient
	private String projectName;
	
	@Transient
	private String spellword;
	
	@Transient
	private String xprimeword;

}
