package com.asl.model.report;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since May 8, 2021
 */
@Data
@XmlRootElement(name = "finishedGood")
@XmlAccessorType(XmlAccessType.FIELD)
public class FinishedGood {

	private String code;
	private String namee;
	private BigDecimal productionRawQty;
	private BigDecimal productionQty;
	private BigDecimal completedQuantity;
	private BigDecimal wastage;
	private String sp;
	private BigDecimal deviation;
}
