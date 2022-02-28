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
@Table(name = "lcamend")
@IdClass(LcamendPK.class)
@EqualsAndHashCode(of = { "zid", "xlcno","xrow" }, callSuper = false)
public class Lcamend extends AbstractModel<String>{

	
	private static final long serialVersionUID = -6870882966159232031L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xlcno")
	private String xlcno;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xapprover")
	private String xapprover;
	
	@Column(name = "xnote")
	private String xnote;

}
