package com.asl.model.report;

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
@XmlRootElement(name="stockadjustmentreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class StockAdjustmentReport extends AbstractReportModel{

	
	private static final long serialVersionUID = 1681950699231981404L;
	
	private String xtagnum;
	private String xdate;
	private String xhwh;
	private String projectName;
	private String xwh;
	private String storeName;
	private String xstatustag;
	private String xref;
	
	
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();
}
