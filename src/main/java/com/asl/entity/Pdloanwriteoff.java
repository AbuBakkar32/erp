package com.asl.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pdloanwriteoff")
@IdClass(PdloanwriteoffPK.class)
@EqualsAndHashCode(of = { "zid", "xvoucher","xrow" }, callSuper = false)
public class Pdloanwriteoff extends AbstractModel<String>{

	
	private static final long serialVersionUID = 1155838303272630626L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private String xvoucher;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xloanamt")
	private BigDecimal xloanamt;
	
	@Column(name = "xnote")
	private String  xnote;
	
	@Column(name = "xstatus")
	private String  xstatus;
	
	@Column(name = "xstatustag")
	private String  xstatustag;

}
