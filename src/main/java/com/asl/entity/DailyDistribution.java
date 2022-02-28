package com.asl.entity;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since May 9, 2021
 */
@Data
@XmlRootElement(name = "dailydistribution")
@XmlAccessorType(XmlAccessType.FIELD)
public class DailyDistribution {

	private String xitem;
	private String xdesc;
	private String xunitpur;
	private BigDecimal opening;
	private BigDecimal production;
	private BigDecimal totalinventory;
	private BigDecimal distribution;
	private BigDecimal physicalstock;
}
