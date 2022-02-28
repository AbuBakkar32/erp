package com.asl.model.report;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jun 15, 2021
 */
@Data
@XmlRootElement(name = "subitem")
@XmlAccessorType(XmlAccessType.FIELD)
public class HallBookingSubItems implements Serializable {

	private static final long serialVersionUID = -563201517628059244L;

	private String zid;
	private String xordernum;
	private int xrow;
	private String xunit;
	private String xitem;
	private BigDecimal xqtyord;
	private BigDecimal xrate;
	private String  xcomtype;
	private BigDecimal xqtydel;
	private String xlong;
	private String xdesc;
	private String xgitem;
	private String xcatitem;
	private BigDecimal xlineamt;
	private String xtype;
	private int xparentrow;

}
