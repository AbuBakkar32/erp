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
@XmlRootElement(name = "directsalesinvoicereport")
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectSalesReport extends AbstractReportModel{
	
	private static final long serialVersionUID = 9165958499260360612L;
	
	private String xdornum;
	private String xdate;
	private String xcus;
	private String xorg;
	private String xmadd;
	private String xphone;
	private String xref;
	private String xnote;
	private String xpaymenttype;
	private BigDecimal xtotamt;
	private BigDecimal xvatamt;
	private BigDecimal xaitamt;
	private BigDecimal xvatait;
	private BigDecimal xdiscamt;
	private BigDecimal xgrandtot;
	private BigDecimal totalQty;
	private String xprimeword;
	private String spellword;
	private String xpurpose;
	private String xland;
	
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
