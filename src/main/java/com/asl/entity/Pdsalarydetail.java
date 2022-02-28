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
@Table(name = "pdsalarydetail")
@IdClass(PdsalarydetailPK.class)
@EqualsAndHashCode(of = { "zid", "xstaff","xrow" }, callSuper = false)
public class Pdsalarydetail extends AbstractModel<String>{

	
	private static final long serialVersionUID = 7431866698474606596L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xstaff")
	private String xstaff;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xcode")
	private String xcode;
	
	@Column(name = "xamount")
	private BigDecimal xamount;
	
	@Column(name = "xsign")
	private String xsign;
	
	

}
