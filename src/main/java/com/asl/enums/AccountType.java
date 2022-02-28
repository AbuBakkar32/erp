package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2021
 */
public enum AccountType {

	ASSET("Asset", "Asset"), 
	LIABILITY("Liability", "Liability"), 
	INCOME("Income", "Income"),
	EXPENDITURE("Expenditure", "Expenditure");

	private String code;
	private String description;

	private AccountType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
