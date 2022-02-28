package com.asl.model;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Data
public class ProductionSuggestion {

	private String xitem;
	private String xitemdes;
	private BigDecimal xqtyord;
	private String xitemunit;

	private String xrawitem;
	private String xrawdes;
	private BigDecimal xrawqty;
	private String xrawunit;

	private Long seqn;
}
