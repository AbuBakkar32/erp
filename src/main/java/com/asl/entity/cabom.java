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
@Table(name = "cabom")
@IdClass(cabomPK.class)
@EqualsAndHashCode(of = { "zid", "xbom","xitembom" }, callSuper = false)
public class cabom extends AbstractModel<String>{

	
	private static final long serialVersionUID = 3723968595930399526L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbom")
	private String xbom;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xitembom")
	private int xitembom;
	
	@Column(name = "xdept")
	private String xdept;
	
	@Column(name = "xemail")
	private String xemail;

	
	@Column(name = "zemail")
	private String zemail;


}
