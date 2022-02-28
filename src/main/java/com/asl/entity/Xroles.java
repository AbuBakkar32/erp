package com.asl.entity;

import java.math.BigDecimal;

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
@Table(name = "xroles")
@IdClass(XrolesPK.class)
@EqualsAndHashCode(of = { "zid", "xrole" }, callSuper = false)
public class Xroles extends AbstractModel<String> {

	private static final long serialVersionUID = -4092186415852339797L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrole")
	private String xrole;

	@Column(name = "xaccess")
	private String xaccess;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xmaxdisc")
	private BigDecimal xmaxdisc;

	@Column(name = "xmaxdiscf")
	private BigDecimal xmaxdiscf;

	@Column(name = "xshopno")
	private String xshopno;

	@Transient
	private boolean newdata;
}
