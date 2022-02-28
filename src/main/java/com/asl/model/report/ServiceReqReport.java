package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.entity.Termstrn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlRootElement(name="servicereqreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceReqReport  extends AbstractReportModel{
	
	
	private static final long serialVersionUID = 5385731600702211413L;

	private String xporeqnum;
	private String xdate;
	private String xcus;
	private String xwh;
	private String xquotnum;
	private String xref;
	private String xpreparer;
	private String xprepdate;
	private String xnote;
	private String xsignatory1;
	private String xsignatorydate1;
	private String xsignatorynote1;
	private String xstatusreq;
	private String xstatus;
	private BigDecimal xtotamt;
	private String storeName;
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
