
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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "imtrn")
@IdClass(ImtrnPK.class)
@EqualsAndHashCode(of = { "zid", "ximtrnnum" }, callSuper = false)
public class Imtrn extends AbstractModel<String> {

	private static final long serialVersionUID = 6798215592259037811L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "ximtrnnum")
	private String ximtrnnum;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xordernum")
	private String xordernum;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xval")
	private BigDecimal xval;

	@Column(name = "xdocnum")
	private String xdocnum;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	@Column(name = "xstatusjv")
	private String xstatusjv;

	@Column(name = "xsign")
	private Integer xsign;

	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xvalpost")
	private BigDecimal xvalpost;
	
	@Column(name = "xcqtyuse")
	private BigDecimal xcqtyuse;
	
	@Column(name = "xrateavg")
	private BigDecimal xrateavg;
	
	@Column(name = "xpornum")
	private String xpornum;
	
	@Column(name = "xhwh")
	private String xhwh;

	@Transient
	private String itemdes;

	@Transient
	private String xlong;

	@Transient
	private String storename;

	@Transient
	private String xtypetrn;
	
	@Transient
	private String projectName;

}
