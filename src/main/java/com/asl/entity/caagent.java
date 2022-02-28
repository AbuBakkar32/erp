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
@Table(name = "caagent")
@IdClass(caagentPK.class)
@EqualsAndHashCode(of = { "zid", "xcus","xname" }, callSuper = false)
public class caagent extends AbstractModel<String>{
	
	
	private static final long serialVersionUID = 5150957555618057406L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xcus")
	private String xcus;

	@Id
	@Basic(optional = false)
	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xadd")
	private String xadd;

	@Column(name = "xcity")
	private String xcity;
	
	@Column(name = "xcountry")
	private String xcountry;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "zemail")
	private String zemail;
}
