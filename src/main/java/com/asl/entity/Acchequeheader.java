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
@Table(name = "acchequeheader")
@IdClass(AcchequeheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xchqbookno" }, callSuper = false)
public class Acchequeheader extends AbstractModel<String>{
	
	private static final long serialVersionUID = -6772835259876135780L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xchqbookno")
	private String xchqbookno;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xdaterec")
	@Temporal(TemporalType.DATE)
	private Date xdaterec;
	
	@Column(name = "xbank")
	private String xbank;
	
	@Column(name = "xnofleafs")
	private Integer xnofleafs;
	
	@Column(name = "xprefix")
	private String xprefix;
	
	@Column(name = "xfirstleaf")
	private String xfirstleaf;
	
	@Column(name = "xlastleaf")
	private String xlastleaf;
	
	@Column(name = "xstatus")
	private String xsatus;
	
	@Column(name = "xnote")
	private String xnote;
	
}
