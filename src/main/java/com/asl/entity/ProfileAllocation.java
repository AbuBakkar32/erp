package com.asl.entity;

import java.util.List;

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
 * @since Jan 4, 2021
 */
@Data
@Entity
@Table(name = "profileallocation")
@IdClass(ProfileAllocationPK.class)
@EqualsAndHashCode(of = { "zid","paid","zemail" }, callSuper = false)
public class ProfileAllocation extends AbstractModel<String> {

	private static final long serialVersionUID = -8668618359438471462L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "paid")
	private String paid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail", nullable = false)
	private String zemail;

	@Column(name = "rpcode")
	private String rpcode;

	@Column(name = "mpcode")
	private String mpcode;

	@Column(name = "upcode")
	private String upcode;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Transient
	private String xname;
	@Transient
	private List<Profile> reportProfiles;
	@Transient
	private List<Profile> menuProfiles;
	@Transient
	private List<Profile> userProfiles;
}
