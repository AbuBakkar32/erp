package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pogrndetail")
@IdClass(PogrnDetailPK.class)
@EqualsAndHashCode(of = { "zid", "xgrnnum", "xrow" }, callSuper = false)
public class PogrnDetail extends AbstractModel<String> {

	private static final long serialVersionUID = 208492314185601309L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xgrnnum")
	private String xgrnnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xdocrow")
	private int xdocrow;

	@Column(name = "xcomtype")
	private String xcomtype;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqtygrn")
	private BigDecimal xqtygrn;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xqtyprn")
	private BigDecimal xqtyprn;

	@Column(name = "xunitpur")
	private String xunitpur;

	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xpornum")
	private String xpornum;
	
	@Column(name = "xbase")
	private BigDecimal xbase;
	
	@Column(name = "xqtyrec")
	private BigDecimal xqtyrec;
	
	@Column(name = "xqtyclaim")
	private BigDecimal xqtyclaim;
	
	@Column(name = "xdisc")
	private BigDecimal xdisc;
	
	@Column(name = "xdiscf")
	private BigDecimal xdiscf;
	
	@Column(name = "xqtyinsp")
	private BigDecimal xqtyinsp;
	
	@Column(name = "xcfpur")
	private BigDecimal xcfpur;
	
	@Column(name = "xqtycom")
	private BigDecimal xqtycom;

	@Column(name = "xstatusimgl")
	private String xstatusimgl;
	
	@Column(name = "xspecification")
	private String xspecification;
	
	@Column(name = "xvatrate")
	private BigDecimal xvatrate;
	
	@Column(name = "xvatamt")
	private BigDecimal xvatamt;
	
	@Column(name = "xait")
	private BigDecimal xait;
	
	@Column(name = "xaitamt")
	private BigDecimal xaitamt;
	
	@Column(name = "xamtother")
	private BigDecimal xamtother;

	@Column(name = "xqtypur")
	private BigDecimal xqtypur;

	@Column(name = "xpurpose")
	private String xpurpose;
	
	@Column(name = "xgitem")
	private String xgitem;
	
	@Transient
	private String xitemdesc;
	@Transient
	private String xcatitem;

	@Transient
	private BigDecimal prevqty;
}
