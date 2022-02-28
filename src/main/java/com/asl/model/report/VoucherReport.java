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
@XmlRootElement(name = "voucherreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherReport extends AbstractReportModel {
	
	private static final long serialVersionUID = 9187326549593607462L;
	
	private String xvoucher;
	private String xdate;
	private String xstatusjv;
	private Integer xyear;
	private String xref;
	private String xlcno;
	private String xchequeno;
	private Integer xper;
	private BigDecimal totalDebit;
	private BigDecimal totalCredit;
	private String spellword;
	private String xprimeword;
	private String xwh;
	private String xlong;
	private String xbin;
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();
	
}
