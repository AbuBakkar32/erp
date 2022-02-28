package com.asl.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Transient;

@Data
@Entity
@Table(name = "optoari")
@IdClass(OptoariPK.class)
@EqualsAndHashCode(of = { "zid", "xtype", "xgcus", "xgitem" }, callSuper = false)
public class Optoari extends AbstractModel<String>{

	
	private static final long serialVersionUID = -4044410193866645018L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtype")
	private String xtype;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xgcus")
	private String xgcus;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xgitem")
	private String xgitem;

	@Column(name = "xaccdr")
	private String xaccdr;
	
	@Column(name = "xacccr")
	private String xacccr;
	
	@Transient
	private boolean newData;
	
	@Transient
	private String xddesc;
	
	@Transient
	private String xcdesc;

}
