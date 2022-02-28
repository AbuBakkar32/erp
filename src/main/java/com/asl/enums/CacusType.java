package com.asl.enums;



/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum CacusType {

	SUP("Supplier"),
	CUS("Customer"),
	BOTH("Both");

	private String code;

	private CacusType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
