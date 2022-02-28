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

/**
 * @author Zubayer Ahamed
 * @since Mar 22, 2021
 */
@Data
@Entity
@Table(name = "pro_sugg")
@IdClass(ProductionSuggestionPK.class)
@EqualsAndHashCode(of = { "zid", "chalan", "xitem" }, callSuper = false)
public class ProductionSuggestion extends AbstractModel<String> {

	private static final long serialVersionUID = -2124426271096419821L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "chalan")
	private String chalan;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private String xitem;

	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrawitem")
	private String xrawitem;

	@Column(name = "xrawqty")
	private BigDecimal xrawqty;

	@Column(name = "xrawdes")
	private String xrawdes;

	@Column(name = "xitemdesc")
	private String xitemdesc;

	@Column(name = "proid")
	private String proid;

	@Column(name = "xrawunit")
	private String xrawunit;

	@Column(name = "xitemunit")
	private String xitemunit;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

}
