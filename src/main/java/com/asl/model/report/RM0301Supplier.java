package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@XmlRootElement(name = "supplier")
@XmlAccessorType(XmlAccessType.FIELD)
public class RM0301Supplier {

	private String supplierCode;
	private String supplierName;
	private String supplierAddress;

	@XmlElementWrapper(name = "orders")
	@XmlElement(name = "order")
	private List<RM0301PurchaseOrder> orders = new ArrayList<>();

	private BigDecimal totalQtyOrder;
	private BigDecimal totalQtyPurchased;
	private BigDecimal totalAmount;

	private Map<String, RM0301PurchaseOrder> om = new HashMap<>();
}
