package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "imissuedetail")
@IdClass(ImissuedetailPK.class)
@EqualsAndHashCode(of = { "zid","xissuenum" ,"xrow"}, callSuper = false)
@XmlRootElement(name = "issuedetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class Imissuedetail extends AbstractModel<String>{

	private static final long serialVersionUID = 5103530263796158895L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xissuenum")
	private String xissuenum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;
	
	@Column(name = "xrate")
	private BigDecimal xrate;
	
	@Column(name = "xpurpose")
	private String xpurpose;

	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xgitem")
	private String xgitem;
	
	@Transient
	private String itemname;


}
