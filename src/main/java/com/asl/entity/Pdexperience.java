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
@Table(name = "pdexperience")
@IdClass(PdexperiencePK.class)
@EqualsAndHashCode(of = { "zid", "xstaff","xrow" }, callSuper = false)
public class Pdexperience extends AbstractModel<String>{
	
	private static final long serialVersionUID = 4541720188896850003L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xstaff")
	private String xstaff;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	
	@Column(name = "xnote")
	private String xnote;
}
