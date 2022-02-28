package com.asl.entity;

import java.util.Date;

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
@Table(name = "achrc1")
@IdClass(Achrc1PK.class)
@EqualsAndHashCode(of = { "zid", "xhrc1" }, callSuper = false)
public class Achrc1 extends AbstractModel<String>{
	
	private static final long serialVersionUID = -3861938671021034446L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xhrc1")
	private String xhrc1;
	
	@Column(name = "xdecs")
	private String xdecs;
	
	@Column(name = "xrow")
	private Integer xrow;
	
	@Column(name = "xacctype")
	private String xacctype;
	
	
}
