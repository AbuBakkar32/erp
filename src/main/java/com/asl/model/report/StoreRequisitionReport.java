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
@XmlRootElement(name="storerequisitionreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class StoreRequisitionReport extends AbstractReportModel{
	
	
	private static final long serialVersionUID = -5769991334077914391L;
	
	private String xtornum;
	private String xreqdate;
	private String xfhwh;
	private String fPojectName;
	private String xfwh;
	private String xfwhdesc;
	private String xtwh;
	private String xtwhdesc;
	private String xpreparerName;
	private String xstatustor;
	private String xref;
	private BigDecimal totalAmount;
	private BigDecimal totalQty;
	
	private String xstatus;
	private String xpreparer;
	private String xlong;
	private String xsignatory1;
	private String xsignatory1Name;
	private String xacknowledgeName;
	private String xsignatorydate1;
	private String xsignatorynote1;
	private String xacknowledge;
	private String xdaterec;
	private String xacknowledgenote;
	private String approvalStatus;
	private String rejectStatus;
	private String ackStatus;
	private String storeName;
	
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
