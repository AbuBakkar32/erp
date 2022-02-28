package com.asl.entity;

import java.math.BigDecimal;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "camachine")
@IdClass(ProMachineMasterPK.class)
@EqualsAndHashCode(of = { "zid", "xmch" }, callSuper = false)
public class ProMachineMaster extends AbstractModel<String>{

	
	private static final long serialVersionUID = 8754771163087953878L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xmch")
	private String xmch;
	
	@Column(name = "xname")
	private String xname;
	
	@Column(name = "xqty")
	private BigDecimal xqty;
	
	@Column(name = "xprloss")
	private BigDecimal xprloss;
	
	@Column(name = "xqtymain")
	private BigDecimal xqtymain;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xdept")
	private String xdept;
	
	@Column(name = "xemail")
	private String xemail;
	
	

}
