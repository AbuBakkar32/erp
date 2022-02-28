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
@Table(name = "btchpldt")
@IdClass(btchpldtPK.class)
@EqualsAndHashCode(of = { "zid", "xbtchpln","xrow" }, callSuper = false)
public class btchpldt extends AbstractModel<String>{
	 
	private static final long serialVersionUID = -4764193510040020939L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbtchpln")
	private String xbtchpln;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xmch")
	private String xmch;
	
	@Column(name = "mcqty")
	private BigDecimal mcqty;
	
	@Column(name = "xfono")
	private String xfono;
	
	@Column(name = "xfab")
	private String xfab;
	
	@Column(name = "xgauge")
	private String xgauge;
	
	@Column(name = "xgsm")
	private String xgsm;
	
	@Column(name = "xcolor")
	private String xcolor;
	
	@Column(name = "xplnqty")
	private BigDecimal xplnqty;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;
	
	
	
	

}
