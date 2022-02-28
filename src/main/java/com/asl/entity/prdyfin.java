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
@Table(name = "prdyfin")
@IdClass(prdyfinPK.class)
@EqualsAndHashCode(of = { "zid", "xprod","xrow" }, callSuper = false)
public class prdyfin extends AbstractModel<String>{
	
	
	private static final long serialVersionUID = -290950912022985680L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprod")
	private String xprod;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xplnprps")
	private String xplnprps;
	
	@Column(name = "xbtch")
	private String xbtch;
	
	@Column(name = "xprocess")
	private String xprocess;
	
	@Column(name = "xrecipe")
	private String xrecipe;
	
	@Column(name = "xrecqty")
	private BigDecimal xrecqty;
	
	@Column(name = "xmch")
	private String xmch;
	
	@Column(name = "xmcqty")
	private BigDecimal xmcqty;
	
	@Column(name = "xplanqty")
	private BigDecimal xplanqty;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xrate")
	private String xrate;
	
	@Column(name = "xfeder")
	private String xfeder;
	
	@Column(name = "xfedqty")
	private BigDecimal xfedqty;
	
	@Column(name = "xfab")
	private String xfab;
	
	@Column(name = "xply")
	private String xply;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;
	
	
}
