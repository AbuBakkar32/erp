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
@Table(name = "cawhbudget")
@IdClass(CawhbudgetPK.class)
@EqualsAndHashCode(of = { "zid","xbudget" }, callSuper = false)
public class Cawhbudget extends AbstractModel<String>{

	private static final long serialVersionUID = -6301025430170233576L;
	

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbudget")
	private String xbudget;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xprepdate")
	@Temporal(TemporalType.DATE)
	private Date xprepdate;

	@Column(name = "xproject")
	private String xproject;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Transient
	private String xorg;
	
	@Transient
	private String siteName;
	
	@Transient
	private String projectName;
	

}
