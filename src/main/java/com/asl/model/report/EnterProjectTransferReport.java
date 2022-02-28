package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
@XmlRootElement(name="enterprojecttransreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnterProjectTransferReport extends AbstractReportModel{

	private static final long serialVersionUID = 4714612640589402400L;
	
	private String xtornum;
	private String xfhwh;
	private String fPojectName;
	private String xfwh;
	private String xfwhdesc;
	private String xstaff;
	private String xstaffName;
	private String xlong;
	private String xdate;
	private String xthwh;
	private String tPojectName;
	private String xtwh;
	private String xtwhdesc;
	private String xref;
	private String xstatustor;

	
	
	
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
