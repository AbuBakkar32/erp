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
@Table(name="cacommittee")
@IdClass(LandCommitteeInfoPK.class)
@EqualsAndHashCode(of= {"zid", "xcommittee"}, callSuper = false)

public class LandCommitteeInfo extends AbstractModel<String>{


	private static final long serialVersionUID = 4109623688511970299L;
	
	@Id
	@Basic(optional=false)
	@Column(name="zid")
	private String zid;

	@Id
	@Basic(optional=false)
	@Column(name="xcommittee")
	private String xcommittee;

	@Column(name="xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name="xelecdate")
	@Temporal(TemporalType.DATE)
	private Date xelecdate;
	
	@Column(name="xstartdate")
	@Temporal(TemporalType.DATE)
	private Date xstartdate;

	@Column(name="xenddate")
	@Temporal(TemporalType.DATE)
	private Date xenddate;

	@Column(name="xexpdate")
	@Temporal(TemporalType.DATE)
	private Date xexpdate;

	@Column(name="xstatus")
	private String xstatus;

	@Column(name="xnote ")
	private String xnote;
	
	@Transient
	private boolean newData;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;
}
