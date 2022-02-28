package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name = "purchasereturn")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseReturnReport extends AbstractReportModel{
	
	private static final long serialVersionUID = 6450074092446260156L;
	
	private String xcrnnum;
	private String xdate;
	private String xgrnnum;
	private String xcus;
	private String xorg;
	private String xhwh;
	private String projectName;
	private String xwh;
	private String storeName;
	private String xpaymenttype;
	private String xvoucher;
	private String xnote;
	private String xstatusgrn;
	private BigDecimal totalAmount;
	private BigDecimal totalQty;
	private String spellword;
	private String xprimeword;
	private String xmadd;
	
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();
	

}
