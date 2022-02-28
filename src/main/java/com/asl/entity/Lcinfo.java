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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "lcinfo")
@IdClass(LcinfoPK.class)
@EqualsAndHashCode(of = { "zid", "xlcno" }, callSuper = false)
public class Lcinfo extends AbstractModel<String> {

	private static final long serialVersionUID = 7710691356591990506L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xlcno")
	private String xlcno;

	@Column(name = "xshipdate")
	@Temporal(TemporalType.DATE)
	private Date xshipdate;

	@Column(name = "xlcissuedate")
	@Temporal(TemporalType.DATE)
	private Date xlcissuedate;

	@Column(name = "xexpirydate")
	@Temporal(TemporalType.DATE)
	private Date xexpirydate;

	@Column(name = "xopenbank")
	private String xopenbank;

	@Column(name = "xadvisingbank")
	private String xadvisingbank;

	@Column(name = "xcur")
	private String xcur;

	@Column(name = "xexch")
	private BigDecimal xexch;

	@Column(name = "xlctype")
	private String xlctype;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xlcmargin")
	private BigDecimal xlcmargin;

	@Column(name = "xpiref")
	private String xpiref;

	@Column(name = "xstatuspor")
	private String xstatuspor;

	@Column(name = "xdatepi")
	@Temporal(TemporalType.DATE)
	private Date xdatepi;

	@Transient
	private String xorg;

	@Transient
	private String xname;

}
