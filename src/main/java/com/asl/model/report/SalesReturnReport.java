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

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name="salesreturnreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesReturnReport extends AbstractReportModel{

	
	private static final long serialVersionUID = 1736156242537386179L;
	
	private String xcrnnum;
	private String xdate;
	private String xcus;
	private String xorg;
	private String xhwh;
	private String projectName;
	private String xwh;
	private String storeName;
	private String xmadd;
	private String xdornum;
	private String xpaymenttype;
	private String xnote;
	private String xvoucher;
	private String xstatuscrn;
	private BigDecimal xtotamt;
	private BigDecimal xvatamt;
	private BigDecimal xaitamt;
	private BigDecimal xdiscamt;
	private String xstatucgrn;
	private BigDecimal totalAmount;
	private BigDecimal totalQty;
	private String xprimeword;
	private String spellword;
	
	
	//Item Details
	private String xitem;
	private String xdesc;
	private String xunit;
	private BigDecimal xqtyord;
	private String xrate;
	private String xlineamt;
	

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
