package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "cadocument")
@IdClass(LandDocumentPK.class)
@EqualsAndHashCode(of = { "zid", "xdoc", "xrow" }, callSuper = false)
@XmlRootElement(name = "documents")
@XmlAccessorType(XmlAccessType.FIELD)
public class LandDocument extends AbstractModel<String> {

	private static final long serialVersionUID = -1482216443995609989L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdoc")
	private String xdoc;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xperson")
	private String xperson;

	@Column(name = "xsurveyor")
	private String xsurveyor;

	@Column(name = "xland")
	private String xland;

	@Column(name = "xdoctype")
	private String xdoctype;

	@Column(name = "xname")
	private String xname;

	@Column(name = "xdocument")
	private String xdocument;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xnameold")
	private String xnameold;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xproject")
	private String xproject;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xwh")
	private String xwh;

	@Transient
	private boolean newData;
	
	@Transient
	private String siteName;

}
