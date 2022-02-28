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

@Data
@Entity
@Table(name = "prqcfin")
@IdClass(prqcfinPK.class)
@EqualsAndHashCode(of = { "zid", "xqcno","xrow" }, callSuper = false)
public class prqcfin extends AbstractModel<String>{
	

	private static final long serialVersionUID = -5124274816759281218L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xqcno")
	private String xqcno;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xqcfor")
	private String xqcfor;
	
	@Column(name = "xprod")
	private String xprod;
	
	@Column(name = "xqtydel")
	private BigDecimal xqtydel;
	
	@Column(name = "xqty")
	private BigDecimal xqty;
	
	@Column(name = "xqtyrej")
	private BigDecimal xqtyrej;
	
	@Column(name = "xqcrept")
	private String xqcrept;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;


}
