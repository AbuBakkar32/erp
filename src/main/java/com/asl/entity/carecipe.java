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
@Table(name = "carecipe")
@IdClass(carecipePK.class)
@EqualsAndHashCode(of = { "zid", "xrecipe","xrow" }, callSuper = false)
public class carecipe extends AbstractModel<String>{
	
	private static final long serialVersionUID = 1263071832998139813L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrecipe")
	private String xrecipe;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xrecqty")
	private BigDecimal xrecqty;
	
	@Column(name = "xchm1")
	private String xchm1;
	
	@Column(name = "xqty1")
	private BigDecimal xqty1;
	
	@Column(name = "xchm2")
	private String xchm2;
	
	@Column(name = "xqty2")
	private BigDecimal xqty2;
	
	@Column(name = "xcolor1")
	private String xcolor1;
	
	@Column(name = "xcolorqty1")
	private BigDecimal xcolorqty1;
	
	@Column(name = "xcolor2")
	private String xcolor2;
	
	@Column(name = "xcolorqty2")
	private BigDecimal xcolorqty2;
	
	@Column(name = "xqtyw")
	private BigDecimal xqtyw;
	
	@Column(name = "xdept")
	private String xdept;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;
}
