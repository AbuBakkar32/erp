package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "modetail")
@IdClass(ModetailPK.class)
@XmlRootElement(name = "batch")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(of = { "zid", "xrow", "xbatch" }, callSuper = false)
public class Modetail extends AbstractModel<String> {

	private static final long serialVersionUID = -4998991037047476853L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Id
	@Basic(optional = false)
	@Column(name = "xbatch")
	private String xbatch;

	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xqtyplan")
	private BigDecimal xqtyplan;
	
	@Column(name = "xqty")
	private BigDecimal xqty;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xsign")
	private int xsign;
	
	@Column(name = "xnote")
	private String xnote;
	
	//Previous Fields
	
	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xstype")
	private String xstype;

	@Column(name = "xqtyreq")
	private BigDecimal xqtyreq;

	@Column(name = "xqtyactual")
	private BigDecimal xqtyactual;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xbomrow")
	private int xbomrow;

	@Transient
	private String xdesc;

	@Transient
	private BigDecimal displayableXqtyReq;
}
