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
@Table(name = "imtordetail")
@IdClass(ImtorDetailPK.class)
@EqualsAndHashCode(of = { "zid", "xtornum", "xrow" }, callSuper = false)
public class ImtorDetail extends AbstractModel<String> {
	
	private static final long serialVersionUID = 7944093590743606577L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtornum")
	private String xtornum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xchalanref")
	private String xchalanref;
	
	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xunit")
	private String xunit;
		
	@Column(name = "xqtyord")
	private BigDecimal xqtyord;
	
	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xlineamt")
	private BigDecimal xlineamt;
	
	@Transient
	private String xitemdesc;
	@Transient
	private String xcatitem;
	@Transient
	private String xgitem;
	
}
