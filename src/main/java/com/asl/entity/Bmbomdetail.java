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
@Table(name = "bmbomdetail")
@IdClass(BmbometailPK.class)
@EqualsAndHashCode(of = { "zid", "xbomrow", "xbomkey" }, callSuper = false)
public class Bmbomdetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1351221374079490990L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbomrow")
	private int xbomrow;

	@Id
	@Basic(optional = false)
	@Column(name = "xbomkey")
	private String xbomkey;

	@Column(name = "xstype")
	private String xstype;

	@Column(name = "xqtymix")
	private BigDecimal xqtymix;

	@Column(name = "xbomcomp")
	private String xbomcomp;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xqtymin")
	private int xqtymin;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xwhdesc")
	private String xwhdesc;

	@Column(name = "xassembly")
	private String xassembly;

	@Column(name = "xfixedqty")
	private String xfixedqty;

}
