package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "cabank")
@IdClass(CabankPK.class)
@EqualsAndHashCode(of = { "zid", "xbank" }, callSuper = false)
public class Cabank extends AbstractModel<String> {

	private static final long serialVersionUID = -6051833120994771096L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbank")
	private String xbank;

	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xname")
	private String xname;

	@Column(name = "xbranch")
	private String xbranch;

	@Column(name = "xmadd")
	private String xmadd;

	@Column(name = "xacc")
	private String xacc;

	@Column(name = "xswiftcode")
	private String xswiftcode;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Transient
	private String accountname;

}
