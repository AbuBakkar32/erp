package com.asl.model.report;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "supplierpayment")
@XmlAccessorType(XmlAccessType.FIELD)
public class SupplierPaymentReport extends AbstractReportModel{
	
	private static final long serialVersionUID = -8871321362731147620L;
	
	private String xvoucher;
	private String xdate;
	private String xpaymenttype;
	private String xref;
	private BigDecimal xprime;
	private String spellword;
	private String xprimeword;
	private String xcus;
	private String xorg;
	private String xmadd;

}
