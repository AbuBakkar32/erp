package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Apr 7, 2021
 */
@Data
@XmlRootElement(name = "RM0301")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class RM0301Report extends AbstractReportModel {

	private static final long serialVersionUID = -2108948210560662297L;

	@XmlElementWrapper(name = "suppliers")
	@XmlElement(name = "supplier")
	private List<RM0301Supplier> suppliers = new ArrayList<>();

	private BigDecimal totalQtyOrder;
	private BigDecimal totalQtyPurchased;
	private BigDecimal totalAmount;
}
