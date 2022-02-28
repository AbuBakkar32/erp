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
@Table(name = "piestimtn")
@IdClass(piestimtnPk.class)
@EqualsAndHashCode(of = { "zid", "xpino","xrow" }, callSuper = false)
public class piestimtn extends AbstractModel<String>{

	
	private static final long serialVersionUID = 8037722992028495639L;


	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpino")
	private String xpino;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xcolor")
	private String xcolor;
	
	@Column(name = "xinspcdate")
	private String xinspcdate;
	
	@Column(name = "xtitemprt")
	private String xtitemprt;
	
	@Column(name = "xpacktyp")
	private String xpacktyp;
	
	@Column(name = "xpackqty")
	private BigDecimal xpackqty;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xsize1")
	private String xsize1;
	
	@Column(name = "xqty1")
	private BigDecimal xqty1;
	
	@Column(name = "xrate1")
	private BigDecimal xrate1;
	
	@Column(name = "xsize2")
	private String xsize2;
	
	@Column(name = "xqty2")
	private BigDecimal xqty2;
	
	@Column(name = "xrate2")
	private BigDecimal xrate2;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;
	
}
