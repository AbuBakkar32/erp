package com.asl.entity;

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
@Table(name = "btchhd")
@IdClass(btchhdPK.class)
@EqualsAndHashCode(of = { "zid", "xbtch" }, callSuper = false)
public class btchhd extends AbstractModel<String>{
	
	private static final long serialVersionUID = -4803437788515804016L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbtch")
	private String xbtch;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xfono")
	private String xfono;
	
	@Column(name = "xcolor")
	private String xcolor;
	
	@Column(name = "xrack")
	private String xrack;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;

}
