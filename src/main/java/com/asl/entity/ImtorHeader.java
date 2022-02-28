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
@Table(name = "imtorheader")
@IdClass(ImtorHeaderPK.class)
@EqualsAndHashCode(of = { "zid", "xtornum" }, callSuper = false)
public class ImtorHeader extends AbstractModel<String> {

	private static final long serialVersionUID = -1246892187349642823L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtornum")
	private String xtornum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xdatecom")
	@Temporal(TemporalType.DATE)
	private Date xdatecom;

	@Column(name = "xfwh")
	private String xfwh;

	@Column(name = "xtwh")
	private String xtwh;

	@Column(name = "xstatustor")
	private String xstatustor;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xchalanref")
	private String xchalanref;

	@Column(name = "xtypetrn")
	private String xtypetrn;
	
	@Column(name = "xtranfer")
	private String xtranfer;

	@Column(name = "xpreparer")
	private String xpreparer;

	/*
	 * @Column(name = "xstatusreq") private String xstatusreq;
	 */
	
	@Column(name = "xstatus")
	private String xstatus;

	@CreationTimestamp
	@Column(name = "xreqdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xreqdate = new Date();
	
	@Column(name = "xsignatory1")
	private String xsignatory1;
	
	@CreationTimestamp
	@Column(name = "xsignatorydate1")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xsignatorydate1 = new Date();
	
	@Column(name = "xsignatorynote1")
	private String xsignatorynote1;
	
	@Column(name = "xacknowledge")
	private String xacknowledge;
	
	@Column(name = "xacknowledgenote")
	private String xacknowledgenote;
	
	@CreationTimestamp
	@Column(name = "xdaterec")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xdaterec = new Date();
	
	@Column(name = "xfhwh")
	private String xfhwh;

	@Column(name = "xthwh")
	private String xthwh;
	
	@Column(name = "xtotamt")
	private BigDecimal xtotamt;
	
	@Column(name = "xstaff")
	private String xstaff;
	
	@Column(name = "xdocnum")
	private String xdocnum;
	
	@Transient
	private String xorg;
	
	@Transient
	private String xfwhdesc;
	
	@Transient
	private String xtwhdesc;
	
	@Transient
	private String fPojectName;
	
	@Transient
	private String tPojectName;
	
	@Transient
	private String xpreparerName;
	
	@Transient
	private String xsignatory1Name;
	
	@Transient
	private String xacknowledgeName;
	
	@Transient
	private String xstaffName;
}
