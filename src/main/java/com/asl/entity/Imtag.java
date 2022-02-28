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
@Table(name = "imtag")
@IdClass(ImtagPK.class)
@EqualsAndHashCode(of = { "zid", "xtagnum" }, callSuper = false)
public class Imtag extends AbstractModel<String> {

	private static final long serialVersionUID = -2195786645048298298L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtagnum")
	private String xtagnum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xdatecom")
	@Temporal(TemporalType.DATE)
	private Date xdatecom;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xstatustag")
	private String xstatustag;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;
	
	@Column(name = "xhwh")
	private String xhwh;

	@Transient
	private String xtrnimtag;
	
	@Transient
	private String itemname;
	
	@Transient
	private String projectName;
	
	@Transient
	private String storeName;
}
