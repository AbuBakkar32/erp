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
@Table(name = "caeventmember")
@IdClass(LandEventsMemberPK.class)
@EqualsAndHashCode(of = { "zid", "xrow" }, callSuper = false)
public class LandEventsMember extends AbstractModel<String> {
	
	private static final long serialVersionUID = 5282790808037861201L;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Column(name = "xevent")
	private String xevent;

	@Column(name = "xperson ")
	private String xperson ;

	@Column(name = "xmembertype")
	private String xmembertype;

	@Column(name = "xname ")
	private String xname ;

	@Column(name = "xnote")
	private String xnote;
}
