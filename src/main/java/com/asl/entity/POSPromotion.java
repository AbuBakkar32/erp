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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pospromotion")
@IdClass(POSPromotionPK.class)
@EqualsAndHashCode(of = { "zid", "xpromono" }, callSuper = false)
public class POSPromotion extends AbstractModel<String>{
	
	
	private static final long serialVersionUID = -5099011764190463394L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpromono")
	private String xpromono;

	@Column(name = "xstartdate")
	@Temporal(TemporalType.DATE)
	private Date xstartdate;

	@Column(name = "xenddate")
	@Temporal(TemporalType.DATE)
	private Date xenddate;
	
	@Column(name = "xstarttime")
	private String xstarttime;
	
	@Column(name = "xendtime")
	private String xendtime;
	
	@Column(name = "xdisc")
	private BigDecimal xdisc;
	
	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xdisctype")
	private String xdisctype;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Transient
	private boolean newdata;
	

}
