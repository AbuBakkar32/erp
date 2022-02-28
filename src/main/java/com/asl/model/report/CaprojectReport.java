package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.entity.Caprojectplan;
import com.asl.entity.Cawh;
import com.asl.entity.Cawhfact;
import com.asl.entity.LandDocument;
import com.asl.entity.LandExperience;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name="caprojectreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class CaprojectReport extends AbstractReportModel{
	
	
	private static final long serialVersionUID = -5769991334077914391L;
	
	private String xproject;
	private String xname;
	private String xcus;
	private String xdate;
	private String xestartdate;
	private String xeenddate;
	private String xastartdate;
	private String xaenddate;
	private String xstatus;
	private String xref;
	private String xnote;
	private BigDecimal xamountbudgt;
	private BigDecimal xamountcost;
	private String xcontact;
	private String xplannedby;
	private String xdesc;
	
	private String cusName;
	private String xcontactName;
	private String xpreparerName;
	
	@XmlElementWrapper(name = "documents")
	@XmlElement(name = "document") 
	private List<LandDocument> documents = new ArrayList<>();
	
	@XmlElementWrapper(name = "sites")
	@XmlElement(name = "site")
	private List<Cawh> sites = new ArrayList<>();
	
	@XmlElementWrapper(name = "plans")
	@XmlElement(name = "plan")
	private List<Caprojectplan> plans = new ArrayList<>();
	
	@XmlElementWrapper(name = "tasks")
	@XmlElement(name = "task")
	private List<Cawhfact> tasks = new ArrayList<>();
	
}
