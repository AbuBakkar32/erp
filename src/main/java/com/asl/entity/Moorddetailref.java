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
@Table(name = "moorddetailref")
@IdClass(MoorddetailrefPK.class)
@EqualsAndHashCode(of = { "zid", "xordernum","xrow" }, callSuper = false)
public class Moorddetailref extends AbstractModel<String> {

	private static final long serialVersionUID = -6051833120994771096L;

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
	@Column(name = "xordernum")
	private String xordernum;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xunitsel")
	private String xunitsel;

	@Column(name = "xdocnum")
	private String xdocnum;

	@Column(name = "xdocrow")
	private int xdocrow;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xqtydoc")
	private BigDecimal xqtydoc;
	
	@Column(name = "xcfsel")
	private BigDecimal xcfsel;


}
