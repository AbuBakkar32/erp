package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.asl.enums.ProfileType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
@Data
@Entity
@Table(name = "Profile")
@IdClass(ProfilePK.class)
@EqualsAndHashCode(of = { "zid", "profilecode" }, callSuper = false)
public class Profile extends AbstractModel<String> {

	private static final long serialVersionUID = 2616243655037864169L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "profilecode")
	@Size(max = 50, message = "Profile code maximum 50 character allowed")
	private String profilecode;

	@Column(name = "description")
	@Size(max = 500, message = "Description maximum 500 character allowed")
	private String description;

	@Column(name = "profiletype")
	@Enumerated(EnumType.STRING)
	private ProfileType profiletype;

	@Transient
	private String newflag;
}
