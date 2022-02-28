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

@Data
@XmlRootElement(name = "purchaseorder")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseOrder {
	
	private String orderNumber;
	private String date;
	private String status;
	private String supplier;
	private String supplierName;
	private String supplierAddress;
	private String warehouse;
	private String xpreparer;
	private String xpreparername;
	private String xhwh;
	private String projectName;
	private String xwh;
	private String storeName;
	private String xref;
	private String totalAmount;
	private String xstatuspor;
	private String xnote;
	private String vatAmount;
	private String discountAmount;
	private String grandTotalAmount;
	private BigDecimal totalQty;
	private String xprimeword;
	private String spellword;
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
