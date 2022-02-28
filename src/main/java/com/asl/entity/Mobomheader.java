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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "mobomheader")
@IdClass(MobomheaderPK.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = { "zid", "xbomkey" }, callSuper = false)
public class Mobomheader extends AbstractModel<String> {

	private static final long serialVersionUID = -6051833120994771096L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbomkey")
	private String xbomkey;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xcost")
	private BigDecimal xcost;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xisdefaut")
	private Boolean xisdefaut = Boolean.TRUE;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xtrn")
	private String xtrn;
	
	@Transient
	private BigDecimal xitemName;
	
	@Transient
	private BigDecimal xcusName;

}
