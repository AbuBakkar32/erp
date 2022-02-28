package com.asl.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since May 8, 2021
 */
@Data
public class DailyProductionBatchDetail {

	private String xbatch;
	private Date xdate;
	private String xitem;
	private String finishedgood;
	private String xbomcomp;
	private String rawmaterial;
	private BigDecimal xproduction;
	private BigDecimal xqtyprd;
	private BigDecimal xqtycom;
	private BigDecimal wastage;
	private BigDecimal deviation;
}
