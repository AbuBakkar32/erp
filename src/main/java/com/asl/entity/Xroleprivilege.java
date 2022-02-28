package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ASL
 * @since Jan 30, 2021
 */
@Data
@Entity
@IdClass(XroleprivilegePK.class)
@Table(name = "xroleprivilege")
@EqualsAndHashCode(of = { "zid", "zscreen", "xrole" }, callSuper = false)
public class Xroleprivilege extends AbstractModel<String> {

	private static final long serialVersionUID = 476805549058959852L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrole")
	private String xrole;

	@Id
	@Basic(optional = false)
	@Column(name = "zscreen")
	private String zscreen;

	@Column(name = "xfields")
	private String xfields;

	@Column(name = "xoption")
	private String xoption;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xshopno")
	private String xshopno;

}
