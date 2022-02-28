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
@Table(name = "acheader")
@IdClass(AcheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xvoucher" }, callSuper = false)
public class Acheader extends AbstractModel<String> {

	private static final long serialVersionUID = 6717625365457687256L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xstatusjv")
	private String xstatusjv;

	@Column(name = "xyear")
	private Integer xyear;

	@Column(name = "xper")
	private Integer xper;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xsub")
	private String xsub;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xlcno")
	private String xlcno;

	@Column(name = "xgrnno")
	private String xgrnno;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xdatechq")
	@Temporal(TemporalType.DATE)
	private Date xdatechq;

	@Column(name = "xchequeno")
	private String xchequeno;

	@Column(name = "xbank")
	private String xbank;

	@Column(name = "xinvnum")
	private String xinvnum;

	@Column(name = "xpornum")
	private String xpornum;

	@Column(name = "xdornum")
	private String xdornum;

	@Column(name = "xporeqnum")
	private String xporeqnum;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xpreparer")
	private String xpreparer;

	@Column(name = "xsignatory1")
	private String xsignatory1;

	@Column(name = "xsigndate1")
	@Temporal(TemporalType.DATE)
	private Date xsigndate1;

	@Column(name = "xfileref")
	private String xfileref;

	@Column(name = "xexch")
	private String xexch;

	@Column(name = "xcur")
	private String xcur;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xordernum")
	private String xordernum;

	@Column(name = "xbin")
	private String xbin;

	@Transient
	private String spellword;

	@Transient
	private String xprimeword;

	@Transient
	private String titlenum;

}
