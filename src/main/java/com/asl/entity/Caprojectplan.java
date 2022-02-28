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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "caprojectplan")
@IdClass(CaprojectplanPK.class)
@EqualsAndHashCode(of = {"zid","xproject","xrow"}, callSuper = false)
@XmlRootElement(name = "plans")
@XmlAccessorType(XmlAccessType.FIELD)
public class Caprojectplan extends AbstractModel<String>{

	private static final long serialVersionUID = -5194100843502047877L;
	

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xproject")
	private String xproject;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xrole")
	private String xrole;

	@Column(name = "xresponsibility")
	private String xresponsibility;

	@Column(name = "xfdate")
	@Temporal(TemporalType.DATE)
	private Date xfdate;

	@Column(name = "xtdate")
	@Temporal(TemporalType.DATE)
	private Date xtdate;

	@Column(name = "xnote")
	private String xnote;

	@Transient
	private String xstaffName;
	
	@Transient
	private String siteName;

}
