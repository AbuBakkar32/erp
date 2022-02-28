package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "calanddag")
@IdClass(LandDagDetailsPK.class)
@EqualsAndHashCode(of = { "zid", "xrow" }, callSuper = false)
@XmlRootElement(name = "dags")
@XmlAccessorType(XmlAccessType.FIELD)
public class LandDagDetails extends AbstractModel<String> {

	
	private static final long serialVersionUID = -1980295047853746547L;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;
	
	@Column(name = "xland")
	private String xland;
	
	@Column(name = "xdagtype")
	private String xdagtype;
	
	@Column(name = "xdagnum")
	private Integer xdagnum;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xqty")
	private BigDecimal xqty;
	
	@Column(name = "xunit")
	private String xunit;

	
}
