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
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pdloantrn")
@IdClass(PdloantrnPK.class)
@EqualsAndHashCode(of = { "zid", "xvoucher" }, callSuper = false)
public class Pdloantrn extends AbstractModel<String>{

	
	private static final long serialVersionUID = -4624102931227588900L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xstaff")
	private String xstaff;
	
	@Column(name = "xloanamt")
	private BigDecimal xloanamt;
	
	@Column(name = "xinstallment")
	private int xinstallment;
	
	@Column(name = "xinsamt")
	private BigDecimal xinsamt;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xdateeff")
	@Temporal(TemporalType.DATE)
	private Date xdateeff;
	
	@Column(name = "xyear")
	private int xyear;
	
	@Column(name = "xper")
	private int xper;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xlastinsamt")
	private BigDecimal xlastinsamt;
	
	@Column(name = "xpaidinstallment")
	private BigDecimal xpaidinstallment;
	
	@Column(name = "xpaid")
	private BigDecimal xpaid;
	 
	@Column(name = "xamount")
	private BigDecimal  xamount;
	
	@Column(name = "xstatustag")
	private String  xstatustag;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;
	
	@Transient
	private String xename;

}
