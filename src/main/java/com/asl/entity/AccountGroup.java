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
 * @since Jul 2, 2021
 */
@Data
@Entity
@Table(name = "acgroup")
@IdClass(AccountGroupPK.class)
@EqualsAndHashCode(of = { "zid","xagcode" }, callSuper = false)
public class AccountGroup extends AbstractModel<String> {

	private static final long serialVersionUID = -3471003487292096846L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xagcode")
	private String xagcode;

	@Column(name = "xagtype")
	private String xagtype;

	@Column(name = "xaglevel")
	private int xaglevel;

	@Column(name = "xagname")
	private String xagname;

	@Column(name = "xagparent")
	private String xagparent;

	@Transient
	private String parentname;

	@Transient
	private boolean existingRecord;
}
