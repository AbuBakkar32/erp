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
@Table(name = "cadag")
@IdClass(CadagPK.class)
@EqualsAndHashCode(of = { "zid", "xdagtype", "xdagnum", "xdagid" }, callSuper = false)
public class Cadag extends AbstractModel<String> {

	private static final long serialVersionUID = -7142504662225344710L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdagnum")
	private int xdagnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xdagtype")
	private String xdagtype;

	@Id
	@Basic(optional = false)
	@Column(name = "xdagid")
	private int xdagid;

	@Column(name = "xamt")
	private BigDecimal xamt;

	@Column(name = "xamtmap")
	private BigDecimal xamtmap;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xnote")
	private String xnote;
}
