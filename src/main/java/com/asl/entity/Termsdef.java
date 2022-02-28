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
@Table(name = "termsdef")
@IdClass(TermsdefPK.class)
@EqualsAndHashCode(of = { "zid", "xtype","xterm","xtermid"}, callSuper = false)
public class Termsdef extends AbstractModel<String> {


	private static final long serialVersionUID = 8882672884177365019L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtype")
	private String xtype;

	@Id
	@Basic(optional = false)
	@Column(name = "xterm")
	private String xterm;

	@Id
	@Basic(optional = false)
	@Column(name = "xtermid")
	private int xtermid;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xserial")
	private int xserial;

}
