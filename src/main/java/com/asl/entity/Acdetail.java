package com.asl.entity;

import java.math.BigDecimal;
import java.util.List;

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
@Table(name = "acdetail")
@IdClass(AcdetailPK.class)
@EqualsAndHashCode(of = { "zid", "xvoucher", "xrow" }, callSuper = false)
public class Acdetail extends AbstractModel<String> {

	private static final long serialVersionUID = -4071410919170114859L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private String xvoucher;

	@Column(name = "xacc")
	private String xacc;

	@Column(name = "xprime")
	private BigDecimal xprime;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xsub")
	private String xsub;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xdebit")
	private BigDecimal xdebit;

	@Column(name = "xcredit")
	private BigDecimal xcredit;

	@Column(name = "xcountry")
	private String xcountry;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xregi")
	private String xregi;

	@Column(name = "xlcno")
	private String xlcno;

	@Column(name = "xinvnum")
	private String xinvnum;

	@Column(name = "xdeptname")
	private String xdeptname;

	@Column(name = "xbase")
	private String xbase;

	@Transient
	private String accountname;

	@Transient
	private String xorg;
	
	@Transient
	private String spellword;
	
	@Transient
	private String xprimeword;

	@Transient
	private List<Acsubview> subAccounts;

}
