package com.asl.model.report;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 7, 2021
 */
@Data
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class RM0301Item {

	private int sl;
	private String itemCode;
	private String itemName;
	private BigDecimal qtyOrder;
	private BigDecimal qtyPurchased;
	private BigDecimal rate;
	private String unit;
	private BigDecimal amount;
}
