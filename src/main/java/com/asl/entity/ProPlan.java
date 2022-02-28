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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "prplan")
@IdClass(ProPlanPK.class)
@EqualsAndHashCode(of = { "zid", "xprplan","xrow" }, callSuper = false)
public class ProPlan extends AbstractModel<String>{
	 
	private static final long serialVersionUID = 8131378513578963920L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprplan")
	private String xprplan;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xplanfor")
	private String xplanfor;
	
	@Column(name = "xscf")
	private String xscf;
	
	@Column(name = "xclosedate")
	@Temporal(TemporalType.DATE)
	private Date xclosedate;
	
	@Column(name = "xplnprps")
	private String xplnprps;
	
	@Column(name = "xfono")
	private String xfono;
	
	@Column(name = "xbuyer")
	private String xbuyer;
	
	@Column(name = "xplntyp")
	private String xplntyp;
	
	@Column(name = "xplanqty")
	private BigDecimal xplanqty;
	
	@Column(name = "xmch")
	private String xmch;
	
	@Column(name = "mcqty")
	private BigDecimal mcqty;
	
	@Column(name = "xdia")
	private String xdia;
	
	@Column(name = "xgauge")
	private String xgauge;
	
	@Column(name = "xgsm")
	private String xgsm;
	
	@Column(name = "xhead")
	private String xhead;
	
	@Column(name = "xlngth")
	private String xlngth;
	
	@Column(name = "xwdth")
	private String xwdth;
	
	@Column(name = "xcolor")
	private String xcolor;
	
	@Column(name = "xrate")
	private BigDecimal xrate;
	
	@Column(name = "xfeder")
	private String xfeder;
	
	@Column(name = "xfedqty")
	private BigDecimal xfedqty;
	
	@Column(name = "xnedqty")
	private BigDecimal xnedqty;
	
	@Column(name = "xrpm")
	private String xrpm;
	
	@Column(name = "xfab")
	private BigDecimal xfab;
	
	@Column(name = "xply")
	private String xply;
	
	@Column(name = "xcpi")
	private String xcpi;
	
	@Column(name = "xprlos")
	private String xprlos;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	
}
