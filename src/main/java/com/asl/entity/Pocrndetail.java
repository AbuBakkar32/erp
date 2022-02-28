package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pocrndetail")
@IdClass(PocrndetailPK.class)
@EqualsAndHashCode(of = { "zid", "xrow", "xcrnnum" }, callSuper = false)
public class Pocrndetail extends AbstractModel<String> {

	private static final long serialVersionUID = -3661015633860005572L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcrnnum")
	private String xcrnnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xqtygrn")
	private BigDecimal xqtygrn;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xdocrow")
	private int xdocrow;

	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xrategrn")
	private BigDecimal xrategrn;
	
	@Column(name = "xcost")
	private BigDecimal xcost;
	
	@Column(name = "xcfpur")
	private BigDecimal xcfpur;

	@Transient
	private String xitemname;

	@Transient
	private BigDecimal prevqty;
}
