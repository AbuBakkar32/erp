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
@Table(name = "pofodesnd")
@IdClass(pofodesndPK.class)
@EqualsAndHashCode(of = { "zid", "xfono","xrow" }, callSuper = false)
public class pofodesnd extends AbstractModel<String>{
	
	private static final long serialVersionUID = -3104368033096858696L;
	

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xfono")
	private String xfono;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xfabdate")
	@Temporal(TemporalType.DATE)
	private Date xfabdate;
	
	@Column(name = "xprocess")
	private String xprocess;
	
	@Column(name = "xpino")
	private String xpino;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;
	
	


}
