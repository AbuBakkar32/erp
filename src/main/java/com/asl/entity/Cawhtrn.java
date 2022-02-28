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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "cawhtrn")
@IdClass(CawhtrnPK.class)
@EqualsAndHashCode(of = { "zid","xtrnnum" }, callSuper = false)
public class Cawhtrn extends AbstractModel<String>{

	private static final long serialVersionUID = 8186241214317811461L;
	

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtrnnum ")
	private String xtrnnum;

	@Column(name = "xtype ")
	private String xtype ;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xpurpose ")
	private String xpurpose ;
	
	@Column(name = "xamount")
	private BigDecimal xamount;

	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xsign")
	private String xsign;
	
	@Column(name = "xdocnum ")
	private String xdocnum;
	
	@Column(name = "xdocrow ")
	private int xdocrow ;

	@Column(name = "xwh")
	private String xwh;
	
	@Column(name = "xproject ")
	private String xproject ;
	
	@Column(name = "xcus")
	private String xcus;
 
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;

}
