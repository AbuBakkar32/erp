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
@Table(name = "achrc5")
@IdClass(Achrc5PK.class)
@EqualsAndHashCode(of = { "zid", "xhrc5" }, callSuper = false)
public class Achrc5 extends AbstractModel<String>{
	
	private static final long serialVersionUID = -3861938671021034446L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xhrc5")
	private String xhrc5;
	
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

	@Column(name = "xhrc3")
	private String xhrc3;
	
	@Column(name = "xhrc4")
	private String xhrc4;
}
