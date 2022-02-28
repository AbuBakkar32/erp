package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pdeducation")
@IdClass(PdeducationPK.class)
@EqualsAndHashCode(of = { "zid", "xstaff","xrow" }, callSuper = false)
public class Pdeducation extends AbstractModel<String>{

	
	private static final long serialVersionUID = -1334320293203766547L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xstaff")
	private String xstaff;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xyear")
	private String xyear;

	@Column(name = "xexam")
	private String xexam;

	@Column(name = "xinstitute")
	private String xinstitute;

	@Column(name = "xmajor")
	private String xmajor;

	@Column(name = "xresult")
	private String xresult;

}
