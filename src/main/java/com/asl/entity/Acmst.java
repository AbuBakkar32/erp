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
@Table(name = "acmst")
@IdClass(AcmstPK.class)
@EqualsAndHashCode(of = { "zid", "xacc" }, callSuper = false)
public class Acmst extends AbstractModel<String> {

	private static final long serialVersionUID = -3861938671021034446L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xacc")
	private String xacc;

	@Column(name = "xgroup")
	private String xgroup;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xacctype")
	private String xacctype;

	@Column(name = "xnum")
	private Integer xnum;

	@Column(name = "xaccusage")
	private String xaccusage;

	@Column(name = "xlong")
	private String xlong;

	@Transient
	private String xhrc1;

	@Transient
	private String xhrc2;

	@Transient
	private String xhrc3;

	@Transient
	private String xhrc4;

	@Transient
	private String xhrc5;

	@Transient
	private String xgroupname;
	
	@Transient
	private String xsub;
	
	@Transient
	private String xorg;
}

