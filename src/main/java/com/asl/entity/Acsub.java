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

/**
 * @author Zubayer Ahamed
 * @since Jul 25, 2021
 */
@Data
@Entity
@Table(name = "acsub")
@IdClass(AcsubPK.class)
@EqualsAndHashCode(of = {"zid","xacc","xsub"}, callSuper = false)
public class Acsub extends AbstractModel<String> {

	private static final long serialVersionUID = -4130957583791605752L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xacc")
	private String xacc;

	@Id
	@Basic(optional = false)
	@Column(name = "xsub")
	private String xsub;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xtypeobj")
	private String xtypeobj;

	@Transient
	private String accname;

	@Transient
	private boolean newData;
}
