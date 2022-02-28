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
@Table(name = "pocrnheader")
@IdClass(PocrnheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xcrnnum" }, callSuper = false)
public class Pocrnheader extends AbstractModel<String> {

	private static final long serialVersionUID = -5765658972092206991L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcrnnum")
	private String xcrnnum;

	@Column(name = "xgrnnum")
	private String xgrnnum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xstatusgrn")
	private String xstatusgrn;

	@Column(name = "xstatuscrn")
	private String xstatuscrn;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xhwh")
	private String xhwh;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xstatusap")
	private String xstatusap;

	@Column(name = "xpaymenttype")
	private String xpaymenttype;

	@Column(name = "xstatusjv")
	private String xstatusjv;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xdategl")
	@Temporal(TemporalType.DATE)
	private Date xdategl;

	@Column(name = "xtypetrn")
	private String xtypetrn;
	
	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Transient
	private String xorg;

	@Transient
	private String spellword;

	@Transient
	private String xprimeword;
	
	@Transient
	private String projectName;
	
	@Transient
	private String storeName;

}
