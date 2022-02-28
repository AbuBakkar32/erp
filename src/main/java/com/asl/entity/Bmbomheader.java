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
@Table(name = "bmbomheader")
@IdClass(BmbomheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xbomkey" }, callSuper = false)
public class Bmbomheader extends AbstractModel<String> {

	private static final long serialVersionUID = 244706165955249047L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbomkey")
	private String xbomkey;

	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xdiv")
	private String xdiv;

	@Column(name = "xqtybase")
	private Integer xqtybase;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xpreferbatchqty")
	private Integer xpreferbatchqty;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xtypetrn")
	private String xtypetrn;

}
