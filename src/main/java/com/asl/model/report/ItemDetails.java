package com.asl.model.report;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2021
 */
@Data
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemDetails implements Serializable {

	private static final long serialVersionUID = -7858634465702990400L;

	private String itemCode;
	private String itemName;
	private String itemQty;
	private String itemUnit;
	private String itemCategory;
	private String itemGroup;
	private BigDecimal xcfpur;
	private BigDecimal xcfsel;
	private String xunit;
	private String xunitsel;
	private String xpurpose;
	private BigDecimal xvatamt;
	private String xlong;
	private BigDecimal xqtydel;
	private String xnote;
	
	// for loans & advance details
	private String xdate;
	private BigDecimal xloanamt;
	private String  xstatus;
	private String  xstatustag;

	
	private String itemRate;
	private BigDecimal rate;
	private BigDecimal lineamt;
	private String itemTotalAmount;
	private BigDecimal grandTotal;

	// For qty details 
	private String orderQty;
	private String receivedQty;
	private String deliveredQty;
	
	//For Voucher Details
	private String xacc;
	private String accountname;
	private BigDecimal xdebit;
	private BigDecimal xcredit;
	private String xsub;
	private String xorg;
	private String spellword;
	private String xprimeword;
	
	//For Land Education
	
	private String year;
	private String exam;
	private String institude;
	private String session;
	private String major;
	private String result;
	
	//service Requisition
	
	private String purpose;
	private String xwh;

}
