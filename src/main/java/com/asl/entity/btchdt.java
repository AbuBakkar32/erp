package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "btchdt")
@IdClass(btchdtPK.class)
@EqualsAndHashCode(of = { "zid", "xbtch","xrow" }, callSuper = false)
public class btchdt extends AbstractModel<String>{
	
	private static final long serialVersionUID = 1690923974331679680L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbtch")
	private String xbtch;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xroll")
	private String xroll;
	
	@Column(name = "xbtchpln")
	private String xbtchpln;
	
	@Column(name = "xrolqty")
	private BigDecimal xrolqty;
	
	@Column(name = "xprlos")
	private String xprlos;
	
	@Column(name = "xqty")
	private BigDecimal xqty;
	
	@Column(name = "xyarn")
	private String xyarn;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;
}
