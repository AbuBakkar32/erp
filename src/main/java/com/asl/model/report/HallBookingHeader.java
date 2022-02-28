package com.asl.model.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jun 15, 2021
 */
@Data
@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
public class HallBookingHeader implements Serializable {

	private static final long serialVersionUID = -388738192249709943L;

	private String xordernum;
	private String xdate;
	private String xcus;
	private String xref;
	private String xstatusord;
	private String xstatus;
	private String xstaff;
	private String xnote;
	private String xpornum;
	private String xchalanref;
	private boolean batchcreated;
	private boolean productioncompleted;
	private boolean invoicecreated;
	private String xfunction;
	private Integer xtotguest;
	private Date xbookdate;
	private Date xfuncdate;
	private BigDecimal xfacamt;
	private BigDecimal xfoodamt;
	private BigDecimal xdiscamt;
	private BigDecimal xtotamt;
	private BigDecimal xgrandtot;
	private BigDecimal xadvamt;
	private Date xcheckindate;
	private Date xcheckoutdate;
	private BigDecimal xroomamt;
	private String xdornum;
	private String xtornum;
	private String xstartdate;
	private String xenddate;
	private String xstarttime;
	private String xendtime;
	private BigDecimal xhallamt;
	private BigDecimal xfunctionamt;
	private String xvatait;
	private BigDecimal xvatamt;
	private BigDecimal xaitamt;
	private String xpaymenttype;
	private String xpaystatus;
	private BigDecimal xpaid;
	private boolean suggestionCreated;
	private BigDecimal xdue;
	private String rawxtornum;
	private String finishedxtornum;
	private boolean rawtocomp;
	private boolean finishedtocomp;
	
	private String clientaddress;
	private String clientphone;
	private String refby;
}
