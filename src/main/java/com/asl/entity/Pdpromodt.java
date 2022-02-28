package com.asl.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pdpromodt")
@IdClass(PdpromodtPK.class)
@EqualsAndHashCode(of = { "zid", "xstaff","xrow" }, callSuper = false)
public class Pdpromodt extends AbstractModel<String>{

	
	private static final long serialVersionUID = 6009399903898081850L;
	
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
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xdesignation")
	private String xdesignation;
	
	@Column(name = "xdesigpromo")
	private String xdesigpromo;
	
	@Column(name = "xgrade")
	private String xgrade;
	
	@Column(name = "xgradetran")
	private String xgradetran;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xdateeff")
	@Temporal(TemporalType.DATE)
	private Date xdateeff;
	
	@Column(name = "xgrossold")
	private String xgrossold;

}
