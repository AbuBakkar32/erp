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

import com.asl.entity.Cawh;
import com.asl.entity.Cawhcostest;
import com.asl.entity.LandDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name="cawhreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class CawhReport extends AbstractReportModel{
	
	
	private static final long serialVersionUID = -5769991334077914391L;
	
	private String xwh;
	private String xname;
	private String xproject;
	private String xcus;
	private String xlocation;
	private String xsupevisor;
	private String xdate;
	private String xstartdate;
	private String xenddate;
	private BigDecimal xamountbudgt;
	private BigDecimal xamountcost;
	private String xref;
	private String xnote;
	private String xstatus;
	private String xdesc;
	private String projectName;
	private String xfdate;
	
	private String cusName;
	private String xsupervisorName;

	@XmlElementWrapper(name = "documents")
	@XmlElement(name = "document") 
	private List<LandDocument> documents = new ArrayList<>();
	
	@XmlElementWrapper(name = "costests")
	@XmlElement(name = "costest")
	private List<Cawhcostest> costests = new ArrayList<>();
	
}
