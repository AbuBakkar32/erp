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
@Table(name = "prodfocn")
@IdClass(ProFODetailsPK.class)
@EqualsAndHashCode(of = { "zid", "xfono" }, callSuper = false)
public class ProFODetails extends AbstractModel<String> {
	
	private static final long serialVersionUID = 6226751036986789888L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xfono")
	private String xfono;

	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xfloor")
	private String xfloor;
	
	@Column(name = "xlong")
	private String xlong;
	



}
