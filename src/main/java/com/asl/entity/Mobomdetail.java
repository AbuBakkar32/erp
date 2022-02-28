package com.asl.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "mobomdetail")
@IdClass(MobomdetailPK.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = { "zid", "xbomkey", "xrow" }, callSuper = false)
public class Mobomdetail extends AbstractModel<String> {

	private static final long serialVersionUID = -4071410919170114859L;

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
	@Column(name = "xbomkey")
	private String xbomkey;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xsign")
	private int xsign;

}
