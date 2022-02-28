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
@Table(name = "cntdtl")
@IdClass(CountDePK.class)
@EqualsAndHashCode(of = { "zid", "xprplan","xrow","xcount" }, callSuper = false)
public class CountDe extends AbstractModel<String> {
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprplan")
	private String xprplan;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xcount")
	private int xcount;
	
	@Column(name = "xcomposition")
	private String xcomposition;
	
	@Column(name = "xtitem")
	private String xtitem;
	
	@Column(name = "xqty")
	private BigDecimal xqty;
	
	@Column(name = "xemail")
	private String xemail;
}
