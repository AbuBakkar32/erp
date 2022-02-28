package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "imtogli")
@IdClass(ImtogliPK.class)
@EqualsAndHashCode(of = { "zid", "xtrn", "xgitem" }, callSuper = false)
public class Imtogli extends AbstractModel<String>{

	
	private static final long serialVersionUID = 3570219155177916404L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtrn")
	private String xtrn;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xgitem")
	private String xgitem;

	@Column(name = "xacc")
	private String xacc;
	
	@Column(name = "xaccdr")
	private String xaccdr;
	
	@Column(name = "xacccr")
	private String xacccr;

	@Transient
	private boolean newData;
	
	@Transient
	private String xddesc;
	
	@Transient
	private String xcdesc;
	
	@Transient
	private String xaccount;
}
