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
@Table(name = "prplandy")
@IdClass(prplandyPK.class)
@EqualsAndHashCode(of = { "zid", "xprplan","xrow" }, callSuper = false)
public class prplandy extends AbstractModel<String>{

	
	private static final long serialVersionUID = 3121755106155113270L;

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
	
	@Column(name = "xclosedate")
	@Temporal(TemporalType.DATE)
	private Date xclosedate;
	
	@Column(name = "xbtch")
	private String xbtch;
	
	@Column(name = "xplnprps")
	private String xplnprps;
	
	@Column(name = "xprocess")
	private String xprocess;
	
	@Column(name = "xfono")
	private String xfono;
	
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
