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
@Table(name="poreqdetail")
@IdClass(PoreqdetailPK.class)
@EqualsAndHashCode(of = {"zid","xporeqnum","xrow"}, callSuper=false   )
public class Poreqdetail extends AbstractModel<String> {

	private static final long serialVersionUID = 7207543265686553579L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xporeqnum")
	private String xporeqnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xunitpur")
	private String xunitpur;
		
	@Column(name = "xqtypur")
	private BigDecimal xqtypur;
	
	@Column(name = "xrate")
	private BigDecimal xrate;
	
	@Column(name = "xlineamt")
	private BigDecimal xlineamt;
	
	@Column(name = "xspecification")
	private String xspecification;
	
	@Column(name = "xcfpur")
	private BigDecimal xcfpur;

	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xpurpose")
	private String xpurpose;

	@Column(name = "xgitem")
	private String xgitem;

	
	@Transient
	private String xitemdesc;

}
