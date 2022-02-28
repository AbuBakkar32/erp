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

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "poordheader")
@IdClass(PoordHeaderPK.class)
@EqualsAndHashCode(of = { "zid", "xpornum" }, callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoordHeader extends AbstractModel<String> {

	private static final long serialVersionUID = -5079810075388406733L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private String xpornum;

	@Column(name = "xsup")
	private String xsup;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xhwh")
	private String xhwh;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xstatuspor")
	private String xstatuspor;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xgrnnum")
	private String xgrnnum;

	@Column(name = "xordernum")
	private String xordernum;

	@Column(name = "xreqtype")
	private String xreqtype;

	@Column(name = "xpiref")
	private String xpiref;

	@Column(name = "xpidate")
	@Temporal(TemporalType.DATE)
	private Date xpidate;

	@Column(name = "xshipdate")
	@Temporal(TemporalType.DATE)
	private Date xshipdate;

	@Column(name = "xdeliloc")
	private String xdeliloc;

	@Column(name = "xlcno")
	private String xlcno;

	@Column(name = "xcur")
	private String xcur;

	@Column(name = "xexch")
	private BigDecimal xexch;

	@Column(name = "xvatrate")
	private BigDecimal xvatrate;

	@Column(name = "xdisc")
	private BigDecimal xdisc;

	@Column(name = "xdiscf")
	private BigDecimal xdiscf;

	@Column(name = "xtransport")
	private BigDecimal xtransport;

	@Column(name = "xdateeta")
	@Temporal(TemporalType.DATE)
	private Date xdateeta;
	
	@Column(name = "xdateetd")
	@Temporal(TemporalType.DATE)
	private Date xdateetd;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xrem")
	private String xrem;

	@Column(name = "xporeqnum")
	private String xporeqnum;

	@Column(name = "xnote1")
	private String xnote1;

	@Column(name = "xpreparer")
	private String xpreparer;
	
	@Column(name = "xvatamt")
	private BigDecimal xvatamt;
	
	@Column(name = "xregi")
	private String xregi;
	
	@Column(name = "xsign")
	private String xsign;
	
	@Column(name = "xprime")
	private BigDecimal xprime;
	
	@Column(name = "xbasetransport")
	private String xbasetransport;
	
	@Column(name = "xtwh")
	private String xtwh;
	
	@Column(name = "xsignreject")
	private String xsignreject;
	
	@Column(name = "xtornum")
	private String xtornum;
	
	@Column(name = "xquotnum")
	private String xquotnum;
	
	@CreationTimestamp
	@Column(name = "xprepdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xprepdate = new Date();
	
	@Column(name = "xsignatory1")
	private String xsignatory1;
	
	@Column(name = "xsignatorynote1")
	private String xsignatorynote1;
	
	@CreationTimestamp
	@Column(name = "xsignatorydate1")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xsignatorydate1 = new Date();
	
	@Column(name = "xdatedel")
	@Temporal(TemporalType.DATE)
	private Date xdatedel;
	
	@Column(name = "xinvnum")
	private String xinvnum;

	@Transient
	private String xorg;

	@Transient
	private String storeName;

	@Transient
	private String xpreparername;

	@Transient
	private String xsignatoryname;

	@Transient
	private String spellword;

	@Transient
	private String xprimeword;

	@Transient
	private String projectName;
}
