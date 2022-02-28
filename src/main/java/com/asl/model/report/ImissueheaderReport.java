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
import com.asl.entity.Imissuedetail;
import com.asl.entity.LandDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name="imissueheaderreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImissueheaderReport extends AbstractReportModel{
	
	
	private static final long serialVersionUID = -5769991334077914391L;
	
	private String xissuenum;
	private String xdate;
	private String xwh;
	private String xstatus;
	private BigDecimal xtotamt;
	private String xref;
	private String xperparer;
	private String xprepdate;
	private String xstatusjv;
	private String xvoucher;
	private String xnote;
	private String siteName;

	private String storeName;
	private String xpreparername;
	private String totalAmount;

	@XmlElementWrapper(name = "issuedetails")
	@XmlElement(name = "issuedetail")
	private List<Imissuedetail> issuedetails = new ArrayList<>();
	
}
