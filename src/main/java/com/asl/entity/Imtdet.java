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
@Table(name = "imtdet")
@IdClass(ImtdetPK.class)
@EqualsAndHashCode(of = { "zid", "xtagnum", "xrow" }, callSuper = false)
public class Imtdet extends AbstractModel<String> {

	private static final long serialVersionUID = -2277712716108894115L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtagnum")
	private String xtagnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xstatustag")
	private String xstatustag;

	// Additionaly added
	@Column(name = "xunit")
	private String xunit;

	@Transient
	private String itemname;
	@Transient
	private String xgitem;
	@Transient
	private String xcatitem;

}
