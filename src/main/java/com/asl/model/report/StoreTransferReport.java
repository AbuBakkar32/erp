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
@XmlRootElement(name="storetransreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class StoreTransferReport extends AbstractReportModel{
	
	
	private static final long serialVersionUID = -5769991334077914391L;
	
	private String xtornum;
	private String xdate;
	private String xchalanref;
	private String xfhwh;
	private String fPojectName;
	private String xfwh;
	private String xtwh;
	private String xstaff;
	private String xstaffName;
	private String xstatustor;
	private String xref;
	private String xlong;
	private BigDecimal totalAmount;
	private BigDecimal totalQty;
	private String xfwhdesc;
	private String xtwhdesc;
	
	
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
