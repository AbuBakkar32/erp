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
@XmlRootElement(name = "salesorderreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesOrderReport extends AbstractReportModel {
	
	private static final long serialVersionUID = 9107262146739026364L;
	
	private String xordernum;
	private String xdate;
	private String xcus;
	private String xorg;
	private String xhwh;
	private String projectName;
	private String xwh;
	private String storeName;
	private String xref;
	private String xstatusord;
	private String xnote;
	private BigDecimal xtotamt;
	private String spellword;
	private String xprimeword;
	
	
	//Item Details
	private String xitem;
	private String xdesc;
	private BigDecimal xqtyord;
	private String xrate;
	private String xunit;
	private String xlineamt;
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();
}
