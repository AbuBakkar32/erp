package com.asl.model.report;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 7, 2021
 */
@Data
public class RM0301 {

	private String xpornum;
	private Date xdate;
	private String xcus;
	private String xmadd;
	private String xorg;
	private String xstatuspor;
	private String xitem;
	private String xdesc;
	private BigDecimal xqtyord;
	private BigDecimal xqtygrn;
	private BigDecimal xrate;
	private String xunitpur;
	private BigDecimal xlineamt;
}
