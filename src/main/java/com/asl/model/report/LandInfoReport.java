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

import com.asl.entity.LandDagDetails;
import com.asl.entity.LandDocument;
import com.asl.entity.LandEvents;
import com.asl.entity.LandOwner;
import com.asl.entity.LandSurvey;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jun 23, 2021
 */
@Data
@XmlRootElement(name = "landinforeport")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class LandInfoReport extends AbstractReportModel {

	private static final long serialVersionUID = 3417261808169544623L;

	private String xland;
	private String xlname;
	private String xblock;
	private String xroad;
	private BigDecimal xlandqty;
	private String xlandunit;
	private BigDecimal xlandgrsqty;
	private String xlanggrsunit;
	private Integer xlanddedroad;
	private Integer xlanddedother;
	private BigDecimal xlandnetqty;
	private String xlandnetunit;
	private BigDecimal xriversideqty;
	private String xriversideunit;
	private String xnote;
	private String xlandparent;
	private String xstatus;
	private BigDecimal xroadred;
	private BigDecimal xotherred;
	private Date xdateborn;
	private String xtypetrn;
	private String xtrn;
	private String xname;
	private String xcus;
	private String xmemname;
	private BigDecimal xtotded;
	private BigDecimal xtotrem;
	
	private BigDecimal xlandqtyd;
	private BigDecimal xlandqtyk;
	private String xnote2;
	private BigDecimal xtotdedprice;
	private BigDecimal xamtar;
	private BigDecimal xamtrv;
	private BigDecimal xlandqtydsg;
	private BigDecimal xlandqtydsn;
	private String xsurveyor;
	private String surveyorName;
	private String xdatesrv;
	private BigDecimal balance;
	private BigDecimal grossDev;
	private BigDecimal netDev;
	

	@XmlElementWrapper(name = "owners")
	@XmlElement(name = "owner")
	private List<LandOwner> owners = new ArrayList<>();

	@XmlElementWrapper(name = "dags")
	@XmlElement(name = "dag")
	private List<LandDagDetails> dags = new ArrayList<>();

	@XmlElementWrapper(name = "surveys")
	@XmlElement(name = "survey")
	private List<LandSurvey> surveys = new ArrayList<>();
	
	@XmlElementWrapper(name = "documents")
	@XmlElement(name = "document") 
	private List<LandDocument> documents = new ArrayList<>();
	
	@XmlElementWrapper(name = "activities")
	@XmlElement(name = "activity") 
	private List<LandEvents> activities = new ArrayList<>();

}
