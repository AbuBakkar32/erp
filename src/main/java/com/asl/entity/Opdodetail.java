package com.asl.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "opdodetail")
@IdClass(OpdodetailPK.class)
@EqualsAndHashCode(of = { "zid", "xrow", "xdornum" }, callSuper = false)
public class Opdodetail extends AbstractModel<String> {

	private static final long serialVersionUID = -2050527945264418934L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Id
	@Basic(optional = false)
	@Column(name = "xdornum")
	private String xdornum;

	@Column(name = "xunitsel")
	private String xunitsel;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xcomtype")
	private String xcomtype;

	@Column(name = "xdorrow")
	private int xdorrow;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xrategrn")
	private BigDecimal xrategrn;

	@Column(name = "xqtycrn")
	private BigDecimal xqtycrn;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xparentrow")
	private int xparentrow;

	@Column(name = "xserial")
	private String xserial;

	@Column(name = "xland")
	private String xland;

	@Column(name = "xvatrate")
	private BigDecimal xvatrate;

	@Column(name = "xvatamt")
	private BigDecimal xvatamt;

	@Column(name = "xopreqrow")
	private int xopreqrow;

	@Column(name = "xaitamt")
	private BigDecimal xaitamt;
	
	@Column(name = "xcfsel")
	private BigDecimal xcfsel;
	
	@Column(name = "xqtydel")
	private BigDecimal xqtydel;

	@Transient
	private String xdesc;
	@Transient
	private String xgitem;
	@Transient
	private String xcatitem;
	
	@Transient
	private String xitemdesc;
	
	@Transient
	private BigDecimal prevqty;
	
	@Transient
	private BigDecimal prevrate;
	
	@Transient
	private boolean forupdate;

	@Transient
	private List<Opdodetail> subitems = new ArrayList<>();
}
