package com.asl.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "acdef")
@EqualsAndHashCode(of = {"zid"}, callSuper = false)
public class Acdef extends AbstractModel<String> {

	private static final long serialVersionUID = 8879484564833804963L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Column(name = "xyear")
	private String xyear;

	@Column(name = "xper")
	private int xper;

	@Column(name = "xoffset")
	private int xoffset;

	@Column(name = "xaccpl")
	private String xaccpl;

	@Column(name = "xaccrule")
	private String xaccrule;

	@Column(name = "xgetper")
	private String xgetper;

	@Column(name = "xname")
	private String xname;

	@Column(name = "xlength")
	private int xlength;

	@Column(name = "xaccgl")
	private String xaccgl;

	@Column(name = "xyesno")
	private String xyesno;

	@Column(name = "xday")
	private int xday;

	@Column(name = "xdatedue")
	@Temporal(TemporalType.DATE)
	private Date xdatedue;

	@Column(name = "xpreventry")
	private String xpreventry;

	@Column(name = "xdateexp")
	@Temporal(TemporalType.DATE)
	private Date xdateexp;

	@Column(name = "xbacklock")
	private String xbacklock;
	
	@Column(name = "xaccrec")
	private String xaccrec;
	
	@Column(name = "xaccpay")
	private String xaccpay;

	@Transient
	private boolean dataExist;

	@Transient 
	private String xaccpldesc;
	
	@Transient
	private String xaccgldesc;
	
	@Transient
	private String xaccrecdesc;
	
	@Transient
	private String xaccpaydesc;

}
