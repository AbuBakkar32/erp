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
@Table(name = "moordheader")
@IdClass(MoordheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xordernum" }, callSuper = false)
public class Moordheader extends AbstractModel<String> {

	private static final long serialVersionUID = -6051833120994771096L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xordernum")
	private String xordernum;

	@Column(name = "xdatepln")
	@Temporal(TemporalType.DATE)
	private Date xdatepln;

	@Column(name = "xpreparer")
	private String xpreparer;

	@Column(name = "xhwh")
	private String xhwh;
	
	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xcontact")
	private String xcontact;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xstatusord")
	private String xstatusord;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xtrn")
	private String xtrn;
	
	@Column(name = "xstatusbom")
	private String xstatusbom;

}
