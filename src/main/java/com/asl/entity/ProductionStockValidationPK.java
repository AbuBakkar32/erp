package com.asl.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since May 20, 2021
 */
@Data
public class ProductionStockValidationPK implements Serializable {

	private static final long serialVersionUID = -1719411473698592684L;

	private String zid;
	private String xchalan;
	private String xbatch;
}
