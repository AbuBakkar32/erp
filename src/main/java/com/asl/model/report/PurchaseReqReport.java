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
@XmlRootElement(name="purchasereqreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseReqReport extends AbstractReportModel{

	private static final long serialVersionUID = -1337534943908496637L;
	
	private String xporeqnum;
	private String xdate;
	private String xcus;
	private String xorg;
	private String xpreparer;
	private String xpreparername;
	private String xhwh;
	private String projectName;
	private String xwh;
	private String storeName;
	private String xref;
	private BigDecimal xtotamt;
	private String xstatus;
	private String xstatusreq;
	private String xnote;
	private String totalAmount;
	private String spellword;
	private String xprimeword;
	private String xquotnum;
	
	
	private String xprepdate;
	
	private String xsignatory1;
	private String xsignatorydate1;
	private String xsignatorynote1;
	
	
	
	
	private String cusName;
	private String preparerName;
	private String signatoryName;
	
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
