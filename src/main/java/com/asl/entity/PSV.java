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

/**
 * @author Zubayer Ahamed
 * @since May 20, 2021
 */
@Data
@Entity
@Table(name = "psv")
@IdClass(ProductionStockValidationPK.class)
@EqualsAndHashCode(of = { "zid", "xchalan", "xbatch" }, callSuper = false)
public class PSV extends AbstractModel<String> {

	private static final long serialVersionUID = 3545140129310367647L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xchalan")
	private String xchalan;

	@Id
	@Basic(optional = false)
	@Column(name = "xbatch")
	private String xbatch;

	@Column(name = "xrawitem")
	private String xrawitem;

	@Column(name = "xprod")
	private BigDecimal xprod;
}
