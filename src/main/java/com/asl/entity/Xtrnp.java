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
@Table(name = "xtrnp")
@IdClass(XtrnpPK.class)
@EqualsAndHashCode(of = { "zid", "xtypetrn", "xtrn", "xtyperel" }, callSuper = false)
public class Xtrnp extends AbstractModel<String> {

	private static final long serialVersionUID = 8598532177594698314L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Id
	@Basic(optional = false)
	@Column(name = "xtrn")
	private String xtrn;

	@Id
	@Basic(optional = false)
	@Column(name = "xtyperel")
	private String xtyperel;

	@Column(name = "zemail")
	private String zemail;
	
	@Column(name = "xrel")
	private String xrel;
	
	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xlong")
	private String xlong;

}
