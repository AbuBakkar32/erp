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
@Table(name = "pofodesn")
@IdClass(pofodesnPK.class)
@EqualsAndHashCode(of = { "zid", "xfono" }, callSuper = false)
public class pofodesn extends AbstractModel<String>{
	
	
	private static final long serialVersionUID = 8256937866801983036L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xfono")
	private String xfono;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xbuyer")
	private String xbuyer;
	
	@Column(name = "xagent")
	private String xagent;
	
	@Column(name = "xfostrt")
	private String xfostrt;
	
	@Column(name = "xfile")
	private String xfile;
	
	@Column(name = "xshipdate")
	@Temporal(TemporalType.DATE)
	private Date xshipdate;
	
	@Column(name = "xordate")
	@Temporal(TemporalType.DATE)
	private Date xordate;
	
	@Column(name = "xshipment")
	private String xshipment;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xstatustep")
	private String xstatustep;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;
}
