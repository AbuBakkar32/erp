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

/**
 * @author Zubayer Ahamed
 * @since Jun 13, 2021
 */
@Data
@Entity
@Table(name = "caitemdetail")
@IdClass(CaitemdetailPK.class)
@EqualsAndHashCode(of = {"zid", "xitem", "xsubitem"}, callSuper = false)
public class Caitemdetail extends AbstractModel<String> {

	private static final long serialVersionUID = -6955919075141116226L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private String xitem;

	@Id
	@Basic(optional = false)
	@Column(name = "xsubitem")
	private String xsubitem;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xunit")
	private String xunit;

	@Transient
	private String xdesc;
}
