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
@Table(name = "achrc3")
@IdClass(Achrc3PK.class)
@EqualsAndHashCode(of = { "zid", "xhrc3" }, callSuper = false)
public class Achrc3 extends AbstractModel<String>{
	
	private static final long serialVersionUID = -3861938671021034446L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xhrc3")
	private String xhrc3;
	
	@Column(name = "xhrc1")
	private String xhrc1;
	
	@Column(name = "xdecs")
	private String xdecs;
	
	@Column(name = "xrow")
	private Integer xrow;
	
	@Column(name = "xacctype")
	private String xacctype;
	
	@Column(name = "xhrc2")
	private String xhrc2;

}
