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
@Table(name = "cawhbudgetdt")
@IdClass(CawhbudgetdtPK.class)
@EqualsAndHashCode(of = { "zid","xbudget" ,"xrow"}, callSuper = false)
@XmlRootElement(name = "budgetdetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cawhbudgetdt extends AbstractModel<String>{

	private static final long serialVersionUID = 6815335490617259246L;
	

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbudget")
	private String xbudget;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xpurpose")
	private String xpurpose;

	@Column(name = "xamount")
	private BigDecimal xamount;

	@Column(name = "xtolerance")
	private BigDecimal xtolerance;

	@Column(name = "xnote")
	private String xnote;
	
	@Transient
	private BigDecimal tolPer;;

}
