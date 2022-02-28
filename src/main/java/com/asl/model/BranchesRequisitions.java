package com.asl.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class BranchesRequisitions {

	private String xpornum;
	private String zorg;
	private BigDecimal xtotamt;
	private String xstatuspor;
	private String xstatusreq;
	private String xsonumber;
	private Date xdate;
	private String xitem;
	private String xdesc;
	private BigDecimal xqtyord;
	private String branchzid;
	private String xunitpur;
	private String xordernum;
	private String xcatitem;
	private Long seqn;
}
