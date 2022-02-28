package com.asl.model.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jun 15, 2021
 */
@Data
@XmlRootElement(name = "fooddetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class HallBookingFoodDetail implements Serializable {

	private static final long serialVersionUID = 4055808531681377614L;

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

	@XmlElementWrapper(name = "subitems")
	@XmlElement(name = "subitem")
	List<HallBookingSubItems> subitems = new ArrayList<>();
}
