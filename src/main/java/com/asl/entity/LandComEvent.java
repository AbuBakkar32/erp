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
@Table(name = "caeventheader")
@IdClass(LandComEventPK.class)
@EqualsAndHashCode(of = { "zid", "xevent" }, callSuper = false)
public class LandComEvent extends AbstractModel<String>{

	
	private static final long serialVersionUID = -3121918395069610188L;

	@Id
	@Basic(optional=false)
	@Column(name="zid")
	private String zid;

	@Id
	@Basic(optional=false)
	@Column(name="xevent")
	private String xevent;

	@Column(name = "xcommittee")
	private String xcommittee;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xstartdate")
	@Temporal(TemporalType.DATE)
	private Date xstartdate;

	@Column(name = "xenddate")
	@Temporal(TemporalType.DATE)
	private Date xenddate;

	@Column(name = "xstarttime")
	private String xstarttime;
	
	@Column(name = "xendtime")
	private String xendtime;

	@Column(name = "xagenda")
	private String xagenda;

	@Column(name = "xplace")
	private String xplace;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

}
