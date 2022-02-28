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
@Table(name = "prsewplan")
@IdClass(prsewplanPK.class)
@EqualsAndHashCode(of = { "zid", "xprsewpln","xrow" }, callSuper = false)
public class prsewplan extends AbstractModel<String>{


	
	private static final long serialVersionUID = 1732901715334725089L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprsewpln")
	private String xprsewpln;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xbtch")
	private String xbtch;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xsize")
	private String xsize;
	
	@Column(name = "xcolor")
	private String xcolor;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xmch")
	private String xmch;
	
	@Column(name = "xpart1")
	private String xpart1;
	
	@Column(name = "qty1")
	private String qty1;
	
	@Column(name = "xpart2")
	private String xpart2;
	
	@Column(name = "qty2")
	private String qty2;
	
	@Column(name = "xqty")
	private String xqty;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;

	
}
