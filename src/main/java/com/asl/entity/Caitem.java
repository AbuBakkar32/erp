package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Data
@Entity
@Table(name = "caitem")
@IdClass(CaitemPK.class)
@EqualsAndHashCode(of = { "zid","xitem" }, callSuper = false)
public class Caitem extends AbstractModel<String> {

	private static final long serialVersionUID = 304110246928300496L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xcatitem")
	private String xcatitem;
	
	@Column(name = "xsubcatitem")
	private String xsubcatitem;
	
	@Column(name = "xmember")
	private String xmember;

	@Column(name = "xgitem")
	private String xgitem;

	@Column(name = "xunitpur")
	private String xunitpur;

	@Column(name = "xcfpur")
	private BigDecimal xcfpur;

	@Column(name = "xunitsel")
	private String xunitsel;

	@Column(name = "xcfsel")
	private BigDecimal xcfsel;

	@Column(name = "xcodeold")
	private String xcodeold;

	@Column(name = "xcost")
	private BigDecimal xcost;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xdealerp")
	private BigDecimal xdealerp;

	@Column(name = "xminqty")
	private BigDecimal xminqty;
	
	@Column(name = "xminreqqty")
	private BigDecimal xminreqqty;

	@Column(name = "xmrp")
	private BigDecimal xmrp;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xtype")
	private String xtype;

	@Transient
	private String codeType;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xmodel")
	private String xmodel;

	@Column(name = "xproditem")
	private boolean xproditem;

	@Column(name = "xseqn")
	private Long xseqn = Long.valueOf(999999);

	@Column(name = "allowreq")
	private boolean allowreq;

	@Column(name = "xvatrate")
	private BigDecimal xvatrate;

	@Column(name = "xsetmenu")
	private boolean xsetmenu;
	
	@Column(name = "xbimage")
	private String xbimage;
	
	@Lob
	@Column(name = "ximage")
	private byte[] ximage;

	@Column(name = "xresource")
	private String xresource;

	@Column(name = "xbrand")
	private String xbrand;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xsize")
	private String xsize;
	
	@Column(name = "xhscode")
	private String xhscode;
	
	@Column(name = "xvolume")
	private BigDecimal xvolume;
	
	@Column(name = "xweight")
	private BigDecimal xweight;
	
	@Column(name = "xctype")
	private String xctype;

	@Transient
	private boolean selected;

	@Transient
	private boolean booked;
	
	@Transient
	private BigDecimal prevrate;

	@Transient
	private BigDecimal xqtyord = BigDecimal.ZERO;

	@Transient
	private BigDecimal lineamt;

	@Transient
	private boolean requested;
	
	@Transient
	private String xorg;
}
