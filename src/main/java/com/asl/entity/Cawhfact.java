package com.asl.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "cawhfact")
@IdClass(CawhfactPK.class)
@EqualsAndHashCode(of = { "zid", "xtrnnum"}, callSuper = false)
@XmlRootElement(name = "tasks")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cawhfact extends AbstractModel<String> {

	private static final long serialVersionUID = 6542254159561402512L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtrnnum")
	private String xtrnnum;
	
	@Column(name = "xproject")
	private String xproject;
	
	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xstatus")
	private String xstatus;
	
	@Column(name = "xweight")
	private String xweight;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xwh")
	private String xwh;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xfdate")
	@Temporal(TemporalType.DATE)
	private Date xfdate;
	
	@Column(name = "xtdate")
	@Temporal(TemporalType.DATE)
	private Date xtdate;
	
	@Column(name = "xhours")
	private int xhours;
	
	@Column(name = "xstaff")
	private String xstaff;
	
	@Column(name = "xprogress")
	private int xprogress;
	
	@Column(name = "xtrn")
	private String xtrn;
	
	@Column(name = "xdtrnnum")
	private String xdtrnnum;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;
	
	@Transient
	private String projectName;
	
	@Transient
	private String siteName;
	
	@Transient
	private String staffName;
	
	@Transient
	private String taskName;

}
