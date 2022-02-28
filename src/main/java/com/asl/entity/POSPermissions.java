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
@Table(name = "pospermissions")
@IdClass(POSPermissionsPK.class)
@EqualsAndHashCode(of = { "zid", "xacc", "xrole" }, callSuper = false)
public class POSPermissions extends AbstractModel<String>{
	
	private static final long serialVersionUID = -6851870028563283249L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xacc")
	private String xacc;

	@Id
	@Basic(optional = false)
	@Column(name = "xrole")
	private String xrole;

	@Column(name = "xcusserial")
	private String xcusserial;

	@Column(name = "xdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xdate;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xdisc")
	private BigDecimal xdisc;

	@Column(name = "xlength")
	private Integer xlength;

	@Column(name = "xrel")
	private String xrel;

	@Column(name = "xservicecharge")
	private BigDecimal xservicecharge;

	@Column(name = "xsupserial")
	private String xsupserial;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xvatrate")
	private BigDecimal xvatrate;

	@Column(name = "xyear")
	private Integer xyear;

	@Column(name = "xyearperdate")
	private Integer xyearperdate;

	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xshopno")
	private String xshopno;

}
