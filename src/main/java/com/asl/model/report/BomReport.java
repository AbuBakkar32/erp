package com.asl.model.report;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XmlRootElement(name="bomreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class BomReport extends AbstractReportModel{
	
	private static final long serialVersionUID = -4573589663068572392L;
	
	private String xbomkey;
	private String xdesc;
	private String xdate;
	private String xitem;
	private String xqty;
	private String xunit;
	private BigDecimal xcost;
	private String xcus;
	private Boolean xisdefaut = Boolean.TRUE;
	private String xref;
	private String xnote;

	

}
