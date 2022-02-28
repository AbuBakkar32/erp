package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


import com.asl.entity.Pdloanwriteoff;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XmlRootElement(name="pdloantrnreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class PdloantrnReport extends AbstractReportModel{

	
	private static final long serialVersionUID = 5508642954115442113L;
	
	private String xvoucher;
	private String xdate;
	private String xstaff;
	private String xstaffName;
	private String xtype;
	private BigDecimal xloanamt;
	private int xinstallment;
	private BigDecimal xinsamt;
	private String xdateeff;
	private BigDecimal xlastinsamt;
	private BigDecimal xpaid;
	private String xstatus;
	private String  xstatustag;
	private BigDecimal  xamount;
	
	
	
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
