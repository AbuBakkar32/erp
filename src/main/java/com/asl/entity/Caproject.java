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
@Table(name = "caproject")
@IdClass(CaprojectPK.class)
@EqualsAndHashCode(of = { "zid","xproject" }, callSuper = false)
public class Caproject extends AbstractModel<String>{

	private static final long serialVersionUID = -562740803186200396L;


	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xproject")
	private String xproject;

	@Column(name = "xcus")
	private String xcus;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xestartdate")
	@Temporal(TemporalType.DATE)
	private Date xestartdate;
	
	@Column(name = "xeenddate")
	@Temporal(TemporalType.DATE)
	private Date xeenddate;
	
	@Column(name = "xastartdate")
	@Temporal(TemporalType.DATE)
	private Date xastartdate;
	
	@Column(name = "xaenddate")
	@Temporal(TemporalType.DATE)
	private Date xaenddate;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xamountbudgt")
	private BigDecimal xamountbudgt;
	
	@Column(name = "xamountcost")
	private BigDecimal xamountcost;
	
	@Column(name = "xcontact")
	private String xcontact;
	
	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xplannedby")
	private String xplannedby;
	
	@Column(name = "xdesc")
	private String xdesc;
	
	@Column(name = "xtrn")
	private String xtrn;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Transient
	private String xorg;
	
	@Transient
	private String xcontactName;
	
	@Transient
	private String xplannedbyName;
	
	@Transient
	private String projectName;
	
	@Transient
	private boolean newData;
}
