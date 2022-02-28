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
@Table(name = "prod")
@IdClass(ProProductionDePK.class)
@EqualsAndHashCode(of = { "zid", "xprod","xrow" }, callSuper = false)
public class ProProductionDe extends AbstractModel<String>{

	
	private static final long serialVersionUID = 6224901670082278346L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprod")
	private String xprod;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xprfor")
	private String xprfor;
	
	@Column(name = "xprplan")
	private String xprplan;
	
	@Column(name = "xrolqty")
	private BigDecimal xrolqty;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;

}
