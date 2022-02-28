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

@Data
@Entity
@Table(name = "cawh")
@IdClass(CawhPK.class)
@EqualsAndHashCode(of = { "zid","xwh" }, callSuper = false)
@XmlRootElement(name = "sites")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cawh extends AbstractModel<String>{

	private static final long serialVersionUID = 4133958615400805829L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xname")
	private String xname;

	@Column(name = "xproject")
	private String xproject;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xlocation")
	private String xlocation;
	
	@Column(name = "xsupervisor")
	private String xsupervisor;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xstartdate")
	@Temporal(TemporalType.DATE)
	private Date xstartdate;
	
	@Column(name = "xenddate")
	@Temporal(TemporalType.DATE)
	private Date xenddate;
	
	@Column(name = "xamountbudgt")
	private BigDecimal xamountbudgt;
	
	@Column(name = "xamountcost")
	private BigDecimal xamountcost;
	
	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xdesc")
	private String xdesc;
	
	@Column(name = "xtotamt")
	private BigDecimal xtotamt;
	
	@Column(name = "xtrn")
	private String xtrn;

	@Transient
	private String xorg;
	
	@Transient
	private boolean newData;
	
	@Transient
	private String xsupervisorName;

	@Transient
	private String xwhName;
	
	@Transient
	private String projectName;
	
	@Transient
	private String siteName;

}
