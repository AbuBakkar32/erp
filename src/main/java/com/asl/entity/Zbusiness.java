package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.asl.enums.ProfileType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Feb 27; 2021
 */

@Data
@Entity
@Table(name = "zbusiness")
@EqualsAndHashCode(of = { "zid" }, callSuper = false)
public class Zbusiness extends AbstractModel<String> {

	private static final long serialVersionUID = 6962533993345872473L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Column(name = "zorg")
	private String zorg;

	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xmadd")
	private String xmadd;

	@Column(name = "xphone")
	private String xphone;

	@Column(name = "xpadd")
	private String xpadd;

	@Column(name = "xfax")
	private String xfax;

	@Column(name = "xcontact")
	private String xcontact;

	@Column(name = "xcity")
	private String xcity;

	@Column(name = "xemail")
	private String xemail;

	@Column(name = "xsignatory")
	private String xsignatory;

	@Column(name = "xcur")
	private String xcur;

	@Column(name = "xstate")
	private String xstate;

	@Column(name = "xzip")
	private String xzip;

	@Column(name = "xdformat")
	private String xdformat;

	@Column(name = "xdsep")
	private String xdsep;

	@Column(name = "xurl")
	private String xurl;

	@Column(name = "xtin")
	private String xtin;

	@Column(name = "xcountry")
	private String xcountry;

	@Column(name = "xvatregno")
	private String xvatregno;

	@Column(name = "xcustom")
	private String xcustom;

	@Column(name = "xtimage")
	private String xtimage;

	@Column(name = "xbimage")
	private String xbimage;

	@Column(name = "central")
	private Boolean central;

	@Column(name = "branch")
	private Boolean branch;

	@Column(name = "centralzid")
	private String centralzid;

	@Column(name = "xoutlet")
	private String xoutlet;

	@Column(name = "xshopno")
	private String xshopno;

	@Column(name = "xterminalno")
	private String xterminalno;

	@Column(name = "xmusakno")
	private String xmusakno;

	@Column(name = "xdine")
	@Enumerated(EnumType.STRING)
	private ProfileType xdine;

	@Lob
	@Column(name = "ximage")
	private byte[] ximage;

	@Column(name = "xresource")
	private String xresource;

}
