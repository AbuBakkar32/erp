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

@Data
@Entity
@Table(name = "potoapi")
@IdClass(PotoapiPK.class)
@EqualsAndHashCode(of = { "zid", "xtype", "xgsup", "xgitem" }, callSuper = false)
public class Potoapi extends AbstractModel<String>{

	
	private static final long serialVersionUID = 986061444723042558L;
	
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
	@Column(name = "xgsup")
	private String xgsup;
	
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
