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
@Table(name = "acchequedetail")
@IdClass(AcchequedetailPK.class)
@EqualsAndHashCode(of = { "zid", "xchqbookno", "xchequeno" }, callSuper = false)
public class Acchequedetail extends AbstractModel<String>{

	private static final long serialVersionUID = 1142110757029487483L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xchqbookno")
	private String xchqbookno;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xchequeno")
	private String xchequeno;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xmaturitydate")
	@Temporal(TemporalType.DATE)
	private Date xmaturitydate;
	
	@Column(name = "xacc")
	private String xacc;
	
	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xstatusjv")
	private String xstatusjv;
	
	@Column(name = "xstatusap")
	private String xstatusap;
	
	@Column(name = "xcus")
	private String xcus;
	
	@Column(name = "xpaidto")
	private String xpaidto;
	
	@Column(name = "xamount")
	private Integer xamount;
	
	@Column(name = "xstatus")
	private String xpaidtxstatuso;
	
	@Column(name = "xnote")
	private String xnote;
}
