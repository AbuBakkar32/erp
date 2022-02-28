package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jun 20, 2021
 */
@Data
@Entity
@Table(name = "opreqdetail")
@IdClass(OpreqdetailPK.class)
@EqualsAndHashCode(of = {"zid", "xdoreqnum", "xrow"}, callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Opreqdetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1335364690831742660L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdoreqnum")
	private String xdoreqnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrate")
	private BigDecimal xrate;
	
	@Column(name = "xqtydel")
	private BigDecimal xqtydel;
	
	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xitemdesc")
	private String xitemdesc;

	@Column(name = "xcatitem")
	private String xcatitem;

	@Column(name = "xgitem")
	private String xgitem;
	
	@Column(name = "xcfsel")
	private BigDecimal xcfsel;
	
	@Column(name = "xunitsel")
	private String xunitsel;
	
	@Transient
	private String xdesc;
	
	@Transient
	private String itemname;
	
}
