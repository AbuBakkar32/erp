package com.asl.entity;

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
@Table(name = "caperson")
@IdClass(LandPersonPK.class)
@EqualsAndHashCode(of = { "zid", "xperson" }, callSuper = false)
public class LandPerson extends AbstractModel<String> {

	private static final long serialVersionUID = -1930128578223377808L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xperson")
	private String xperson;

	@Column(name = "xname")
	private String xname;

	@Column(name = "xfname")
	private String xfname;

	@Column(name = "xmname")
	private String xmname;

	@Column(name = "xdob")
	@Temporal(TemporalType.DATE)
	private Date xdob;

	@Column(name = " xoccupation")
	private String xoccupation;

	@Column(name = "xcontact")
	private String xcontact;

	@Column(name = "xemail")
	private String xemail;

	@Column(name = "xphone")
	private String xphone;

	@Column(name = "xmadd")
	private String xmadd;

	@Column(name = "xpadd")
	private String xpadd;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xgender")
	private String xgender;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;
}
