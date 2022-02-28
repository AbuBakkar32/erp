package com.asl.model.report;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Aug 22, 2021
 */
@Data
@XmlRootElement(name = "moneyreceipt")
@XmlAccessorType(XmlAccessType.FIELD)
public class MoneyReceiptReport extends AbstractReportModel {

	private static final long serialVersionUID = 5290371888242836755L;

	private String xvoucher;
	private String xdate;
	private String xcus;
	private String xorg;
	private String xmadd;
	private BigDecimal xprime;
	private String xprimeword;
	private String xbank;
	private String bankname;
	private String xpaymenttype;
	private String xref;
	private String spellword;
	private String xpurpose;
	private String xland;

}
