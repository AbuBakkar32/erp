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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Data
@Entity
@Table(name = "oporddetail")
@IdClass(OporddetailPK.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "zid", "xordernum", "xrow" }, callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "oporddetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class Oporddetail extends AbstractModel<String> {

	private static final long serialVersionUID = -8200619366025104933L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xordernum")
	private String xordernum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xcomtype")
	private String  xcomtype;

	@Column(name = "xqtydel")
	private BigDecimal xqtydel;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xgitem")
	private String xgitem;

	@Column(name = "xcatitem")
	private String xcatitem;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;
	
	@Column(name = "xdocrow")
	private int xdocrow;

	public Oporddetail(String xitem, BigDecimal xqtyord) {
		this.xitem = xitem;
		this.xqtyord = xqtyord;
	}

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xparentrow")
	private int xparentrow;
	
	@Column(name = "xcfsel")
	private BigDecimal xcfsel;
	
	@Column(name = "xunitsel")
	private String xunitsel;

	@Transient
	private List<Oporddetail> subitems = new ArrayList<>();

	@Transient
	private String itemname;
	
	@Transient
	private String xgroup;
	
	@Transient
	private String xcategory;
}
