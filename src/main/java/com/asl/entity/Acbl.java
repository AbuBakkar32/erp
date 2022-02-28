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
@Table(name = "acbl")
@IdClass(AcblPK.class)
@EqualsAndHashCode(of = { "zid", "xvoucher", "xrow" }, callSuper = false)
public class Acbl extends AbstractModel<String>{

	private static final long serialVersionUID = -7016112989379562652L;
	
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
	private Integer xrow;
	
	@Column(name = "xacc")
	private String xacc;
	
	@Column(name = "xprime")
	private Integer xprime;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xyear")
	private Integer xyear;
	
	@Column(name = "xper")
	private Integer xper;
	
	@Column(name = "xsub")
	private String xsub;
	
	@Column(name = "xwh")
	private String xwh;
	
	@Column(name = "xhwh")
	private String xhwh;
	
	@Column(name = "xinvnum")
	private String xinvnum;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xlcno")
	private String xlcno;
	
	@Column(name = "xhlong")
	private String xhlong;
	
	@Column(name = "xgrnnum")
	private String xgrnnum;
	
	@Column(name = "xdatechq")
	@Temporal(TemporalType.DATE)
	private Date xdatechq;
	
	@Column(name = "xchequeno")
	private String xchequeno;

}
