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
@Table(name = "cawhcostest")
@IdClass(CawhcostestPK.class)
@EqualsAndHashCode(of = {"zid","xwh","xrow"}, callSuper = false)
@XmlRootElement(name = "costests")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cawhcostest extends AbstractModel<String>{

	private static final long serialVersionUID = -5194100843502047877L;
	

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xwh")
	private String xwh;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xpurpose")
	private String xpurpose;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xfdate")
	@Temporal(TemporalType.DATE)
	private Date xfdate;

	@Column(name = "xtdate")
	@Temporal(TemporalType.DATE)
	private Date xtdate;

	@Column(name = "xgitem")
	private String xgitem;
	
	@Transient
	private String xitemdesc;

}
