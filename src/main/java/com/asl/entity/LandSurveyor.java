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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "casurveyor")
@IdClass(LandSurveyorPK.class)
@EqualsAndHashCode(of = { "zid", "xsurveyor" }, callSuper = false)
public class LandSurveyor extends AbstractModel<String> {

	private static final long serialVersionUID = -7233867795317696070L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xsurveyor")
	private String xsurveyor;

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
	
	@Transient
	private String xsername;

}
