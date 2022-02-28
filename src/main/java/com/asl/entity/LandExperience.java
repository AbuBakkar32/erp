package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "caexperience")
@IdClass(LandExperiencePK.class)
@EqualsAndHashCode(of = { "zid", "xperson", "xrow" }, callSuper = false)
@XmlRootElement(name = "experiences")
@XmlAccessorType(XmlAccessType.FIELD)
public class LandExperience extends AbstractModel<String> {

	private static final long serialVersionUID = -8985247501677946563L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xperson")
	private String xperson;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xname")
	private String xname;

	@Column(name = "xduration")
	private int xduration;

	@Column(name = "xdesignation")
	private String xdesignation;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xnote")
	private String xnote;

	@Transient
	public boolean newData;

}
