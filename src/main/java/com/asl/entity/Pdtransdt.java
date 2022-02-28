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
@Table(name = "pdtransdt")
@IdClass(PdtransdtPK.class)
@EqualsAndHashCode(of = { "zid", "xstaff","xrow" }, callSuper = false)
public class Pdtransdt extends AbstractModel<String>{

	
	private static final long serialVersionUID = 9192522828582527213L;

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
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xdeptname")
	private String xdeptname;
	
	@Column(name = "xdepttran")
	private String xdepttran;
	
	@Column(name = "xdateeff")
	@Temporal(TemporalType.DATE)
	private Date xdateeff;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xstatus")
	private String xstatus;
	
}
