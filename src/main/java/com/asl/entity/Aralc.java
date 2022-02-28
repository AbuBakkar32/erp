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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "aralc")
@IdClass(AralcPK.class)
@EqualsAndHashCode(of = { "zid", "xvoucher", "xrow" }, callSuper = false)
public class Aralc extends AbstractModel<String>{

	private static final long serialVersionUID = -7016112989379562652L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private String xvoucher;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;
	
	@Column(name = "xinvnum")
	private String xinvnum;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xdatedue")
	@Temporal(TemporalType.DATE)
	private Date xdatedue;
	
	@Column(name = "xbalance")
	private Integer xbalance;
	
	@Column(name = "xamount")
	private Integer xamount;
	
	@Column(name = "xdornum")
	private String xdornum;
	
	@Column(name = "xpornum")
	private String xpornum;

	@Column(name = "xgrnnum")
	private String xgrnnum;
	
	@Column(name = "xprimebuyer")
	private Integer xprimebuyer;
	
	@Column(name = "xbase")
	private Integer xbase;

}
