package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "poorddetail")
@IdClass(PoordDetailPK.class)
@EqualsAndHashCode(of = { "zid", "xpornum", "xrow" }, callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoordDetail extends AbstractModel<String> {

	private static final long serialVersionUID = 4124498529281042988L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private String xpornum;

	@Id
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xunitpur")
	private String xunitpur;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xqtygrn")
	private BigDecimal xqtygrn;

	@Column(name = "xcfpur")
	private BigDecimal xcfpur;

	@Column(name = "xqtypur")
	private BigDecimal xqtypur;

	@Column(name = "xitemdesc")
	private String xitemdesc;

	@Column(name = "xcatitem")
	private String xcatitem;

	@Column(name = "xgitem")
	private String xgitem;

	@Column(name = "xqtylcop")
	private BigDecimal xqtylcop;

	@Column(name = "xlcrate")
	private BigDecimal xlcrate;

	@Column(name = "xlclineamt")
	private BigDecimal xlclineamt;

	@Column(name = "xspecification")
	private String xspecification;
	
	@Column(name = "xlcbase")
	private BigDecimal xlcbase;
	
	@Column(name = "xrategrn")
	private BigDecimal xrategrn;

	@Column(name = "xdocrow")
	private BigDecimal xdocrow;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xbase")
	private BigDecimal xbase;

	@Column(name = "xqtyprn")
	private BigDecimal xqtyprn;

	@Column(name = "xdisc")
	private BigDecimal xdisc;

	@Column(name = "xdiscf")
	private BigDecimal xdiscf;

	@Column(name = "xpurpose")
	private String xpurpose;

	@Transient
	private String itemname;
}
