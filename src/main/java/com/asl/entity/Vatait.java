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
@Table(name = "vatait")
@IdClass(VataitPK.class)
@EqualsAndHashCode(of = { "zid", "xvatait" }, callSuper = false)
public class Vatait extends AbstractModel<String> {

	private static final long serialVersionUID = -6070326246496029469L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvatait")
	private String xvatait;

	@Column(name = "xvat")
	private BigDecimal xvat;

	@Column(name = "xait")
	private BigDecimal xait;

	@Column(name = "xstartdate")
	@Temporal(TemporalType.DATE)
	private Date xstartdate;

	@Column(name = "xenddate")
	@Temporal(TemporalType.DATE)
	private Date xenddate;

}
