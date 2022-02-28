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
@Table(name = "calandevent")
@IdClass(LandEventsPK.class)
@EqualsAndHashCode(of = { "zid", "xland","xrow" }, callSuper = false)
public class LandEvents extends AbstractModel<String> {

	
	private static final long serialVersionUID = -1880326655786232349L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xland")
	private String xland;

	@Column(name = "xperson")
	private String xperson;

	@Column(name="xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xplace")
	private String xplace;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xtype")
	private String xtype;
	
	@Transient
	private String xpername;
	
	@Transient
	private String formatedDate;
	
}
