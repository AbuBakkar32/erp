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
@Table(name = "prcutplan")
@IdClass(prcutplanPK.class)
@EqualsAndHashCode(of = { "zid", "xprcutpln","xrow" }, callSuper = false)
public class prcutplan extends AbstractModel<String>{
	

	
	private static final long serialVersionUID = 8860807201233004252L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprcutpln")
	private String xprcutpln;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xbtch")
	private String xbtch;
	
	@Column(name = "xroll")
	private String xroll;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xsize")
	private String xsize;
	
	@Column(name = "xcolor")
	private String xcolor;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xpart1")
	private String xpart1;
	
	@Column(name = "xmch1")
	private String xmch1;
	
	@Column(name = "qty1")
	private BigDecimal qty1;
	
	@Column(name = "xpart2")
	private String xpart2;
	
	@Column(name = "xmch2")
	private String xmch2;
	
	@Column(name = "qty2")
	private BigDecimal qty2;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;

}
