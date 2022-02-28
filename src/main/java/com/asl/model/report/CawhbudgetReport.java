package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.entity.Cawhbudgetdt;
import com.asl.entity.LandDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name="cawhbudgetreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class CawhbudgetReport extends AbstractReportModel{
	
	
	private static final long serialVersionUID = -5769991334077914391L;
	
	private String xbudget;
	private String xwh;
	private String xdate;
	private BigDecimal xtotamt;
	private String xref;
	private String xnote;
	private String xstatus;
	private String xprepdate;
	private String xproject;
	private String xcus;
	private String siteName;
	private String projectName;
	private BigDecimal tolPer;
	private String totalAmount;

	private String cusName;

	@XmlElementWrapper(name = "budgetdetails")
	@XmlElement(name = "budgetdetail")
	private List<Cawhbudgetdt> budgetdetails = new ArrayList<>();
	
}
