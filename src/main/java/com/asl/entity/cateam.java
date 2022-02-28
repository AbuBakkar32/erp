package com.asl.entity;

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
@Table(name = "cateam")
@IdClass(cateamPK.class)
@EqualsAndHashCode(of = { "zid", "xmember" }, callSuper = false)
public class cateam extends AbstractModel<String>{
	
	private static final long serialVersionUID = -593503398582190260L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xmember")
	private String xmember;

	
	@Column(name = "xteam")
	private String xteam;
	
	@Column(name = "xrole")
	private String xrole;

	@Column(name = "zemail")
	private String zemail;

	
}
