package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Data
@Entity
@Table(name = "xtrn")
@IdClass(XtrnPK.class)
@EqualsAndHashCode(of = { "zid","xtypetrn","xtrn" }, callSuper = false)
public class Xtrn extends AbstractModel<String> {

	private static final long serialVersionUID = 304110246928300496L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Id
	@Basic(optional = false)
	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xaction")
	private String xaction;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xnum")
	private Integer xnum;  // Starting

	@Column(name = "xinc")
	private Integer xinc;  // Increment

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xacc")
	private String xacc;

	@Column(name = "xrprefix")
	private String xrprefix;

	@Column(name = "xrsufix")
	private String xrsufix;

	@Column(name = "xformat")
	private String xformat;
}
