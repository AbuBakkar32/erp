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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "calanservey")
@IdClass(LandSurveyPK.class)
@EqualsAndHashCode(of = { "zid", "xland", "xrow" }, callSuper = false)
@XmlRootElement(name = "surveys")
@XmlAccessorType(XmlAccessType.FIELD)
public class LandSurvey extends AbstractModel<String> {

	private static final long serialVersionUID = -3036589229579591189L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xland")
	private String xland;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xsurveyor")
	private String xsurveyor;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xnote")
	private String xnote;

	
	@Column(name = "xlandqtyg")
	private BigDecimal xlandqtyg;
	
	@Column(name = "xlandunitg")
	private String xlandunitg;
	
	@Column(name = "xlandqtydsg")
	private BigDecimal xlandqtydsg;
	
	@Column(name = "xlandqtyksg")
	private BigDecimal xlandqtyksg;
	
	@Column(name = "xlandqtyn")
	private BigDecimal xlandqtyn;
	
	@Column(name = "xlandunitn")
	private String xlandunitn;
	
	@Column(name = "xlandqtydsn")
	private BigDecimal xlandqtydsn;
	
	@Column(name = "xlandqtyksn")
	private BigDecimal xlandqtyksn;

	@Column(name = "xstatus")
	private String xstatus;

	@Transient
	private boolean newData;
	
	@Transient
	private String formatedDate;
	
	@Transient
	private String xsername;

}
