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
@Table(name = "opcrndetail")
@IdClass(PocrndetailPK.class)
@EqualsAndHashCode(of = { "zid", "xrow", "xcrnnum" }, callSuper = false)
public class Opcrndetail extends AbstractModel<String> {

	private static final long serialVersionUID = -8703054248831312503L;

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

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xlong")
	private BigDecimal xlong;

	@Column(name = "xvatrate")
	private BigDecimal xvatrate;

	@Column(name = "xsrate")
	private BigDecimal xsrate;

	@Column(name = "xrategrn")
	private BigDecimal xrategrn;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	@Column(name = "xvatamt")
	private BigDecimal xvatamt;

	@Column(name = "xdiscamt")
	private BigDecimal xdiscamt;

	@Column(name = "xaitamt")
	private BigDecimal xaitamt;

	@Column(name = "xaitrate")
	private BigDecimal xaitrate;
	
	@Column(name = "xcfsel")
	private BigDecimal xcfsel;
	
	@Column(name = "xunitsel")
	private String xunitsel;


	@Transient
	private String xitemname;

	@Transient
	private BigDecimal prevqty;

}
