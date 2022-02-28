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

/**
 * @author Zubayer Ahamed
 * @since Apr 7, 2021
 */
@Data
@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class RM0301PurchaseOrder {

	private String orderNumber;
	private String orderDate;
	private String status;

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<RM0301Item> items = new ArrayList<>();

	private BigDecimal totalQtyOrder;
	private BigDecimal totalQtyPurchased;
	private BigDecimal totalAmount;
}
